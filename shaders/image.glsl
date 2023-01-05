uniform int imageWidth;
uniform int imageHeight;

uniform int offsetX;
uniform int offsetY;
uniform int width;
uniform int height;

uint index() {
	uint x = gl_GlobalInvocationID.x + offsetX;
	uint y = gl_GlobalInvocationID.y + offsetY;

	return x + y * imageWidth;
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
