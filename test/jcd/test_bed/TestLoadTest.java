/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Saeid
 */
public class TestLoadTest {
    
    public TestLoadTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of loadDesignOne method, of class TestLoad.
     */
    @Test
    public void testLoadDesignOne() {
        System.out.println("loadDesignOne");
        String expResult = TestLoad.loadDesignOne("work/DesignSaveTestOneExpected.json");
        String result = TestLoad.loadDesignOne("work/DesignSaveTestOne.json");
        assertEquals(expResult, result);
    }

    /**
     * Test of loadDesignTwo method, of class TestLoad.
     */
    @Test
    public void testLoadDesignTwo() {
        System.out.println("loadDesignTwo");
        String expResult = TestLoad.loadDesignTwo("work/DesignSaveTestTwoExpected.json");
        String result = TestLoad.loadDesignTwo("work/DesignSaveTestTwo.json");
        assertEquals(expResult, result);
    }

    /**
     * Test of loadDesignThree method, of class TestLoad.
     */
    @Test
    public void testLoadDesignThree() {
        System.out.println("loadDesignThree");
        String expResult = TestLoad.loadDesignThree("work/DesignSaveTestThreeExpected.json");
        String result = TestLoad.loadDesignThree("work/DesignSaveTestThree.json");
        assertEquals(expResult, result);
    }
    
}
