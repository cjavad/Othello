package othello.faskinen;

import othello.faskinen.opengl.GL;

public class ParticleSystem {
	Buffer buffer;
	int index = 0;
	int count = 0;

	static Shader updateShader = new Shader("particle.comp", GL.COMPUTE_SHADER);
	static Shader renderShader = new Shader("geometry_particle.vert", "geometry.frag");

	public ParticleSystem(int count) {
		this.buffer = new Buffer(Particle.stride() * count);
		this.buffer.upload();
	}

	public int maxCount() {
		return this.buffer.sizeof() / Particle.stride();
	}

	public void pushParticle(Particle particle) {
		if (this.count == this.maxCount()) {
			throw new RuntimeException("count == maxCount");
		}

		int index = (this.index + this.count) % this.maxCount();
		int offset = index * Particle.stride();

		Mat4 model = Mat4.translation(particle.position);
		this.buffer.writeFloat(offset + 0, model.x.x);
		this.buffer.writeFloat(offset + 4, model.x.y);
		this.buffer.writeFloat(offset + 8, model.x.z);
		this.buffer.writeFloat(offset + 12, model.x.w);
		this.buffer.writeFloat(offset + 16, model.y.x);
		this.buffer.writeFloat(offset + 20, model.y.y);
		this.buffer.writeFloat(offset + 24, model.y.z);
		this.buffer.writeFloat(offset + 28, model.y.w);
		this.buffer.writeFloat(offset + 32, model.z.x);
		this.buffer.writeFloat(offset + 36, model.z.y);
		this.buffer.writeFloat(offset + 40, model.z.z);
		this.buffer.writeFloat(offset + 44, model.z.w);
		this.buffer.writeFloat(offset + 48, model.w.x);
		this.buffer.writeFloat(offset + 52, model.w.y);
		this.buffer.writeFloat(offset + 56, model.w.z);
		this.buffer.writeFloat(offset + 60, model.w.w);
		this.buffer.writeFloat(offset + 64, particle.velocity.x);
		this.buffer.writeFloat(offset + 68, particle.velocity.y);
		this.buffer.writeFloat(offset + 72, particle.velocity.z);

		this.buffer.upload();

		this.count += 1;
	}

	public void update(float dt) {
		updateShader.use();

		updateShader.setFloat("dt", dt);
		updateShader.setInt("index", this.index);
		updateShader.setInt("count", this.count);
		updateShader.setBuffer("ParticleBuffer", this.buffer, 0);

		GL.DispatchCompute(this.count / 64 + 1, 1, 1);
	}

	public void drawRange(Primitive primitive, int start, int count) {
		this.buffer.bind(GL.ARRAY_BUFFER);
		GL.EnableVertexAttribArray(10);
		GL.VertexAttribPointer(10, 4, GL.FLOAT, 0, Particle.stride(), 0);
		GL.EnableVertexAttribArray(11);
		GL.VertexAttribPointer(11, 4, GL.FLOAT, 0, Particle.stride(), 16);
		GL.EnableVertexAttribArray(12);
		GL.VertexAttribPointer(12, 4, GL.FLOAT, 0, Particle.stride(), 32);
		GL.EnableVertexAttribArray(13);
		GL.VertexAttribPointer(13, 4, GL.FLOAT, 0, Particle.stride(), 48);

		GL.VertexAttribDivisor(10, 1);
		GL.VertexAttribDivisor(11, 1);
		GL.VertexAttribDivisor(12, 1);
		GL.VertexAttribDivisor(13, 1);

		GL.assertNoError();

		GL.DrawElementsInstanced(
			GL.TRIANGLES,
			primitive.mesh.indexCount, 
			GL.UNSIGNED_INT, 
			0, 
			count
		);
	}

	public void draw(Model model, Camera camera) {
		if (this.count == 0) return;

		renderShader.use();

		renderShader.setCamera(camera, 1);

		for (Primitive primitive : model.primitives) {	
			primitive.mesh.bind();

			renderShader.setVec3("baseColor", primitive.material.baseColor);
			renderShader.setFloat("roughness", primitive.material.roughness);
			renderShader.setFloat("metallic", primitive.material.metallic);
			renderShader.setVec3("emissive", primitive.material.emissive);
			renderShader.setFloat("reflectance", primitive.material.reflectance);

			if (primitive.material.baseColorTexture != -1) {
				Texture texture = model.textures[primitive.material.baseColorTexture];
				renderShader.setTexture("baseColorMap", texture);
			} else {
				renderShader.setTexture("baseColorMap", Texture.RGBA8WHITE);
			}

			if (primitive.material.metallicRoughnessTexture != -1) {
				Texture texture = model.textures[primitive.material.metallicRoughnessTexture];
				renderShader.setTexture("metallicRoughnessMap", texture);
			} else {
				renderShader.setTexture("metallicRoughnessMap", Texture.RGBA8WHITE);
			}

			if (primitive.material.normalTexture != -1) {
				Texture texture = model.textures[primitive.material.normalTexture];
				renderShader.setTexture("normalMap", texture);
			} else {
				renderShader.setTexture("normalMap", Texture.RGBA8NORMAL);
			}

			if (primitive.material.emissiveTexture != -1) {
				Texture texture = model.textures[primitive.material.emissiveTexture];
				renderShader.setTexture("emissiveMap", texture);
			} else {
				renderShader.setTexture("emissiveMap", Texture.RGBA8WHITE);
			}

			if (this.index + this.count > this.maxCount()) {
				this.drawRange(primitive, this.index, this.maxCount() - this.index);
				this.drawRange(primitive, 0, this.index + this.count - this.maxCount());
			} else {
				this.drawRange(primitive, this.index, this.count);
			}
		}
	}
}
