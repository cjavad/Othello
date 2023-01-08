#include "lighting.glsl"
#include "common.glsl"
#include "camera.glsl"
#include "gbuffer.glsl"
#include "surface.glsl"
#include "poisson.glsl"

in vec2 uv;

layout(location = 0) out vec4 o_color;

uniform vec3 lightDirection;
uniform vec3 lightColor;
uniform float lightIntensity;
uniform float lightSoftness;
uniform float lightFalloff;

uniform sampler2D shadowMap;
uniform mat4 lightViewProj;
uniform float lightSize;
uniform float lightDepth;

uniform int blockerSearchSamples;
uniform int penumbraSearchSamples;

float shadowNoise(vec3 position) {
	vec3 s = position + 0.2127 + position.x * position.y * position.z * 0.3713;
	vec3 r = 4.789 * sin(489.123 * s);
	return fract(r.x * r.y * r.z * (1.0 + s.x));
}

vec2 rotate(vec2 position, vec2 trig) {
	return vec2(
		position.x * trig.x - position.y * trig.y,
		position.x * trig.y + position.y * trig.x
	);
}

bool isUvValid(vec2 uv) {
	return uv.x >= 0.0 && uv.x <= 1.0 && uv.y >= 0.0 && uv.y <= 1.0;
}

float penumbraRadiusUV(float zReceiver, float zBlocker) {
	return zReceiver - zBlocker;
}

vec2 findBlocker(vec2 lightSpace, float z0, float bias, float radius, vec2 trig) {
	float blockerSum = 0.0;
	float blockerCount = 0.0;

	float biasedDepth = z0 - bias;

	for (int i = 0; i < blockerSearchSamples; i++) {
		vec2 _offset = POISSON_DISK64[i] * radius;
		_offset = rotate(_offset, trig);

		vec2 _sample = lightSpace + _offset;

		vec2 uv = _sample * 0.5 + 0.5;	

		if (!isUvValid(uv)) continue;

		float depth = textureLod(shadowMap, uv, 0.0).r * 2.0 - 1.0;

		if (biasedDepth > depth) {
			blockerSum += depth;
			blockerCount += 1.0;
		}
	}

	float averageBlockerDepth = blockerSum / blockerCount;

	return vec2(averageBlockerDepth, blockerCount);
}

float PCFFilter(vec2 lightSpace, float z0, float bias, vec2 radius, vec2 trig) {
	float sum = 0.0;

	float biasedDepth = z0 - bias;

	for (int i = 0; i < penumbraSearchSamples; i++) {
		vec2 _offset = POISSON_DISK64[i] * radius;
		_offset = rotate(_offset, trig);

		vec2 _sample = lightSpace + _offset;

		vec2 uv = _sample * 0.5 + 0.5;	

		if (!isUvValid(uv)) continue;

		float depth = textureLod(shadowMap, uv, 0.0).r * 2.0 - 1.0;

		if (biasedDepth < depth) {
			sum += 1.0;
		}
	}

	return sum / penumbraSearchSamples;
}

float PCSS(vec2 lightSpace, float z, float bias, float zVS, vec2 trig) {
	float searchRadius = lightSoftness / lightSize; 
	vec2 blocker = findBlocker(lightSpace, z, bias, searchRadius, trig);

	if (blocker.y < 1.0) {
		return 1.0;
	}

	float avgZ = blocker.x * lightDepth;

	float penumbra = penumbraRadiusUV(zVS, avgZ) * lightSoftness / 16.0 + 0.01;
	penumbra = 1.0 - pow(1.0 - penumbra, lightFalloff);

	vec2 filterRadius = vec2(penumbra - 0.015 * lightSoftness) / lightSize;
	filterRadius = min(vec2(searchRadius), filterRadius);

	return PCFFilter(lightSpace, z, bias, filterRadius, trig);
}

float shadow(vec3 position, vec3 normal) {
	vec4 lightSpace = lightViewProj * vec4(position, 1.0);

	if (lightSpace.w < 0.0) {
		return 1.0;
	}

	lightSpace.xyz /= lightSpace.w;

	if (lightSpace.z < -1.0 || lightSpace.z > 1.0) {
		return 1.0;
	}

	float z = lightSpace.z;
	float zVS = z * lightDepth;

	float noise = shadowNoise(gl_FragCoord.xyz);
	float angle = noise * 2.0 * PI;
	vec2 trig = vec2(cos(angle), sin(angle));

	float biasScale = 0.1;
	float bias = 1.0 / lightDepth * biasScale;

	return PCSS(lightSpace.xy, z, bias, zVS, trig);
}

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
	light.direction = -normalize(lightDirection);
	light.intensity = lightIntensity;
	light.attenuation = 1.0;
	light.occlusion = shadow(position, normal);

	vec4 lightSpace = lightViewProj * vec4(position, 1.0);
	lightSpace.xyz /= lightSpace.w;

	vec2 uv = lightSpace.xy * 0.5 + 0.5;

	vec3 color = lightSurface(surface, light);
	o_color = vec4(color, 1.0);
}
