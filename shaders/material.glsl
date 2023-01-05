struct Material {
	vec3 color;
};

Material defaultMaterial() {
	Material material;

	material.color = vec3(1.0, 1.0, 1.0);

	return material;
}
