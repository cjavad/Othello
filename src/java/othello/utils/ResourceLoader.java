package othello.utils;

import othello.game.Board2D;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/*
    Basic resource loader to find and read model data and the like.
 */
public class ResourceLoader {
    public static String getShaderResource(String shaderName) throws IOException {
        String shaderPath = "shaders/" + shaderName;
        InputStream shaderStream = ResourceLoader.resourceStream(shaderPath);
        if (shaderStream == null) {
            throw new IOException("Could not find shader " + shaderPath);
        }
        byte[] shaderBytes = shaderStream.readAllBytes();
        return new String(shaderBytes);
    }

    public static byte[] getModelResource(String modelName) throws IOException {
        String modelPath = "models/" + modelName;
        InputStream modelStream = ResourceLoader.resourceStream(modelPath);
        if (modelStream == null) {
            throw new IOException("Could not find model " + modelPath);
        }
        return modelStream.readAllBytes();
    }

    public static byte[] getIntegratedDFG() throws IOException {
        String path = "misc/integratedDFG";
        InputStream integratedDFDStream = ResourceLoader.resourceStream(path);
        if (integratedDFDStream == null) {
            throw new IOException("Could not find integrated DFD " + path);
        }
        return integratedDFDStream.readAllBytes();
    }

    public static InputStream resourceStream(String path) {
        System.out.println("Looking for resource for " + path + ": " + ClassLoader.getSystemClassLoader().getResource(path));
        return ClassLoader.getSystemClassLoader().getResourceAsStream(path);
    }
    public static byte[] readAsBytes(String path) throws IOException {
        return ResourceLoader.resourceStream(path).readAllBytes();
    }

    public static String readAsString(String path) throws IOException {
        return Arrays.toString(ResourceLoader.readAsBytes(path));
    }

    public static HashMap<String, Integer> readSavedConfig() {
        String path = ResourceLoader.getConfigFolderPath() + "/profile";
        try {
            return ResourceLoader.readObject(path);
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static void saveConfig(HashMap<String, Integer> options) {
        String path = ResourceLoader.getConfigFolderPath() + "/profile";

        try {
            ResourceLoader.writeObject(options, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] listOfSavedGames() {
        String path = ResourceLoader.getConfigFolderPath() + "/saves";
        File folder = new File(path);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null || listOfFiles.length < 1) return new String[0];

        String[] savedGames = new String[listOfFiles.length];

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                savedGames[i] = listOfFiles[i].getName();
            }
        }
        return savedGames;
    }

    public static void saveGameObject(Board2D game, String name) {
        String path = ResourceLoader.getConfigFolderPath() + "/saves/" + name;
        try {
            ResourceLoader.writeObject(game, path);
        } catch (IOException e) {
            // No
        }
    }

    public static Board2D readGameObject(String name) {
        String path = ResourceLoader.getConfigFolderPath() + "/saves/" + name;
        try {
            return ResourceLoader.readObject(path);
        } catch (IOException | ClassNotFoundException e) {
            // No
        }
        return null;
    }

    private static <T> T readObject(String path) throws IOException, ClassNotFoundException {
       return (T) new ObjectInputStream(new FileInputStream(path)).readObject();
    }

    private static void writeObject(Object obj, String path) throws IOException {
        // Serialize the board with Serializable
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(obj);
        oos.close();
    }

    public static String getConfigFolderPath() {
        // Ensure folder exists, otherwise create it
        String path = "./data";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return path;
    }
}
