out vec2 uv;
out vec2 clip;

// full screen quad
void main() {
	vec2[] positions = vec2[](
		vec2(-1.0, -1.0),
		vec2(1.0, -1.0),
		vec2(-1.0, 1.0),
		vec2(1.0, -1.0),
		vec2(-1.0, 1.0),
		vec2(1.0, 1.0)
	);

	vec2[] uvs = vec2[](
		vec2(0.0, 0.0),
		vec2(1.0, 0.0),
		vec2(0.0, 1.0),
		vec2(1.0, 0.0),
		vec2(0.0, 1.0),
		vec2(1.0, 1.0)
	);
	
	uv = uvs[gl_VertexID];
	clip = positions[gl_VertexID];
	gl_Position = vec4(clip, 0.0, 1.0);
}
