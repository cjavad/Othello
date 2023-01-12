#include "lighting.glsl"
#include "gbuffer.glsl"
#include "camera.glsl"
#include "surface.glsl"
#include "environment.glsl"

in vec2 uv;
in vec2 clip;

layout(location = 0) out vec4 o_color;

uniform float intensity;

void main() {
	vec3 position = texture(g_position, uv).xyz;
	vec3 normal = texture(g_normal, uv).xyz;
	vec3 baseColor = texture(g_baseColor, uv).xyz;
	uvec4 material = texture(g_material, uv);
	vec4 material_0 = rgbaFromUint(material.x);

	float metallic = material_0.x;
	float roughness = material_0.y;
	float reflectance = material_0.z;	

	Surface surface;

	surface.position = position;
	surface.normal = normalize(normal);

	vec4 far = invViewProj * vec4(clip, 1.0, 1.0);
	far.xyz /= far.w;
	surface.view = flip(normalize(cameraPosition - far.xyz));

	surface.F0 = computeF0(baseColor, metallic, reflectance);
	surface.F90 = computeF90(surface.F0);

	surface.diffuse = baseColor * (1.0 - metallic);
	surface.perceptualRoughness = clamp(roughness, MIN_PERCEPTUAL_ROUGHNESS, 0.99);
	surface.roughness = computeRoughness(surface.perceptualRoughness);
	surface.metallic = metallic;

	if (texture(g_depth, uv).x == 1.0) {
		o_color = vec4(texture(skyMap, surface.view).rgb * 0.8, 1.0);	
		return;
	}
	
	vec3 color = lightEnvironment(surface) * intensity;
	o_color = vec4(color, 1.0);
}
