package codemetropolis.toolchain.placing.layout.pack;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.cmxml.Point;
import junit.framework.TestCase;

public class PackLayoutTest extends TestCase {
	
	PackLayout testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testGetMaxSizes() {
		
		testObj = new PackLayout();
		
		Buildable b = new Buildable("id", "name", Type.FLOOR, new Point(), new Point(10,10,10));
		BuildableWrapper w = new BuildableWrapper(b);
		
		Collection<BuildableWrapper> list = new ArrayList<>();
		
		try {
			Method method = testObj.getClass().getDeclaredMethod("getMaxSizes", Collection.class);
			method.setAccessible(true);
			
			Point result = (Point) method.invoke(testObj, list);
			assertEquals(0, result.getX());
			assertEquals(0, result.getY());
			assertEquals(0, result.getZ());
			
			list.add(w);
			result = (Point) method.invoke(testObj, list);
			
			assertEquals(10, result.getX());
			assertEquals(0, result.getY());
			assertEquals(10, result.getZ());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testCalculateParentSize() {
		
		testObj = new PackLayout();
		
		Buildable b = new Buildable("id", "name", Type.FLOOR, new Point(1,1,1), new Point(10,5,2));
		BuildableWrapper w = new BuildableWrapper(b);
		
		Collection<BuildableWrapper> list = new ArrayList<>();
		list.add(w);
		
		//space: 2
		//minX: 1, maxX: 1+10, minZ: 1, maxZ: 1+2
		//return: x: 11-1+4, z: 3-1+4
		try {	
			Method method = testObj.getClass().getDeclaredMethod("calculateParentSize", Collection.class, int.class);
			method.setAccessible(true);
			
			Point result = (Point) method.invoke(testObj, list, 2);
			assertEquals(14, result.getX());
			assertEquals(0, result.getY());
			assertEquals(6, result.getZ());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
