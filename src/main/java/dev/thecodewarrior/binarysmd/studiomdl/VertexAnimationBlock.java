package dev.thecodewarrior.binarysmd.studiomdl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VertexAnimationBlock extends SMDFileBlock {
    public @NotNull List<@NotNull Keyframe> keyframes = new ArrayList<>();

    public static class Keyframe {
        public int time;
        public @NotNull List<@NotNull VertexState> states = new ArrayList<>();

        public Keyframe(int time) {
            this.time = time;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Keyframe)) return false;
            Keyframe keyframe = (Keyframe) o;
            return time == keyframe.time &&
                    states.equals(keyframe.states);
        }

        @Override
        public int hashCode() {
            return Objects.hash(time, states);
        }
    }

    public static class VertexState {
        public int bone;
        public float posX, posY, posZ;
        public float normX, normY, normZ;

        public VertexState(int bone, float posX, float posY, float posZ, float normX, float normY, float normZ) {
            this.bone = bone;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.normX = normX;
            this.normY = normY;
            this.normZ = normZ;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof VertexState)) return false;
            VertexState vertexState = (VertexState) o;
            return bone == vertexState.bone &&
                    Float.compare(vertexState.posX, posX) == 0 &&
                    Float.compare(vertexState.posY, posY) == 0 &&
                    Float.compare(vertexState.posZ, posZ) == 0 &&
                    Float.compare(vertexState.normX, normX) == 0 &&
                    Float.compare(vertexState.normY, normY) == 0 &&
                    Float.compare(vertexState.normZ, normZ) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(bone, posX, posY, posZ, normX, normY, normZ);
        }
    }
}
