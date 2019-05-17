package codemetropolis.toolchain.rendering.model.primitive;

import static org.junit.Assert.*;

import javax.swing.Box;

import org.junit.Test;

import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.model.BasicBlock;

public class BoxelTest {

	@Test
	public void toCSVTestWithoutProperties() {
		Boxel boxel = new Boxel(BasicBlock.AIR, new Point(0, 0, 0), "testInfo");
		
		String csv = boxel.toCSV();
		String expected = "minecraft:air;;0;0;0;testInfo";
		
		assertTrue(expected.equals(csv));
	}
	
	@Test
	public void toCSVTestWithProperties() {
		Boxel boxel = new Boxel(BasicBlock.REDSTONE_LAMP, new Point(0, 0, 0), "testInfo");
		
		String csv = boxel.toCSV();
		String expected = "minecraft:redstone_lamp;lit=true;0;0;0;testInfo";

		assertTrue(expected.equals(csv));
	}
	
	@Test
	public void parseCSVWithoutProperties() {
		
		Boxel parsed = Boxel.parseCSV("minecraft:air;;0;0;0;testInfo");
		Boxel expected = new Boxel(BasicBlock.AIR, new Point(0, 0, 0), "testInfo");
		
		assertTrue(expected.equals(parsed));
	}
	
	@Test
	public void parseCSVWithProperties() {
		
		Boxel parsed = Boxel.parseCSV("minecraft:redstone_lamp;lit=true;0;0;0;testInfo");
		Boxel expected = new Boxel(BasicBlock.REDSTONE_LAMP, new Point(0, 0, 0), "testInfo");
		
		assertTrue(expected.equals(parsed));
	}

}
