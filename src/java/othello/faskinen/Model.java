package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Model {
	public Primitive[] primitives;
	public Texture[] textures;

	public Model() {
		this.primitives = new Primitive[0];
	}

	public Model(MemorySegment segment) {
		int primitiveCount = segment.get(ValueLayout.JAVA_INT, 0);
		this.primitives = new Primitive[primitiveCount];

		long offset = 4;
		for (int i = 0; i < primitiveCount; i++) {
			this.primitives[i] = new Primitive(segment.asSlice(offset));
			offset += this.primitives[i].sizeof();
		}

		int textureCount = segment.get(ValueLayout.JAVA_INT, offset);
		this.textures = new Texture[textureCount];

		offset += 4;
		for (int i = 0; i < textureCount; i++) {
			int pixelSize = segment.get(ValueLayout.JAVA_INT, offset);
			int width = segment.get(ValueLayout.JAVA_INT, offset + 4);
			int height = segment.get(ValueLayout.JAVA_INT, offset + 8);
			offset += 12;

			System.out.println("Loading texture " + i + " with size " + width + "x" + height + " and pixel size " + pixelSize);

			int imageSize = width * height * pixelSize;
			MemorySegment data = MemorySession.openImplicit().allocate(width * height * pixelSize);
			data.copyFrom(segment.asSlice(offset, imageSize));
			this.textures[i] = Texture.rgba8(width, height, data.toArray(ValueLayout.JAVA_BYTE));	

			offset += imageSize;
		}
	}

	public Model(Primitive[] primitives) {
		this.primitives = primitives;
	}

	public static Model read(String path) {
		byte[] bytes;

		try {
			bytes = Files.readAllBytes(Paths.get(path));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		MemorySegment segment = MemorySession.openShared().allocate(bytes.length, 8);
		segment.copyFrom(MemorySegment.ofArray(bytes));

		return new Model(segment);
	}
}
