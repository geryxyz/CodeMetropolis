package codemetropolis.toolchain.rendering.model.building;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.primitive.Primitive;

public class GroundTest {

	@Test
	public void constructorTest() {
		Ground ground = null;
		try {
			ground = new Ground(new Buildable("test", "testName", Buildable.Type.CELLAR));
			fail();
		} catch (BuildingTypeMismatchException e) {
			assertTrue(true);
		}

	}


	@Test
	public void prepareBaseTest() {
		Ground ground = null;
		try {
			ground = new Ground(new Buildable("test", "testName", Buildable.Type.GROUND));
		} catch (BuildingTypeMismatchException e) {
			assertTrue(true);
		}

		LinkedList<Primitive> prepareBase = ground.prepareBase();
		
		if (prepareBase.size() == 1) {
			assertTrue(true);
		} else {
			fail();
		}

	}

	@Test
	public void prepareSigns() {
		Ground ground = null;
		try {
			ground = new Ground(new Buildable("test", "testName", Buildable.Type.GROUND));
		} catch (BuildingTypeMismatchException e) {
			assertTrue(true);
		}
		
		LinkedList<Primitive> prepareSigns = ground.prepareSigns();
		
		if (prepareSigns.size() == 4) {
			assertTrue(true);
		} else {
			fail();
		}

	}


}
