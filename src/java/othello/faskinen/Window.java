package othello.faskinen;

public abstract class Window {
	public static Window create(String title, int width, int height) {
		switch (Platform.get()) {
		case Windows:
			return new Win32Window(title, width, height);
		case Linux:
			return new X11Window(title, width, height);
		default:
			throw new RuntimeException("Unsupported platform: " + Platform.get());
		}
	}

	public abstract void show();

	public abstract void hide();

	public abstract void pollEvents();

	public abstract void swapBuffers();

	public abstract void makeContextCurrent();

	public abstract void destroy();
}
