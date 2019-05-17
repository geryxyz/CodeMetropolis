package codemetropolis.toolchain.rendering.util;

import codemetropolis.toolchain.rendering.model.BasicBlock;

public enum Character {
	
	STONE,
	COBBLESTONE,
	MOSSY_STONE,
	SANDSTONE,
	OBSIDIAN,
	WOOD,
	DARK_WOOD,
	BIRCH_WOOD,
	PLANKS,
	DARK_PLANKS,
	METAL,
	DIRT,
	SAND,
	RED_SAND,
	BRICK,
	STONE_BRICK,
	DARK_BRICK,
	GLASS,
	GOLD,
	DIAMOND,
	UNDEFINED;
	
	public static Character parse(String str) {
		for(Character c : Character.values()) {
			System.out.println(c);
			if(c.toString().equalsIgnoreCase(str)) return c;
		}
		return Character.UNDEFINED;
	}
	
	public BasicBlock getBlock() throws Exception {
		
		
		switch(this) {
			case STONE: return new BasicBlock( "minecraft:stone" );
			case COBBLESTONE: return new BasicBlock( "minecraft:cobblestone" );
			case MOSSY_STONE: return new BasicBlock( "minecraft:mossy_cobblestone" );
			case SANDSTONE: return new BasicBlock( "minecraft:sandstone" );
			case OBSIDIAN: return new BasicBlock( "minecraft:obsidian" );
			case WOOD: return new BasicBlock( "minecraft:log" );
			case DARK_WOOD: return new BasicBlock( "minecraft:log");
			case BIRCH_WOOD: return new BasicBlock( "minecraft:log");
			case PLANKS: return new BasicBlock( "minecraft:planks" );
			case DARK_PLANKS: return new BasicBlock( "minecraft:planks");
			case METAL: return new BasicBlock( "minecraft:iron_block" );
			case DIRT: return new BasicBlock( "minecraft:dirt" );
			case SAND: return new BasicBlock( "minecraft:sandstone");
			case RED_SAND: return new BasicBlock( "minecraft:sand" );
			case BRICK: return new BasicBlock( "minecraft:double_stone_slab");
			case STONE_BRICK: return new BasicBlock( "minecraft:double_stone_slab" );
			case DARK_BRICK: return new BasicBlock( "minecraft:double_stone_slab");
			case GLASS: return new BasicBlock( "minecraft:glass" );
			case GOLD: return new BasicBlock( "minecraft:gold_block" );
			case DIAMOND: return new BasicBlock( "minecraft:diamond_block" );
			case UNDEFINED: return new BasicBlock( "minecraft:wool" );
			default: throw new Exception("Unsupported Block");
		}
	}
	
	public BasicBlock getTopBlock() throws Exception {
		switch(this) {
			case WOOD:
			case DARK_WOOD:
			case BIRCH_WOOD:
			case PLANKS:
			case DARK_PLANKS:
				return new BasicBlock( "minecraft:fence" );
			default: return getBlock();
		}
	}
}
