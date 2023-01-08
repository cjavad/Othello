package othello.faskinen;

import othello.faskinen.opengl.GL;

public class GBuffer {
	public Texture position;
	public Texture normal;
	public Texture baseColor;
	public Texture material;
	public Texture depth;

	public Framebuffer framebuffer;

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

		this.framebuffer = new Framebuffer(width, height, colorTextures, this.depth);
	}

	public void clear() {
		this.framebuffer.bind();

		GL.ClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL.Clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);

		this.framebuffer.unbind();
	}

	public void delete() {
		this.position.delete();
		this.normal.delete();
		this.baseColor.delete();
		this.material.delete();
		this.depth.delete();
		this.framebuffer.delete();
	}
}
