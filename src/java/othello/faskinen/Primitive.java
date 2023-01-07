package othello.faskinen;

import java.lang.foreign.MemorySegment;

public class Primitive {
	public Mesh mesh;
	public Material material;

	public Primitive(MemorySegment segment) {
		this.mesh = new Mesh(segment);

		long offset = this.mesh.sizeof();
		this.material = new Material(segment.asSlice(offset));
	}

	public long sizeof() {
		return this.mesh.sizeof() + Material.sizeof();
	}
}
