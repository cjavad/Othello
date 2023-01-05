package othello.faskinen;

public class PieceModel {
	public Vec3 position = new Vec3();
	public Vec3 color = new Vec3();

	public PieceModel() {}

	public PieceModel(Vec3 position, Vec3 color) {
		this.position = position;
		this.color = color;
	}
}
