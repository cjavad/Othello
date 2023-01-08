package othello.faskinen;

import java.lang.foreign.MemorySegment;

/**
 * A primitive containing a single mesh and material.
 *
 * Primitives may ONLY be created after a valid OpenGL context has been created.
 *
 * Primitives are usually created by the Model class and should not be created manually.
 */
public class Primitive {
	public Mesh mesh;
	public Material material;

	/**
	 * Reads a Primitive from a MemorySegment.
	 * @param segment The segment to read from.
	 */
	public Primitive(MemorySegment segment) {
		this.mesh = new Mesh(segment);

		long offset = this.mesh.sizeof();
		this.material = new Material(segment.asSlice(offset));
	}

	/**
	 * Size of the primitive in bytes.
	 */
	public long sizeof() {
		return this.mesh.sizeof() + Material.sizeof();
	}

	/**
	 * Deletes all OpenGL resources in the Primitive
	 */
	public void delete() {
		this.mesh.delete();
	}
}
