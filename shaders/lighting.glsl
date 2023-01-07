#include "common.glsl"
#include "surface.glsl"
#include "light.glsl"

float DistributionGGX(float roughness, float NoH) {
	float o = 1.0 - NoH * NoH;
	float a = NoH * roughness;
	float k = roughness / (o + a * a);
	return saturate(k * k * (1.0 / PI));
}

float GeometrySmith(float roughness, float NoV, float NoL) {
	float a2 = roughness * roughness;
	float lambdaV = NoL * sqrt((NoV - a2 * NoV) * NoV + a2);
	float lambdaL = NoV * sqrt((NoL - a2 * NoL) * NoL + a2);
	return saturate(1.0 / (lambdaV + lambdaL));
}

float fresnelSchlick(float F0, float F90, float VoH) {
	return F0 + (F90 - F0) * pow(1.0 - VoH, 5.0);
}

vec3 fresnelSchlick(vec3 F0, float F90, float VoH) {
	return F0 + (F90 - F0) * pow(1.0 - VoH, 5.0);
}

vec3 fresnel(vec3 F0, float LoH) {
	float F90 = saturate(dot(F0, vec3(50.0 * 0.33)));
	return fresnelSchlick(F0, F90, LoH);
}

float fdBurley(float roughness, float NoV, float NoL, float LoH) {
	float F90 = 0.5 + 2.0 * roughness * LoH * LoH;
	float lightScatter = fresnelSchlick(1.0, F90, NoL);
	float viewScatter = fresnelSchlick(1.0, F90, NoV);
	return lightScatter * viewScatter * (1.0 / PI);
}

vec3 specularLobe(float roughness, vec3 F0, float NoV, float NoL, float NoH, float LoH) {
	float d = DistributionGGX(roughness, NoH);
	float v = GeometrySmith(roughness, NoV, NoL);
	vec3 f = fresnel(F0, LoH);

	return d * v * f;
}

vec3 lightSurface(Surface surface, Light light) {
	vec3 N = surface.normal;
	vec3 V = surface.view;
	vec3 L = light.direction;

	vec3 H = normalize(V + L);
	float NoL = saturate(dot(N, L));
	float NoV = saturate(dot(N, V));
	float NoH = saturate(dot(N, H));
	float LoH = saturate(dot(L, H));

	if (NoL <= 0.0) return vec3(0.0);

	vec3 diffuseLight = fdBurley(surface.roughness, NoV, NoL, LoH) * surface.diffuse;
	vec3 specularLight = specularLobe(surface.roughness, surface.F0, NoV, NoL, NoH, LoH);

	vec3 color = (diffuseLight + specularLight) * NoL * light.occlusion;
	color *= light.color * light.intensity * light.attenuation;

	return color;
}
