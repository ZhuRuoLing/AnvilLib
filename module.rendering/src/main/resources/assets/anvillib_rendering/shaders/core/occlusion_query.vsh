#version 330

layout(std140) uniform Transforms {
    mat4 ProjMat;
    mat4 ModelViewMat;
};

in vec3 Position;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}
