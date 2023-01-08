package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;

import othello.faskinen.opengl.GL;

/**
 * A wrapper around an OpenGL framebuffer.
 *
 * Framebuffers may ONLY be created after a valid OpenGL context has been created.
 */
public class Framebuffer {
	public Texture[] textures;
	public Texture depthTexture;

	public int framebufferId;

	/**
	 * Creates a new framebuffer.
	 * @param textures The textures of the framebuffer.
	 * @param depthTexture The depth texture of the framebuffer.
	 * @return The created framebuffer.
	 *
	 * The dimensions of the textures MUST be the same or a fatal error is likely to occur.
	 */
	public Framebuffer(Texture[] textures, Texture depthTexture) {
		MemorySegment framebuffers = MemorySession.openImplicit().allocate(4);
		GL.GenFramebuffers(1, framebuffers.address());

		this.framebufferId = framebuffers.get(ValueLayout.JAVA_INT, 0);

		GL.BindFramebuffer(GL.FRAMEBUFFER, this.framebufferId);

		GL.assertNoError();

		for (int i = 0; i < textures.length; i++) {	
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
			GL.FramebufferTexture2D(
				GL.FRAMEBUFFER, 
				GL.DEPTH_ATTACHMENT, 
				GL.TEXTURE_2D, 
				depthTexture.textureId, 
				0
			);

			GL.assertNoError();
		}

		if (textures.length > 0) {
			MemorySegment drawBuffers = MemorySession.openImplicit().allocate(4 * textures.length);

			for (int i = 0; i < textures.length; i++) {
				drawBuffers.set(ValueLayout.JAVA_INT, i * 4, GL.COLOR_ATTACHMENT0 + i);
			}

			GL.DrawBuffers(textures.length, drawBuffers.address());
		} else {
			GL.DrawBuffer(GL.NONE);
			GL.ReadBuffer(GL.NONE);
		}

		int status = GL.CheckFramebufferStatus(GL.FRAMEBUFFER);
		if (status != GL.FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Framebuffer is not complete: " + status);
		}

		this.unbind();

		this.textures = textures;
		this.depthTexture = depthTexture;
	}

	/**
	 * Creates a new framebuffer with no depth texture.
	 * @param textures The textures of the framebuffer.
	 *
	 * The dimensions of the textures MUST be the same or a fatal error is likely to occur.
	 */
	public Framebuffer(Texture[] textures) {
		this(textures, null);
	}

	/**
	 * Clears the framebuffer.
	 */
	public void clear(float r, float g, float b, float a) {
		this.bind();

		GL.ClearColor(r, g, b, a);
		GL.Clear(GL.COLOR_BUFFER_BIT);

		this.unbind();

		GL.assertNoError();
	}

	/**
	 * Binds the framebuffer.
	 */
	public void bind() {
		GL.BindFramebuffer(GL.FRAMEBUFFER, this.framebufferId);	
	}

	/**
	 * Unbinds the framebuffer.
	 */
	public void unbind() {
		GL.BindFramebuffer(GL.FRAMEBUFFER, 0);
	}

	/**
	 * Deletes the framebuffer.
	 */
	public void delete() {
		MemorySegment framebuffers = MemorySession.openImplicit().allocate(4);
		framebuffers.set(ValueLayout.JAVA_INT, 0, this.framebufferId);
		GL.DeleteFramebuffers(1, framebuffers.address());

		this.framebufferId = -1;

		GL.assertNoError();
	}
}
