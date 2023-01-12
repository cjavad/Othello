const float MIN_PERCEPTUAL_ROUGHNESS = 0.089;

vec3 computeF0(vec3 baseColor, float metallic, float reflectance) {
	float a = 0.16 * reflectance * reflectance * (1.0 - metallic);
	vec3 b = baseColor * metallic;
	return a + b;
}

float computeF90(vec3 F0) {
	return saturate(dot(F0, vec3(50.0 * 0.33)));
}

float computeRoughness(float perceptualRoughness) {
	return perceptualRoughness * perceptualRoughness;
}

struct Surface {
	vec3 position;
	vec3 normal;
	vec3 view;
	vec3 F0;
	float F90;
	vec3 diffuse;
	float perceptualRoughness;
	float roughness;
	float metallic;
};
