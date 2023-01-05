#include "image.glsl"

buffer DepthBuffer { float depths[]; };

float readDepth() {
	return depths[index()];
}

void writeDepth(float depth) {
	depths[index()] = depth;
}

bool depthTest(float depth) {
	float actual = readDepth();
	return depth < actual || actual == 0.0;
}
