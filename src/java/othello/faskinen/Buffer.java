package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;

import othello.faskinen.opengl.GL;

/**
 * A wrapper around an OpenGL buffer.
 *
 * Buffers may ONLY be created after a valid OpenGL context has been created.
 */
public class Buffer {
	public MemorySegment segment;
	public int bufferId;

	/**
	 * Creates a new empty buffer.
	 */
	public Buffer() {
		this.segment = MemorySession.openShared().allocate(4);

		MemorySegment buffers = MemorySession.openImplicit().allocate(4);
		GL.GenBuffers(1, buffers.address());

		this.bufferId = buffers.get(ValueLayout.JAVA_INT, 0);

		GL.assertNoError();
	}

	/**
	 * Creates a new buffer.
	 * @param size The size of the buffer.
	 */
	public Buffer(int size) {
		this();

		this.resize(size);
	}

	/**
	 * Creates a new buffer.
	 * @param segment The segment to use as the buffer.
	 */
	public Buffer(MemorySegment segment) {
		this.segment = segment;

		MemorySegment buffers = MemorySession.openImplicit().allocate(4);
		GL.GenBuffers(1, buffers.address());

		this.bufferId = buffers.get(ValueLayout.JAVA_INT, 0);
	}

	/**
	 * Gets the size of the buffer.
	 * @return The size of the buffer.
	 */
	public int sizeof() {
		return (int) this.segment.byteSize();
	}

	/**
	 * Resizes the buffer.
	 * @param size The new size of the buffer.
	 */
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

	/**
	 * Binds the buffer.
	 * @param target The target to bind the buffer to.
	 */
	public void bind(int target) {
		GL.BindBuffer(target, this.bufferId);
	}

	/**
	 * Unbinds the buffer.
	 * @param target The target to unbind the buffer from.
	 */
	public void unbind(int target) {
		GL.BindBuffer(target, 0);
	}

	/**
	 * Uploads the buffer to the GPU.
	 * @param target The target to upload the buffer to.
	 * @param usage The usage of the buffer.
	 */
	public void upload(int target, int usage) {
		this.bind(target);
		GL.BufferData(target, this.segment.byteSize(), this.segment.address(), usage);
		this.unbind(target);

		GL.assertNoError();
	}

	public void upload() {
		this.upload(GL.ARRAY_BUFFER, GL.DYNAMIC_DRAW);
	}

	/**
	 * Downloads the buffer from the GPU.
	 */
	public void download() {
		this.bind(GL.ARRAY_BUFFER);
		GL.GetBufferSubData(GL.ARRAY_BUFFER, 0, this.segment.byteSize(), this.segment.address());
		this.unbind(GL.ARRAY_BUFFER);

		GL.assertNoError();
	}

	/**
	 * Reads the buffer as a byte array.
	 * @return The buffer as a byte array.
	 *
	 * The data returned reflects the data as of the last download or manual change.
	 */
	public byte[] bytes() {
		return this.segment.toArray(ValueLayout.JAVA_BYTE);
	}

	/**
	 * Writes an integer to the buffer.
	 * @param offset The offset to write the integer to.
	 * @param value The value to write.
	 */
	public void writeInt(long offset, int value) {
		this.segment.set(ValueLayout.JAVA_INT, offset, value);
	}

	/**
	 * Writes a float to the buffer.
	 * @param offset The offset to write the float to.
	 * @param value The value to write.
	 */
	public void writeFloat(long offset, float value) {
		this.segment.set(ValueLayout.JAVA_FLOAT, offset, value);
	}

	/**
	 * Reads the buffer as an integer array.
	 * @return The buffer as an integer array.
	 */
	public int[] toIntArray() {
		return this.segment.toArray(ValueLayout.JAVA_INT);
	}

	/**
	 * Reads the buffer as a float array.
	 * @return The buffer as a float array.
	 */
	public float[] toFloatArray() {
		return this.segment.toArray(ValueLayout.JAVA_FLOAT);
	}

	/**
	 * Reads an integer from the buffer.
	 * @param offset The offset to read the integer from.
	 * @return The integer read.
	 */
	public int readInt(long offset) {
		return this.segment.get(ValueLayout.JAVA_INT, offset);
	}

	/**
	 * Reads a float from the buffer.
	 * @param offset The offset to read the float from.
	 * @return The float read.
	 */
	public float readFloat(long offset) {
		return this.segment.get(ValueLayout.JAVA_FLOAT, offset);
	}

	/**
	 * Deletes the buffer.
	 *
	 * Using the buffer after it has been deleted will result in undefined behaviour.
	 */
	public void delete() {
		MemorySegment buffers = MemorySession.openImplicit().allocate(4);
		buffers.set(ValueLayout.JAVA_INT, 0, this.bufferId);

		this.bufferId = -1;

		GL.DeleteBuffers(1, buffers.address());
	}
}
