package codemetropolis.toolchain.rendering.model.building;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.BasicBlock;
import codemetropolis.toolchain.rendering.model.pattern.Pattern;
import codemetropolis.toolchain.rendering.model.primitive.Primitive;

public class FloorTest {

	Floor floor;

	
	@Before
	public void initFloor() {

		try {
			floor = new Floor(new Buildable("test", "testName", Buildable.Type.FLOOR));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void constructorTrowExceptionTest() {
		try {
			new Floor(new Buildable("test", "testName", Buildable.Type.GARDEN));
			fail();
		} catch (BuildingTypeMismatchException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

	}

	@Test
	public void prepareSignsTest() {
		LinkedList<Primitive> prepareSigns = floor.prepareSigns();
		assertTrue(prepareSigns.size() == 8);
	}

	@Test
	public void prepareStairTest() {
		LinkedList<Primitive> prepareStairs = floor.prepareStairs();
		assertTrue(prepareStairs.size() == 1);
	}

	@Test
	public void prepareDoorTest() {
		LinkedList<Primitive> prepareDoor = floor.prepareDoor();
		assertTrue(prepareDoor.size() == 8);
	}

	@Test
	public void getStairRepetationPatternTest() {
		Pattern stairRepetationPattern = floor.getStairRepetationPattern();

		if (stairRepetationPattern != null) {
			assertTrue(true);
		}

	}

	@Test
	public void prepareWallsTest() throws Exception {
		LinkedList<Primitive> prepareWalls = floor.prepareWalls();
		assertTrue(prepareWalls.size() == 1);
	}

	public boolean arrayEqualsTest(BasicBlock[] original, BasicBlock[] expected) {
		if (original.length != expected.length) {
			return false;
		}

		for (int i = 0; i < original.length; i++) {

			if (!(original[i].equals(expected[i]))) {
				return false;
			}

		}

		return true;
	}

	@Test
	public void torchPatternTest() {

		BasicBlock torch = BasicBlock.TORCH;
		BasicBlock space = BasicBlock.NON_BLOCK;

		BasicBlock[] createTorchPattern0 = floor.createTorchPattern(0, 0);
		BasicBlock[] createTorchPattern1 = floor.createTorchPattern(1, 0);
		BasicBlock[] createTorchPattern2 = floor.createTorchPattern(2, 0);
		BasicBlock[] createTorchPattern3 = floor.createTorchPattern(3, 0);
		BasicBlock[] createTorchPattern4 = floor.createTorchPattern(4, 0);
		BasicBlock[] createTorchPattern5 = floor.createTorchPattern(5, 0);

		assertTrue("case:0", arrayEqualsTest(createTorchPattern0, new BasicBlock[] { space }));
		assertTrue("case:1",
				arrayEqualsTest(createTorchPattern1, new BasicBlock[] { torch, space, space, space, space }));
		assertTrue("case:2", arrayEqualsTest(createTorchPattern2, new BasicBlock[] { torch, space, space, space }));
		assertTrue("case:3", arrayEqualsTest(createTorchPattern3, new BasicBlock[] { torch, space, space }));
		assertTrue("case:4", arrayEqualsTest(createTorchPattern4, new BasicBlock[] { torch, space }));
		assertTrue("case:5", arrayEqualsTest(createTorchPattern5, new BasicBlock[] { torch }));
	}

}
