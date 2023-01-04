package othello.faskinen;

public abstract class Window {
	public static Window create() {
		switch (Platform.getPlatform()) {
		case Windows:
			return new Win32Window();
		default:
			throw new RuntimeException("Unsupported platform: " + Platform.getPlatform());
		}
	}

	public abstract void destroy();
}
