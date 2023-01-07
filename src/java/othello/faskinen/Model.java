package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Model {
	public Primitive[] primitives;

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
