package dev.thecodewarrior.binarysmd.formats;

import dev.thecodewarrior.binarysmd.studiomdl.*;
import dev.thecodewarrior.binarysmd.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class SMDTextReader {

    public SMDFile read(@NotNull BufferedReader data) {
        SMDFile file = new SMDFile();
        Tokenizer tokenizer = new Tokenizer(data);
        tokenizer.next().expect("version");
        tokenizer.next().expect("1");
        tokenizer.next().expectLine();

        while(!tokenizer.eof()) {
            String block = tokenizer.next().value;
            tokenizer.next().expectLine();
            switch(block) {
                case "nodes":
                    file.blocks.add(readNodesBlock(tokenizer));
                    break;
                case "skeleton":
                    file.blocks.add(readSkeletonBlock(tokenizer));
                    break;
                case "triangles":
                    file.blocks.add(readTrianglesBlock(tokenizer));
                    break;
                case "vertexanimation":
                    file.blocks.add(readVertexAnimationBlock(tokenizer));
                    break;
            }
        }

        return file;
    }

    private NodesBlock readNodesBlock(@NotNull Tokenizer file) {
        NodesBlock block = new NodesBlock();

        while(!file.current().test("end")) {
            block.bones.add(new NodesBlock.Bone(file.next().toInt(), file.next().toString(), file.next().toInt()));
            file.next().expectLine();
        }
        file.next().expect("end");
        file.next().expectLine();

        return block;
    }

    private SkeletonBlock readSkeletonBlock(@NotNull Tokenizer file) {
        SkeletonBlock block = new SkeletonBlock();

        if(file.current().test("end")) {
            file.advance().next().expectLine();
        }
        file.current().expect("time");

        SkeletonBlock.Keyframe keyframe = new SkeletonBlock.Keyframe(-1); // this temporary keyframe is immediately overwritten
        while(!file.current().test("end")) {
            if(file.current().test("time")) {
                keyframe = new SkeletonBlock.Keyframe(file.advance().next().toInt());
                block.keyframes.add(keyframe);
            } else {
                keyframe.states.add(
                        new SkeletonBlock.BoneState(file.next().toInt(),
                                file.next().toFloat(), file.next().toFloat(), file.next().toFloat(),
                                file.next().toFloat(), file.next().toFloat(), file.next().toFloat()
                        )
                );
            }
            file.next().expectLine();
        }
        file.next().expect("end");
        file.next().expectLine();

        return block;
    }

    private TrianglesBlock readTrianglesBlock(@NotNull Tokenizer file) {
        TrianglesBlock block = new TrianglesBlock();

        while(!file.current().test("end")) {
            String material = file.next().value;
            file.next().expectLine();
            block.triangles.add(new TrianglesBlock.Triangle(
                    material,
                    readVertex(file),
                    readVertex(file),
                    readVertex(file)
            ));
        }
        file.next().expect("end");
        file.next().expectLine();

        return block;
    }

    private TrianglesBlock.Vertex readVertex(Tokenizer file) {
        TrianglesBlock.Vertex vertex = new TrianglesBlock.Vertex(
                file.next().toInt(), // parent bone
                file.next().toFloat(), file.next().toFloat(), file.next().toFloat(), // pos
                file.next().toFloat(), file.next().toFloat(), file.next().toFloat(), // normal
                file.next().toFloat(), file.next().toFloat() // uv
        );

        if(!file.current().test("\n")) {
            int linkCount = file.next().toInt();
            for (int i = 0; i < linkCount; i++) {
                vertex.links.add(new TrianglesBlock.Link(file.next().toInt(), file.next().toFloat()));
            }
        }

        file.next().expectLine();
        return vertex;
    }

    private VertexAnimationBlock readVertexAnimationBlock(@NotNull Tokenizer file) {
        VertexAnimationBlock block = new VertexAnimationBlock();

        if(file.current().test("end")) {
            file.advance().next().expectLine();
        }
        file.current().expect("time");

        VertexAnimationBlock.Keyframe keyframe = new VertexAnimationBlock.Keyframe(-1); // this temporary keyframe is immediately overwritten
        while(!file.current().test("end")) {
            if(file.current().test("time")) {
                keyframe = new VertexAnimationBlock.Keyframe(file.advance().next().toInt());
                block.keyframes.add(keyframe);
            } else {
                keyframe.states.add(
                        new VertexAnimationBlock.VertexState(file.next().toInt(),
                                file.next().toFloat(), file.next().toFloat(), file.next().toFloat(),
                                file.next().toFloat(), file.next().toFloat(), file.next().toFloat()
                        )
                );
            }
            file.next().expectLine();
        }
        file.next().expect("end");
        file.next().expectLine();

        return block;
    }
}


