struct Particle {
	mat4 model;
	vec3 velocity;
	// ...
};

vec3 particle_position(Particle p) {
	return p.model[3].xyz;
}

void set_particle_position(inout Particle p, vec3 pos) {
	p.model[3].xyz = pos;
}

mat4 particle_rotation(Particle p) {
	return mat4(
		vec4(p.model[0].xyz, 0.0),
		vec4(p.model[1].xyz, 0.0),
		vec4(p.model[2].xyz, 0.0),
		vec4(0.0, 0.0, 0.0, 1.0)
	);
}

void set_particle_rotation(inout Particle p, mat4 rot) {
	p.model[0].xyz = rot[0].xyz;
	p.model[1].xyz = rot[1].xyz;
	p.model[2].xyz = rot[2].xyz;
}
