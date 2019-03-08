package codemetropolis.toolchain.mapping.conversions;

import codemetropolis.toolchain.mapping.model.Limit;
import junit.framework.TestCase;

public class ToDoubleConversionTest extends TestCase {
	
	ToDoubleConversion testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testApply() {
		testObj = new ToDoubleConversion();
		
		assertEquals(2.0, testObj.apply(2.0, new Limit()));
		assertEquals(2.0, testObj.apply("2.0", new Limit()));
	}
	
}