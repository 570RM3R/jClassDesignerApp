/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Saeid
 */
public class TestSaveTest {
    
    public TestSaveTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of saveDesignOne method, of class TestSave.
     */
    @Test
    public void testSaveDesignOne() {
        System.out.println("saveDesignOne");
        TestSave.saveDesignOne();
        try {
            assertEquals("The files differ!",
                    FileUtils.readFileToString(new File("work/DesignSaveTestOneExpected.json"), "utf-8"),
                    FileUtils.readFileToString(new File("work/DesignSaveTestOne.json"), "utf-8"));
        } catch (IOException ex) {
            Logger.getLogger(TestSaveTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of saveDesignTwo method, of class TestSave.
     */
    @Test
    public void testSaveDesignTwo() {
        System.out.println("saveDesignTwo");
        System.out.println("saveDesignOne");
        TestSave.saveDesignOne();
        try {
            assertEquals("The files differ!",
                    FileUtils.readFileToString(new File("work/DesignSaveTestTwoExpected.json"), "utf-8"),
                    FileUtils.readFileToString(new File("work/DesignSaveTestTwo.json"), "utf-8"));
        } catch (IOException ex) {
            Logger.getLogger(TestSaveTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of saveDesignThree method, of class TestSave.
     */
    @Test
    public void testSaveDesignThree() {
        System.out.println("saveDesignThree");
        TestSave.saveDesignThree();
                System.out.println("saveDesignOne");
        TestSave.saveDesignOne();
        try {
            assertEquals("The files differ!",
                    FileUtils.readFileToString(new File("work/DesignSaveTestThreeExpected.json"), "utf-8"),
                    FileUtils.readFileToString(new File("work/DesignSaveTestThree.json"), "utf-8"));
        } catch (IOException ex) {
            Logger.getLogger(TestSaveTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
