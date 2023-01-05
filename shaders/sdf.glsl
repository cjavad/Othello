#include "material.glsl"

struct Sdf {
	float distance;
	Material material;
};

Sdf emptySdf() {
	Sdf sdf;
	sdf.distance = 1.0 / 0.0;

	return sdf;
}

Sdf sdfUnion(Sdf a, Sdf b) {
	if (a.distance < b.distance) {
		return a;
	} else {
		return b;
	}
} 

Sdf sdfSphere(vec3 p, float radius, Material material) {
	Sdf sdf;
	sdf.distance = length(p) - radius;
	sdf.material = material;

	return sdf;
}

Sdf sdfRoundedCylender(vec3 p, float radius, float height, float roundness, Material material) {
	Sdf sdf;

	vec2 d = vec2(length(p.xz) - 2.0 * radius + roundness, abs(p.y) - height);
	sdf.distance = min(max(d.x, d.y), 0.0) + length(max(d, 0.0)) - roundness;
	sdf.material = material;
	
	return sdf;
}

Sdf sdfPiece(vec3 p, Material material) {
	Sdf sdf = sdfRoundedCylender(p, 0.2, 0.05, 0.1, material);
	return sdf;
}

