package dev.thecodewarrior.binarysmd;

import dev.thecodewarrior.binarysmd.formats.SMDTextFormat;
import dev.thecodewarrior.binarysmd.studiomdl.SMDFile;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

//@RunWith(Theories.class)
public class BinarySMDTestRunner {

//    @DataPoints
//    public static String[] files = new String[] {
//            "test.smd"
//    };

//    @Theory
//    public void temporaryTest(String file) throws FileNotFoundException {
    @Test
    public void temporaryTest() throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("test.smd")));
        SMDFile parsed = new SMDTextFormat().read(reader);
        System.out.println(parsed.hashCode());
    }
}
