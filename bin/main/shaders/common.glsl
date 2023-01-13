const float PI = 3.1415926535897932384626433832795f;

vec4 rgbaFromUint(uint value) {
	return vec4(
		float((value & 0x000000FFu) >>  0u) / 255.0,
		float((value & 0x0000FF00u) >>  8u) / 255.0,
		float((value & 0x00FF0000u) >> 16u) / 255.0,
		float((value & 0xFF000000u) >> 24u) / 255.0
	);
}

uint uintFromRgba(vec4 rgba) {
	return uint(rgba.r * 255.0) <<  0u |
	       uint(rgba.g * 255.0) <<  8u |
	       uint(rgba.b * 255.0) << 16u |
	       uint(rgba.a * 255.0) << 24u;
}

float saturate(float value) {
	return clamp(value, 0.0, 1.0);
}

vec3 saturate(vec3 value) {
	return clamp(value, 0.0, 1.0);
}

vec4 saturate(vec4 value) {
	return clamp(value, 0.0, 1.0);
}

vec3 flip(vec3 v) {
	return vec3(v.x, -v.y, v.z);
}
