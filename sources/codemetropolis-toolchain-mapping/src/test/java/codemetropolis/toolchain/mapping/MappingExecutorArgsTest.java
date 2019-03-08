package codemetropolis.toolchain.mapping;

import junit.framework.TestCase;

public class MappingExecutorArgsTest extends TestCase {

	MappingExecutorArgs testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testMappingExecutorArgs() {
		testObj = new MappingExecutorArgs("testCDF", "testOutput", "testMapping", 1.0, true);
		assertEquals("testCDF", testObj.getCdfFile());
		assertEquals("testOutput", testObj.getOutputFile());
		assertEquals("testMapping", testObj.getMappingFile());
		assertEquals(1.0, testObj.getScale());
		assertEquals(true, testObj.isHierarchyValidationEnabled());
	}
	
}
