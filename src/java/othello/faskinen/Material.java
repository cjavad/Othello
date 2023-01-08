package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

/**
 * A material belonging to a Primitive.
 *
 * Textures are indices into the Model's texture array.
 * If a texture is -1, it is not used, and a fallback texture is used instead.
 */
public class Material {
	public Vec3 baseColor = new Vec3(1.0f, 1.0f, 1.0f);
	public float roughness = 0.5f;
	public float metallic = 0.0f;
	public float reflectance = 0.5f;
	public int baseColorTexture = -1;
	public int metallicRoughnessTexture = -1;
	public int normalTexture = -1;

	public Material() {}

	/**
	 * Reads a Material from a MemorySegment.
	 */
	public Material(MemorySegment segment) {
		this.baseColor = new Vec3(segment);
		this.roughness = segment.get(ValueLayout.JAVA_FLOAT, 12);
		this.metallic = segment.get(ValueLayout.JAVA_FLOAT, 16);
		this.baseColorTexture = segment.get(ValueLayout.JAVA_INT, 20);
		this.metallicRoughnessTexture = segment.get(ValueLayout.JAVA_INT, 24);
		this.normalTexture = segment.get(ValueLayout.JAVA_INT, 28);
	}

	/**
	 * Size of the material in bytes.
	 */
	public static int sizeof() {
		return Vec3.sizeof() + 5 * 4;
	}
}
