package othello.faskinen;

public class Particle {
	public Vec3 position = new Vec3();
	public Vec3 velocity = new Vec3();

	public Particle() {
		this.position = new Vec3();
		this.velocity = new Vec3();
	}

	public static int stride() {
		return 16 * 2;
	}
}
