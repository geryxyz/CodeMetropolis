package codemetropolis.toolchain.rendering.model.primitive;

import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.model.BasicBlock;
import junit.framework.TestCase;

public class BoxelTest extends TestCase {
	
	Boxel testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testToCSV() {
		
		testObj = new Boxel(new BasicBlock((short) 10, 3), new Point(2,5,6), "info");
		assertEquals("10;3;2;5;6;info", testObj.toCSV());
		
		testObj = new Boxel(new BasicBlock((short) 11, 3), new Point(2,5,6), "");
		assertEquals("11;3;2;5;6;NULL", testObj.toCSV());
	}
	
}