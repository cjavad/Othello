package othello.ui;

import java.util.HashSet;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import othello.faskinen.Buffer;
import othello.faskinen.Faskinen;
import othello.faskinen.PieceModel;
import othello.faskinen.Vec3;

public class BoardViewer3D extends SceneProvider {
	WritableImage image;
	PixelWriter writer;
	ImageView imageView;

	Faskinen faskinen;

	float mouseX, mouseY;
	HashSet<String> keys = new HashSet<String>();

	public BoardViewer3D(SceneManager manager) {
		super(manager, "BoardViewer3D");

		this.faskinen = new Faskinen();	

		this.faskinen.pieces = new PieceModel[64];
		
		for (int x = 0; x < 8; x++) {
			for (int z = 0; z < 8; z++) {
				int i = x + z * 8;

				this.faskinen.pieces[i] = new PieceModel();
				this.faskinen.pieces[i].position = new Vec3(x, -3, z);

				if ((x + z) % 2 == 0) {
					this.faskinen.pieces[i].color = new Vec3(1.0f, 1.0f, 1.0f);
				} else {
					this.faskinen.pieces[i].color = new Vec3(0.01f, 0.01f, 0.01f);
				}
			}
		}

		this.image = new WritableImage(this.faskinen.imageWidth, this.faskinen.imageHeight);
		this.writer = this.image.getPixelWriter();
		this.imageView = new ImageView(this.image);
		this.imageView.setScaleY(-1);

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				renderImage();
			}
		};
		timer.start();

		StackPane root = new StackPane();
		root.getChildren().add(this.imageView);
		Scene scene = new Scene(root, manager.getWidth(), manager.getHeight());

		scene.setOnMouseMoved(this::handleMouseMoved);
		scene.setOnMouseDragged(this::handleMouseDragged);

		scene.setOnKeyPressed(this::handleKeyPressed);
		scene.setOnKeyReleased(this::handleKeyReleased);

		this.setScene(scene);
	}

	public void renderImage() {
		Vec3 movement = new Vec3();

		if (this.keys.contains("W")) {
			movement = movement.add(this.faskinen.camera.forward());
		}

		if (this.keys.contains("S")) {
			movement = movement.sub(this.faskinen.camera.forward());
		}

		if (this.keys.contains("A")) {
			movement = movement.sub(this.faskinen.camera.right());
		}

		if (this.keys.contains("D")) {
			movement = movement.add(this.faskinen.camera.right());
		}

		movement.y = 0.0f;

		Vec3 newPosition = this.faskinen.camera.position.add(movement.mul(0.1f));
		this.faskinen.camera.position = newPosition;

		Buffer buffer = faskinen.renderImage();	
		this.writer.setPixels(
			0, 0, 
			faskinen.imageWidth, faskinen.imageHeight, 
			PixelFormat.getByteBgraInstance(), 
			buffer.bytes(), 
			0, faskinen.imageWidth * 4
		);
	}

	public void handleMouseDragged(MouseEvent event) {
		if (!event.isSecondaryButtonDown()) return;

		float x = (float) event.getX();
		float y = (float) event.getY();
		float deltaX = x - this.mouseX;
		float deltaY = y - this.mouseY;
		this.mouseX = x;
		this.mouseY = y;

		this.faskinen.camera.yaw += deltaX / 1000.0f;
		this.faskinen.camera.pitch += deltaY / 1000.0f;
	}

	public void handleMouseMoved(MouseEvent event) {
		float x = (float) event.getX();
		float y = (float) event.getY();
		this.mouseX = x;
		this.mouseY = y;
	}

	public void handleKeyPressed(KeyEvent event) {
		this.keys.add(event.getCode().toString());
	}

	public void handleKeyReleased(KeyEvent event) {
		this.keys.remove(event.getCode().toString());
	}
}