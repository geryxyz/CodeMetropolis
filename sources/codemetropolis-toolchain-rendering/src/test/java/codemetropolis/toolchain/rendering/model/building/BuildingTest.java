package codemetropolis.toolchain.rendering.model.building;

import static org.junit.Assert.*;

import org.junit.Test;

public class BuildingTest {


	@Test
	public void adjustSizeTestLowerThanMinValue() {
		int adjustSize = Building.adjustSize(2);
		assertEquals(9, adjustSize);
		
	}
	
	@Test
	public void adjustSizeTestWithEvenNumber() {
		int adjustSize = Building.adjustSize(10);
		assertEquals(11, adjustSize);
	}
	
	@Test
	public void adjustSizeTestWithOddNumber() {
		int adjustSize = Building.adjustSize(11);
		assertEquals(11, adjustSize);
		
	}

}
