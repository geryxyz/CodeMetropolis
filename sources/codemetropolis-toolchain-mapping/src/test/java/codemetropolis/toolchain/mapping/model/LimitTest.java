package codemetropolis.toolchain.mapping.model;

import junit.framework.TestCase;

public class LimitTest extends TestCase {
	
	Limit testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testAdd() {
		testObj = new Limit();
		
		testObj.add(3.0);
		assertEquals(1, testObj.getValueSetSize());
		assertEquals(3.0, testObj.getMax());
		assertEquals(3.0, testObj.getMin());
	}
	
}