package dev.anvilcraft.lib.v2.rendering.optimization.occlusion;

import java.util.List;

public record CullingStatistics(
    int total,
    int frustumPrePass,
    int cameraInside,
    int culled,
    int rendered,
    int features,
    List<String> message
) {

}
