package othello.faskinen;

public class Light {
	public Vec3 direction;
	public Vec3 color;

	public static int SHADOWMAP_SIZE = 2048;

	public Light(Vec3 direction, Vec3 color) {
		this.direction = direction;
		this.color = color;
	}
}
