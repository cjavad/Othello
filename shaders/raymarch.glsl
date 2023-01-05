#include "image.glsl"
#include "tonemap.glsl"
#include "sdf.glsl"
#include "material.glsl"
#include "ray.glsl"
#include "camera.glsl"

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

vec3 shadeSolid(Hit hit) {
	vec3 sunDir = normalize(vec3(1.0, 1.0, -1.0));
	vec3 skyDir = normalize(vec3(0.0, 1.0, 0.0));
	vec3 bouDir = normalize(vec3(0.0, -1.0, 0.0));

	Ray shadowRay = newRay(hit.position + hit.normal * 0.1, sunDir);
	float sunShadow = shadow(shadowRay, 8.0);

	float sunDiffuse = max(dot(hit.normal, sunDir), 0.0) * 0.9 + 0.1;
	float skyDiffuse = max(dot(hit.normal, skyDir), 0.0) * 0.8 + 0.2;
	float bouDiffuse = max(dot(hit.normal, bouDir), 0.0) * 0.9 + 0.1;

	vec3 sunColor = vec3(1.0, 1.0, 0.5);
	vec3 skyColor = vec3(0.5, 0.7, 1.0);
	vec3 bouColor = vec3(0.5, 0.5, 0.5);

	vec3 light = vec3(0.0);
	light += sunDiffuse * sunColor * sunShadow;
	light += skyDiffuse * skyColor;
	light += bouDiffuse * bouColor;

	vec3 baseColor = hit.material.color;

	return baseColor * light;
}

layout(local_size_x = 16, local_size_y = 16) in;
void main() {
	if (!isPixelValid()) return;	

	Hit hit = intersect(getRay());

	vec3 color;
	if (hit.hit) {
		float depth = readDepth();
		if (depth < hit.distance && depth != 0.0) return;

		color = shadeSolid(hit);
		writeDepth(hit.distance);
	} else {
		if (readDepth() != 0.0) return;
	
		color = vec3(0.0, 0.0, 0.0);
	}

	writeColor(vec4(gammeCorrect(aces(color)), 1.0));
}
