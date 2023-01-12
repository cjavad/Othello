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

import othello.faskinen.Faskinen;
import othello.faskinen.Mat4;
import othello.faskinen.Model;
import othello.faskinen.Vec3;
import othello.faskinen.opengl.GL;

public class BoardViewer3D extends SceneProvider {
	WritableImage image;
	PixelWriter writer;
	ImageView imageView;
	StackPane root;
	Scene scene;

	Faskinen faskinen;

	float mouseX, mouseY;
	HashSet<String> keys = new HashSet<String>();

	Model chipWhite;
	Model chipBlack;
	Model board;

	public BoardViewer3D(SceneManager manager) {
		super(manager, "BoardViewer3D");

		int width = this.getSceneManager().getWidth();
		int height = this.getSceneManager().getHeight();

		this.faskinen = new Faskinen(width, height);

		this.faskinen.camera.position = new Vec3(0, 3, -5);

		this.chipWhite = Model.read("chip_white.bin");
		this.chipBlack = Model.read("chip_black.bin");
		this.board = Model.read("chess_board.bin");

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

		this.root = new StackPane();
		root.getChildren().add(this.imageView);
		this.scene = new Scene(this.root, manager.getWidth(), manager.getHeight());

		// handle resize
		scene.widthProperty().addListener((obs, oldVal, newVal) -> {
			int newWidth = (int) this.root.getWidth();
			int newHeight = (int) this.root.getHeight();
			this.handleResize(newWidth, newHeight);
		});
		scene.heightProperty().addListener((obs, oldVal, newVal) -> {	
			int newWidth = (int) this.root.getWidth();
			int newHeight = (int) this.root.getHeight();
			this.handleResize(newWidth, newHeight);
		});

		scene.setOnMousePressed(this::handleMousePressed);

		scene.setOnMouseMoved(this::handleMouseMoved);
		scene.setOnMouseDragged(this::handleMouseDragged);

		scene.setOnKeyPressed(this::handleKeyPressed);
		scene.setOnKeyReleased(this::handleKeyReleased);

		this.setScene(this.scene);
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

		Vec3 newPosition = this.faskinen.camera.position.add(movement.normalize().mul(0.1f));
		this.faskinen.camera.position = newPosition;

		this.faskinen.clear();

		Vec3 boardPosition = new Vec3(0, -1.25f, 0);
		this.faskinen.pushModel(this.board, Mat4.translation(boardPosition));

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Vec3 position = new Vec3(x - 3.5f, 0.05f, y - 3.5f);

				int id = x + y * 8;

				if ((x + y) % 2 == 0) {
					this.faskinen.pushModel(this.chipWhite, Mat4.translation(position), id);
				} else {
					this.faskinen.pushModel(this.chipBlack, Mat4.translation(position), id);
				}
			}
		}

		GL.Enable(GL.CULL_FACE);

		this.faskinen.geometryPass();
		this.faskinen.shadowPass();

		GL.Disable(GL.CULL_FACE);

		this.faskinen.clearRenderStack();

		GL.Disable(GL.DEPTH_TEST);

		this.faskinen.light();
		this.faskinen.tonemap();

		byte[] bytes = faskinen.imageBytes();	
		this.writer.setPixels(
			0, 0, 
			faskinen.imageWidth, faskinen.imageHeight, 
			PixelFormat.getByteBgraInstance(), 
			bytes, 
			0, faskinen.imageWidth * 4
		);
	}

	public void handleMousePressed(MouseEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		int id = this.faskinen.getPixelId(x, y);

		System.out.println("id: " + id);
	}

	public void handleResize(int width, int height) {	
		this.faskinen.resize(width, height);
		this.image = new WritableImage(this.faskinen.imageWidth, this.faskinen.imageHeight);
		this.writer = this.image.getPixelWriter();
		this.imageView.setImage(this.image);
	}

	public void handleMouseDragged(MouseEvent event) {
		if (!event.isSecondaryButtonDown()) return;

		float x = (float) event.getX();
		float y = (float) event.getY();
		float deltaX = x - this.mouseX;
		float deltaY = y - this.mouseY;
		this.mouseX = x;
		this.mouseY = y;

		this.faskinen.camera.yaw -= deltaX / 1000.0f;
		this.faskinen.camera.pitch -= deltaY / 1000.0f;
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
