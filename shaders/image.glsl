uniform int imageWidth;
uniform int imageHeight;

uniform int offsetX;
uniform int offsetY;
uniform int width;
uniform int height;

buffer ColorBuffer { int colors[]; };
buffer DepthBuffer { float depths[]; };

uint index() {
	uint x = gl_GlobalInvocationID.x + offsetX;
	uint y = gl_GlobalInvocationID.y + offsetY;

	return x + y * imageWidth;
}

int rgbaToInt(vec4 rgba) {
	rgba = clamp(rgba, 0.0, 1.0);

	// convert to 0-255 bgra
	return int(rgba.a * 255.0) << 24 
		 | int(rgba.r * 255.0) << 16 
		 | int(rgba.g * 255.0) << 8 
		 | int(rgba.b * 255.0);
}

void writeColor(vec4 color) {
	colors[index()] = rgbaToInt(color);
}

float readDepth() {
	return depths[index()];
}

void writeDepth(float depth) {
	depths[index()] = depth;
}

bool isPixelValid() {
	return gl_GlobalInvocationID.x < width && gl_GlobalInvocationID.y < height;
}

ivec2 getPixel() {
	return ivec2(gl_GlobalInvocationID.x + offsetX, gl_GlobalInvocationID.y + offsetY);
}

vec2 getUV() {
	return vec2(getPixel()) / vec2(imageWidth, imageHeight);
}
