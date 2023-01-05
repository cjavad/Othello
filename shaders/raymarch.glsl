#include "image.glsl"
#include "tonemap.glsl"
#include "sdf.glsl"
#include "material.glsl"
#include "ray.glsl"
#include "hit.glsl"
#include "camera.glsl"
#include "color.glsl"
#include "depth.glsl"
#include "shade.glsl"

vec3 normal(in vec3 p) {
	vec3 e = vec3(0.001, 0.0, 0.0);

	return normalize(vec3(
		sdfScene(p + e.xyy).distance - sdfScene(p - e.xyy).distance,
		sdfScene(p + e.yxy).distance - sdfScene(p - e.yxy).distance,
		sdfScene(p + e.yyx).distance - sdfScene(p - e.yyx).distance
	));
}

Hit intersect(Ray ray) {
	Hit hit = emptyHit();

	for (int i = 0; i < 128; i++) {
		hit.position = ray.origin + ray.direction * hit.distance;

		Sdf sdf = sdfScene(hit.position);
		hit.distance += sdf.distance;
		hit.material = sdf.material;

		if (abs(sdf.distance) < 0.01) {
			hit.hit = true;
			hit.normal = normal(hit.position);

			break;
		}

		if (hit.distance > 100.0) break;
	}

	return hit;
}

float shadow(in Ray ray, float k) {
    float shadow = 1.0;

    float t = 0.01;
    for(int i = 0; i < 64; i++) {
        vec3 position = ray.origin + ray.direction * t;
		Sdf sdf = sdfScene(position);

        shadow = min(shadow, k * max(sdf.distance, 0.0) / t);
        if(shadow < 0.01) break;

        t += clamp(sdf.distance, 0.01, 5.0);

		if (t > 100.0) break;
    }

    return shadow;
}

layout(local_size_x = 16, local_size_y = 16) in;
void main() {
	if (!isPixelValid()) return;	

	Hit hit = intersect(getRay());

	vec3 color;
	if (hit.hit) {
		if (!depthTest(hit.distance)) return;

		color = shadeSolid(hit);
		writeDepth(hit.distance);
	} else {
		if (!depthTest(100.0)) return;
		
		color = vec3(0.0);
	}

	writeColor(vec4(tonemap(color), 1.0));
}
