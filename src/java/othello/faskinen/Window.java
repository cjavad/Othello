package othello.faskinen;

/**
 * An abstract window backed by a system specific implementation.
 */
public abstract class Window {
	public static void initialize() {
		Window window = Window.create("context", 1, 1);	
		window.makeContextCurrent();
		Window.CURRENT_CONTEXT = window;
	}

	public static Window CURRENT_CONTEXT;

	/**
	 * Creates a new window.
	 * @param title The title of the window.
	 * @param width The width of the window.
	 * @param height The height of the window.
	 * @return The created window.
	 *
	 * The implementation of this method is system specific.
	 */
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

	/**
	 * Show the window.
	 */
	public abstract void show();

	/**
	 * Hide the window.
	 */
	public abstract void hide();

	/**
	 * Poll for events.
	 */
	public abstract void pollEvents();

	/**
	 * Swap the buffers.
	 */
	public abstract void swapBuffers();

	/**
	 * Make the window's context current.
	 */
	public abstract void makeContextCurrent();

	/**
	 * Destroy the window.
	 */
	public abstract void destroy();
}
