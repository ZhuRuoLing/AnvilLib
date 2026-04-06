#version 330

#moj_import<anvillib_rendering:util.glsl>

layout(std140) uniform BlurParameters {
    vec2 BlurDir;
};

uniform sampler2D DiffuseSampler;

const float weight[7] = float[] (0.227027, 0.1945946, 0.1516216, 0.124054, 0.016216, 0.0111, 0.0100);

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec2 texOffset = 1.0 / textureSize(DiffuseSampler, 0);

    vec3 result = texture(DiffuseSampler, texCoord).rgb * weight[0];
    for (int i = 1; i < 7; ++i) {
        result += texture(DiffuseSampler, texCoord + vec2(BlurDir.x * texOffset.x * i * 1.943, BlurDir.y * texOffset.y * i * 1.943)).rgb * weight[i];
        result += texture(DiffuseSampler, texCoord - vec2(BlurDir.x * texOffset.x * i * 1.943, BlurDir.y * texOffset.y * i * 1.943)).rgb * weight[i];
    }
    fragColor = vec4(saturate(toneMap(result) * 1.105), 1.0);
}
