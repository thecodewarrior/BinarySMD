package dev.thecodewarrior.binarysmd;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

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
        StudiomdlFile parsed = new StudiomdlFile(reader);
        System.out.println(parsed.hashCode());
    }
}
