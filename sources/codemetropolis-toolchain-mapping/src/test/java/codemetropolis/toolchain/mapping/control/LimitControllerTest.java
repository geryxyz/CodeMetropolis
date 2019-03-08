package codemetropolis.toolchain.mapping.control;

import junit.framework.TestCase;

public class LimitControllerTest extends TestCase {
	
	LimitController testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testAdd() {
		testObj = new LimitController();
		
		testObj.add("name", "from", 2.0);
		
		assertNotNull(testObj.getLimit("name", "from"));
		assertEquals(1, testObj.getLimit("name", "from").getValueSetSize());
	}
	
}