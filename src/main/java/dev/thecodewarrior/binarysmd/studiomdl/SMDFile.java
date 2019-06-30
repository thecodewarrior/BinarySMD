package dev.thecodewarrior.binarysmd.studiomdl;

import dev.thecodewarrior.binarysmd.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class SMDFile {
    public @NotNull List<@NotNull SMDFileBlock> blocks = new ArrayList<>();
}
