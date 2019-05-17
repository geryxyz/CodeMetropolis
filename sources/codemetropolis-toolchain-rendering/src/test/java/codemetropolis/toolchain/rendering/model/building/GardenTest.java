package codemetropolis.toolchain.rendering.model.building;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Test;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.primitive.Primitive;

public class GardenTest {

	@Test
	public void constructorTest() {
		Garden garden = null;
		try {
			garden = new Garden(new Buildable("test", "testName", Buildable.Type.CELLAR));
			fail();
		} catch (BuildingTypeMismatchException e) {
			assertTrue(true);
		}

	}

	@Test
	public void prepareDoorTest() {
		Garden garden = null;
		try {
			garden = new Garden(new Buildable("test", "testName", Buildable.Type.GARDEN));
		} catch (BuildingTypeMismatchException e) {
			fail();
		}

		LinkedList<Primitive> prepareDoor = garden.prepareDoor();

		if (prepareDoor.size() == 4) {
			assertTrue(true);
		} else {
			fail();
		}

	}

	@Test
	public void prepareBaseTest() {
		Garden garden = null;
		try {
			garden = new Garden(new Buildable("test", "testName", Buildable.Type.GARDEN));
		} catch (BuildingTypeMismatchException e) {
			fail();
		}

		LinkedList<Primitive> prepareBase = garden.prepareBase();
		
		
		if (prepareBase.size() == 1) {
			assertTrue(true);
		} else {
			fail();
		}

	}

	@Test
	public void prepareSigns() {
		Garden garden = null;
		try {
			garden = new Garden(new Buildable("test", "testName", Buildable.Type.GARDEN));
		} catch (BuildingTypeMismatchException e) {
			fail();
		}

		LinkedList<Primitive> prepareSigns = garden.prepareSigns();
		
		if (prepareSigns.size() == 4) {
			assertTrue(true);
		} else {
			fail();
		}

	}

}
