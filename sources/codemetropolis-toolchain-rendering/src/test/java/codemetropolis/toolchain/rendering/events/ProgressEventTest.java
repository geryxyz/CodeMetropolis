package codemetropolis.toolchain.rendering.events;

import junit.framework.TestCase;

public class ProgressEventTest extends TestCase {
	
	ProgressEvent testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testGetPercent() {
		testObj = new ProgressEvent(new Object(), null, 100, 200, 100);
		assertEquals(50.0, testObj.getPercent());
		
		testObj = new ProgressEvent(new Object(), null, 53, 56, 400);
		assertEquals(94.64, testObj.getPercent(), 0.01);
		
		testObj = new ProgressEvent(new Object(), null, 30, 30, 1000);
		assertEquals(100.0, testObj.getPercent());
	}
	
	public void testGetTimeLeftInMillis() {
		testObj = new ProgressEvent(new Object(),  null, 100, 200, 100);
		assertEquals(100, testObj.getTimeLeftInMillis());
		
		testObj = new ProgressEvent(new Object(), null, 53, 56, 400);
		assertEquals(22, testObj.getTimeLeftInMillis());
		
		testObj = new ProgressEvent(new Object(), null, 30, 30, 1000);
		assertEquals(0, testObj.getTimeLeftInMillis());
	}

}
