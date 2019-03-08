package codemetropolis.toolchain.converter;

import java.util.HashMap;

import codemetropolis.toolchain.converter.control.ConverterType;
import junit.framework.TestCase;

public class ConverterExecutorArgsTest extends TestCase {
	ConverterExecutorArgs testObj;
	
	public void setup () {
		testObj = null;
	}
	
	public void testConverterExecutorArgsConstructorNoParams() {
		testObj = new ConverterExecutorArgs(ConverterType.SOURCEMETER, "testSource", "testOutput");
		assertEquals("testOutput", testObj.getOutputFile());
		assertEquals("testSource", testObj.getSource());
		assertEquals(ConverterType.SOURCEMETER, testObj.getType());
	}
	
	public void testConverterExecutorArgsConstructor() {
		testObj = new ConverterExecutorArgs(ConverterType.SONARQUBE, "testSource", "testOutput", new HashMap<String, String>());
		assertEquals("testOutput", testObj.getOutputFile());
		assertEquals("testSource", testObj.getSource());
		assertEquals(ConverterType.SONARQUBE, testObj.getType());
		assertNotNull(testObj.getParams());
		assertTrue(testObj.getParams().isEmpty());
	}
}
