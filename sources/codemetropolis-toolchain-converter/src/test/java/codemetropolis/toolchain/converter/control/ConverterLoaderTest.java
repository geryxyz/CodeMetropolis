package codemetropolis.toolchain.converter.control;

import java.util.HashMap;
import java.util.Map;

import codemetropolis.toolchain.commons.cdf.converter.CdfConverter;
import codemetropolis.toolchain.converter.control.*;
import codemetropolis.toolchain.converter.sonarqube.SonarQubeConverter;
import codemetropolis.toolchain.converter.sourcemeter.GraphConverter;
import junit.framework.TestCase;

public class ConverterLoaderTest extends TestCase {
	
	CdfConverter result;
	
	
	public void setup() {
		result = null;
	}
	
	public void testLoadSourceMeter() {
		result = ConverterLoader.load(ConverterType.SOURCEMETER, new HashMap<String, String>());
		assert(result instanceof GraphConverter);
	}
	
	public void testLoadSonarQube() {
		result = ConverterLoader.load(ConverterType.SONARQUBE, new HashMap<String, String>());
		assert(result instanceof SonarQubeConverter);
	}
}
