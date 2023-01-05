package othello.faskinen;

import java.time.Instant;

import othello.faskinen.opengl.GL;

public class Faskinen {
	public Window window;
	public Shader pieceShader;

	public Buffer colorBuffer;
	public Buffer depthBuffer;
	public Instant startTime;

	public int imageWidth = 1920;
	public int imageHeight = 1080;

	public Camera camera = new Camera();
	public PieceModel[] pieces = new PieceModel[0];

	public Faskinen() {
		this.window = Window.create("context", 1, 1);
		this.window.makeContextCurrent();
	
		this.colorBuffer = new Buffer(this.imageWidth * this.imageHeight * 4);
		this.depthBuffer = new Buffer(this.imageWidth * this.imageHeight * 4);
		this.colorBuffer.upload();
		this.depthBuffer.upload();

		this.pieceShader = new Shader("shaders/piece.glsl");

		this.startTime = Instant.now();
	}

	public Buffer renderImage() {
		this.colorBuffer.upload();
		this.depthBuffer.upload();

		this.pieceShader.use();

		float time = (float) (Instant.now().toEpochMilli() - this.startTime.toEpochMilli()) / 1000.0f;
		this.pieceShader.setFloat("time", time);

		float aspect = (float) this.imageWidth / (float) this.imageHeight;

		Mat4 viewProj = this.camera.viewProj(aspect);
		this.pieceShader.setMat4("viewProj", viewProj);

		this.pieceShader.setInt("imageWidth", this.imageWidth);
		this.pieceShader.setInt("imageHeight", this.imageHeight);	

		BoundingRect imageRect = new BoundingRect(0, 0, this.imageWidth, this.imageHeight);

		this.pieceShader.setBuffer("ColorBuffer", this.colorBuffer, 0);
		this.pieceShader.setBuffer("DepthBuffer", this.depthBuffer, 1);	

		for (int i = 0; i < this.pieces.length; i++) {
			PieceModel piece = this.pieces[i];

			BoundingRect pieceRect = piece.getBoundingRect(viewProj, this.imageWidth, this.imageHeight);
	
			if (!imageRect.intersects(pieceRect)) continue;
			pieceRect = imageRect.intersect(pieceRect);

			this.pieceShader.setInt("offsetX", pieceRect.x);
			this.pieceShader.setInt("offsetY", pieceRect.y);
			this.pieceShader.setInt("width", pieceRect.width);
			this.pieceShader.setInt("height", pieceRect.height);

			this.pieceShader.setVec3("position", piece.position);
			this.pieceShader.setVec3("color", piece.color);
			GL.DispatchCompute(pieceRect.width / 16 + 1, pieceRect.height / 16 + 1, 1);
			GL.Finish();
		}

		this.colorBuffer.download();

		return this.colorBuffer;
	}
}
