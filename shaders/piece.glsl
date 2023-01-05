#version 450

#include "sdf.glsl"

uniform float time;
uniform vec3 color;
uniform vec3 position;

Sdf sdfScene(in vec3 p) {
	Material material = defaultMaterial();
	material.color = color;

	return sdfPiece(p - position, material);
}

#include "raymarch.glsl"
