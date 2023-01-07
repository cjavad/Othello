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

		GL.assertNoError();
	}

	public Buffer(int size) {
		this();

		this.resize(size);
	}

	public Buffer(MemorySegment segment) {
		this.segment = segment;

		MemorySegment buffers = MemorySession.openImplicit().allocate(4);
		GL.GenBuffers(1, buffers.address());

		this.bufferId = buffers.get(ValueLayout.JAVA_INT, 0);
	}

	public int sizeof() {
		return (int) this.segment.byteSize();
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

	public void bind(int target) {
		GL.BindBuffer(target, this.bufferId);
	}

	public void unbind(int target) {
		GL.BindBuffer(target, 0);
	}

	public void upload(int target) {
		this.bind(target);
		GL.BufferData(target, this.segment.byteSize(), this.segment.address(), GL.STATIC_DRAW);
		this.unbind(target);

		GL.assertNoError();
	}

	public void upload() {
		this.upload(GL.ARRAY_BUFFER);
	}

	public void download() {
		this.bind(GL.ARRAY_BUFFER);
		GL.GetBufferSubData(GL.ARRAY_BUFFER, 0, this.segment.byteSize(), this.segment.address());
		this.unbind(GL.ARRAY_BUFFER);

		GL.assertNoError();
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
