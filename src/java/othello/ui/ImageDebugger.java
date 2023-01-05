package othello.ui;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import othello.faskinen.Buffer;
import othello.faskinen.Faskinen;

public class ImageDebugger extends SceneProvider {
	public ImageDebugger(SceneManager manager) {
		super(manager, "ImageDebugger");

		Faskinen faskinen = new Faskinen();	
		Buffer buffer = faskinen.renderImage();

		WritableImage image = new WritableImage(faskinen.imageWidth, faskinen.imageHeight);
		PixelWriter writer = image.getPixelWriter();

		writer.setPixels(
			0, 0, 
			faskinen.imageWidth, faskinen.imageHeight, 
			PixelFormat.getByteBgraInstance(), 
			buffer.bytes(), 
			0, faskinen.imageWidth * 4
		);

		ImageView imageView = new ImageView(image);
		StackPane root = new StackPane();
		root.getChildren().add(imageView);
		Scene scene = new Scene(root, manager.getWidth(), manager.getHeight());
		this.setScene(scene);
	}
}
