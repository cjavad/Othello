#version 450

layout(local_size_x = 16, local_size_y = 16) in;

uniform int imageWidth;
uniform int imageHeight;

buffer ColorBuffer {
	int colors[];
};

uint index() {
	return gl_GlobalInvocationID.x + gl_GlobalInvocationID.y * imageWidth;
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

void main() {
	vec2 uv = vec2(gl_GlobalInvocationID.xy) / vec2(imageWidth, imageHeight);
	uv = uv * vec2(2.0, -2.0) + vec2(-1.0, 1.0);

	vec4 color = vec4(uv, 0.0, 1.0);
	writeColor(color);
}
