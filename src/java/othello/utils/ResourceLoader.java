package othello.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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
}
