package othello.faskinen;

import java.time.Instant;

import othello.faskinen.opengl.GL;

public class Faskinen {
	public Window window;
	public Shader shader;
	public Buffer colorBuffer;
	public Instant startTime;
	public int imageWidth = 1024;
	public int imageHeight = 1024;

	public Faskinen() {
		this.window = Window.create("context", 1, 1);
		this.window.makeContextCurrent();

		this.colorBuffer = new Buffer(this.imageWidth * this.imageHeight * 4);
		this.shader = new Shader("shaders/raymarch.glsl");

		this.startTime = Instant.now();
	}

	public Buffer renderImage() {
		this.colorBuffer.bind();
		this.colorBuffer.setData();

		this.shader.use();

		float time = (float) (Instant.now().toEpochMilli() - this.startTime.toEpochMilli()) / 1000.0f;
		this.shader.setFloat("time", time);

		this.shader.setInt("imageWidth", this.imageWidth);
		this.shader.setInt("imageHeight", this.imageHeight);
		this.shader.setBuffer("ColorBuffer", this.colorBuffer, 0);

		GL.DispatchCompute(this.imageWidth / 16, this.imageHeight / 16, 1);

		this.colorBuffer.readData();

		return this.colorBuffer;
	}
}
