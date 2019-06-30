package dev.thecodewarrior.binarysmd;

import dev.thecodewarrior.binarysmd.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class StudiomdlFile {
    public @NotNull List<@NotNull SMDFileBlock> blocks = new ArrayList<>();

    public StudiomdlFile(@NotNull BufferedReader data) {
        Tokenizer tokenizer = new Tokenizer(data);
        tokenizer.next().expect("version");
        tokenizer.next().expect("1");
        tokenizer.next().expectLine();

        while(!tokenizer.eof()) {
            String block = tokenizer.next().value;
            tokenizer.next().expectLine();
            switch(block) {
                case "nodes":
                    blocks.add(new NodesBlock(tokenizer));
                    break;
                case "skeleton":
                    blocks.add(new SkeletonBlock(tokenizer));
                    break;
                case "triangles":
                    blocks.add(new TrianglesBlock(tokenizer));
                    break;
                case "vertexanimation":
                    blocks.add(new VertexAnimationBlock(tokenizer));
                    break;
            }
        }
    }
}
