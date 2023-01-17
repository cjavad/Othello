package othello;

import javafx.application.Application;
import javafx.stage.Stage;
import othello.faskinen.Platform;
import othello.faskinen.Window;
import othello.ui.SceneManager;
import othello.ui.StartMenu;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
		Window.initialize();

        stage.setTitle("Othello");
        SceneManager sceneManager = new SceneManager(stage, 800, 600);
        StartMenu startMenu = new StartMenu(sceneManager);
        sceneManager.setActive(startMenu);
    }

    private static final String[] JVM_ARGS = {"--enable-preview", "--enable-native-access=ALL-UNNAMED"};

    public static void main(String[] args)
    {
        requireVersion(19, 0, 0);

        boolean hasArgs = true;

        for (String arg : JVM_ARGS) {
            if (!checkJVMArg(arg)) hasArgs = false;
        }

        if (!hasArgs) {
            runWithJVMArgs((args));
        }

        launch(args);
    }

    private static boolean checkJVMArg(String name) {
        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (arg.equals(name)) return true;
        }
        return false;
    }

    private static boolean checkModule(String name) {
        return ModuleLayer.boot().findModule(name).isPresent();
    }

    private static void requireVersion(int major, int minor, int patch)
    {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) version = version.substring(2);

        String[] vlist = version.split("[.|_]");

        int vmaj = Integer.parseInt(vlist[0]);
        int vmin = Integer.parseInt(vlist[1]);
        int vpch = Integer.parseInt(vlist[2]);

        if (vmaj > major) return;
        if (vmaj == major)
        {
            if (vmin > minor) return;
            if (vmin == minor)
            {
                if (vpch >= patch) return;
            }
        }

        System.out.println("Application requires java " + major + '.' + minor + '.' + patch + " as a minumum but got version " + vmaj + '.' + vmin + '.' + vpch + " instead");

        System.exit(0);
    }

    // re run the program with jdk.incubator.vector (preserving given JVM args and program args)
    private static void runWithJVMArgs(String[] args)
    {
        try {
            String jar = App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (Platform.isWindows()) jar = jar.substring(1, jar.length());

            String java = System.getProperty("java.home") + "/bin/java";
            if (Platform.isWindows()) java += ".exe";

            ProcessBuilder builder = new ProcessBuilder(java);
            builder.command().addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
            for (int i = 0; i < JVM_ARGS.length; i++) builder.command().add(JVM_ARGS[i]);

            if (jar.endsWith(".jar"))
            {
                builder.command().add("-jar");
                builder.command().add(jar);
            }
            else
            {
                if (Platform.isWindows()) jar += ';';
                else jar += ':';
                jar += Paths.get(".").toAbsolutePath().normalize().toString() + "/build/resources/main";

                builder.command().add("-cp");
                builder.command().add(jar);
                builder.command().add(App.class.getName());
            }

            for (int i = 0; i < args.length; i++) builder.command().add(args[i]);

            builder.inheritIO();
            builder.directory(Paths.get(".").toAbsolutePath().normalize().toFile());
            Process proc = builder.start();
            proc.waitFor();
            System.exit(proc.exitValue());
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load required modules, exiting");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Process interrupted, complete execution failed");
        }
        System.exit(-1);
    }
}
