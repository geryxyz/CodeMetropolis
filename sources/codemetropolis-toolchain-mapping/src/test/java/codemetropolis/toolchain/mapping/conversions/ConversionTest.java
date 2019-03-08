package codemetropolis.toolchain.mapping.conversions;

import codemetropolis.toolchain.mapping.model.Parameter;
import junit.framework.TestCase;

public class ConversionTest extends TestCase {
	
	Conversion result = null;
	
	public void setup() {
		result = null;
	}
	
	public void testCreateFromName() {
		result = Conversion.createFromName("to_int");
		assert(result instanceof ToIntConversion);
		
		result = Conversion.createFromName("to_double");
		assert(result instanceof ToDoubleConversion);
		
		result = Conversion.createFromName("multiply");
		assert(result instanceof MultiplyConversion);
		
		result = Conversion.createFromName("quantization");
		assert(result instanceof QuantizationConversion);
		
		result = Conversion.createFromName("switch");
		assert(result instanceof SwitchConversion);
		
		result = Conversion.createFromName("abcd");
		assertNull(result);
	}
	
	public void testToDouble() {
		double testResult;
		
		testResult = Conversion.toDouble("42");
		assertEquals(42.0, testResult);
		
		testResult = Conversion.toDouble((double)42.0);
		assertEquals(42.0, testResult);
	}
	
	public void testToInt() {
		int testResult;
		
		testResult = Conversion.toInt("13");
		assertEquals(13, testResult);
		
		testResult = Conversion.toInt("13.13");
		assertEquals(13, testResult);
		
		testResult = Conversion.toInt(4.9);
		assertEquals(4, testResult);
	}
	
	public void testAddParameter() {
		result = new MultiplyConversion();
		
		assertEquals(0, result.getParameters().length);
		
		result.addParameter(new Parameter("testKey", "testValue"));
		
		assertEquals(1, result.getParameters().length);
		assertEquals("testKey", result.getParameters()[0].getName());
		assertEquals("testValue", result.getParameters()[0].getValue());
	}
	
	public void testAddParameters() {
		result = new MultiplyConversion();
		
		assertEquals(0, result.getParameters().length);
		
		result.addParameters(new Parameter[] {new Parameter("testKey1", "testValue1")});
		assertEquals(1, result.getParameters().length);
		assertEquals("testKey1", result.getParameters()[0].getName());
		assertEquals("testValue1", result.getParameters()[0].getValue());
		
		result.addParameters(new Parameter[] {new Parameter("testKey2", "testValue2"), new Parameter("testKey3", "testValue3")});
		assertEquals(3, result.getParameters().length);
		assertEquals("testKey1", result.getParameters()[0].getName());
		assertEquals("testValue1", result.getParameters()[0].getValue());
		assertEquals("testKey2", result.getParameters()[1].getName());
		assertEquals("testValue2", result.getParameters()[1].getValue());
		assertEquals("testKey3", result.getParameters()[2].getName());
		assertEquals("testValue3", result.getParameters()[2].getValue());
	}
	
	public void testClearParameters() {
		result = new MultiplyConversion();
		
		result.clearParameters();
		assertEquals(0, result.getParameters().length);
		
		result.addParameter(new Parameter("testKey", "testValue"));
		result.clearParameters();
		assertEquals(0, result.getParameters().length);
		
		result.addParameter(new Parameter("testKey1", "testValue1"));
		result.addParameter(new Parameter("testKey2", "testValue2"));
		result.clearParameters();
		assertEquals(0, result.getParameters().length);
	}
	
}
