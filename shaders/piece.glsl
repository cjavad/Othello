#version 450

layout(local_size_x = 16, local_size_y = 16) in;

buffer ColorBuffer {
	int colors[];
};

void main() {
	colors[gl_GlobalInvocationID.x] = 5;
}
