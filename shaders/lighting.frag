#include "lighting.glsl"
#include "common.glsl"
#include "camera.glsl"
#include "gbuffer.glsl"
#include "surface.glsl"

in vec2 uv;

layout(location = 0) out vec4 o_color;

uniform vec3 lightDirection;
uniform vec3 lightColor;

void main() {
	float depth = texture(g_depth, uv).x;
	if (depth == 1.0) discard;

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
	surface.view = flip(normalize(cameraPosition - position));

	surface.F0 = computeF0(baseColor, metallic, reflectance);
	surface.F90 = computeF90(surface.F0);

	surface.diffuse = baseColor * (1.0 - metallic);
	surface.perceptualRoughness = clamp(roughness, MIN_PERCEPTUAL_ROUGHNESS, 0.99);
	surface.roughness = computeRoughness(surface.perceptualRoughness);
	surface.metallic = metallic;

	Light light;
	light.color = lightColor;
	light.direction = -lightDirection;
	light.intensity = 1.0;
	light.attenuation = 1.0;
	light.occlusion = 1.0;

	vec3 color = lightSurface(surface, light);
	o_color = vec4(color, 1.0);
}
