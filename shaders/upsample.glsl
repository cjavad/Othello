#version 450

layout(local_size_x = 16, local_size_y = 16) in;

uniform int dstWidth;
uniform int dstHeight;

buffer srcBuffer {
	int srcData[];
};

buffer dstBuffer {
	int dstData[];
};

int rgbaToInt(vec4 rgba) {
	rgba = clamp(rgba, 0.0, 1.0);
	// convert to 0-255 argb
	return int(rgba.a * 255.0) << 24 
		 | int(rgba.r * 255.0) << 16 
		 | int(rgba.g * 255.0) << 8 
		 | int(rgba.b * 255.0);
}

vec4 intToRgba(int rgba) {
	// convert to 0-1 argb
	return vec4(
		float((rgba >> 0 ) & 0xFF) / 255.0,
		float((rgba >> 8 ) & 0xFF) / 255.0,
		float((rgba >> 16) & 0xFF) / 255.0,
		float((rgba >> 24) & 0xFF) / 255.0
	);
}

vec4 sampleSrc(int x, int y) {
	return intToRgba(srcData[y * dstWidth / 2 + x]);
}

void writeDst(int x, int y, vec4 rgba) {
	dstData[y * dstWidth + x] = rgbaToInt(rgba);
}

layout(local_size_x = 16, local_size_y = 16) in;
void main() {
	vec2 srcCoord = vec2(gl_GlobalInvocationID.xy) / 2.0 + 0.25;
	ivec2 dstCoord = ivec2(gl_GlobalInvocationID.xy);	

	ivec2 baseCoord = ivec2(floor(srcCoord));
	vec2 fracCoord = fract(srcCoord);

	vec4 a = sampleSrc(baseCoord.x    , baseCoord.y    );
	vec4 b = sampleSrc(baseCoord.x + 1, baseCoord.y    );
	vec4 c = sampleSrc(baseCoord.x    , baseCoord.y + 1);
	vec4 d = sampleSrc(baseCoord.x + 1, baseCoord.y + 1);

	vec4 ab = mix(a, b, fracCoord.x);
	vec4 cd = mix(c, d, fracCoord.x);

	vec4 rgba = mix(ab, cd, fracCoord.y);

	writeDst(dstCoord.x, dstCoord.y, rgba);
}
