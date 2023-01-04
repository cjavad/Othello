package othello.faskinen.opengl;

import othello.faskinen.Platform;

public class GL {

    static
    {
        if (Platform.getContext() == Platform.Windows) {
            System.loadLibrary("opengl32");
        }
        else if (Platform.getContext() == Platform.Linux) {
            
        }
        else {
            throw new RuntimeException("Unsupported platform");
        }
    }

}
