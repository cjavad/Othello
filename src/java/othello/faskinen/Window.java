package othello.faskinen;

public abstract class Window {
	public static Window create() {
		if (Context.platform == Platform.Windows) {
			return new Win32Window();
		} else {
			throw new RuntimeException("Unsupported platform: " + Context.platform);
		}
	}
}
