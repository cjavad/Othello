#include "camera.glsl"

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec4 tangent;
layout(location = 3) in vec2 uv;

uniform mat4 model;

out vec3 w_position;
out vec3 w_normal;
out vec3 w_tangent;
out vec3 w_bitangent;
out vec2 w_uv;

void main() {
	w_position = (model * vec4(position, 1.0)).xyz;
	w_normal = normalize((model * vec4(normal, 0.0)).xyz);
	w_tangent = normalize((model * vec4(tangent.xyz, 0.0)).xyz);
	w_bitangent = cross(w_normal, w_tangent) * tangent.w;
	w_uv = uv;
	gl_Position = viewProj * vec4(w_position, 1.0);
}
