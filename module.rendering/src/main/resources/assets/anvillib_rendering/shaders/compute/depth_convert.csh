#version 460 core

layout(local_size_x = 16, local_size_y = 16, local_size_z = 1) in;

layout(std140, binding = 0) uniform ConvertParam {
    int uWidth;
    int uHeight;
    float uPadValue;
};

layout(binding = 1) uniform sampler2D Input;

layout(binding = 2, r32f) writeonly uniform image2D Output;

void main() {
    ivec2 idx = ivec2(gl_GlobalInvocationID.xy);

    if (idx.x >= uWidth || idx.y >= uHeight) {
        imageStore(Output, idx, vec4(1, 1, 1, 1));
        return;
    }

    vec4 converted = texelFetch(Input, idx, 0);

    imageStore(Output, idx, converted);
}