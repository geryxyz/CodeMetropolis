package codemetropolis.toolchain.mapping.conversions;

import codemetropolis.toolchain.mapping.model.Limit;
import junit.framework.TestCase;

public class ToIntConversionTest extends TestCase {
	
	ToIntConversion testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testApply() {
		testObj = new ToIntConversion();
		
		assertEquals(3, testObj.apply(3.21, new Limit()));
		assertEquals(4, testObj.apply("4", new Limit()));
	}
	
}