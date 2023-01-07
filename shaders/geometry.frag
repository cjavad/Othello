in vec3 w_position;
in vec3 w_normal;

layout(location = 0) out vec4 o_position;
layout(location = 1) out vec4 o_normal;
layout(location = 2) out vec4 o_baseColor;

uniform vec3 baseColor;

void main() {
	o_position = vec4(w_position, 0.0);
	o_normal = vec4(w_normal, 0.0);
	o_baseColor = vec4(baseColor, 1.0);
}
