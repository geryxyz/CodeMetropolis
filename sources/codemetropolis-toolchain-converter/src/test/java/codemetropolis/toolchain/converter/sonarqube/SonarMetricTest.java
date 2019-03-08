package codemetropolis.toolchain.converter.sonarqube;

import codemetropolis.toolchain.converter.sonarqube.SonarMetric.MetricType;
import junit.framework.TestCase;

public class SonarMetricTest extends TestCase {
	
	SonarMetric testObj;
	
	
	public void setup() {
		testObj = null;
	}
	
	public void testHashCode() {
		
		testObj = new SonarMetric(null, null, null);
		assertEquals(29791, testObj.hashCode());
		
		testObj = new SonarMetric("name1", "value1", null);
		int value = ((31+"name1".hashCode())*31)*31+"value1".hashCode();
		assertEquals(value, testObj.hashCode());
	}
	
	public void testEquals() {
		testObj = new SonarMetric("name1", "value1", MetricType.STRING);
		
		assertEquals(true, testObj.equals(testObj));
		assertEquals(true, testObj.equals(new SonarMetric("name1", "value1", MetricType.STRING)));
		assertEquals(false, testObj.equals(null));
		assertEquals(false, testObj.equals(3));
		assertEquals(false, testObj.equals(new SonarMetric(null, null, null)));
		assertEquals(false, testObj.equals(new SonarMetric("name1", "value1", null)));
	}
}
