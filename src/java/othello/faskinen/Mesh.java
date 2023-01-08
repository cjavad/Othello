package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;

import othello.faskinen.opengl.GL;

/**
 * A mesh belonging to a Primitive.
 */
public class Mesh {
	public static final int VERTEX_SIZE = 48;

	public Buffer vertexBuffer;
	public Buffer indexBuffer;
	public int vertexArrayId;

	public int vertexCount;
	public int indexCount;

	/**
	 * Reads a Mesh from a MemorySegment.
	 *
	 * This may ONLY be called after a valid OpenGL context has been created.
	 */
	public Mesh(MemorySegment segment) {
		this.vertexCount = segment.get(ValueLayout.JAVA_INT, 0);
		this.indexCount = segment.get(ValueLayout.JAVA_INT, 4);

		long vertexBufferSize = this.vertexCount * VERTEX_SIZE;
		long indexBufferSize = this.indexCount * 4;

		long vertexBufferOffset = 8;
		long indexBufferOffset = vertexBufferOffset + vertexBufferSize;

		this.vertexBuffer = new Buffer(segment.asSlice(vertexBufferOffset, vertexBufferSize));
		this.indexBuffer = new Buffer(segment.asSlice(indexBufferOffset, indexBufferSize));

		MemorySegment vertexArraySegment = MemorySession.openShared().allocate(4);
		GL.GenVertexArrays(1, vertexArraySegment.address());

		this.vertexArrayId = vertexArraySegment.get(ValueLayout.JAVA_INT, 0);

		GL.BindVertexArray(this.vertexArrayId);
		this.vertexBuffer.bind(GL.ARRAY_BUFFER);
		GL.EnableVertexAttribArray(0);
		GL.VertexAttribPointer(0, 3, GL.FLOAT, GL.FALSE, VERTEX_SIZE, 0);
		GL.EnableVertexAttribArray(1);
		GL.VertexAttribPointer(1, 3, GL.FLOAT, GL.FALSE, VERTEX_SIZE, 12);
		GL.EnableVertexAttribArray(2);
		GL.VertexAttribPointer(2, 4, GL.FLOAT, GL.FALSE, VERTEX_SIZE, 24);
		GL.EnableVertexAttribArray(3);
		GL.VertexAttribPointer(3, 2, GL.FLOAT, GL.FALSE, VERTEX_SIZE, 40);

		GL.BindVertexArray(0);
		this.vertexBuffer.unbind(GL.ARRAY_BUFFER);

		this.vertexBuffer.upload();
		this.indexBuffer.upload(GL.ELEMENT_ARRAY_BUFFER, GL.STATIC_DRAW);
	}

	/**
	 * Size of the mesh in bytes.
	 */
	public long sizeof() {
		return 8 + this.vertexBuffer.sizeof() + this.indexBuffer.sizeof();
	}

	/**
	 * Binds the mesh.
	 */
	public void bind() {
		GL.BindVertexArray(this.vertexArrayId);
		this.indexBuffer.bind(GL.ELEMENT_ARRAY_BUFFER);
	}

	/**
	 * Unbinds the mesh.
	 */
	public void unbind() {
		GL.BindVertexArray(0);
		this.indexBuffer.unbind(GL.ELEMENT_ARRAY_BUFFER);
	}

	/**
	 * Deletes all the OpenGL resources in the Mesh
	 */
	public void delete() {
		this.vertexBuffer.delete();
		this.indexBuffer.delete();

		MemorySegment arrays = MemorySession.openImplicit().allocate(4);
		arrays.set(ValueLayout.JAVA_INT, 0, this.vertexArrayId);
		GL.DeleteVertexArrays(1, arrays.address());
	}
}
