package dev.thecodewarrior.binarysmd;

import dev.thecodewarrior.binarysmd.formats.SMDTextReader;
import dev.thecodewarrior.binarysmd.formats.SMDTextWriter;
import dev.thecodewarrior.binarysmd.studiomdl.SMDFile;
import dev.thecodewarrior.binarysmd.tokenizer.TokenPrinter;
import dev.thecodewarrior.binarysmd.tokenizer.Tokenizer;
import org.junit.Test;

import java.io.*;

//@RunWith(Theories.class)
public class BinarySMDTestRunner {

//    @DataPoints
//    public static String[] files = new String[] {
//            "test.smd"
//    };

//    @Theory
//    public void temporaryTest(String file) throws FileNotFoundException {
    @Test
    public void temporaryTest() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("test.smd")));
        SMDFile parsed = new SMDTextReader().read(reader);

        TokenPrinter printer = new TokenPrinter();
        Tokenizer tokenizer = new Tokenizer(new BufferedReader(new FileReader(new File("test.smd"))));
        printer.print(tokenizer);
        FileWriter writer = new FileWriter(new File("test.reprint.smd"));
        writer.write(printer.toString());
        writer.close();

        new SMDTextWriter().write(parsed, new BufferedWriter(new FileWriter(new File("test.transcode.smd"))));
    }
}
