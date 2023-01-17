in vec2 uv;
out vec4 o_color;

uniform int mip;
uniform float scale;
uniform float threshold;
uniform vec3 curve;

uniform sampler2D source;

vec4 quadratic_threshold(vec4 color, float threshold, vec3 curve) {
	float br = max(max(color.r, color.g), color.b);

	float rq = clamp(br - curve.x, 0.0, curve.y);
	rq = curve.z * rq * rq;

	return color * max(rq, br - threshold) / max(br, 0.0001); 
}

void main() {
	if (scale == 0.0) {
		o_color = texture(source, uv);
		return;
	}

	vec2 texel_size = 1.0 / vec2(textureSize(source, mip));
	vec2 scale = texel_size * scale;
	
	vec4 a = textureLod(source, uv + vec2(-1.0, -1.0) * scale, float(mip));
	vec4 b = textureLod(source, uv + vec2( 0.0, -1.0) * scale, float(mip));
	vec4 c = textureLod(source, uv + vec2( 1.0, -1.0) * scale, float(mip));
	vec4 d = textureLod(source, uv + vec2(-0.5, -0.5) * scale, float(mip));
	vec4 e = textureLod(source, uv + vec2( 0.5, -0.5) * scale, float(mip));
	vec4 f = textureLod(source, uv + vec2(-1.0,  0.0) * scale, float(mip));
	vec4 g = textureLod(source, uv + vec2( 0.0,  0.0) * scale, float(mip));
	vec4 h = textureLod(source, uv + vec2( 1.0,  0.0) * scale, float(mip));
	vec4 i = textureLod(source, uv + vec2(-0.5,  0.5) * scale, float(mip));
	vec4 j = textureLod(source, uv + vec2( 0.5,  0.5) * scale, float(mip));
	vec4 k = textureLod(source, uv + vec2(-1.0,  1.0) * scale, float(mip));
	vec4 l = textureLod(source, uv + vec2( 0.0,  1.0) * scale, float(mip));
	vec4 m = textureLod(source, uv + vec2( 1.0,  1.0) * scale, float(mip));

	vec2 div = (1.0 / 4.0) * vec2(0.5, 0.125);

	vec4 o = (d + e + i + j) * div.x;
	o = o + (a + b + g + f) * div.y;
	o = o + (b + c + h + g) * div.y;
	o = o + (f + g + l + k) * div.y;
	o = o + (g + h + m + l) * div.y;

	if (threshold > 0.0) {
		o = quadratic_threshold(o, threshold, curve);
	}

	o_color = o;
}
