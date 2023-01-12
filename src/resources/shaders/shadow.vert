layout(location = 0) in vec3 position;

uniform mat4 model;
uniform mat4 viewProj;

void main() {
	gl_Position = viewProj * model * vec4(position, 1.0);
}
