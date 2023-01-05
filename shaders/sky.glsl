#version 450

#include "image.glsl"
#include "tonemap.glsl"
#include "ray.glsl"
#include "camera.glsl"
#include "image.glsl"
#include "color.glsl"
#include "depth.glsl"

layout (local_size_x = 16, local_size_y = 16) in;
void main() {
	if (!isPixelValid()) return;
	if (!depthTest(100.0)) return;

	Ray ray = getRay();
	vec3 sky = mix(vec3(0.3, 0.8, 0.9), vec3(0.1, 0.3, 0.9), max(ray.direction.y, 0.0));

	writeColor(vec4(tonemap(sky), 1.0));
}
