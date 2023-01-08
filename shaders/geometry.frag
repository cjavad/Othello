#include "common.glsl"

in vec3 w_position;
in vec3 w_normal;
in vec3 w_tangent;
in vec3 w_bitangent;
in vec2 w_uv;

layout(location = 0) out vec4 o_position;
layout(location = 1) out vec4 o_normal;
layout(location = 2) out vec4 o_baseColor;
layout(location = 3) out uvec4 o_material;

uniform vec3 baseColor;
uniform float metallic;
uniform float roughness;
uniform float reflectance;

uniform uint objectId;

uniform sampler2D baseColorMap;
uniform sampler2D metallicRoughnessMap;
uniform sampler2D normalMap;

void main() {
	o_position = vec4(w_position, 0.0);

	mat3 TBN = mat3(w_tangent, w_bitangent, w_normal);
	vec3 normal = texture(normalMap, w_uv).rgb * 2.0 - 1.0;
	o_normal = vec4(normalize(TBN * normal), 0.0);

	o_baseColor = texture(baseColorMap, w_uv) * vec4(baseColor, 1.0);

	float metallic = texture(metallicRoughnessMap, w_uv).x * metallic;
	float roughness = texture(metallicRoughnessMap, w_uv).y * roughness;

	o_material = uvec4(
		uintFromRgba(vec4(metallic, roughness, reflectance, 1.0)),
		0, 
		0,
		objectId
	);
}
