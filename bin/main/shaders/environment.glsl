#include "surface.glsl"
#include "common.glsl"

uniform samplerCube irradianceMap;
uniform samplerCube indirectMap;
uniform samplerCube skyMap;

uniform sampler2D integratedDFG;

vec2 sampleDFG(float perceptualRoughness, float NoV) {
	vec2 uv = vec2(perceptualRoughness, NoV);
	return texture(integratedDFG, uv).xy;
}

vec3 environmentIndirect(float perceptualRoughness, vec3 R) {
	float mipLevels = float(textureQueryLevels(indirectMap));
	float mip = clamp(perceptualRoughness * (mipLevels - 1.0), 0.0, mipLevels - 1.0);
	return textureLod(indirectMap, flip(R), mip).rgb;
}

vec3 lightEnvironment(Surface surface) {
	vec3 N = surface.normal;
	vec3 V = surface.view;
	vec3 R = reflect(-V, N);

	float NoV = saturate(dot(N, V));

	vec2 dfg = sampleDFG(surface.perceptualRoughness, NoV);
	vec3 e = surface.F0 * dfg.x + surface.F0 * dfg.y;

	vec3 diffuse = surface.diffuse * texture(irradianceMap, N).rgb;
	R = mix(R, N, surface.roughness * surface.roughness);
	vec3 specular = environmentIndirect(surface.roughness, R);

	diffuse *= 1.0 - e;
	specular *= e;

	return diffuse + specular;
}
