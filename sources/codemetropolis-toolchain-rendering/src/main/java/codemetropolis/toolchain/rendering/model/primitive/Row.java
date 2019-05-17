package codemetropolis.toolchain.rendering.model.primitive;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.model.BasicBlock;

public class Row implements Primitive {
	
	public enum Direction {
		UP,
		DOWN,
		NORTH,
		SOUTH,
		WEST,
		EAST;
	}
	
	public enum BlockFacing {
		NONE,
		NORTH,
		SOUTH,
		WEST,
		EAST;
		
	}
	
	private Point position;
	private int length;
	private Direction orientation;
	private BasicBlock[] pattern;
	private BlockFacing facing;
	
	public Row(Point position, int length, Direction orientation, BasicBlock[] pattern, BlockFacing facing) {
		super();
		this.facing = facing;
		this.position = position;
		this.length = length;
		this.orientation = orientation;
		this.pattern = pattern;
	}
	
	public Row(Point position, int length, Direction orientation, BasicBlock[] pattern) {
		this(position, length, orientation, pattern, BlockFacing.NONE);
	}
	
	@Override
	public int toCSVFile(File directory) {
		int c = 0;
		for(int i = 0; i < length; i++) {
			
			BasicBlock block = new BasicBlock(pattern[c]);
			if(facing != BlockFacing.NONE) {
				block.getProperties().put("facing", facing.toString().toLowerCase());
			}
			
			Point blockPos = null;
			switch(orientation) {
				case UP:
					blockPos = new Point(position.getX(), position.getY() + i, position.getZ());
					break;
				case DOWN:
					blockPos = new Point(position.getX(), position.getY() - i, position.getZ());
					break;
				case NORTH:
					blockPos = new Point(position.getX(), position.getY(), position.getZ() + i);
					break;
				case SOUTH:
					blockPos = new Point(position.getX(), position.getY(), position.getZ() - i);
					break;
				case WEST:
					blockPos = new Point(position.getX() + i, position.getY(), position.getZ());
					break;
				case EAST:
					blockPos = new Point(position.getX() - i, position.getY(), position.getZ());
					break;
			}
			
			new Boxel(block, blockPos).toCSVFile(directory);
			if(++c > pattern.length - 1) c = 0;
		}
		return length;
	}
	
	@Override
	public int getNumberOfBlocks() {
		return length;
	}
	
}
