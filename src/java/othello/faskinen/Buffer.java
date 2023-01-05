package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;

import othello.faskinen.opengl.GL;

public class Buffer {
	public MemorySegment segment;
	public int bufferId;

	public Buffer() {
		this.segment = MemorySession.openShared().allocate(4);

		MemorySegment buffers = MemorySession.openImplicit().allocate(4);
		GL.GenBuffers(1, buffers.address());

		this.bufferId = buffers.get(ValueLayout.JAVA_INT, 0);
	}

	public Buffer(int size) {
		this();

		this.resize(size);
	}

	public void resize(int size) {
		if (this.segment.byteSize() == size) {
			return;
		}

		ByteBuffer buffer = this.segment.asByteBuffer();
		this.segment = MemorySession.openShared().allocate(size);

		if (buffer.limit() > size) {
			buffer.limit(size);
		}

		this.segment.asByteBuffer().put(buffer);
	}

	public void bind() {
		GL.BindBuffer(GL.SHADER_STORAGE_BUFFER, this.bufferId);
	}

	public void upload() {
		this.bind();
		GL.BufferData(GL.SHADER_STORAGE_BUFFER, this.segment.byteSize(), this.segment.address(), GL.DYNAMIC_READ);
	}

	public void download() {
		this.bind();
		GL.GetBufferSubData(GL.SHADER_STORAGE_BUFFER, 0, this.segment.byteSize(), this.segment.address());
	}

	public byte[] bytes() {
		return this.segment.toArray(ValueLayout.JAVA_BYTE);
	}

	public void writeInt(long offset, int value) {
		this.segment.set(ValueLayout.JAVA_INT, offset, value);
	}

	public void writeFloat(long offset, float value) {
		this.segment.set(ValueLayout.JAVA_FLOAT, offset, value);
	}

	public int[] toIntArray() {
		return this.segment.toArray(ValueLayout.JAVA_INT);
	}

	public float[] toFloatArray() {
		return this.segment.toArray(ValueLayout.JAVA_FLOAT);
	}

	public int readInt(long offset) {
		return this.segment.get(ValueLayout.JAVA_INT, offset);
	}

	public float readFloat(long offset) {
		return this.segment.get(ValueLayout.JAVA_FLOAT, offset);
	}

	public void delete() {
		MemorySegment buffers = MemorySession.openImplicit().allocate(4);
		buffers.set(ValueLayout.JAVA_INT, 0, this.bufferId);

		GL.DeleteBuffers(1, buffers.address());
	}
}
