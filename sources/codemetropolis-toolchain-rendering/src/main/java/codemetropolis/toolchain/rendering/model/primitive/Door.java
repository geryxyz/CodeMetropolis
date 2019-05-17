package codemetropolis.toolchain.rendering.model.primitive;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.model.BasicBlock;

public class Door implements Primitive {

	public enum Orientation {
		NORTH, SOUTH, WEST, EAST;

	}

	private Point position;
	private Orientation orientation;

	public Door(int x, int y, int z, Orientation orientation) {
		super();
		this.position = new Point(x, y, z);
		this.orientation = orientation;

	}

	@Override
	public int toCSVFile(File directory) {

		Map<String, String> upperDoorProperties = new HashMap<>();
		upperDoorProperties.put("facing", orientation.toString().toLowerCase());
		upperDoorProperties.put("half", "upper");

		Map<String, String> lowerDoorProperties = new HashMap<>();
		lowerDoorProperties.put("facing", orientation.toString().toLowerCase());
		lowerDoorProperties.put("half", "lower");

		BasicBlock upperDoor = new BasicBlock(BasicBlock.DOOR.getId(), upperDoorProperties);
		BasicBlock lowerDoor = new BasicBlock(BasicBlock.DOOR.getId(), lowerDoorProperties);

		new Boxel(lowerDoor, position).toCSVFile(directory);
		new Boxel(upperDoor, new Point(position.getX(), position.getY() + 1, position.getZ())).toCSVFile(directory);
		return 2;
	}

	@Override
	public int getNumberOfBlocks() {
		return 2;
	}

}
