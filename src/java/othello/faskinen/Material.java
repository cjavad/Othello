package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Material {
	public Vec3 baseColor = new Vec3(1.0f, 1.0f, 1.0f);
	public float roughness = 0.5f;
	public float metallic = 0.0f;

	public Material() {}

	public Material(MemorySegment segment) {
		this.baseColor = new Vec3(segment);
		this.roughness = segment.get(ValueLayout.JAVA_FLOAT, 12);
		this.metallic = segment.get(ValueLayout.JAVA_FLOAT, 16);
	}

	public static int sizeof() {
		return Vec3.sizeof() + 2 * 4;
	}
}
