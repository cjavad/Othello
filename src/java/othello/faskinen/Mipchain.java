package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;

import othello.faskinen.opengl.GL;

public class Mipchain {
	Texture texture;
	int[] framebuffers;

	public Mipchain(int width, int height) {
		this.texture = Texture.rgba16f(width, height);

		int levels = (int) Math.floor(Math.log(Math.max(width, height)) / Math.log(2));
		this.framebuffers = new int[levels];

		MemorySegment segment = MemorySession.openImplicit().allocate(4 * levels);
		GL.GenFramebuffers(levels, segment.address());

		for (int i = 0; i < framebuffers.length; i++) {
			framebuffers[i] = segment.get(ValueLayout.JAVA_INT, i * 4);

			GL.BindFramebuffer(GL.FRAMEBUFFER, framebuffers[i]);
			GL.FramebufferTexture2D(
				GL.FRAMEBUFFER, 
				GL.COLOR_ATTACHMENT0, 
				GL.TEXTURE_2D, 
				texture.textureId, 
				i
			);
			GL.BindFramebuffer(GL.FRAMEBUFFER, 0);
		}
	}

	public int levels() {
		return this.framebuffers.length;
	}

	public void bind(int level) {
		GL.BindFramebuffer(GL.FRAMEBUFFER, this.framebuffers[level]);
	}

	public void unbind() {
		GL.BindFramebuffer(GL.FRAMEBUFFER, 0);
	}

	public int framebuffer(int level) {
		return this.framebuffers[level];
	}

	public int width(int level) {
		return (int) (this.texture.width >> level);
	}

	public int height(int level) {
		return (int) (this.texture.height >> level);
	}

	public void delete() {
		MemorySegment segment = MemorySession.openImplicit().allocate(4 * this.framebuffers.length);

		for (int i = 0; i < this.framebuffers.length; i++) {
			segment.set(ValueLayout.JAVA_INT, i * 4, this.framebuffers[i]);
		}

		GL.DeleteFramebuffers(this.framebuffers.length, segment.address());

		this.texture.delete();
	}
}
