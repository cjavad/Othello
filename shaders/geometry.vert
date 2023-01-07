in vec3 position;
in vec3 normal;

uniform mat4 model;
uniform mat4 viewProj;

out vec3 w_position;
out vec3 w_normal;

void main() {
	w_position = (model * vec4(position, 1.0)).xyz;
	w_normal = (model * vec4(normal, 0.0)).xyz;
	gl_Position = viewProj * vec4(w_position, 1.0);
}
