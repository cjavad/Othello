package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Vec3 {	
	public float x, y, z;	

	public static int sizeof() {
		return 3 * 4;
	}

	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3(float v) {
		this(v, v, v);
	}

	public Vec3() {
		this(0, 0, 0);
	}

	public Vec3(MemorySegment segment) {
		this.x = segment.get(ValueLayout.JAVA_FLOAT, 0);
		this.y = segment.get(ValueLayout.JAVA_FLOAT, 4);
		this.z = segment.get(ValueLayout.JAVA_FLOAT, 8);
	}

	public Vec4 extend(float w) {
		return new Vec4(this.x, this.y, this.z, w);
	}

	public Vec3 add(Vec3 v) {
		return new Vec3(this.x + v.x, this.y + v.y, this.z + v.z);
	}

	public Vec3 add(float s) {
		return new Vec3(this.x + s, this.y + s, this.z + s);
	}

	public Vec3 sub(Vec3 v) {
		return new Vec3(this.x - v.x, this.y - v.y, this.z - v.z);
	}

	public Vec3 sub(float s) {
		return new Vec3(this.x - s, this.y - s, this.z - s);
	}

	public Vec3 mul(Vec3 v) {
		return new Vec3(this.x * v.x, this.y * v.y, this.z * v.z);
	}

	public Vec3 mul(float s) {
		return new Vec3(this.x * s, this.y * s, this.z * s);
	}

	public Vec3 div(Vec3 v) {
		return new Vec3(this.x / v.x, this.y / v.y, this.z / v.z);
	}

	public Vec3 div(float s) {
		return new Vec3(this.x / s, this.y / s, this.z / s);
	}

	public float dot(Vec3 v) {
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}

	public Vec3 cross(Vec3 v) {
		return new Vec3(
			this.y * v.z - this.z * v.y,
			this.z * v.x - this.x * v.z,
			this.x * v.y - this.y * v.x
		);
	}

	public float length_squared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public float length() {
		return (float)Math.sqrt(this.length_squared());
	}

	public Vec3 normalize() {
		float len = this.length();

		if (len == 0.0) {
			return new Vec3();
		}

		return new Vec3(this.x / len, this.y / len, this.z / len);
	}
}
