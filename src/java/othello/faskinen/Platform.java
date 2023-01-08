package othello.faskinen;

/**
 * Describes the platform the program is running on.
 *
 * Supported platforms are Windows, Linux and MacOS.
 */
public enum Platform {
	/**
	 * The program is running on Windows and will use the Win32 API.
	 */
	Windows, 
	/**
	 * The program is running on Linux and will use the X11 API.
	 *
	 * Wayland is not supported.
	 */
	Linux, 
	/**
	 * The program is running on MacOS and is not currently supported.
	 */
	Mac;

	private static Platform platform = null;

	/**
	 * Get the current platform by checking the system property `os.name`.
	 * @return The current platform.
	 */
	public static Platform get() {
		// if the platform has already been cached, return it
		if (Platform.platform != null) {
			return Platform.platform;
		}

		String osName = System.getProperty("os.name");
		if (osName.contains("Windows")) {
			Platform.platform = Platform.Windows;

			return Platform.platform;
		} else if (osName.contains("Linux")) {
			Platform.platform = Platform.Linux;

			return Platform.platform;
		} else if (osName.contains("Mac")) {
			Platform.platform = Platform.Mac;

			return Platform.platform;
		} else {
			throw new RuntimeException("Unknown platform: " + osName);
		}
	}

	public static boolean isWindows() {
		return Platform.get() == Platform.Windows;
	}

	public static boolean isLinux() {
		return Platform.get() == Platform.Linux;
	}

	public static boolean isMac() {
		return Platform.get() == Platform.Mac;
	}
}
