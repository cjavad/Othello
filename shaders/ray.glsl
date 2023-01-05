struct Ray {
	vec3 origin;
	vec3 direction;
};

Ray newRay(vec3 origin, vec3 direction) {
	return Ray(origin, direction);
}
