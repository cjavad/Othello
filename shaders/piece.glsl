#version 450

layout(local_size_x = 32, local_size_y = 32) in;

buffer ColorBuffer {
	int colors[];
};

void main() {
	colors[gl_GlobalInvocationID.x] = 5;
}
