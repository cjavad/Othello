package othello.faskinen.opengl;

import othello.faskinen.Platform;

public class GL {

    static
    {
        if (Platform.get() == Platform.Windows) {
            System.loadLibrary("opengl32");
        }
        else if (Platform.get() == Platform.Linux) {

        }
        else {
            throw new RuntimeException("Unsupported platform");
        }
    }

}
