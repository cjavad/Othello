package othello.components.board;

import java.util.HashSet;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.faskinen.Faskinen;
import othello.faskinen.Mat4;
import othello.faskinen.Model;
import othello.faskinen.Vec3;
import othello.faskinen.opengl.GL;
import othello.game.Board2D;
import othello.game.Player;
import othello.game.Space;

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
	Model boardFrame;
	Model spaceWhite;
	Model spaceBlack;

	public BoardViewer3D(SceneManager manager) {
		this(manager, new Board2D(new Player[] { new Player(0), new Player(1) }, false));
	}

	public BoardViewer3D(SceneManager manager, Board2D board) {
		super(manager, "BoardViewer3D");

		int width = this.getSceneManager().getWidth();
		int height = this.getSceneManager().getHeight();

		this.faskinen = new Faskinen(width, height);

		this.faskinen.camera.position = new Vec3(0, 3, 5);
		this.faskinen.camera.pitch = -0.5f;

		this.chipWhite = Model.read("chip_white.bin");
		this.chipBlack = Model.read("chip_black.bin");
		this.boardFrame = Model.read("board_frame.bin");
		this.spaceWhite = Model.read("space_white.bin");
		this.spaceBlack = Model.read("space_black.bin");

		this.image = new WritableImage(this.faskinen.imageWidth, this.faskinen.imageHeight);
		this.writer = this.image.getPixelWriter();
		this.imageView = new ImageView(this.image);
		this.imageView.setScaleY(-1);

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {	
				renderImage(board);
			}
		};
		timer.start();

		this.root = new StackPane();
		root.getChildren().add(this.imageView);
		this.scene = new Scene(this.root, manager.getWidth(), manager.getHeight());

		this.scene.setOnMouseMoved(this::handleMouseMoved);
		this.scene.setOnMouseDragged(this::handleMouseDragged);

		this.scene.setOnKeyPressed(this::handleKeyPressed);
		this.scene.setOnKeyReleased(this::handleKeyReleased);

		this.setScene(this.scene);
	}

	public void renderImage(Board2D board) {
		int newWidth = (int) this.scene.getWidth();
		int newHeight = (int) this.scene.getHeight();
		this.handleResize(newWidth, newHeight);

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

		Vec3 boardPosition = new Vec3(0, 0, 0);
		this.faskinen.pushModel(this.boardFrame, Mat4.translation(boardPosition));

		for (int x = 0; x < board.getColumns(); x++) {
			for (int y = 0; y < board.getRows(); y++) {
				Vec3 position = new Vec3(x - 3.5f, 0, y - 3.5f);

				int id = x + y * 8;

				if ((x + y) % 2 == 0) {
					this.faskinen.pushModel(this.spaceWhite, Mat4.translation(position), id);
				} else {
					this.faskinen.pushModel(this.spaceBlack, Mat4.translation(position), id);
				}

				position.y = 0.3f;

				Space space = new Space(x, y);
				int playerId = board.getSpace(space);
				if (playerId == -1) continue;

				Player player = board.getPlayer(playerId);

				if (player.getColor() == "#FFFFFF") {
					this.faskinen.pushModel(this.chipWhite, Mat4.translation(position), id);
				} else if (player.getColor() == "#101010") {
					this.faskinen.pushModel(this.chipBlack, Mat4.translation(position), id);
				} else {
					System.out.println("Unknown player color: " + player.getColor());
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

	public int getPixelId(MouseEvent event) {
		Point2D point = this.imageView.localToScene(0, 0);
		System.out.println("x="+event.getX()+" y="+event.getY());
		System.out.println("x = " + event.getX() + ", y = " + (event.getScreenY() - this.getScene().getWindow().getY() - this.getScene().getY()));
		return this.faskinen.getPixelId((int) event.getX(), (int) event.getY());
	}

	public int getPixelId(int x, int y) {
		return this.faskinen.getPixelId(x, y);
	}

	public void handleResize(int width, int height) {	
		if (this.faskinen.imageWidth == width && this.faskinen.imageHeight == height) {
			return;
		}

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
