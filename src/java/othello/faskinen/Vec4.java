package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Vec4 {
	public float x, y, z, w;

	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vec4() {
		this(0, 0, 0, 0);
	}	

	public float[] array() {
		return new float[] { x, y, z, w };
	}

	public byte[] bytes() {
		MemorySegment segment = MemorySegment.ofArray(new float[] { x, y, z, w });
		return segment.toArray(ValueLayout.JAVA_BYTE);
	}

	public byte[] rgba8() {
		return new byte[] {
			(byte)(x * 255),
			(byte)(y * 255),
			(byte)(z * 255),
			(byte)(w * 255)
		};

	}

	public Vec3 truncate() {
		return new Vec3(this.x, this.y, this.z);
	}

	public Vec4 add(Vec4 v) {
		return new Vec4(this.x + v.x, this.y + v.y, this.z + v.z, this.w + v.w);
	}

	public Vec4 sub(Vec4 v) {
		return new Vec4(this.x - v.x, this.y - v.y, this.z - v.z, this.w - v.w);
	}

	public Vec4 mul(Vec4 v) {
		return new Vec4(this.x * v.x, this.y * v.y, this.z * v.z, this.w * v.w);
	}

	public Vec4 div(Vec4 v) {
		return new Vec4(this.x / v.x, this.y / v.y, this.z / v.z, this.w / v.w);
	}

	public float dot(Vec4 v) {
		return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w;
	}	

	public float length_squared() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}

	public float length() {
		return (float)Math.sqrt(this.length_squared());
	}

	public Vec4 normalize() {
		float len = this.length();

		if (len == 0.0) {
			return new Vec4();
		}

		return new Vec4(this.x / len, this.y / len, this.z / len, this.w / len);
	}
}
