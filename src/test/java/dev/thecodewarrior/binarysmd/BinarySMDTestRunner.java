package dev.thecodewarrior.binarysmd;

import dev.thecodewarrior.binarysmd.formats.SMDBinaryWriter;
import dev.thecodewarrior.binarysmd.formats.SMDTextReader;
import dev.thecodewarrior.binarysmd.formats.SMDTextWriter;
import dev.thecodewarrior.binarysmd.studiomdl.SMDFile;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(Theories.class)
public class BinarySMDTestRunner {

    private static Path testsInput = Paths.get("tests");
    private static Path textOutput = Paths.get("output_text");
    private static Path binaryOutput = Paths.get("output_binary");

    @DataPoints
    public static String[] files;

    static {
        try {
            files = Files.walk(testsInput)
                    .filter(file -> file.toString().endsWith(".smd"))
                    .map(file -> testsInput.relativize(file).toString())
                    .toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            files = new String[] {};
        }

        deleteDir(textOutput.toFile());
        deleteDir(binaryOutput.toFile());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    @Theory
    public void text2text(String file) throws IOException {
        String input = new String(Files.readAllBytes(testsInput.resolve(file)));
        SMDFile parsed = new SMDTextReader().read(input);

        String reprint = new SMDTextWriter().write(parsed);

        Path output = textOutput.resolve(file);
        Files.createDirectories(output.getParent());
        Files.write(output, reprint.getBytes());
    }

    @Theory
    public void text2binary(String file) throws IOException {
        String input = new String(Files.readAllBytes(testsInput.resolve(file)));
        SMDFile parsed = new SMDTextReader().read(input);

        Path output = binaryOutput.resolve(file);
        Files.createDirectories(output.getParent());

        try (MessagePacker packer = MessagePack.newDefaultPacker(Files.newOutputStream(output))) {
            new SMDBinaryWriter().write(parsed, packer);
        }
    }
}
