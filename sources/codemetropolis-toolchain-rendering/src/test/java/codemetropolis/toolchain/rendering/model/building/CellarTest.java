package codemetropolis.toolchain.rendering.model.building;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.primitive.Primitive;

public class CellarTest {
	
	

	@Test
	public void constructorTrowExceptionTest() {
		try {
			new Cellar(new Buildable("test", "testName", Buildable.Type.CONTAINER));
			fail();
		} catch (BuildingTypeMismatchException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	public void constructorTest() {
		try {
			new Cellar(new Buildable("test", "testName", Buildable.Type.CELLAR));
			assertTrue(true);
		} catch (BuildingTypeMismatchException e) {
			fail();
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	public void makePrimitivesTest() {
		Cellar cellar = null;
		
		try {
			cellar = new Cellar(new Buildable("test", "testName", Buildable.Type.CELLAR));
			
		} catch (BuildingTypeMismatchException e) {
			fail();
		} catch (Exception e) {
			fail();
		}
		
		LinkedList<Primitive> makePrimitives = cellar.makePrimitives();
		
		if(makePrimitives.size() == 2) {
			assertTrue(true);
		}else {
			fail();
		}
		
	}

}
