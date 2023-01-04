package othello.faskinen.opengl;

import othello.faskinen.Platform;

public class GL {

    static
    {
        switch (Platform.get())
        {
            case Windows:
                System.loadLibrary("opengl32");
                break;
            case Linux:
                System.loadLibrary("GL");
                break;
            default:
                throw new RuntimeException("Unsupported platform, not a real gamer");
        }
    }



}
