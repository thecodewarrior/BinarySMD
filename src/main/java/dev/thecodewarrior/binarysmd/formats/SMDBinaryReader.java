package dev.thecodewarrior.binarysmd.formats;

import dev.thecodewarrior.binarysmd.studiomdl.*;
import org.jetbrains.annotations.NotNull;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.Objects;

public class SMDBinaryReader {

    // | block count (int) |
    //   | block |
    public SMDFile read(@NotNull MessageUnpacker data) throws IOException {
        SMDFile file = new SMDFile();
        int blockCount = data.unpackInt();
        for (int i = 0; i < blockCount; i++) {
            int block = data.unpackByte();
            switch(block) {
                case 0:
                    file.blocks.add(readNodesBlock(data));
                    break;
                case 1:
                    file.blocks.add(readSkeletonBlock(data));
                    break;
                case 2:
                    file.blocks.add(readTrianglesBlock(data));
                    break;
                case 3:
                    file.blocks.add(readVertexAnimationBlock(data));
                    break;
            }
        }

        return file;
    }

    // | block type = 0 (byte) | count (int) |
    //   | id (int) | name (string) | parent (int) |
    private NodesBlock readNodesBlock(@NotNull MessageUnpacker file) throws IOException {
        NodesBlock block = new NodesBlock();

        int boneCount = file.unpackInt();
        for (int i = 0; i < boneCount; i++) {
            block.bones.add(new NodesBlock.Bone(file.unpackInt(), file.unpackString(), file.unpackInt()));
        }

        return block;
    }

    // | block type = 1 (byte) |
    // | vector count (int) |
    //   | vectorX (float) | vectorY (float) | vectorZ (float) |
    // | keyframe count (int) |
    //   | keyframe time (int) | bone count (int) |
    //    | bone ID (int) | pos index (int) | rot index (int) |
    private SkeletonBlock readSkeletonBlock(@NotNull MessageUnpacker file) throws IOException {
        SkeletonBlock block = new SkeletonBlock();

        LookupTable<Vector3> vectors = new LookupTable<>();

        int vectorCount = file.unpackInt();
        for (int i = 0; i < vectorCount; i++) {
            vectors.add(new Vector3(file.unpackFloat(), file.unpackFloat(), file.unpackFloat()));
        }

        int keyframeCount = file.unpackInt();
        for (int i = 0; i < keyframeCount; i++) {
            SkeletonBlock.Keyframe keyframe = new SkeletonBlock.Keyframe(file.unpackInt());
            int boneCount = file.unpackInt();
            for (int j = 0; j < boneCount; j++) {
                int boneID = file.unpackInt();
                Vector3 pos = vectors.value(file.unpackInt());
                Vector3 rot = vectors.value(file.unpackInt());
                keyframe.states.add(
                        new SkeletonBlock.BoneState(boneID,
                                pos.x, pos.y, pos.z,
                                rot.x, rot.y, rot.z
                        )
                );
            }
            block.keyframes.add(keyframe);
        }

        return block;
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
    private TrianglesBlock readTrianglesBlock(@NotNull MessageUnpacker file) throws IOException {
        TrianglesBlock block = new TrianglesBlock();

        LookupTable<String> materials = new LookupTable<>();
        int count = file.unpackInt();
        for (int i = 0; i < count; i++) {
            materials.add(file.unpackString());
        }

        LookupTable<Vector3> vectors = new LookupTable<>();
        count = file.unpackInt();
        for (int i = 0; i < count; i++) {
            vectors.add(new Vector3(file.unpackFloat(), file.unpackFloat(), file.unpackFloat()));
        }

        LookupTable<TexCoord> uvs = new LookupTable<>();
        count = file.unpackInt();
        for (int i = 0; i < count; i++) {
            uvs.add(new TexCoord(file.unpackFloat(), file.unpackFloat()));
        }

        count = file.unpackInt();
        for (int i = 0; i < count; i++) {
            String material = materials.value(file.unpackInt());
            TrianglesBlock.Vertex[] vertices = new TrianglesBlock.Vertex[3];
            for (int j = 0; j < vertices.length; j++) {

                //   | parent bone (int) | pos index (int) | normal index (int) | UV index (int) | link count (int) |
                //     | link bone ID (int) | link weight (float) |
                int parent = file.unpackInt();
                Vector3 pos = vectors.value(file.unpackInt());
                Vector3 norm = vectors.value(file.unpackInt());
                TexCoord uv = uvs.value(file.unpackInt());
                TrianglesBlock.Vertex vertex = new TrianglesBlock.Vertex(parent,
                        pos.x, pos.y, pos.z,
                        norm.x, norm.y, norm.z,
                        uv.u, uv.v
                );
                int linkCount = file.unpackInt();
                for (int k = 0; k < linkCount; k++) {
                    vertex.links.add(new TrianglesBlock.Link(file.unpackInt(), file.unpackFloat()));
                }
                vertices[j] = vertex;
            }
            block.triangles.add(new TrianglesBlock.Triangle(
                    material,
                    vertices[0],
                    vertices[1],
                    vertices[2]
            ));
        }

        return block;
    }

    // | block type = 3 (byte) | vector count (int) |
    //   | vectorX (float) | vectorY (float) | vectorZ (float) |
    // | frame count (int) |
    //   | frame time (int) | vertex count (int) |
    //     | vertex ID (int) | pos index (int) | normal index (int) |
    private VertexAnimationBlock readVertexAnimationBlock(@NotNull MessageUnpacker file) throws IOException {
        VertexAnimationBlock block = new VertexAnimationBlock();

        LookupTable<Vector3> vectors = new LookupTable<>();

        int vectorCount = file.unpackInt();
        for (int i = 0; i < vectorCount; i++) {
            vectors.add(new Vector3(file.unpackFloat(), file.unpackFloat(), file.unpackFloat()));
        }

        int keyframeCount = file.unpackInt();
        for (int i = 0; i < keyframeCount; i++) {
            VertexAnimationBlock.Keyframe keyframe = new VertexAnimationBlock.Keyframe(file.unpackInt());
            int boneCount = file.unpackInt();
            for (int j = 0; j < boneCount; j++) {
                int vertexID = file.unpackInt();
                Vector3 pos = vectors.value(file.unpackInt());
                Vector3 norm = vectors.value(file.unpackInt());
                keyframe.states.add(
                        new VertexAnimationBlock.VertexState(vertexID,
                                pos.x, pos.y, pos.z,
                                norm.x, norm.y, norm.z
                        )
                );
            }
            block.keyframes.add(keyframe);
        }

        return block;
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


