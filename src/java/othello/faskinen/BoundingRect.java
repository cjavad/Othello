package othello.faskinen;

public class BoundingRect {
	public int x, y, width, height;

	public BoundingRect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int left() {
		return this.x;
	}

	public int right() {
		return this.x + this.width;
	}

	public int top() {
		return this.y;
	}

	public int bottom() {
		return this.y + this.height;
	}	

	public BoundingRect intersect(BoundingRect other) {
		int x = Math.max(this.left(), other.left());
		int y = Math.max(this.top(), other.top());
		int w = Math.min(this.right(), other.right()) - x;
		int h = Math.min(this.bottom(), other.bottom()) - y;

		return new BoundingRect(x, y, w, h);
	}

	public boolean intersects(BoundingRect other) {
		return this.left() < other.right() && 
			   this.right() > other.left() && 
			   this.top() < other.bottom() && 
			   this.bottom() > other.top();
	}

	public static BoundingRect sphere(Mat4 viewProj, Vec3 position, float radius, int width, int height) {
		Vec4 p = viewProj.mul(position.extend(1.0f));
		float x = (p.x / p.w + 1.0f) * 0.5f * width;
		float y = (p.y / p.w + 1.0f) * 0.5f * height;
		float z = p.z / p.w;
		float r = (float)Math.sqrt(1.0f - z * z) * radius * width;

		return new BoundingRect((int)(x - r), (int)(y - r), (int)(2.0f * r), (int)(2.0f * r));
	}
}
