in vec2 uv;
out vec4 o_color;

uniform int mip;
uniform float scale;

uniform sampler2D source_down;
uniform sampler2D source_up;

vec4 sample_3x3_tent(sampler2D tex, vec2 uv, vec2 scale) {
	vec4 d = vec4(1.0, 1.0, -1.0, 0.0);

	vec4 s = textureLod(tex, uv - d.xy * scale, float(mip + 1));
	s += textureLod(tex, uv - d.wy * scale, float(mip + 1)) * 2.0;
	s += textureLod(tex, uv - d.zy * scale, float(mip + 1));
                
	s += textureLod(tex, uv + d.zw * scale, float(mip + 1)) * 2.0;
	s += textureLod(tex, uv				  , float(mip + 1)) * 4.0;
	s += textureLod(tex, uv + d.xw * scale, float(mip + 1)) * 2.0;
               
	s += textureLod(tex, uv + d.zy * scale, float(mip + 1));
	s += textureLod(tex, uv + d.wy * scale, float(mip + 1)) * 2.0;
	s += textureLod(tex, uv + d.xy * scale, float(mip + 1));

	return s / 16.0;
}

void main() {
	vec2 texel_size = 1.0 / vec2(textureSize(source_down, mip));
	vec4 color = textureLod(source_down, uv, mip);
	vec4 mip_color = sample_3x3_tent(source_up, uv, texel_size * scale);

	o_color = color + mip_color;
}
