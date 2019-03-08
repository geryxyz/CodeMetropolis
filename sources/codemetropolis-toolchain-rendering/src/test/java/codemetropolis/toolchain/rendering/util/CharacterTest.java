package codemetropolis.toolchain.rendering.util;

import junit.framework.TestCase;

public class CharacterTest extends TestCase {
	
	Character testObj;
	
	public void setup() {
		testObj = null;
	}
	
	public void testGetBlock() {
		testObj = Character.STONE;assertEquals("minecraft:stone", testObj.getBlock().getName());	
		testObj = Character.COBBLESTONE;assertEquals("minecraft:cobblestone", testObj.getBlock().getName());
		testObj = Character.MOSSY_STONE;assertEquals("minecraft:mossy_cobblestone", testObj.getBlock().getName());
		testObj = Character.SANDSTONE; assertEquals("minecraft:sandstone", testObj.getBlock().getName());
		testObj = Character.OBSIDIAN; assertEquals("minecraft:obsidian", testObj.getBlock().getName());
		testObj = Character.WOOD; assertEquals("minecraft:log", testObj.getBlock().getName());
		testObj = Character.DARK_WOOD; assertEquals("minecraft:log", testObj.getBlock().getName());
		testObj = Character.BIRCH_WOOD; assertEquals("minecraft:log", testObj.getBlock().getName());
		testObj = Character.PLANKS; assertEquals("minecraft:planks", testObj.getBlock().getName());
		testObj = Character.DARK_PLANKS; assertEquals("minecraft:planks", testObj.getBlock().getName());
		testObj = Character.METAL; assertEquals("minecraft:iron_block", testObj.getBlock().getName());
		testObj = Character.DIRT; assertEquals("minecraft:dirt", testObj.getBlock().getName());
		testObj = Character.SAND; assertEquals("minecraft:sandstone", testObj.getBlock().getName());
		testObj = Character.RED_SAND; assertEquals("minecraft:sand", testObj.getBlock().getName());
		testObj = Character.BRICK; assertEquals("minecraft:double_stone_slab", testObj.getBlock().getName());
		testObj = Character.STONE_BRICK; assertEquals("minecraft:double_stone_slab", testObj.getBlock().getName());
		testObj = Character.DARK_BRICK; assertEquals("minecraft:double_stone_slab", testObj.getBlock().getName());
		testObj = Character.GLASS; assertEquals("minecraft:glass", testObj.getBlock().getName());
		testObj = Character.GOLD; assertEquals("minecraft:gold_block", testObj.getBlock().getName());
		testObj = Character.DIAMOND; assertEquals("minecraft:diamond_block", testObj.getBlock().getName());
		testObj = Character.UNDEFINED; assertEquals("minecraft:wool", testObj.getBlock().getName());
	}
	
	public void testGetTopBlock() {
		testObj = Character.WOOD; assertEquals("minecraft:fence", testObj.getTopBlock().getName());
		testObj = Character.DARK_WOOD; assertEquals("minecraft:fence", testObj.getTopBlock().getName());
		testObj = Character.BIRCH_WOOD; assertEquals("minecraft:fence", testObj.getTopBlock().getName());
		testObj = Character.PLANKS; assertEquals("minecraft:fence", testObj.getTopBlock().getName());
		testObj = Character.DARK_PLANKS; assertEquals("minecraft:fence", testObj.getTopBlock().getName());
		
		testObj = Character.STONE;assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());	
		testObj = Character.COBBLESTONE;assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.MOSSY_STONE;assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.SANDSTONE; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.OBSIDIAN; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.METAL; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.DIRT; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.SAND; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.RED_SAND; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.BRICK; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.STONE_BRICK; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.DARK_BRICK; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.GLASS; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
		testObj = Character.GOLD; assertEquals(testObj.getBlock().getName(), testObj.getTopBlock().getName());
	}
	

}
