#include "hit.glsl"

vec3 shadeSolid(Hit hit) {
	vec3 sunDir = normalize(vec3(1.0, 1.0, -1.0));
	vec3 skyDir = normalize(vec3(0.0, 1.0, 0.0));
	vec3 bouDir = normalize(vec3(0.0, -1.0, 0.0));	

	float sunDiffuse = max(dot(hit.normal, sunDir), 0.0) * 0.9 + 0.1;
	float skyDiffuse = max(dot(hit.normal, skyDir), 0.0) * 0.8 + 0.2;
	float bouDiffuse = max(dot(hit.normal, bouDir), 0.0) * 0.9 + 0.1;

	vec3 sunColor = vec3(1.0, 1.0, 0.5);
	vec3 skyColor = vec3(0.5, 0.7, 1.0);
	vec3 bouColor = vec3(0.5, 0.5, 0.5);

	vec3 light = vec3(0.0);
	light += sunDiffuse * sunColor;
	light += skyDiffuse * skyColor;
	light += bouDiffuse * bouColor;

	vec3 baseColor = hit.material.color;

	return baseColor * light;
}
