#version 150

uniform sampler2D Sampler0;

in vec2 texCoord0;
uniform float time; // For dynamic SSTV effect
uniform float scanLineIntensity; // Intensity of the scan lines
uniform float Saturation; // Saturation
uniform mat3 ColorFilter; // ColorFilter
uniform int nightVision;
float lum;
out vec4 fragColor;

vec3 lerp(vec3 colorone, vec3 colortwo, float value)
{
	return (colorone + value*(colortwo-colorone));
}
vec3 RGBToHSV( vec3 RGB ){

	vec4 k = vec4(0.0, -1.0/3.0, 2.0/3.0, -1.0);
	vec4 p = RGB.g < RGB.b ? vec4(RGB.b, RGB.g, k.w, k.z) : vec4(RGB.gb, k.xy);
	vec4 q = RGB.r < p.x   ? vec4(p.x, p.y, p.w, RGB.r) : vec4(RGB.r, p.yzx);
	float d = q.x - min(q.w, q.y);
	float e = 1.0e-10;
	return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 HSVToRGB( vec3 HSV ){

	vec4 k = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
	vec3 p = abs(fract(HSV.xxx + k.xyz) * 6.0 - k.www);
	return HSV.z * lerp(k.xxx, clamp(p - k.xxx, 0.0, 1.0), HSV.y);
}

void main() {
	vec2 center = vec2(0.5, 0.5); // Center of the screen
	vec2 coord = texCoord0 - center; // Coordinate relative to the center
	float dist = length(coord); // Distance from the center

	// Apply bulge effect with the increased distance factor
	vec2 bulgedCoord = texCoord0 + coord * dist * 0.5  ;

	// COLOR STUFF
	vec4 sourceColor = texture(Sampler0, bulgedCoord);
	vec3 col = sourceColor.rgb;

	if(nightVision >= 1){
		col *= ColorFilter;
	}

	col = RGBToHSV(col);
	col.g *= Saturation;
	col = HSVToRGB(col);

	vec4 color = vec4(col, 1.0);

	// Apply SSTV effect
	float scanLine = sin(texCoord0.y * 800.0 + time * 10.0) * scanLineIntensity;
	float noise = (fract(sin(dot(texCoord0, vec2(12.9898, 78.233)) * 43758.5453)) - 0.5) * 0.1;

	color.rgb += scanLine + noise;

	fragColor = color;
}
