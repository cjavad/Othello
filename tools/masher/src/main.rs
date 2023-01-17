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
    emissive: Vec3,
    base_color_texture: i32,
    metallic_roughness_texture: i32,
    normal_texture: i32,
    emissive_texture: i32,
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

            let Some(mut positions) = reader.read_positions() else { continue; };
            let Some(mut normals) = reader.read_normals() else { continue; };
            let Some(mut tangents) = reader.read_tangents() else { 
                println!("[Warning]: Missing tangents!");
                println!("        |> This primitive will be skipped '{}'.", node.name().unwrap_or("Unnamed"));
                println!("        |");
                println!("        |>  Try checking the \"Generate Tangents\" option in Blender.");
                println!("        \\>  If that doesn't work, try triangulating the mesh before exporting.");
                continue; 
            };
            let Some(mut uvs) = reader.read_tex_coords(0).map(|t| t.into_f32()) else { continue; };

            let mut vertices = Vec::with_capacity(positions.len());

            for _ in 0..positions.len() {
                let position = global.transform_point3(positions.next().unwrap().into());
                let normal = global.transform_vector3(normals.next().unwrap().into());

                let mut tangent: Vec4 = tangents.next().unwrap().into();
                tangent = (global.transform_vector3(tangent.truncate())).extend(tangent.w);

                let vertex = Vertex {
                    position: position.into(),
                    normal: normal.into(),
                    tangent: tangent.into(),
                    uv: uvs.next().unwrap().into(),
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

            let base_color_texture = if let Some(tex) = pbr.base_color_texture() {
                tex.texture().index() as i32
            } else {
                -1
            };

            let metallic_roughness_texture = if let Some(tex) = pbr.metallic_roughness_texture() {
                tex.texture().index() as i32
            } else {
                -1
            };

            let normal_texture = if let Some(tex) = material.normal_texture() {
                tex.texture().index() as i32
            } else {
                -1
            };

            let emissive_texture = if let Some(tex) = material.emissive_texture() {
                tex.texture().index() as i32
            } else {
                -1
            };

            let material = Material {
                base_color: Vec4::from(pbr.base_color_factor()).truncate(),
                roughness: pbr.roughness_factor(),
                metallic: pbr.metallic_factor(),
                emissive: Vec3::from(material.emissive_factor()),
                base_color_texture,
                metallic_roughness_texture,
                normal_texture,
                emissive_texture,
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

fn into_rgba(pixels: &[u8], format: gltf::image::Format) -> Vec<u8> {
    const BIT_16_MAX: f32 = u16::MAX as f32;

    match format {
        gltf::image::Format::R8 => pixels.iter().map(|p| [*p, *p, *p, 255]).flatten().collect(),
        gltf::image::Format::R8G8 => pixels
            .chunks_exact(2)
            .map(|p| [p[0], p[1], 0, 255])
            .flatten()
            .collect(),
        gltf::image::Format::R8G8B8 => pixels
            .chunks_exact(3)
            .map(|p| [p[0], p[1], p[2], 255])
            .flatten()
            .collect(),
        gltf::image::Format::R8G8B8A8 => pixels.to_vec(),
        gltf::image::Format::B8G8R8 => pixels
            .chunks_exact(3)
            .map(|p| [p[2], p[1], p[0], 255])
            .flatten()
            .collect(),
        gltf::image::Format::B8G8R8A8 => pixels
            .chunks_exact(4)
            .map(|p| [p[2], p[1], p[0], p[3]])
            .flatten()
            .collect(),
        gltf::image::Format::R16 => pixels
            .chunks_exact(2)
            .map(|p| {
                let p = u16::from_le_bytes([p[0], p[1]]);
                let p = (p as f32 / BIT_16_MAX * 255.0) as u8;
                [p, p, p, 255]
            })
            .flatten()
            .collect(),
        gltf::image::Format::R16G16 => pixels
            .chunks_exact(4)
            .map(|p| {
                let p1 = u16::from_le_bytes([p[0], p[1]]);
                let p1 = (p1 as f32 / BIT_16_MAX * 255.0) as u8;
                let p2 = u16::from_le_bytes([p[2], p[3]]);
                let p2 = (p2 as f32 / BIT_16_MAX * 255.0) as u8;
                [p1, p2, 0, 255]
            })
            .flatten()
            .collect(),
        gltf::image::Format::R16G16B16 => pixels
            .chunks_exact(6)
            .map(|p| {
                let p1 = u16::from_le_bytes([p[0], p[1]]);
                let p1 = (p1 as f32 / BIT_16_MAX * 255.0) as u8;
                let p2 = u16::from_le_bytes([p[2], p[3]]);
                let p2 = (p2 as f32 / BIT_16_MAX * 255.0) as u8;
                let p3 = u16::from_le_bytes([p[4], p[5]]);
                let p3 = (p3 as f32 / BIT_16_MAX * 255.0) as u8;
                [p1, p2, p3, 255]
            })
            .flatten()
            .collect(),
        gltf::image::Format::R16G16B16A16 => pixels
            .chunks_exact(8)
            .map(|p| {
                let p1 = u16::from_le_bytes([p[0], p[1]]);
                let p1 = (p1 as f32 / BIT_16_MAX * 255.0) as u8;
                let p2 = u16::from_le_bytes([p[2], p[3]]);
                let p2 = (p2 as f32 / BIT_16_MAX * 255.0) as u8;
                let p3 = u16::from_le_bytes([p[4], p[5]]);
                let p3 = (p3 as f32 / BIT_16_MAX * 255.0) as u8;
                let p4 = u16::from_le_bytes([p[6], p[7]]);
                let p4 = (p4 as f32 / BIT_16_MAX * 255.0) as u8;
                [p1, p2, p3, p4]
            })
            .flatten()
            .collect(),
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

    let texture_count = document.textures().len() as u32;
    file.write_all(&texture_count.to_le_bytes()).unwrap();

    for texture in document.textures() {
        let image = texture.source();
        let data = &data.images[image.index()];
        let pixels = into_rgba(&data.pixels, data.format);

        let pixel_size: u32 = 4;
        file.write_all(&pixel_size.to_le_bytes()).unwrap();
        file.write_all(&data.width.to_le_bytes()).unwrap();
        file.write_all(&data.height.to_le_bytes()).unwrap();
        file.write_all(&pixels).unwrap();
    }
}
