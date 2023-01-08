package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.file.Files;
import java.nio.file.Paths;

import othello.faskinen.opengl.GL;

/**
 * An pre-baked environment.
 *
 * Environments may ONLY be created after a valid OpenGL context has been created.
 *
 * Environments are read from binary files created by the `bakery` tool.
 * Run `cargo install --path tools/bakery` to install the tool.
 * Run `bakery -h` for more information.
 */
public class Environment {
	public float intensity = 0.75f;

	public int irradianceSize;
	public int irradianceId;

	public int indirectSize;
	public int indirectMipLevels;
	public int indirectId;

	public int skySize;
	public int skyId;

	/**
	 * Reads an environment from a MemorySegment.
	 * @param segment The segment to read from.
	 */
	public Environment(MemorySegment segment) {
		this.irradianceSize = segment.get(ValueLayout.JAVA_INT, 0);
		this.indirectSize = segment.get(ValueLayout.JAVA_INT, 4);
		this.indirectMipLevels = segment.get(ValueLayout.JAVA_INT, 8);
		this.skySize = segment.get(ValueLayout.JAVA_INT, 12);

		int memoryOffset = 16;

		MemorySegment textures = MemorySession.openImplicit().allocate(4 * 3);
		GL.GenTextures(3, textures.address());

		this.irradianceId = textures.get(ValueLayout.JAVA_INT, 0);
		this.indirectId = textures.get(ValueLayout.JAVA_INT, 4);
		this.skyId = textures.get(ValueLayout.JAVA_INT, 8);	

		GL.BindTexture(GL.TEXTURE_CUBE_MAP, this.irradianceId);
		for (int i = 0; i < 6; i++) {
			int size = this.irradianceSize * this.irradianceSize * 8;
			MemorySegment slice = segment.asSlice(memoryOffset);
			memoryOffset += size;

			GL.TexImage2D(
				GL.TEXTURE_CUBE_MAP_POSITIVE_X + i, 
				0, 
				GL.RGBA16F, 
				this.irradianceSize, 
				this.irradianceSize, 
				0, 
				GL.RGBA, 
				GL.HALF_FLOAT, 
				slice.address()
			);
		}

		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_MIN_FILTER, GL.LINEAR);
		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_MAG_FILTER, GL.LINEAR);

		GL.BindTexture(GL.TEXTURE_CUBE_MAP, this.indirectId);
		for (int i = 0; i < 6; i++) {
			for (int mip = 0; mip < this.indirectMipLevels; mip++) {
				int mipSize = this.indirectSize >> mip;
				int size = mipSize * mipSize * 8;

				MemorySegment slice = segment.asSlice(memoryOffset, size);
				memoryOffset += size;

				GL.TexImage2D(
					GL.TEXTURE_CUBE_MAP_POSITIVE_X + i, 
					mip, 
					GL.RGBA, 
					mipSize, 
					mipSize, 
					0, 
					GL.RGBA, 
					GL.HALF_FLOAT, 
					slice.address()
				);
			}
		}
	
		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_MIN_FILTER, GL.LINEAR_MIPMAP_LINEAR);
		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_MAG_FILTER, GL.LINEAR);
		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_MAX_LEVEL, this.indirectMipLevels - 1);
		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_WRAP_S, GL.CLAMP_TO_EDGE);
		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_WRAP_T, GL.CLAMP_TO_EDGE);
		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_WRAP_R, GL.CLAMP_TO_EDGE);
		GL.GenerateMipmap(GL.TEXTURE_CUBE_MAP);

		GL.BindTexture(GL.TEXTURE_CUBE_MAP, this.skyId);
		for (int i = 0; i < 6; i++) {
			int size = this.skySize * this.skySize * 8;
			MemorySegment slice = segment.asSlice(memoryOffset, size);
			memoryOffset += size;

			GL.TexImage2D(
				GL.TEXTURE_CUBE_MAP_POSITIVE_X + i, 
				0, 
				GL.RGBA16F, 
				this.skySize, 
				this.skySize, 
				0, 
				GL.RGBA, 
				GL.HALF_FLOAT,
				slice.address()
			);
		}

		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_MIN_FILTER, GL.LINEAR);
		GL.TexParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_MAG_FILTER, GL.LINEAR);

		GL.BindTexture(GL.TEXTURE_CUBE_MAP, 0);
	}

	/**
	 * Reads an environment from a file.
	 * @param path The path to the file to read from.
	 * @return The environment.
	 */
	public static Environment read(String path) {
		byte[] bytes;

		try {
			bytes = Files.readAllBytes(Paths.get(path));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		MemorySegment segment = MemorySession.openShared().allocate(bytes.length, 4);
		segment.copyFrom(MemorySegment.ofArray(bytes));

		return new Environment(segment);
	}
}
