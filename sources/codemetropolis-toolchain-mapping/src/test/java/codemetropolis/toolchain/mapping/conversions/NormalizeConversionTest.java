package codemetropolis.toolchain.mapping.conversions;

import codemetropolis.toolchain.mapping.model.Limit;
import junit.framework.TestCase;

public class NormalizeConversionTest extends TestCase {

	NormalizeConversion testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testApply() {
		testObj = new NormalizeConversion();
		
		Limit l1 = new Limit();
		l1.add(42);
		
		Limit l2 = new Limit();
		l2.add(41);
		l2.add(43);
		
		assertEquals(1.0, testObj.apply(24.0, l1));
		assertEquals(0.5, testObj.apply(42.0, l2));
	}
	
}
