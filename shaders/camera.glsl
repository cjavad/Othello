#include "ray.glsl"
#include "image.glsl"

uniform mat4 viewProj;

Ray getRay() {
	vec2 uv = getUV();

	mat4 inverseViewProj = inverse(viewProj);

	vec4 near = vec4(uv	* 2.0 - 1.0, 0.0, 1.0);
	near = inverseViewProj * near;

	vec4 far = near + inverseViewProj[2];
	near.xyz /= near.w;
	far.xyz /= far.w;

	return newRay(near.xyz, normalize(far.xyz - near.xyz));
}
