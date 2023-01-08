package othello.faskinen;

import othello.faskinen.opengl.GL;

/**
 * A geometry buffer.
 *
 * The geometry buffer is a framebuffer that contains the following textures:
 * - Position (Rgba16f)
 * - Normal (Rgba16f)
 * - Base color (Rrgba8unorm)
 * - Material (Rgba32ui)
 * - Depth (Depth32f)
 *
 * Materials are stored as follows:
 * - R:
 *   - [0..7]: metallic
 *   - [8..15]: roughness
 *   - [16..23]: reflectance
 * - A: object id
 */
public class GBuffer {
	public Texture position;
	public Texture normal;
	public Texture baseColor;
	public Texture material;
	public Texture depth;

	public Framebuffer framebuffer;

	/**
	 * Creates a new GBuffer.
	 * @param width The width of the GBuffer.
	 * @param height The height of the GBuffer.
	 */
	public GBuffer(int width, int height) {
		this.position = Texture.rgba16f(width, height);
		this.normal = Texture.rgba16f(width, height);
		this.baseColor = Texture.rgba8(width, height);
		this.material = Texture.rgba32u(width, height);
		this.depth = Texture.depth32f(width, height);

		Texture[] colorTextures = new Texture[] {
			this.position,
			this.normal,
			this.baseColor,
			this.material
		};

		this.framebuffer = new Framebuffer(colorTextures, this.depth);
	}

	/**
	 * Gets the id of the pixel at the specified coordinates.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The id of the pixel.
	 */
	public int getPixelId(int x, int y) {
		int[] pixel = this.material.readPixelInt(x, y);
		return pixel[3] - 1;
	}

	/**
	 * Clears the GBuffer.
	 */
	public void clear() {
		this.framebuffer.bind();

		GL.ClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL.Clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);

		this.framebuffer.unbind();
	}

	/**
	 * Binds the GBuffer.
	 */
	public void bind() {
		this.framebuffer.bind();
	}

	/**
	 * Unbinds the GBuffer.
	 */
	public void unbind() {
		this.framebuffer.unbind();
	}

	/**
	 * Deletes the GBuffer.
	 */
	public void delete() {
		this.position.delete();
		this.normal.delete();
		this.baseColor.delete();
		this.material.delete();
		this.depth.delete();
		this.framebuffer.delete();
	}
}
