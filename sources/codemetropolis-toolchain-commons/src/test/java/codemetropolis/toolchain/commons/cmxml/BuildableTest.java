package codemetropolis.toolchain.commons.cmxml;

import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import junit.framework.TestCase;

public class BuildableTest extends TestCase {

	Buildable b1, b2, b3;
	
	public void setup() {
		b1 = null;
		b2 = null;
		b3 = null;
	}
	
	public void testIsOverlapping() {
		b1 = new Buildable("test1", "testBuilding1", Type.FLOOR, new Point(0, 0, 0), new Point(2, 2, 2));
		b2 = new Buildable("test2", "testBuilding2", Type.FLOOR, new Point(1, 1, 1), new Point(2, 2, 2));
		b3 = new Buildable("test3", "testBuilding3", Type.FLOOR, new Point(-1, -1, -1), new Point(2, 2, 2));
		
		assertTrue(b1.isOverlapping(b2));
		assertTrue(b3.isOverlapping(b1));
		assertFalse(b2.isOverlapping(b3));
	}
	
}
