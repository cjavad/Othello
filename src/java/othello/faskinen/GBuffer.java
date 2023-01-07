package othello.faskinen;

import othello.faskinen.opengl.GL;

public class GBuffer {
	public Texture positionTexture;
	public Texture normalTexture;
	public Texture baseColorTexture;
	public Texture depthTexture;

	public Framebuffer framebuffer;

	public GBuffer(int width, int height) {
		this.positionTexture = Texture.rgba16f(width, height);
		this.normalTexture = Texture.rgba16f(width, height);
		this.baseColorTexture = Texture.rgba8(width, height);
		this.depthTexture = Texture.depth32f(width, height);

		Texture[] textures = new Texture[] {
			this.positionTexture,
			this.normalTexture,
			this.baseColorTexture
		};

		this.framebuffer = new Framebuffer(width, height, textures, this.depthTexture);
	}

	public void clear() {
		this.framebuffer.bind();

		GL.ClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL.Clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);

		this.framebuffer.unbind();
	}
}
