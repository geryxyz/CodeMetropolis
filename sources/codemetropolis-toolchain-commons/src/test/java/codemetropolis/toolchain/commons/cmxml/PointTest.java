package codemetropolis.toolchain.commons.cmxml;

import junit.framework.TestCase;

public class PointTest extends TestCase {

	Point testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testTranslate() {
		testObj = new Point(0, 0, 0);
		
		Point newPoint = testObj.translate(new Point(1, 2, 3));
		
		assertEquals(1, newPoint.getX());
		assertEquals(2, newPoint.getY());
		assertEquals(3, newPoint.getZ());
	}
	
}
