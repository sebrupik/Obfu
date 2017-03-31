/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obfu;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author snr
 */
public class SwapEngineTest {
    
    public SwapEngineTest() {
        
        //testSwapMacAddress();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of swapMacAddress method, of class SwapEngine.
     */
    @Test
    public void testSwapMacAddress() {
        System.out.println("swapMacAddress");
        String[] macOri = new String[]{"AA-BB-CC-DD-EE-FF"};
        int count = 1;
        int replace = 4;
        String[] expResult = new String[]{"XX:X1:CC:DD:EE:FF"};
        String result;
        
        for(int i=0; i<macOri.length;i++) {
            result = SwapEngine.swapMacAddress(macOri[i], count, replace);
            assertEquals(expResult[i], result);
            
            // TODO review the generated test code and remove the default call to fail.
            //fail("The test case is a prototype.");
        }
        
    }

    /**
     * Test of swapIPv4Address method, of class SwapEngine.
     */
    @Test
    public void testSwapIPv4Address() {
        System.out.println("swapIPv4Address");
        String[] addressOri = new String[]{"192.168.1.25"};
        int count = 1;
        int replace = 2;
        String[] expResult = new String[]{"XXX.XX1.1.25"};
        String result;
        
        for(int i=0; i<addressOri.length;i++) {
            result = SwapEngine.swapIPv4Address(addressOri[i], count, replace);
            assertEquals(expResult[i], result);
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of swapIPv6LLAddress method, of class SwapEngine.
     */
    @Test
    public void testSwapIPv6LLAddress() {
        System.out.println("swapIPv6LLAddress");
        String addressOri = "";
        int count = 0;
        int replace = 0;
        String expResult = "";
        String result = SwapEngine.swapIPv6LLAddress(addressOri, count, replace);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of swapIPv6GUAddress method, of class SwapEngine.
     */
    @Test
    public void testSwapIPv6GUAddress() {
        System.out.println("swapIPv6GUAddress");
        String addressOri = "2001:630:d0:aaaa::1";
        int count = 1;
        int replace = 0;
        HashMap ip6np = new HashMap();
        String expResult = "2001:0000:0000:0001:0000:0000:0000:0001";
        String result = SwapEngine.swapIPv6GUAddress(addressOri, count, replace, ip6np);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
