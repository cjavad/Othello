in vec2 uv;

layout(location = 0) out vec4 o_color;

uniform sampler2D hdr;

vec3 aces(vec3 x) {
	const float a = 2.51;
	const float b = 0.03;
	const float c = 2.43;
	const float d = 0.59;
	const float e = 0.14;
	return clamp((x * (a * x + b)) / (x * (c * x + d) + e), 0.0, 1.0);
}

vec3 gamma_correct(vec3 x) {
	return pow(x, vec3(1.0 / 2.2));
}

vec3 tonemap(vec3 x) {
	return aces(x);
}

void main() {
	vec3 color = textureLod(hdr, uv, 0.0).rgb;
	o_color = vec4(tonemap(color), 1.0);
}
