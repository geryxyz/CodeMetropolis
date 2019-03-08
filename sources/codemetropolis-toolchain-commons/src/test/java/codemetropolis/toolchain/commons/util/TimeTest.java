package codemetropolis.toolchain.commons.util;

import junit.framework.TestCase;

public class TimeTest extends TestCase {

	Time testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testGetHours() {
		testObj = new Time(0);
		assertEquals(0, testObj.getHours());
		
		testObj = new Time(3599999); // 0 hrs 59 mins 59 seconds, 999 milliseconds
		assertEquals(0, testObj.getHours());
		
		testObj = new Time(86400001); // 24 hrs 1 milliseconds
		assertEquals(24, testObj.getHours());
	}
	
	public void testGetMinutes() {
		testObj = new Time(0);
		assertEquals(0, testObj.getMinutes());
		
		testObj = new Time(6999);
		assertEquals(0, testObj.getMinutes());
		
		testObj = new Time(59999);
		assertEquals(0, testObj.getMinutes());
		
		testObj = new Time(60000);
		assertEquals(1, testObj.getMinutes());
		
		testObj = new Time(3600000);
		assertEquals(0, testObj.getMinutes());
		
		testObj = new Time(3730000);
		assertEquals(2, testObj.getMinutes());
	}
	
	public void testGetSeconds() {
		testObj = new Time(0);
		assertEquals(0, testObj.getSeconds());
		
		testObj = new Time(3999);
		assertEquals(3, testObj.getSeconds());
		
		testObj = new Time(60000);
		assertEquals(0, testObj.getSeconds());
		
		testObj = new Time(74500);
		assertEquals(14, testObj.getSeconds());
	}
	
}
