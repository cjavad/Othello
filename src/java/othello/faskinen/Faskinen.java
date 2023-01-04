package othello.faskinen;

public class Faskinen {
	public Window window;
	public Shader pieceShader;

	public Faskinen() {
		this.window = Window.create("context", 1, 1);
		this.window.makeContextCurrent();

		this.pieceShader = new Shader("shaders/piece.glsl");
	}
}
