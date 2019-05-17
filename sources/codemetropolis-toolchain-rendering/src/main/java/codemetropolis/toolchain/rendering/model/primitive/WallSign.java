package codemetropolis.toolchain.rendering.model.primitive;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.model.BasicBlock;

public class WallSign implements Primitive {
	
	public enum Orientation {
		NORTH,
		SOUTH,
		WEST,
		EAST;
	}
	
	private Point position;
	private Orientation orientation;
	private String text;

	public WallSign(int x, int y, int z, Orientation orientation, String text) {
		this(new Point(x,y,z), orientation, text);
	}
	
	public WallSign(Point position, Orientation orientation, String text) {
		super();
		this.position = position;
		this.orientation = orientation;
		this.text = text;
	}
	
	@Override
	public int toCSVFile(File directory) {
		
		Map<String, String> properties = new HashMap<>();
		properties.put("facing", orientation.toString().toLowerCase());
		
		BasicBlock wallSign = new BasicBlock(BasicBlock.WALL_SIGN.getId(), properties);
		
		
		new Boxel(wallSign, position, text).toCSVFile(directory);
		return 1;
	}
	@Override
	public int getNumberOfBlocks() {
		return 1;
	}
	
}
