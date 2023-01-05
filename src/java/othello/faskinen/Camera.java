package othello.faskinen;

public class Camera {
	public Vec3 position = new Vec3(0, 0, 0);
	public float pitch = 0;
	public float yaw = 0;

	public Camera() {}

	public Vec3 localX() {
		return this.viewMatrix().x.truncate().normalize();
	}

	public Vec3 localZ() {
		return this.viewMatrix().z.truncate().normalize();
	}

	public Mat4 viewMatrix() {
		Mat4 view = Mat4.rotationX(this.pitch);
		view = view.mul(Mat4.rotationY(this.yaw));
		view = view.mul(Mat4.translation(this.position));

		return view;
	}
}
