#version 460 core

#ifndef MAX_MIP_LEVELS
#define MAX_MIP_LEVELS 12
#endif

struct AABB { // maybe its better using 3 * vec2: {minXY, maxXY, minMaxZZ}
    vec4 minPos; // w = 0
    vec4 maxPos; // w = 0
};

layout(std430, binding = 0) uniform CB {
    int elementCount; // number of aabb elements
    int mipLevels; // number of valid entries of uInputs and mipLayers, including mip 0
    vec2 viewportSize;
    vec4 cameraPos; // avoid vec3 in std140, cameraPos.w = 1
    mat4 ProjMat; // CameraRenderState#projectionMatrix
    mat4 CameraMat; // new Matrix4f(CameraRenderState#viewRotationMatrix)
} cbOcclusionTest;

layout(std430, binding = 0) buffer ShaderInput {
    ivec2 mipLayers[MAX_MIP_LEVELS + 1];
    AABB aabbs[];
};

layout(std430, binding = 1) buffer ShaderOutput {
    int result[];
};

layout(binding = 0, r32f) uniform image2D uInputs[MAX_MIP_LEVELS + 1];

layout(local_size_x = 32, local_size_y = 1, local_size_z = 1) in;

void main() {
    uint index = gl_GlobalInvocationID.x;
    if (index >= uint(cbOcclusionTest.elementCount)) {
        return;
    }

}
