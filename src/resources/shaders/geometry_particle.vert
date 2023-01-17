#include "camera.glsl"

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec4 tangent;
layout(location = 3) in vec2 uv;

layout(location = 10) in vec4 model_0;
layout(location = 11) in vec4 model_1;
layout(location = 12) in vec4 model_2;
layout(location = 13) in vec4 model_3;

out vec3 w_position;
out vec3 w_normal;
out vec3 w_tangent;
out vec3 w_bitangent;
out vec2 w_uv;

void main() {
	mat4 model = mat4(model_0, model_1, model_2, model_3);

	w_position = (model * vec4(position, 1.0)).xyz;
	w_normal = normalize((model * vec4(normal, 0.0)).xyz);
	w_tangent = normalize((model * vec4(tangent.xyz, 0.0)).xyz);
	w_bitangent = cross(w_normal, w_tangent) * tangent.w;
	w_uv = uv;
	gl_Position = viewProj * vec4(w_position, 1.0);
}
