package othello.faskinen;

public class Camera {
	public Vec3 position = new Vec3(0, 0, 0);
	public float pitch = 0;
	public float yaw = 0;

	public Camera() {}	

	public Mat4 view() {
		Mat4 rotationX = Mat4.rotationX(this.pitch);
		Mat4 rotationY = Mat4.rotationY(this.yaw);
		Mat4 translation = Mat4.translation(this.position);

		return translation.mul(rotationY.mul(rotationX));
	}

	public Mat4 proj(float aspect) {
		return Mat4.perspective(50.0f, aspect, 0.01f, 100.0f);
	}

	public Mat4 viewProj(float aspect) {
		return this.proj(aspect).mul(this.view().inverse());
	}

	/// negative z
	public Vec3 forward() {
		return this.view().mul(new Vec4(0, 0, -1, 0)).truncate();
	}

	/// positive x
	public Vec3 right() {
		return this.view().mul(new Vec4(1, 0, 0, 0)).truncate();
	}

	public Vec3 up() {
		return this.forward().cross(this.right());
	}
}
