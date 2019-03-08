package codemetropolis.toolchain.mapping.control;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.mapping.model.Mapping;
import junit.framework.TestCase;

public class MappingControllerTest extends TestCase {
	
	MappingController testObj;
	
	public void setup() {
		testObj = null;
	}

	public void testSetProperty() {
		
		testObj = new MappingController(new Mapping());
		
		Buildable b = new Buildable("id", "name", Type.GARDEN);
		b.setSizeX(0);
		b.setSizeY(0);
		
		try {
			Method method = testObj.getClass().getDeclaredMethod("setProperty", Buildable.class, String.class, Object.class, boolean.class);
			method.setAccessible(true);
		
			method.invoke(testObj, b, "width", 3.0, false);
			
			assertEquals(3, b.getSizeX());
			
			method.invoke(testObj, b, "height", 5.0, false);
			
			assertEquals(5, b.getSizeY());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testFindRoot() {
		
		testObj = new MappingController(new Mapping());
		
		Collection<Buildable> buildables = new ArrayList<>();
		
		Buildable b = new Buildable("id", "name", Type.GARDEN);
		
		buildables.add(b);
		
		try {
			Method method = testObj.getClass().getDeclaredMethod("findRoot", Collection.class);
			method.setAccessible(true);
			
			if (b.isRoot()) {
				assertEquals(b, method.invoke(testObj, buildables));
			} else {
				assertNull(method.invoke(testObj, buildables));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}