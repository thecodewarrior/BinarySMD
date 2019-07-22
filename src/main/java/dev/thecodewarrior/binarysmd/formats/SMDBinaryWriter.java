package dev.thecodewarrior.binarysmd.formats;

import dev.thecodewarrior.binarysmd.studiomdl.*;
import org.jetbrains.annotations.NotNull;
import org.msgpack.core.MessagePacker;

import java.io.IOException;
import java.util.*;

public class SMDBinaryWriter {
    // | block count (int) |
    //   | block |
    public void write(@NotNull SMDFile file, @NotNull MessagePacker packer) throws IOException {
        packer.packInt(file.blocks.size());
        for(SMDFileBlock block : file.blocks) {
            if(block instanceof NodesBlock)
                writeNodesBlock((NodesBlock) block, packer);
            if(block instanceof SkeletonBlock)
                writeSkeletonBlock((SkeletonBlock) block, packer);
            if(block instanceof TrianglesBlock)
                writeTrianglesBlock((TrianglesBlock) block, packer);
            if(block instanceof VertexAnimationBlock)
                writeVertexAnimationBlock((VertexAnimationBlock) block, packer);
        }
    }

    // | block type = 0 (byte) | count (int) |
    //   | id (int) | name (string) | parent (int) |
    private void writeNodesBlock(@NotNull NodesBlock block, @NotNull MessagePacker out) throws IOException {
        out.packByte((byte) 0);

        out.packInt(block.bones.size());
        for(NodesBlock.Bone bone : block.bones) {
            out.packInt(bone.id);
            out.packString(bone.name);
            out.packInt(bone.parent);
        }
    }

    // | block type = 1 (byte) |
    // | vector count (int) |
    //   | vectorX (float) | vectorY (float) | vectorZ (float) |
    // | keyframe count (int) |
    //   | keyframe time (int) | bone count (int) |
    //    | bone ID (int) | pos index (int) | rot index (int) |
    private void writeSkeletonBlock(@NotNull SkeletonBlock block, @NotNull MessagePacker out) throws IOException {
        out.packByte((byte) 1);

        LookupTable<Vector3> vectors = new LookupTable<>();

        for(SkeletonBlock.Keyframe keyframe : block.keyframes) {
            for (SkeletonBlock.BoneState state : keyframe.states) {
                vectors.add(new Vector3(state.posX, state.posY, state.posZ));
                vectors.add(new Vector3(state.rotX, state.rotY, state.rotZ));
            }
        }

        writeVectors(vectors, out);

        out.packInt(block.keyframes.size());
        for(SkeletonBlock.Keyframe keyframe : block.keyframes) {
            out.packInt(keyframe.time);

            out.packInt(keyframe.states.size());
            for (SkeletonBlock.BoneState state : keyframe.states) {
                out.packInt(state.bone);
                out.packInt(vectors.index(new Vector3(state.posX, state.posY, state.posZ)));
                out.packInt(vectors.index(new Vector3(state.rotX, state.rotY, state.rotZ)));
            }
        }
    }

    // | block type = 2 (byte) | material count (int) |
    //   | material name |
    // | vector count (int) |
    //   | vectorX (float) | vectorY (float) | vectorZ (float) |
    // | uv count (int) |
    //   | U (float) | V (float) |
    // | triangle count (int) |
    //   | material index (int) |
    //   | parent bone (int) | pos index (int) | normal index (int) | UV index (int) | link count (int) |
    //     | link bone ID (int) | link weight (float) |
    //   | parent bone (int) | pos index (int) | normal index (int) | UV index (int) | link count (int) |
    //     | link bone ID (int) | link weight (float) |
    //   | parent bone (int) | pos index (int) | normal index (int) | UV index (int) | link count (int) |
    //     | link bone ID (int) | link weight (float) |
    private void writeTrianglesBlock(@NotNull TrianglesBlock block, @NotNull MessagePacker out) throws IOException {
        out.packByte((byte) 2);

        LookupTable<String> materials = new LookupTable<>();
        LookupTable<Vector3> vectors = new LookupTable<>();
        LookupTable<TexCoord> uvs = new LookupTable<>();

        for(TrianglesBlock.Triangle triangle : block.triangles) {
            materials.add(triangle.material);
            for(TrianglesBlock.Vertex vertex : triangle.vertices) {
                vectors.add(new Vector3(vertex.posX, vertex.posY, vertex.posZ));
                vectors.add(new Vector3(vertex.normX, vertex.normY, vertex.normZ));
                uvs.add(new TexCoord(vertex.u, vertex.v));
            }
        }

        out.packInt(materials.size());
        for(String material : materials.values()) {
            out.packString(material);
        }

        writeVectors(vectors, out);

        out.packInt(uvs.size());
        for(TexCoord uv : uvs.values()) {
            out.packFloat(uv.u);
            out.packFloat(uv.v);
        }

        out.packInt(block.triangles.size());
        for(TrianglesBlock.Triangle triangle : block.triangles) {
            out.packInt(materials.index(triangle.material));
            for(TrianglesBlock.Vertex vertex : triangle.vertices) {
                out.packInt(vertex.parentBone);
                out.packInt(vectors.index(new Vector3(vertex.posX, vertex.posY, vertex.posZ)));
                out.packInt(vectors.index(new Vector3(vertex.normX, vertex.normY, vertex.normZ)));
                out.packInt(uvs.index(new TexCoord(vertex.u, vertex.v)));
                out.packInt(vertex.links.size());
                for (TrianglesBlock.Link link : vertex.links) {
                    out.packInt(link.bone);
                    out.packFloat(link.weight);
                }
            }
        }
    }

    // | block type = 3 (byte) | vector count (int) |
    //   | vectorX (float) | vectorY (float) | vectorZ (float) |
    // | frame count (int) |
    //   | frame time (int) | vertex count (int) |
    //     | vertex ID (int) | pos index (int) | normal index (int) |
    private void writeVertexAnimationBlock(@NotNull VertexAnimationBlock block, @NotNull MessagePacker out) throws IOException {
        out.packByte((byte) 2);

        LookupTable<Vector3> vectors = new LookupTable<>();
        for(VertexAnimationBlock.Keyframe keyframe : block.keyframes) {
            for (VertexAnimationBlock.VertexState state : keyframe.states) {
                vectors.add(new Vector3(state.posX, state.posY, state.posZ));
                vectors.add(new Vector3(state.normX, state.normY, state.normZ));
            }
        }

        writeVectors(vectors, out);

        for(VertexAnimationBlock.Keyframe keyframe : block.keyframes) {
            out.packInt(keyframe.time);
            out.packInt(keyframe.states.size());
            for (VertexAnimationBlock.VertexState state : keyframe.states) {
                out.packInt(state.vertex);
                out.packInt(vectors.index(new Vector3(state.posX, state.posY, state.posZ)));
                out.packInt(vectors.index(new Vector3(state.normX, state.normY, state.normZ)));
            }
        }
    }

    private void writeVectors(@NotNull LookupTable<Vector3> vectors, @NotNull MessagePacker out) throws IOException {
        out.packInt(vectors.size());
        for(Vector3 vector : vectors.values()) {
            out.packFloat(vector.x);
            out.packFloat(vector.y);
            out.packFloat(vector.z);
        }
    }

    private static class Vector3 {
        final float x, y, z;

        public Vector3(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Vector3)) return false;
            Vector3 vector3 = (Vector3) o;
            return Float.compare(vector3.x, x) == 0 &&
                    Float.compare(vector3.y, y) == 0 &&
                    Float.compare(vector3.z, z) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    private static class TexCoord {
        final float u, v;

        public TexCoord(float u, float v) {
            this.u = u;
            this.v = v;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TexCoord)) return false;
            TexCoord texCoord = (TexCoord) o;
            return Float.compare(texCoord.u, u) == 0 &&
                    Float.compare(texCoord.v, v) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(u, v);
        }
    }
}
