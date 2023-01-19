package othello.components.board;

import java.util.HashSet;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.events.MoveEvent;
import othello.events.SettingsEvent;
import othello.faskinen.*;
import othello.faskinen.opengl.GL;
import othello.game.Board2D;
import othello.game.Change;
import othello.game.Move;
import othello.game.Player;
import othello.game.Space;

public class BoardScene3D extends SceneProvider {
	WritableImage image;
	PixelWriter writer;
	ImageView imageView;
	Pane root;
	Scene scene;

	Faskinen faskinen;

	float mouseX, mouseY;
	HashSet<String> keys = new HashSet<>();

	static Model chipWhite = Model.read("chip_white.bin");
	static Model chipBlack = Model.read("chip_black.bin");
	static Model boardFrame = Model.read("board_frame.bin");
	static Model spaceWhite = Model.read("space_white.bin");
	static Model spaceBlack = Model.read("space_black.bin");
	static Model rock = Model.read("rock.bin");

	ParticleSystem particles = new ParticleSystem(1000);
                            
	Board2D board;
	Board2D staticBoard;
	float[] animations;
	float[] heights;

	float dt = 0.05f;
	float time = 0.0f;
	float cameraShake = 0.0f;

	public BoardScene3D(SceneManager manager) {
		this(manager, manager.getNewBoard());
	}

	public BoardScene3D(SceneManager manager, Board2D board) {
		super(manager, "BoardViewer3D");

		this.board = board;
		this.staticBoard = null;

		this.animations = new float[board.getRows() * board.getColumns()];
		this.heights = new float[board.getRows() * board.getColumns()];
		for (int i = 0; i < this.animations.length; i++) {
			this.animations[i] = 1.0f;
			this.heights[i] = 0.0f;
		}

		int width = this.getSceneManager().getWidth();
		int height = this.getSceneManager().getHeight();

		this.faskinen = new Faskinen(width, height);

		this.faskinen.camera.position = new Vec3(0, 7, 7);
		this.faskinen.camera.pitch = -0.9f;

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

		this.createNode("timer", timer);

		this.getSceneManager().getActiveScene().setOnMousePressed(this::handleMousePressed);
		this.getSceneManager().getActiveScene().setOnMouseMoved(this::handleMouseMoved);
		this.getSceneManager().getActiveScene().setOnMouseDragged(this::handleMouseDragged);

		this.getSceneManager().getActiveScene().setOnKeyPressed(this::handleKeyPressed);
		this.getSceneManager().getActiveScene().setOnKeyReleased(this::handleKeyReleased);

//		Commented out for now
//		Particle particle = new Particle();
//		particle.position = new Vec3(0, 3, 0);
//		this.particles.pushParticle(particle);

		this.root = new Pane();
		this.root.getChildren().add(this.imageView);
		this.root.setPrefWidth(width * 2/3);
		this.scene = new Scene(this.root, width, height);
		createNode("root", this.root);
	}

	public static float animateHeight(float time, float height) {
		float b = height * 4.0f;
		float a = -b;

		return Math.max(a * time * time + b * time, 0.0f);
	}

	public static float animateRotation(float time) {
		time = Math.min(Math.max(time, 0.0f), 1.0f);

		return time * (float) Math.PI * 2.0f;
	}

	public void renderImage() {
		int newWidth = (int) this.root.getWidth();
		int newHeight = (int) this.root.getHeight();
		this.handleResize(newWidth, newHeight);

		var currentBoard = staticBoard == null ? board : staticBoard;

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

		this.faskinen.camera.position = this.faskinen.camera.position.add(movement.normalize().mul(0.1f));

		Vec3 cameraShake = new Vec3(
			(float) Math.random() * this.cameraShake,
			(float) Math.random() * this.cameraShake,
			(float) Math.random() * this.cameraShake
		);
		this.cameraShake *= 0.9f;

		this.time += this.dt;

		this.faskinen.camera.position = this.faskinen.camera.position.add(cameraShake);

		this.faskinen.clear();

		this.particles.update(0.032f);

		RenderStack stack = new RenderStack();

		Vec3 boardPosition = new Vec3(0, 0, 0);
		stack.pushModel(BoardScene3D.boardFrame, Mat4.translation(boardPosition));

		for (Space space : currentBoard) {
			Vec3 position = new Vec3(space.x - 3.5f, 0, space.y - 3.5f);

			int id = space.x + space.y * this.board.getColumns();

			if (this.board.isValidMove(space, this.board.getCurrentPlayerId()).getKey() == 0) {
				float j = (float) space.x + (float) space.y * 1.62f;

				position.y += 0.1 + (float) Math.sin(this.time + j) * 0.05f;
			}

			if ((space.x + space.y) % 2 == 0) {	
				stack.pushModel(BoardScene3D.spaceWhite, Mat4.translation(position), id);
			} else {
				stack.pushModel(BoardScene3D.spaceBlack, Mat4.translation(position), id);
			}

			float maxHeight = this.heights[id];
			float height = animateHeight(this.animations[id], maxHeight);
			float rotation = animateRotation(this.animations[id]);
			position.y = 0.3f + height;


			if (this.animations[id] < 1.0f && this.animations[id] + this.dt > 1.0f) {
				this.cameraShake += maxHeight * 0.1f;
			}

			this.animations[id] += this.dt;

			int playerId = currentBoard.getSpace(space);
			if (playerId == -1) continue;

			Player player = currentBoard.getPlayer(playerId);

			Mat4 model = Mat4.translation(position);
			model = model.mul(Mat4.rotationX(rotation));

			if (
				player.getColor().contains("#FFFFFF") && this.animations[id] >= 0.5f
				 || player.getColor().contains("#101010") && this.animations[id] < 0.5f
			) {
				stack.pushModel(BoardScene3D.chipWhite, model, id);
			} else if (
				player.getColor().contains("#101010") && this.animations[id] >= 0.5f 
				 || player.getColor().contains("#FFFFFF") && this.animations[id] < 0.5f
			) {
				stack.pushModel(BoardScene3D.chipBlack, model, id);
			} else {
				System.out.println("Unknown player color: " + player.getColor());
			}
		}

		GL.Enable(GL.CULL_FACE);

		this.faskinen.geometryPass(stack);
		this.particles.draw(rock, this.faskinen.camera);
		this.faskinen.shadowPass(stack);

		GL.Disable(GL.CULL_FACE);

		this.faskinen.camera.position = this.faskinen.camera.position.sub(cameraShake);

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
		if (this.staticBoard != null || !event.isPrimaryButtonDown()) return;

		int id = this.getPixelId(event);
		if (id == -1) return;

		int x = id % this.board.getColumns();
		int y = id / this.board.getColumns();

		Space space = new Space(x, y);
		Move move = this.board.move(space);

		if (move == null) return;

		this.root.fireEvent(new MoveEvent(MoveEvent.MOVE, move.getRound()));
		this.root.fireEvent(new SettingsEvent(SettingsEvent.UPDATE));

		for (Change change : move.getChanges()) {
			int cx = change.getColumn();
			int cy = change.getRow();
			float distance = (float) Math.sqrt((cx - x) * (cx - x) + (cy - y) * (cy - y));

			this.animations[cx + cy * this.board.getColumns()] = 0.5f - distance * 0.5f;
			this.heights[cx + cy * this.board.getColumns()] = 1.0f + distance * 0.1f;
		}
	}

	public void setStaticBoard(Board2D staticBoard) {
		this.staticBoard = staticBoard;
	}

	public int getPixelId(MouseEvent event) {
		// this is for some reason the only way to get the pixel id
		// from the mouse event
		// it's not documented anywhere, but it works
		// https://stackoverflow.com/questions/53496882/how-to-get-pixel-id-from-mouseevent-in-javafx
		return this.faskinen.getPixelId((int) (event.getSceneX()), (int) (event.getSceneY() - 28));
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
