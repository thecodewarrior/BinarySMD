package dev.thecodewarrior.binarysmd.studiomdl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrianglesBlock extends SMDFileBlock {
    public @NotNull List<@NotNull Triangle> triangles = new ArrayList<>();

    public static class Triangle {
        public @NotNull String material;
        public @NotNull Vertex v1, v2, v3;
        public @NotNull List<@NotNull Vertex> vertices = new ArrayList<>();

        public Triangle(@NotNull String material, @NotNull Vertex v1, @NotNull Vertex v2, @NotNull Vertex v3) {
            this.material = material;
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.vertices.add(v1);
            this.vertices.add(v2);
            this.vertices.add(v3);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Triangle)) return false;
            Triangle triangle = (Triangle) o;
            return material.equals(triangle.material) &&
                    v1.equals(triangle.v1) &&
                    v2.equals(triangle.v2) &&
                    v3.equals(triangle.v3);
        }

        @Override
        public int hashCode() {
            return Objects.hash(material, v1, v2, v3);
        }
    }

    public static class Vertex {
        public int parentBone;
        public float posX, posY, posZ;
        public float normX, normY, normZ;
        public float u, v;
        public @NotNull List<@NotNull Link> links = new ArrayList<>();

        public Vertex(int parentBone, float posX, float posY, float posZ, float normX, float normY, float normZ, float u, float v) {
            this.parentBone = parentBone;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.normX = normX;
            this.normY = normY;
            this.normZ = normZ;
            this.u = u;
            this.v = v;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Vertex)) return false;
            Vertex vertex = (Vertex) o;
            return parentBone == vertex.parentBone &&
                    Float.compare(vertex.posX, posX) == 0 &&
                    Float.compare(vertex.posY, posY) == 0 &&
                    Float.compare(vertex.posZ, posZ) == 0 &&
                    Float.compare(vertex.normX, normX) == 0 &&
                    Float.compare(vertex.normY, normY) == 0 &&
                    Float.compare(vertex.normZ, normZ) == 0 &&
                    Float.compare(vertex.u, u) == 0 &&
                    Float.compare(vertex.v, v) == 0 &&
                    links.equals(vertex.links);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentBone, posX, posY, posZ, normX, normY, normZ, u, v, links);
        }
    }

    public static class Link {
        public int bone;
        public float weight;

        public Link(int bone, float weight) {
            this.bone = bone;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Link)) return false;
            Link link = (Link) o;
            return bone == link.bone &&
                    Float.compare(link.weight, weight) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(bone, weight);
        }
    }
}
