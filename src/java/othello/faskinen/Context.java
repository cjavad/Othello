package othello.faskinen;

public class Context {
	public static final Platform platform = Context.getPlatform();

	/**
	 * Queries the underlying system to determine the platform the program is running on.
	 *
	 * @return The platform the program is running on.
	 */
	public static Platform getPlatform() {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			return Platform.Windows;
		} else if (os.contains("mac")) {
			return Platform.Mac;
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			return Platform.Linux;
		} else {
			throw new RuntimeException("Unknown platform: " + os);
		}
	}

	public Context() {}
}
