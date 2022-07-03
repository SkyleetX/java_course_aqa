package ru.stqa.pft.mantis.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public class MyTests {

    @Test
    public void testCurrentDir() {
        File currentDir = new File(".");
        System.out.println(currentDir);
        System.out.println(currentDir.getAbsolutePath());
    }
}
