package othello.faskinen;

import java.time.Instant;

import othello.faskinen.opengl.GL;

public class Faskinen {
	public Window window;
	public Shader shader;
	public Buffer colorBuffer;
	public Instant startTime;

	public int imageWidth = 1920;
	public int imageHeight = 1080;

	public Camera camera = new Camera();
	public PieceModel[] pieces = new PieceModel[0];
	private Buffer pieceBuffer;

	public Faskinen() {
		this.window = Window.create("context", 1, 1);
		this.window.makeContextCurrent();

		this.colorBuffer = new Buffer(this.imageWidth * this.imageHeight * 4);
		this.shader = new Shader("shaders/raymarch.glsl");

		this.startTime = Instant.now();
	}

	public void writePieceBuffer() {
		this.pieceBuffer = new Buffer(this.pieces.length * 8 * 4);

		for (int i = 0; i < this.pieces.length; i++) {
			long offset = i * 8 * 4;

			PieceModel piece = this.pieces[i];
			this.pieceBuffer.writeFloat(offset + 0, piece.position.x);
			this.pieceBuffer.writeFloat(offset + 4, piece.position.y);
			this.pieceBuffer.writeFloat(offset + 8, piece.position.z);
			this.pieceBuffer.writeFloat(offset + 16, piece.color.x);
			this.pieceBuffer.writeFloat(offset + 20, piece.color.y);
			this.pieceBuffer.writeFloat(offset + 24, piece.color.z);
		}

		this.pieceBuffer.upload();
	}

	public Buffer renderImage() {
		this.colorBuffer.upload();
		this.writePieceBuffer();

		this.shader.use();

		float time = (float) (Instant.now().toEpochMilli() - this.startTime.toEpochMilli()) / 1000.0f;
		this.shader.setFloat("time", time);

		this.shader.setMat4("viewMatrix", this.camera.viewMatrix());

		this.shader.setInt("imageWidth", this.imageWidth);
		this.shader.setInt("imageHeight", this.imageHeight);

		this.shader.setBuffer("ColorBuffer", this.colorBuffer, 0);
		this.shader.setBuffer("PieceBuffer", this.pieceBuffer, 1);

		GL.DispatchCompute(this.imageWidth / 16, this.imageHeight / 16, 1);

		this.colorBuffer.download();

		return this.colorBuffer;
	}
}
