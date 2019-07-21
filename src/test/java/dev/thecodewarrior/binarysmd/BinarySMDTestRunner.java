package dev.thecodewarrior.binarysmd;

import dev.thecodewarrior.binarysmd.formats.SMDTextReader;
import dev.thecodewarrior.binarysmd.formats.SMDTextWriter;
import dev.thecodewarrior.binarysmd.studiomdl.SMDFile;
import dev.thecodewarrior.binarysmd.tokenizer.TokenPrinter;
import dev.thecodewarrior.binarysmd.tokenizer.Tokenizer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(Theories.class)
public class BinarySMDTestRunner {

    private static Path testsInput = Paths.get("tests");
    private static Path testsOutput = Paths.get("output");

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
        deleteDir(testsOutput.toFile());
    }

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

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Theory
    public void temporaryTest(String file) throws IOException {
        String input = new String(Files.readAllBytes(testsInput.resolve(file)));
        SMDFile parsed = new SMDTextReader().read(input);

//        TokenPrinter printer = new TokenPrinter();
//        printer.print(new Tokenizer(input));
//        String tokenReprint = printer.toString();
        String reprint = new SMDTextWriter().write(parsed);

        Path output = testsOutput.resolve(file);
        Files.createDirectories(output.getParent());
        Files.write(output, reprint.getBytes());

//        Assert.assertTrue(input.equals(tokenReprint));
//        Assert.assertTrue(input.equals(reprint));
    }

    private String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
