use std::{fs::File, io::Write, path::PathBuf};

use bytemuck::{Pod, Zeroable};
use clap::Parser;
use glam::{Mat4, Vec3, Vec4};

#[repr(C)]
#[derive(Clone, Copy, Debug, Zeroable, Pod)]
struct Vertex {
    position: [f32; 3],
    normal: [f32; 3],
    tangent: [f32; 4],
    uv: [f32; 2],
}

struct Mesh {
    vertices: Vec<Vertex>,
    indices: Vec<u32>,
}

impl Mesh {
    fn write(&self, writer: &mut impl Write) {
        let vertex_count = self.vertices.len() as u32;
        let index_count = self.indices.len() as u32;

        writer.write_all(&vertex_count.to_le_bytes()).unwrap();
        writer.write_all(&index_count.to_le_bytes()).unwrap();

        let vertex_bytes = bytemuck::cast_slice(&self.vertices);
        writer.write_all(vertex_bytes).unwrap();

        let index_bytes = bytemuck::cast_slice(&self.indices);
        writer.write_all(index_bytes).unwrap();
    }
}

#[repr(C)]
#[derive(Clone, Copy, Debug, Zeroable, Pod)]
struct Material {
    base_color: Vec3,
    roughness: f32,
    metallic: f32,
}

impl Material {
    fn write(&self, writer: &mut impl Write) {
        writer.write_all(bytemuck::bytes_of(self)).unwrap();
    }
}

struct Primitive {
    mesh: Mesh,
    material: Material,
}

impl Primitive {
    fn write(&self, writer: &mut impl Write) {
        self.mesh.write(writer);
        self.material.write(writer);
    }
}

struct GltfData<'a> {
    document: &'a gltf::Document,
    buffers: &'a [gltf::buffer::Data],
    images: &'a [gltf::image::Data],
}

#[derive(Default)]
struct Model {
    primitives: Vec<Primitive>,
}

impl Model {
    fn load(&mut self, data: &GltfData<'_>, node: gltf::Node<'_>, parent: Mat4) {
        let local = Mat4::from_cols_array_2d(&node.transform().matrix());
        let global = parent * local;

        for primitive in node.mesh().into_iter().flat_map(|m| m.primitives()) {
            let reader = primitive.reader(|buffer| Some(&data.buffers[buffer.index()]));

            let positions: Vec<_> = reader.read_positions().unwrap().collect();
            let normals: Vec<_> = reader.read_normals().unwrap().collect();
            let tangents: Vec<_> = reader.read_tangents().unwrap().collect();
            let uvs: Vec<_> = reader.read_tex_coords(0).unwrap().into_f32().collect();

            let mut vertices = Vec::with_capacity(positions.len());

            for i in 0..positions.len() {
                let position = global.transform_point3(positions[i].into());
                let normal = global.transform_vector3(normals[i].into());

                let mut tangent: Vec4 = tangents[i].into();
                tangent = (global.transform_vector3(tangent.truncate())).extend(tangent.w);

                let vertex = Vertex {
                    position: position.into(),
                    normal: normal.into(),
                    tangent: tangent.into(),
                    uv: uvs[i],
                };

                vertices.push(vertex);
            }

            let indices = reader
                .read_indices()
                .unwrap()
                .into_u32()
                .collect::<Vec<_>>();

            let material = primitive.material();
            let pbr = material.pbr_metallic_roughness();

            let material = Material {
                base_color: Vec4::from(pbr.base_color_factor()).truncate(),
                roughness: pbr.roughness_factor(),
                metallic: pbr.metallic_factor(),
            };

            self.primitives.push(Primitive {
                mesh: Mesh { vertices, indices },
                material,
            });
        }

        for child in node.children() {
            self.load(data, child, global);
        }
    }

    fn write(&self, writer: &mut impl Write) {
        let count = self.primitives.len() as u32;
        writer.write_all(&count.to_le_bytes()).unwrap();

        for primitive in &self.primitives {
            primitive.write(writer);
        }
    }
}

/// Convert a GLTF mesh to a binary format.
#[derive(Parser)]
#[clap(version, author)]
struct Args {
    path: PathBuf,
}

fn main() {
    let args = Args::parse();

    let (document, buffers, images) = gltf::import(&args.path).unwrap();
    let data = GltfData {
        document: &document,
        buffers: &buffers,
        images: &images,
    };

    let mut model = Model::default();

    for scene in document.scenes() {
        for node in scene.nodes() {
            model.load(&data, node, Mat4::IDENTITY);
        }
    }

    let mut file = File::create(args.path.with_extension("bin")).unwrap();
    model.write(&mut file);
}
