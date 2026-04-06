#version 330

layout(std140) uniform BloomParameters {
    float BloomIntensity;
};

uniform sampler2D DiffuseSampler;
uniform sampler2D GameSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 colorGame = texture(DiffuseSampler, texCoord);
    vec3 color = colorGame.rgb;
    vec4 bloom4 = texture(GameSampler, texCoord);
    vec3 bloom = bloom4.rgb * BloomIntensity;
    vec3 finalColor = color + (bloom * pow(0.08, length(color) * 0.8));
    fragColor = vec4(finalColor, colorGame.a + bloom4.a);
}
