#version 450

layout(local_size_x = 16, local_size_y = 16) in;

uniform int imageWidth;
uniform int imageHeight;

uniform mat4 viewMatrix;
uniform float time;

buffer ColorBuffer {
	int colors[];
};

uint index() {
	return gl_GlobalInvocationID.x + gl_GlobalInvocationID.y * imageWidth;
}

int rgbaToInt(vec4 rgba) {
	rgba = clamp(rgba, 0.0, 1.0);

	// convert to 0-255 bgra
	return int(rgba.a * 255.0) << 24 
		 | int(rgba.r * 255.0) << 16 
		 | int(rgba.g * 255.0) << 8 
		 | int(rgba.b * 255.0);
}

void writeColor(vec4 color) {
	colors[index()] = rgbaToInt(color);
}

struct Ray {
	vec3 origin;
	vec3 direction;
};

Ray newRay(vec3 origin, vec3 direction) {
	return Ray(origin, direction);
}

Ray transformRay(mat4 matrix, Ray ray) {
	Ray result;

	result.origin = (matrix * vec4(ray.origin, 1.0)).xyz;
	result.direction = (matrix * vec4(ray.direction, 0.0)).xyz;

	return result;
}

struct Material {
	vec3 color;
};

Material defaultMaterial() {
	Material material;

	material.color = vec3(1.0, 1.0, 1.0);

	return material;
}

struct Map {
	float distance;
	Material material;
};

Map emptyMap() {
	Map map;
	map.distance = 0.0;

	return map;
}

struct Hit {
	bool hit;
	float distance;	
	Material material;
	vec3 position;
	vec3 normal;
};

Hit emptyHit() {
	Hit hit;
	hit.hit = false;
	hit.distance = 0.0;

	return hit;
}

Map sdfUnion(Map a, Map b) {
	if (a.distance < b.distance) {
		return a;
	} else {
		return b;
	}
} 

Map sdfSphere(vec3 p, float radius, Material material) {
	Map map;
	map.distance = length(p) - radius;
	map.material = material;

	return map;
}

Map sdfRoundedCylender(vec3 p, float radius, float height, float roundness, Material material) {
	Map map;

	vec2 d = vec2(length(p.xz) - 2.0 * radius + roundness, abs(p.y) - height);
	map.distance = min(max(d.x, d.y), 0.0) + length(max(d, 0.0)) - roundness;
	map.material = material;
	
	return map;
}

Map sdfPiece(vec3 p, Material material) {
	Map map = sdfRoundedCylender(p, 0.5, 0.1, 0.2, material);
	return map;
}

Map map(in vec3 p) {
	Map d;

	Map sphereA = sdfPiece(p + vec3(0.0, sin(time), 0.0), defaultMaterial());
	Map sphereB = sdfSphere(p + vec3(2.0, 5.0, -2.0), 4.0, defaultMaterial());

	d = sdfUnion(sphereA, sphereB);

	return d;
}

vec3 normal(in vec3 p) {
	vec3 e = vec3(0.001, 0.0, 0.0);

	return normalize(vec3(
		map(p + e.xyy).distance - map(p - e.xyy).distance,
		map(p + e.yxy).distance - map(p - e.yxy).distance,
		map(p + e.yyx).distance - map(p - e.yyx).distance
	));
}

Hit intersect(Ray ray) {
	Hit hit = emptyHit();

	for (int i = 0; i < 256; i++) {
		hit.position = ray.origin + ray.direction * hit.distance;

		Map map = map(hit.position);
		hit.distance += map.distance;
		hit.material = map.material;

		if (abs(map.distance) < 0.001) {
			hit.hit = true;
			hit.normal = normal(hit.position);

			break;
		}

		if (hit.distance > 100.0) break;
	}

	return hit;
}

float shadow(in Ray ray, float k) {
    float shadow = 1.0;

    float t = 0.01;
    for(int i = 0; i < 64; i++) {
        vec3 position = ray.origin + ray.direction * t;
		Map map = map(position);

        shadow = min(shadow, k * max(map.distance, 0.0) / t);
        if(shadow < 0.0001) break;

        t += clamp(map.distance, 0.01, 5.0);
    }

    return shadow;
}

vec3 shadeSolid(Hit hit) {
	vec3 sunDir = normalize(vec3(1.0, 1.0, -1.0));
	vec3 skyDir = normalize(vec3(0.0, 1.0, 0.0));
	vec3 bouDir = normalize(vec3(0.0, -1.0, 0.0));

	Ray shadowRay = newRay(hit.position + hit.normal * 0.1, sunDir);
	float sunShadow = shadow(shadowRay, 8.0);

	float sunDiffuse = max(dot(hit.normal, sunDir), 0.0) * 0.9 + 0.1;
	float skyDiffuse = max(dot(hit.normal, skyDir), 0.0) * 0.8 + 0.2;
	float bouDiffuse = max(dot(hit.normal, bouDir), 0.0) * 0.9 + 0.1;

	vec3 sunColor = vec3(1.0, 1.0, 0.5);
	vec3 skyColor = vec3(0.5, 0.7, 1.0);
	vec3 bouColor = vec3(0.5, 0.5, 0.5);

	vec3 light = vec3(0.0);
	light += sunDiffuse * sunColor * sunShadow;
	light += skyDiffuse * skyColor;
	light += bouDiffuse * bouColor;

	vec3 baseColor = hit.material.color;

	return baseColor * light;
}

vec3 aces(vec3 x) {
  const float a = 2.51;
  const float b = 0.03;
  const float c = 2.43;
  const float d = 0.59;
  const float e = 0.14;
  return clamp((x * (a * x + b)) / (x * (c * x + d) + e), 0.0, 1.0);
}

vec3 toneMap(vec3 x) {
  return aces(x);
}

vec3 gammeCorrect(vec3 x) {
  return pow(x, vec3(1.0 / 2.2));
}

void main() {
	float aspect = float(imageWidth) / float(imageHeight);
	vec2 uv = vec2(gl_GlobalInvocationID.xy) / vec2(imageWidth, imageHeight) - 0.5;
	uv *= vec2(aspect * 2.0, -2.0);

	Ray ray = newRay(vec3(0.0, 0.0, -2.0), vec3(uv, 1.0));
	//ray = transformRay(viewMatrix, ray);

	Hit hit = intersect(ray);

	vec3 color;

	if (hit.hit) {
		color = shadeSolid(hit);
	} else {
		color = vec3(0.0, 0.0, 0.0);
	}

	vec3 mappedColor = toneMap(color);
	writeColor(vec4(gammeCorrect(mappedColor), 1.0));
}
