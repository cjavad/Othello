package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;

import othello.faskinen.opengl.GL;

public class Framebuffer {
	public int width;
	public int height;

	public Texture[] textures;
	public Texture depthTexture;

	public int framebufferId;

	public Framebuffer(int width, int height, Texture[] textures, Texture depthTexture) {
		MemorySegment framebuffers = MemorySession.openImplicit().allocate(4);
		GL.GenFramebuffers(1, framebuffers.address());

		this.framebufferId = framebuffers.get(ValueLayout.JAVA_INT, 0);

		GL.BindFramebuffer(GL.FRAMEBUFFER, this.framebufferId);

		GL.assertNoError();

		for (int i = 0; i < textures.length; i++) {
			assert textures[i].width == width;
			assert textures[i].height == height;

			textures[i].bind();
			GL.FramebufferTexture2D(
				GL.FRAMEBUFFER,
				GL.COLOR_ATTACHMENT0 + i, 
				GL.TEXTURE_2D, 
				textures[i].textureId, 
				0
			);

			GL.assertNoError();
		}

		if (depthTexture != null) {
			assert depthTexture.width == width;
			assert depthTexture.height == height;

			GL.FramebufferTexture2D(
				GL.FRAMEBUFFER, 
				GL.DEPTH_ATTACHMENT, 
				GL.TEXTURE_2D, 
				depthTexture.textureId, 
				0
			);

			GL.assertNoError();
		}

		int status = GL.CheckFramebufferStatus(GL.FRAMEBUFFER);
		if (status != GL.FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Framebuffer is not complete: " + status);
		}

		this.unbind();

		this.width = width;
		this.height = height;

		this.textures = textures;
		this.depthTexture = depthTexture;
	}

	public Framebuffer(int width, int height, Texture[] textures) {
		this(width, height, textures, null);
	}

	public void clear(float r, float g, float b, float a) {
		this.bind();

		GL.ClearColor(r, g, b, a);
		GL.Clear(GL.COLOR_BUFFER_BIT);

		this.unbind();

		GL.assertNoError();
	}

	public void bind() {
		GL.BindFramebuffer(GL.FRAMEBUFFER, this.framebufferId);

		MemorySegment drawBuffers = MemorySession.openImplicit().allocate(4 * textures.length);

		for (int i = 0; i < textures.length; i++) {
			drawBuffers.set(ValueLayout.JAVA_INT, i * 4, GL.COLOR_ATTACHMENT0 + i);
		}

		GL.DrawBuffers(textures.length, drawBuffers.address());
	}

	public void unbind() {
		GL.BindFramebuffer(GL.FRAMEBUFFER, 0);
	}

	public void delete() {
		for (Texture texture : this.textures) {
			texture.delete();
		}

		if (this.depthTexture != null) {
			this.depthTexture.delete();
		}

		MemorySegment framebuffers = MemorySession.openImplicit().allocate(4);
		framebuffers.set(ValueLayout.JAVA_INT, 0, this.framebufferId);
		GL.DeleteFramebuffers(1, framebuffers.address());
	}
}
