package othello.faskinen;

public class Mat4 {
	public Vec4 x, y, z, w;

	public Mat4(Vec4 x, Vec4 y, Vec4 z, Vec4 w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Mat4() {
		this(new Vec4(), new Vec4(), new Vec4(), new Vec4());
	}

	public static Mat4 rotationX(float angle) {
		float c = (float)Math.cos(angle);
		float s = (float)Math.sin(angle);

		return new Mat4(
			new Vec4(1, 0, 0, 0),
			new Vec4(0, c, s, 0),
			new Vec4(0, -s, c, 0),
			new Vec4(0, 0, 0, 1)
		);
	}

	public static Mat4 rotationY(float angle) {
		float c = (float)Math.cos(angle);
		float s = (float)Math.sin(angle);

		return new Mat4(
			new Vec4(c, 0, -s, 0),
			new Vec4(0, 1, 0, 0),
			new Vec4(s, 0, c, 0),
			new Vec4(0, 0, 0, 1)
		);
	}

	public static Mat4 rotationZ(float angle) {
		float c = (float)Math.cos(angle);
		float s = (float)Math.sin(angle);

		return new Mat4(
			new Vec4(c, s, 0, 0),
			new Vec4(-s, c, 0, 0),
			new Vec4(0, 0, 1, 0),
			new Vec4(0, 0, 0, 1)
		);
	}

	public static Mat4 translation(Vec3 v) {
		return new Mat4(
			new Vec4(1, 0, 0, 0),
			new Vec4(0, 1, 0, 0),
			new Vec4(0, 0, 1, 0),
			new Vec4(v.x, v.y, v.z, 1)
		);
	}

	public static Mat4 scale(Vec3 v) {
		return new Mat4(
			new Vec4(v.x, 0, 0, 0),
			new Vec4(0, v.y, 0, 0),
			new Vec4(0, 0, v.z, 0),
			new Vec4(0, 0, 0, 1)
		);
	}

	public static Mat4 perspective(float fov, float aspect, float near, float far) {
		fov = (float)Math.toRadians(fov);

		float sinFov = (float)Math.sin(fov / 2);
		float cosFov = (float)Math.cos(fov / 2);

		float h = cosFov / sinFov;
		float w = h / aspect;
		float r = far / (far - near);

		return new Mat4(
			new Vec4(w, 0, 0, 0),
			new Vec4(0, h, 0, 0),
			new Vec4(0, 0, r, 1),
			new Vec4(0, 0, -r * near, 0)
		);
	}	

	public static Mat4 orthographic(float left, float right, float bottom, float top, float near, float far) {
		float w = 2 / (right - left);
		float h = 2 / (top - bottom);
		float d = 2 / (far - near);

		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(far + near) / (far - near);

		return new Mat4(
			new Vec4(w, 0, 0, 0),
			new Vec4(0, h, 0, 0),
			new Vec4(0, 0, d, 0),
			new Vec4(tx, ty, tz, 1)
		);
	}

	public static Mat4 identity() {
		return new Mat4(
			new Vec4(1, 0, 0, 0),
			new Vec4(0, 1, 0, 0),
			new Vec4(0, 0, 1, 0),
			new Vec4(0, 0, 0, 1)
		);
	}

	public Mat4 transpose() {
		return new Mat4(
			new Vec4(this.x.x, this.y.x, this.z.x, this.w.x),
			new Vec4(this.x.y, this.y.y, this.z.y, this.w.y),
			new Vec4(this.x.z, this.y.z, this.z.z, this.w.z),
			new Vec4(this.x.w, this.y.w, this.z.w, this.w.w)
		);
	}

	public Mat4 add(Mat4 m) {
		return new Mat4(
			this.x.add(m.x),
			this.y.add(m.y),
			this.z.add(m.z),
			this.w.add(m.w)
		);
	}

	public Mat4 sub(Mat4 m) {
		return new Mat4(
			this.x.sub(m.x),
			this.y.sub(m.y),
			this.z.sub(m.z),
			this.w.sub(m.w)
		);
	}		

	public Vec4 mul(Vec4 v) {
		return new Vec4(
			this.x.x * v.x + this.y.x * v.y + this.z.x * v.z + this.w.x * v.w,
			this.x.y * v.x + this.y.y * v.y + this.z.y * v.z + this.w.y * v.w,
			this.x.z * v.x + this.y.z * v.y + this.z.z * v.z + this.w.z * v.w,
			this.x.w * v.x + this.y.w * v.y + this.z.w * v.z + this.w.w * v.w
		);
	}	

	public Vec3 mul(Vec3 v) {
		return this.mul(v.extend(1.0f)).truncate();
	}

	public Mat4 mul(Mat4 m) {
		return new Mat4(
			this.mul(m.x),
			this.mul(m.y),
			this.mul(m.z),
			this.mul(m.w)
		);
	}

	public float determinant() {
		return
			this.x.x * this.y.y * this.z.z * this.w.w +
			this.x.x * this.y.z * this.z.w * this.w.y +
			this.x.x * this.y.w * this.z.y * this.w.z +
			this.x.y * this.y.x * this.z.w * this.w.z +
			this.x.y * this.y.z * this.z.x * this.w.w +
			this.x.y * this.y.w * this.z.z * this.w.x +
			this.x.z * this.y.x * this.z.y * this.w.w +
			this.x.z * this.y.y * this.z.w * this.w.x +
			this.x.z * this.y.w * this.z.x * this.w.y +
			this.x.w * this.y.x * this.z.z * this.w.y +
			this.x.w * this.y.y * this.z.x * this.w.z +
			this.x.w * this.y.z * this.z.y * this.w.x -
			this.x.x * this.y.y * this.z.w * this.w.z -
			this.x.x * this.y.z * this.z.y * this.w.w -
			this.x.x * this.y.w * this.z.z * this.w.y -
			this.x.y * this.y.x * this.z.z * this.w.w -
			this.x.y * this.y.z * this.z.w * this.w.x -
			this.x.y * this.y.w * this.z.x * this.w.z -
			this.x.z * this.y.x * this.z.w * this.w.y -
			this.x.z * this.y.y * this.z.x * this.w.w -
			this.x.z * this.y.w * this.z.y * this.w.x -
			this.x.w * this.y.x * this.z.y * this.w.z -
			this.x.w * this.y.y * this.z.z * this.w.x -
			this.x.w * this.y.z * this.z.x * this.w.y;
	}

	public Mat4 inverse() {
		float det = this.determinant();

		if (det == 0) {
			return this;
		}	

		return new Mat4(
			new Vec4(
				(this.y.y * this.z.z * this.w.w + this.y.z * this.z.w * this.w.y + 
				 this.y.w * this.z.y * this.w.z - this.y.y * this.z.w * this.w.z - 
				 this.y.z * this.z.y * this.w.w - this.y.w * this.z.z * this.w.y) / det,
				(this.x.y * this.z.w * this.w.z + this.x.z * this.z.y * this.w.w + 
				 this.x.w * this.z.z * this.w.y - this.x.y * this.z.z * this.w.w - 
				 this.x.z * this.z.w * this.w.y - this.x.w * this.z.y * this.w.z) / det,
				(this.x.y * this.y.z * this.w.w + this.x.z * this.y.w * this.w.y + 
				 this.x.w * this.y.y * this.w.z - this.x.y * this.y.w * this.w.z - 
				 this.x.z * this.y.y * this.w.w - this.x.w * this.y.z * this.w.y) / det,
				(this.x.y * this.y.w * this.z.z + this.x.z * this.y.y * this.z.w + 
				 this.x.w * this.y.z * this.z.y - this.x.y * this.y.z * this.z.w - 
				 this.x.z * this.y.w * this.z.y - this.x.w * this.y.y * this.z.z) / det
			),
			new Vec4(
				(this.y.x * this.z.w * this.w.z + this.y.z * this.z.x * this.w.w + 
				 this.y.w * this.z.z * this.w.x - this.y.x * this.z.z * this.w.w - 
				 this.y.z * this.z.w * this.w.x - this.y.w * this.z.x * this.w.z) / det,
				(this.x.x * this.z.z * this.w.w + this.x.z * this.z.w * this.w.x + 
				 this.x.w * this.z.x * this.w.z - this.x.x * this.z.w * this.w.z - 
				 this.x.z * this.z.x * this.w.w - this.x.w * this.z.z * this.w.x) / det,
				(this.x.x * this.y.w * this.w.z + this.x.z * this.y.x * this.w.w + 
				 this.x.w * this.y.z * this.w.x - this.x.x * this.y.z * this.w.w - 
				 this.x.z * this.y.w * this.w.x - this.x.w * this.y.x * this.w.z) / det,
				(this.x.x * this.y.z * this.z.w + this.x.z * this.y.w * this.z.x + 
				 this.x.w * this.y.x * this.z.z - this.x.x * this.y.w * this.z.z - 
				 this.x.z * this.y.x * this.z.w - this.x.w * this.y.z * this.z.x) / det
			),
			new Vec4(
				(this.y.x * this.z.y * this.w.w + this.y.y * this.z.w * this.w.x + 
				 this.y.w * this.z.x * this.w.y - this.y.x * this.z.w * this.w.y - 
				 this.y.y * this.z.x * this.w.w - this.y.w * this.z.y * this.w.x) / det,
				(this.x.x * this.z.w * this.w.y + this.x.y * this.z.x * this.w.w + 
				 this.x.w * this.z.y * this.w.x - this.x.x * this.z.y * this.w.w - 
				 this.x.y * this.z.w * this.w.x - this.x.w * this.z.x * this.w.y) / det,
				(this.x.x * this.y.y * this.w.w + this.x.y * this.y.w * this.w.x + 
				 this.x.w * this.y.x * this.w.y - this.x.x * this.y.w * this.w.y - 
				 this.x.y * this.y.x * this.w.w - this.x.w * this.y.y * this.w.x) / det,
				(this.x.x * this.y.w * this.z.y + this.x.y * this.y.x * this.z.w + 
				 this.x.w * this.y.y * this.z.x - this.x.x * this.y.y * this.z.w - 
				 this.x.y * this.y.w * this.z.x - this.x.w * this.y.x * this.z.y) / det
			),
			new Vec4(
				(this.y.x * this.z.z * this.w.y + this.y.y * this.z.x * this.w.z + 
				 this.y.z * this.z.y * this.w.x - this.y.x * this.z.y * this.w.z - 
				 this.y.y * this.z.z * this.w.x - this.y.z * this.z.x * this.w.y) / det,
				(this.x.x * this.z.y * this.w.z + this.x.y * this.z.z * this.w.x + 
				 this.x.z * this.z.x * this.w.y - this.x.x * this.z.z * this.w.y - 
				 this.x.y * this.z.x * this.w.z - this.x.z * this.z.y * this.w.x) / det,
				(this.x.x * this.y.z * this.w.y + this.x.y * this.y.x * this.w.z + 
				 this.x.z * this.y.y * this.w.x - this.x.x * this.y.y * this.w.z - 
				 this.x.y * this.y.z * this.w.x - this.x.z * this.y.x * this.w.y) / det,
				(this.x.x * this.y.y * this.z.z + this.x.y * this.y.z * this.z.x + 
				 this.x.z * this.y.x * this.z.y - this.x.x * this.y.z * this.z.y - 
				 this.x.y * this.y.x * this.z.z - this.x.z * this.y.y * this.z.x) / det
			)
		);
	}
}
