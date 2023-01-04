package othello.faskinen;

/**
 * Describes the platform the program is running on.
 */
public enum Platform {
	Windows, 
	Linux, 
	Mac;

	private static Platform platform = null;
	public static Platform get() {
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
}
