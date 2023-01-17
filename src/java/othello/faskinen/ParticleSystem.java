package othello.faskinen;

import othello.faskinen.opengl.GL;

public class ParticleSystem {
	Buffer buffer;

	static Shader shader = new Shader("particle.comp", GL.COMPUTE_SHADER);

	public ParticleSystem(int count) {
		this.buffer = new Buffer(8 + Particle.stride() * count);
	}

	public int maxCount() {
		return (this.buffer.sizeof() - 8) / Particle.stride();
	}

	public int index() {
		return buffer.readInt(0);
	}

	public void setIndex(int index) {
		buffer.writeInt(0, index);
	}
	
	public int count() {
		return buffer.readInt(4);
	}

	public void setCount(int count) {
		if (count > this.maxCount()) {
			throw new RuntimeException("count > maxCount");
		}

		buffer.writeInt(4, count);
	}	

	public void update(float dt) {
		shader.use();

		shader.setFloat("dt", dt);
		shader.setBuffer("particleSystem", this.buffer, 0);

		GL.DispatchCompute(this.count() / 64, 1, 1);
	}
}
