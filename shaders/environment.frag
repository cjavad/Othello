#include "lighting.glsl"

in vec2 uv;

layout(location = 0) out vec4 o_color;

uniform sampler2D g_position;
uniform sampler2D g_normal;
uniform sampler2D g_baseColor;
uniform sampler2D g_depth;

uniform vec3 ambient_color;

void main() {
	float depth = texture(g_depth, uv).x;
	if (depth == 1.0) discard;

	vec3 position = texture(g_position, uv).xyz;
	vec3 normal = texture(g_normal, uv).xyz;
	vec3 baseColor = texture(g_baseColor, uv).xyz;

	vec3 light = vec3(0.0);
	light += ambient_color;

	vec3 color = baseColor * light;
	o_color = vec4(color, 1.0);
}
