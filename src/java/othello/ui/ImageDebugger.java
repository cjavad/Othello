package othello.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import othello.faskinen.Buffer;
import othello.faskinen.Faskinen;

public class ImageDebugger extends SceneProvider {
	WritableImage image;
	PixelWriter writer;
	ImageView imageView;
	float time;
	Faskinen faskinen;

	public ImageDebugger(SceneManager manager) {
		super(manager, "ImageDebugger");

		this.faskinen = new Faskinen();	

		this.image = new WritableImage(this.faskinen.imageWidth, this.faskinen.imageHeight);
		this.writer = this.image.getPixelWriter();

		this.renderImage();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				renderImage();
			}
		};

		timer.start();

		this.imageView = new ImageView(this.image);
		StackPane root = new StackPane();
		root.getChildren().add(this.imageView);
		Scene scene = new Scene(root, manager.getWidth(), manager.getHeight());
		this.setScene(scene);
	}

	public void renderImage() {
		Buffer buffer = faskinen.renderImage();	
		this.writer.setPixels(
			0, 0, 
			faskinen.imageWidth, faskinen.imageHeight, 
			PixelFormat.getByteBgraInstance(), 
			buffer.bytes(), 
			0, faskinen.imageWidth * 4
		);
	}
}
