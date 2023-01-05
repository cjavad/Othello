#include "image.glsl"

buffer ColorBuffer { int colors[]; };

int rgbaToInt(vec4 rgba) {
	rgba = clamp(rgba, 0.0, 1.0);

	// convert to 0-255 bgra
	return int(rgba.a * 255.0) << 24 
		 | int(rgba.r * 255.0) << 16 
		 | int(rgba.g * 255.0) << 8 
		 | int(rgba.b * 255.0);
}

vec4 intToRgba(int color) {
	vec4 rgba;

	rgba.a = float((color >> 24) & 0xFF) / 255.0;
	rgba.r = float((color >> 16) & 0xFF) / 255.0;
	rgba.g = float((color >> 8) & 0xFF) / 255.0;
	rgba.b = float(color & 0xFF) / 255.0;

	return rgba;
}

vec4 readColor(int x, int y) {
	return intToRgba(colors[index()]);
}

void writeColor(vec4 color) {
	colors[index()] = rgbaToInt(color);
}
