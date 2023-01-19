package othello.faskinen;

/**
 * A 3d light.
 *
 * Lights may ONLY be created after a valid OpenGL context has been created.
 */
public class Light {
	public static int SHADOWMAP_SIZE = 2048;
	public static float SIZE = 20.0f;
	public static float DEPTH = 20.0f;
	public static Mat4 SHADOWMAP_PROJECTION = Mat4.orthographic(
		-SIZE / 2.0f, SIZE / 2.0f,
		-SIZE / 2.0f, SIZE / 2.0f,
		-DEPTH / 2.0f, DEPTH / 2.0f
	);

	public Vec3 direction;
	public Vec3 color;
	public float intensity;
	
	public float shadowSoftness = 1.0f;
	public float shadowFalloff = 2.0f;

	public int blockerSearchSamples = 24;
	public int penumbraSearchSamples = 64;

	public Texture shadowMap;
	public Framebuffer shadowFramebuffer;

	/**
	 * Create a new light.
	 * @param direction The direction of the light.
	 * @param color The color of the light.
	 * @param intensity The intensity of the light.
	 */
	public Light(Vec3 direction, Vec3 color, float intensity) {
		this.direction = direction;
		this.color = color;
		this.intensity = intensity;
		this.shadowMap = Texture.depth32f(SHADOWMAP_SIZE, SHADOWMAP_SIZE);
		this.shadowFramebuffer = new Framebuffer(new Texture[0], this.shadowMap);
	}

	/**
	 * Create a new light.
	 * @param direction The direction of the light.
	 * @param color The color of the light.
	 *
	 * The intensity of the light is set to 10.0f.
	 */
	public Light(Vec3 direction, Vec3 color) {
		this(direction, color, 7.5f);
	}

	/**
	 * Calculate the view projection matrix for the light.
	 * @return The view projection matrix.
	 */
	public Mat4 viewProj() {
		Mat4 view;
		if (this.direction.dot(new Vec3(0.0f, 1.0f, 0.0f)) > 0.999f) {
			view = Mat4.lookAt(new Vec3(0.0f, 0.0f, 0.0f), this.direction, new Vec3(1.0f, 0.0f, 0.0f));
		} else {
			view = Mat4.lookAt(new Vec3(0.0f, 0.0f, 0.0f), this.direction, new Vec3(0.0f, 1.0f, 0.0f));
		}

		Mat4 viewProj = SHADOWMAP_PROJECTION.mul(view);

		return viewProj;
	}

	/**
	 * Deletes the shadowMap and shadowFramebuffer
	 */
	public void delete() {
		this.shadowMap.delete();
		this.shadowFramebuffer.delete();
	}
}
