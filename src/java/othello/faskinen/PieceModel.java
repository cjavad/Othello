package othello.faskinen;

public class PieceModel {
	public Vec3 position = new Vec3();
	public Vec3 color = new Vec3();

	public PieceModel() {}

	public PieceModel(Vec3 position, Vec3 color) {
		this.position = position;
		this.color = color;
	}

	public BoundingRect getBoundingRect(Mat4 viewProj, int width, int height) {
		return BoundingRect.cube(viewProj, this.position, new Vec3(0.5f, 0.4f, 0.5f), width, height);
	}
}
