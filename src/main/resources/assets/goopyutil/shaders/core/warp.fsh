#version 150

uniform sampler2D Sampler0;

in vec2 texCoord0;
uniform float time; // For dynamic SSTV effect
uniform float scanLineIntensity; // Intensity of the scan lines
out vec4 fragColor;

void main() {
	vec2 center = vec2(0.5, 0.5); // Center of the screen
	vec2 coord = texCoord0 - center; // Coordinate relative to the center
	float dist = length(coord); // Distance from the center

	// Apply bulge effect with the increased distance factor
	vec2 bulgedCoord = texCoord0 + coord * dist * 0.5  ;

	// Sample the texture with the new coordinates
	vec4 color = texture(Sampler0, bulgedCoord);

	// Apply SSTV effect
	float scanLine = sin(texCoord0.y * 800.0 + time * 10.0) * scanLineIntensity;
	float noise = (fract(sin(dot(texCoord0, vec2(12.9898, 78.233)) * 43758.5453)) - 0.5) * 0.1;

	color.rgb += scanLine + noise;

	fragColor = color;
}
