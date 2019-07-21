package dev.thecodewarrior.binarysmd.formats;

import dev.thecodewarrior.binarysmd.studiomdl.*;
import dev.thecodewarrior.binarysmd.tokenizer.TokenPrinter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;

public class SMDTextWriter {
    public @NotNull String write(@NotNull SMDFile file) throws IOException {
        TokenPrinter tokenPrinter = new TokenPrinter();
        tokenPrinter.print("version").print("1").newline();
        for(SMDFileBlock block : file.blocks) {
            if(block instanceof NodesBlock)
                writeNodesBlock((NodesBlock) block, tokenPrinter);
            if(block instanceof SkeletonBlock)
                writeSkeletonBlock((SkeletonBlock) block, tokenPrinter);
            if(block instanceof TrianglesBlock)
                writeTrianglesBlock((TrianglesBlock) block, tokenPrinter);
            if(block instanceof VertexAnimationBlock)
                writeVertexAnimationBlock((VertexAnimationBlock) block, tokenPrinter);
        }

        return tokenPrinter.toString();
    }

    private void writeNodesBlock(NodesBlock block, TokenPrinter out) {
        out.print("nodes").newline();
        for(NodesBlock.Bone bone : block.bones) {
            out.print(bone.id).printString(bone.name).print(bone.parent).newline();
        }
        out.print("end").newline();
    }

    private void writeSkeletonBlock(SkeletonBlock block, TokenPrinter out) {
        out.print("skeleton").newline();
        for(SkeletonBlock.Keyframe keyframe : block.keyframes) {
            out.print("time").print(keyframe.time).newline();
            for (SkeletonBlock.BoneState state : keyframe.states) {
                out.print(state.bone)
                        .print(state.posX).print(state.posY).print(state.posZ)
                        .print(state.rotX).print(state.rotY).print(state.rotZ)
                        .newline();
            }
        }
        out.print("end").newline();
    }

    private void writeTrianglesBlock(TrianglesBlock block, TokenPrinter out) {
        out.print("triangles").newline();
        for(TrianglesBlock.Triangle triangle : block.triangles) {
            out.printDirect(triangle.material).newline();
            for(TrianglesBlock.Vertex vertex : triangle.vertices) {
                out.print(vertex.parentBone)
                        .print(vertex.posX).print(vertex.posY).print(vertex.posZ)
                        .print(vertex.normX).print(vertex.normY).print(vertex.normZ)
                        .print(vertex.u).print(vertex.v);
                if(!vertex.links.isEmpty()) {
                    out.print(vertex.links.size());
                    for (TrianglesBlock.Link link : vertex.links) {
                        out.print(link.bone).print(link.weight);
                    }
                }
                out.newline();
            }
        }
        out.print("end").newline();
    }

    private void writeVertexAnimationBlock(VertexAnimationBlock block, TokenPrinter out) {
        out.print("vertexanimation").newline();
        for(VertexAnimationBlock.Keyframe keyframe : block.keyframes) {
            out.print("time").print(keyframe.time).newline();
            for (VertexAnimationBlock.VertexState state : keyframe.states) {
                out.print(state.bone)
                        .print(state.posX).print(state.posY).print(state.posZ)
                        .print(state.normX).print(state.normY).print(state.normZ)
                        .newline();
            }
        }
        out.print("end").newline();
    }
}
