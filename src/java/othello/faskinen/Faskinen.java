package othello.faskinen;

import othello.faskinen.opengl.GL;

public class Faskinen {
	public Window window;
	public Shader pieceShader;
	public Buffer colorBuffer;

	public Faskinen() {
		this.window = Window.create("context", 1, 1);
		this.window.makeContextCurrent();

		this.colorBuffer = new Buffer(1024);
		this.pieceShader = new Shader("shaders/piece.glsl");
	}

	public void dispatch() {
		this.colorBuffer.bind();
		this.colorBuffer.setData();

		this.pieceShader.use();

		this.pieceShader.setBuffer("ColorBuffer", this.colorBuffer, 0);

		GL.DispatchCompute(1, 1, 1);

		this.colorBuffer.readData();

		for (int i = 0; i < 32; i++) {
			System.out.println(this.colorBuffer.readInt(i * 4));
		}
	}
}
