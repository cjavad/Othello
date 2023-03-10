package othello.faskinen;

import java.lang.foreign.Addressable;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer; 

import othello.faskinen.opengl.GL;
import othello.utils.ResourceLoader;

/**
 * A wrapper around an OpenGL texture.
 *
 * Textures may ONLY be created after a valid OpenGL context has been created.
 */
public class Texture {
	public int textureId;
	public int width;
	public int height;
	public int internal;
	public int format;
	public int type;
	public MemorySegment segment;
	public byte[] data;

	/**
	 * Creates a new texture.
	 * @param internal The internal format of the texture.
	 * @param format The format of the texture.
	 * @param type The type of the texture.
	 * @param width The width of the texture.
	 * @param height The height of the texture.
	 * @param data The data of the texture.
	 *
	 * If the length of data is 0, the texture will be created with no data.
	 * If data is not big enough to fill the texture, a fatal error is likely to occur.
	 *
	 * @return The created texture.
	 */
	public Texture(int internal, int format, int type, int width, int height, byte[] data) {
		MemorySegment textures = MemorySession.openImplicit().allocate(4);
		GL.GenTextures(1, textures.address());

		this.textureId = textures.get(ValueLayout.JAVA_INT, 0);
		this.width = width;
		this.height = height;
		this.internal = internal;
		this.format = format;
		this.type = type;

		GL.BindTexture(GL.TEXTURE_2D, this.textureId);

		MemoryAddress address;
		if (data.length > 0) {
			MemorySegment dataSegment = MemorySession.openImplicit().allocate(data.length);
			dataSegment.copyFrom(MemorySegment.ofArray(data));
			address = dataSegment.address();
		} else {
			address = Lib.NULLPTR;
		}

		GL.TexImage2D(GL.TEXTURE_2D, 0, internal, width, height, 0, format, type, address);
		GL.GenerateMipmap(GL.TEXTURE_2D);

		GL.TexParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_S, GL.CLAMP_TO_EDGE);
		GL.TexParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_T, GL.CLAMP_TO_EDGE);

		if (format == GL.RGBA || format == GL.BGRA || format == GL.DEPTH_COMPONENT) {
			GL.TexParameteri(GL.TEXTURE_2D, GL.TEXTURE_MIN_FILTER, GL.LINEAR_MIPMAP_LINEAR);
			GL.TexParameteri(GL.TEXTURE_2D, GL.TEXTURE_MAG_FILTER, GL.LINEAR);
		} else {
			GL.TexParameteri(GL.TEXTURE_2D, GL.TEXTURE_MIN_FILTER, GL.NEAREST);
			GL.TexParameteri(GL.TEXTURE_2D, GL.TEXTURE_MAG_FILTER, GL.NEAREST);
		}
		

		GL.BindTexture(GL.TEXTURE_2D, 0);

		GL.assertNoError();
	}

	public static Texture rgba8(int width, int height, byte[] data) {
		return new Texture(GL.RGBA, GL.RGBA, GL.UNSIGNED_BYTE, width, height, data);
	}

	public static Texture rgba8(int width, int height) {
		return rgba8(width, height, new byte[0]);
	}

	public static Texture bgra8(int width, int height, byte[] data) {
		return new Texture(GL.RGBA, GL.BGRA, GL.UNSIGNED_BYTE, width, height, data);
	}

	public static Texture bgra8(int width, int height) {
		return bgra8(width, height, new byte[0]);
	}

	public static Texture srgba8(int width, int height, byte[] data) {
		return new Texture(GL.SRGB_ALPHA, GL.BGRA, GL.UNSIGNED_BYTE, width, height, data);
	}

	public static Texture srgba8(int width, int height) {
		return srgba8(width, height, new byte[0]);
	}

	public static Texture sbgra8(int width, int height, byte[] data) {
		return new Texture(GL.SRGB_ALPHA, GL.BGRA, GL.UNSIGNED_BYTE, width, height, data);
	}

	public static Texture sbgra8(int width, int height) {
		return sbgra8(width, height, new byte[0]);
	}

	public static Texture rgba16f(int width, int height, byte[] data) {
		return new Texture(GL.RGBA16F, GL.RGBA, GL.FLOAT, width, height, data);
	}

	public static Texture rgba16f(int width, int height) {
		return rgba16f(width, height, new byte[0]);
	}

	public static Texture rgba16u(int width, int height, byte[] data) {
		return new Texture(GL.RGBA16UI, GL.RGBA_INTEGER, GL.UNSIGNED_SHORT, width, height, data);
	}

	public static Texture rgba16u(int width, int height) {
		return rgba16u(width, height, new byte[0]);
	}

	public static Texture rgba16i(int width, int height, byte[] data) {
		return new Texture(GL.RGBA16I, GL.RGBA_INTEGER, GL.SHORT, width, height, data);
	}

	public static Texture rgba16i(int width, int height) {
		return rgba16i(width, height, new byte[0]);
	}

	public static Texture rgba32f(int width, int height, byte[] data) {
		return new Texture(GL.RGBA32F, GL.RGBA, GL.FLOAT, width, height, data);
	}

	public static Texture rgba32f(int width, int height) {
		return rgba32f(width, height, new byte[0]);
	}

	public static Texture rgba32u(int width, int height, byte[] data) {
		return new Texture(GL.RGBA32UI, GL.RGBA_INTEGER, GL.UNSIGNED_INT, width, height, data);
	}

	public static Texture rgba32u(int width, int height) {
		return rgba32u(width, height, new byte[0]);
	}

	public static Texture rgba32i(int width, int height, byte[] data) {
		return new Texture(GL.RGBA32I, GL.RGBA_INTEGER, GL.INT, width, height, data);
	}

	public static Texture rgba32i(int width, int height) {
		return rgba32i(width, height, new byte[0]);
	}

	public static Texture depth32f(int width, int height, byte[] data) {
		return new Texture(GL.DEPTH_COMPONENT32F, GL.DEPTH_COMPONENT, GL.FLOAT, width, height, data);
	}

	public static Texture depth32f(int width, int height) {
		return depth32f(width, height, new byte[0]);
	}	

	public int pixelSize() {
		switch (internal) {
			case GL.RGBA:
			case GL.RGBA8:
			case GL.DEPTH_COMPONENT32F:
			case GL.SRGB_ALPHA:
				return 4;

			case GL.RGBA16F:
			case GL.RGBA16UI:
			case GL.RGBA16I:
				return 8;

			case GL.RGBA32F:
			case GL.RGBA32UI:
			case GL.RGBA32I:
				return 16;

			default:
				throw new RuntimeException("Unsupported internal format");
		}
	}

	public static Texture rgba8White() {
		return rgba8(1, 1, new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF });
	}

	public static Texture RGBA8WHITE = rgba8White();

	public static Texture rgba8Black() {
		return rgba8(1, 1, new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF });
	}

	public static Texture RGBA8BLACK = rgba8Black();

	public static Texture rgba8Normal() {
		return rgba8(1, 1, new byte[] { (byte) 0x80, (byte) 0x80, (byte) 0xFF, (byte) 0xFF });
	}

	public static Texture RGBA8NORMAL = rgba8Normal();

	public static Texture integratedDFG() {
		byte[] bytes;

		try {
			bytes = ResourceLoader.getIntegratedDFG();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return new Texture(GL.RGBA8, GL.RGBA, GL.UNSIGNED_BYTE, 256, 256, bytes);
	}

	public static Texture INTEGRATED_DFG = integratedDFG();

	/**
	 * Reads the texture data from the GPU and returns it as a byte array.
	 */
	public byte[] readBytes() {
		int size = this.width * this.height * this.pixelSize();
		if (this.segment == null || this.segment.byteSize() != size) {
			this.segment = MemorySession.openImplicit().allocate(size);
			this.data = new byte[size];
		}

		this.bind();
		GL.GetTexImage(GL.TEXTURE_2D, 0, this.format, this.type, this.segment.address());
		this.unbind();

		ByteBuffer.wrap(this.data).put(this.segment.asByteBuffer());

		return this.data;
	}

	/**
	 * Reads the data of a single pixel from the GPU and returns it as a MemorySegment.
	 */
	public MemorySegment readPixel(int x, int y) {
		MemorySegment segment = MemorySession.openImplicit().allocate(this.pixelSize());

		GL.GetTextureSubImage(
			this.textureId, 0, 
			x, y, 0, 
			1, 1, 1, 
			this.format, this.type, 
			(int) segment.byteSize(), segment.address()
		);

		return segment;
	}

	/**
	 * Reads the data of a single pixel from the GPU and returns it as a byte array.
	 */
	public byte[] readPixelByte(int x, int y) {
		return this.readPixel(x, y).toArray(ValueLayout.JAVA_BYTE);
	}

	/**
	 * Reads the data of a single pixel from the GPU and returns it as an int array.
	 */
	public int[] readPixelInt(int x, int y) {
		return this.readPixel(x, y).toArray(ValueLayout.JAVA_INT);
	}

	/**
	 * Reads the data of a single pixel from the GPU and returns it as a float array.
	 */
	public float[] readPixelFloat(int x, int y) {
		return this.readPixel(x, y).toArray(ValueLayout.JAVA_FLOAT);
	}

	/**
	 * Binds the texture as GL_TEXTURE_2D.
	 */
	public void bind() {
		GL.BindTexture(GL.TEXTURE_2D, this.textureId);
	}

	/**
	 * Unbinds the texture as GL_TEXTURE_2D.
	 */
	public void unbind() {
		GL.BindTexture(GL.TEXTURE_2D, 0);
	}

	/**
	 * Fills the texture with data at the given address.
	 */
	public void clear(Addressable address) {
		GL.ClearTexImage(this.textureId, 0, this.format, this.type, address.address());
	}

	/**
	 * Fills the texture with data.
	 */
	public void clear(byte[] data) {
		MemorySegment segment = MemorySession.openImplicit().allocate(data.length);
		segment.copyFrom(MemorySegment.ofArray(data));
		this.clear(segment);
	}

	/**
	 * Fills the texture with data.
	 */
	public void clear(int[] data) {
		MemorySegment segment = MemorySession.openImplicit().allocate(data.length * 4);
		segment.copyFrom(MemorySegment.ofArray(data));
		this.clear(segment);
	}

	/**
	 * Fills the texture with data.
	 */
	public void clear(float[] data) {
		MemorySegment segment = MemorySession.openImplicit().allocate(data.length * 4);
		segment.copyFrom(MemorySegment.ofArray(data));
		this.clear(segment);
	}

	/**
	 * Fills the texture with data.
	 */
	public void clear(Vec4 color) {
		this.clear(color.bytes());
	}

	/**
	 * Fills the texture with data.
	 */
	public void clear(int r, int g, int b, int a) {
		MemorySegment segment = MemorySegment.ofArray(new int[] { r, g, b, a });
		this.clear(segment);
	}

	/**
	 * Deletes the texture.
	 *
	 * Using the texture after this will result in undefined behavior.
	 */
	public void delete() {
		MemorySegment segment = MemorySession.openImplicit().allocate(4);
		segment.set(ValueLayout.JAVA_INT, 0, this.textureId);
		GL.DeleteTextures(1, segment.address());

		this.textureId = -1;
		
		GL.assertNoError();
	}
}
