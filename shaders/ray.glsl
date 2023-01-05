struct Ray {
	vec3 origin;
	vec3 direction;
};

Ray newRay(vec3 origin, vec3 direction) {
	return Ray(origin, direction);
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
