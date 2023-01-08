package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A model containing a list of Primitives and a list of Textures.
 *
 * Models may ONLY be created after a valid OpenGL context has been created.
 *
 * Models are read from binary files created by the `masher` tool.
 * Run `cargo install --path tools/masher` to install the tool.
 * Run `masher -h` for more information.
 * Run `masher INSERT_FILE_HERE.(gltf/glb)` to convert a glTF file to a model file.
 */
public class Model {
	public Primitive[] primitives;
	public Texture[] textures;

	/**
	 * Creates a new empty model. 
	 */
	public Model() {
		this.primitives = new Primitive[0];
		this.textures = new Texture[0];
	}

	/**
	 * Reads a model from a MemorySegment.
	 * @param segment The segment to read from.
	 */
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

	/**
	 * Creates a new model.
	 * @param primitives The primitives of the model.
	 */
	public Model(Primitive[] primitives) {
		this.primitives = primitives;
	}

	/**
	 * Reads a model from a file.
	 * @param path The path to the file.
	 * @return The model.
	 *
	 * If the file is invalid, a fatal error is likely to occur.
	 */
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
