package othello.faskinen;

import othello.faskinen.opengl.GL;

public class Bloom {
	Mipchain downsample;
	Mipchain upsample;

	Shader downsampleShader;
	Shader upsampleShader;

	public Bloom(int width, int height) {
		this.downsample = new Mipchain(width, height);
		this.upsample = new Mipchain(width, height);

		this.downsampleShader = new Shader("quad.vert", "downsample.frag");
		this.upsampleShader = new Shader("quad.vert", "upsample.frag");
	}

	public void resize(int width, int height) {
		this.downsample.delete();
		this.upsample.delete();

		this.downsample = new Mipchain(width, height);
		this.upsample = new Mipchain(width, height);
	}

	public void render(Framebuffer target, float threshold, float knee) {
		GL.Disable(GL.DEPTH_TEST);

		this.downsampleShader.use();

		Vec3 curve = new Vec3(
			threshold - knee,
			knee * 2,
			0.25f / (knee * knee)
		);

		int maxLevel = this.downsample.levels() - 2;
		float scale = Math.max(
			this.downsample.width(maxLevel), 
			this.downsample.height(maxLevel)
		) / 2.0f;	

		this.downsampleShader.setInt("mip", 0);
		this.downsampleShader.setFloat("scale", scale);
		this.downsampleShader.setFloat("threshold", threshold);
		this.downsampleShader.setVec3("curve", curve);
		this.downsampleShader.setTexture("source", target.textures[0]);

		GL.BindFramebuffer(GL.FRAMEBUFFER, this.downsample.framebuffer(0));
		GL.Viewport(0, 0, this.downsample.width(0), this.downsample.height(0));

		GL.ClearColor(0, 0, 0, 0);
		GL.Clear(GL.COLOR_BUFFER_BIT);
		GL.DrawArrays(GL.TRIANGLES, 0, 6);

		for (int i = 0; i <= maxLevel; i++) {
			this.downsampleShader.setInt("mip", i);
			this.downsampleShader.setFloat("threshold", 0.0f);
			this.downsampleShader.setFloat("scale", scale);
			this.downsampleShader.setTexture("source", this.downsample.texture);

			GL.BindFramebuffer(GL.FRAMEBUFFER, this.downsample.framebuffer(i + 1));
			GL.Viewport(0, 0, this.downsample.width(i + 1), this.downsample.height(i + 1));

			GL.ClearColor(0, 0, 0, 0);
			GL.Clear(GL.COLOR_BUFFER_BIT);
			GL.DrawArrays(GL.TRIANGLES, 0, 6);
		}

		this.upsampleShader.use();

		for (int i = maxLevel - 1; i > 0; i--) {
			this.upsampleShader.setInt("mip", i);
			this.upsampleShader.setFloat("scale", scale);
			
			if (i == maxLevel - 1) {
				this.upsampleShader.setTexture("source_up", this.downsample.texture);
			} else {
				this.upsampleShader.setTexture("source_up", this.upsample.texture);
			}

			this.upsampleShader.setTexture("source_down", this.downsample.texture);

			GL.BindFramebuffer(GL.FRAMEBUFFER, this.upsample.framebuffer(i));
			GL.Viewport(0, 0, this.upsample.width(i), this.upsample.height(i));

			GL.ClearColor(0, 0, 0, 0);
			GL.Clear(GL.COLOR_BUFFER_BIT);
			GL.DrawArrays(GL.TRIANGLES, 0, 6);
		}

		this.upsampleShader.setInt("mip", 0);
		this.upsampleShader.setFloat("scale", scale);
		this.upsampleShader.setTexture("source_up", this.upsample.texture);
		this.upsampleShader.setTexture("source_down", this.downsample.texture);

		GL.BindFramebuffer(GL.FRAMEBUFFER, target.framebufferId);
		GL.Viewport(0, 0, target.textures[0].width, target.textures[0].height);
	
		GL.Enable(GL.BLEND);
		GL.BlendFunc(GL.SRC_ALPHA, GL.ONE);

		GL.DrawArrays(GL.TRIANGLES, 0, 6);

		GL.Disable(GL.BLEND);
	}

	public void delete() {
		this.downsample.delete();
		this.upsample.delete();

		this.downsampleShader.delete();
		this.upsampleShader.delete();
	}
}
