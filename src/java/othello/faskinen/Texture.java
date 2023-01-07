package othello.faskinen;

import java.lang.foreign.Addressable;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.file.Files;
import java.nio.file.Path;

import othello.faskinen.opengl.GL;

public class Texture {	
	public int textureId;
	public int width;
	public int height;
	public int internal;
	public int format;
	public int type;
	public MemorySegment segment;

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

		GL.TexParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_S, GL.REPEAT);
		GL.TexParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_T, GL.REPEAT);

		if (type == GL.FLOAT) {
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

	public static Texture integratedDFG() {
		byte[] bytes;

		try {
			bytes = Files.readAllBytes(Path.of("integratedDFG"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return new Texture(GL.RGBA8, GL.RGBA, GL.UNSIGNED_BYTE, 256, 256, bytes);
	}

	public byte[] read() {
		int size = this.width * this.height * this.pixelSize();

		if (this.segment == null || this.segment.byteSize() != size) {
			this.segment = MemorySession.openImplicit().allocate(size);
		}

		this.bind();
		GL.GetTexImage(GL.TEXTURE_2D, 0, this.format, this.type, this.segment.address());
		this.unbind();

		return this.segment.toArray(ValueLayout.JAVA_BYTE);
	}

	public void bind() {
		GL.BindTexture(GL.TEXTURE_2D, this.textureId);
	}

	public void unbind() {
		GL.BindTexture(GL.TEXTURE_2D, 0);
	}

	public void clear(Addressable address) {
		GL.ClearTexImage(this.textureId, 0, this.format, this.type, address.address());
	}

	public void clear(byte[] data) {
		MemorySegment segment = MemorySession.openImplicit().allocate(data.length);
		segment.copyFrom(MemorySegment.ofArray(data));
		this.clear(segment);
	}

	public void clear(Vec4 color) {
		this.clear(color.bytes());
	}

	public void clear(int r, int g, int b, int a) {
		MemorySegment segment = MemorySegment.ofArray(new int[] { r, g, b, a });
		this.clear(segment);
	}

	public void delete() {
		GL.DeleteTextures(1, MemorySegment.ofArray(new int[] { this.textureId }).address());
	}
}
