package othello.faskinen;

import othello.faskinen.opengl.GL;

public class Faskinen {
	public Window window;
	public Shader pieceShader;
	public Buffer colorBuffer;
	public int imageWidth = 512;
	public int imageHeight = 512;

	public Faskinen() {
		this.window = Window.create("context", 1, 1);
		this.window.makeContextCurrent();

		this.colorBuffer = new Buffer(this.imageWidth * this.imageHeight * 4);
		this.pieceShader = new Shader("shaders/piece.glsl");
	}

	public Buffer renderImage() {
		this.colorBuffer.bind();
		this.colorBuffer.setData();

		this.pieceShader.use();

		this.pieceShader.setInt("imageWidth", this.imageWidth);
		this.pieceShader.setInt("imageHeight", this.imageHeight);
		this.pieceShader.setBuffer("ColorBuffer", this.colorBuffer, 0);

		GL.DispatchCompute(this.imageWidth / 16, this.imageHeight / 16, 1);

		this.colorBuffer.readData();

		return this.colorBuffer;
	}
}
