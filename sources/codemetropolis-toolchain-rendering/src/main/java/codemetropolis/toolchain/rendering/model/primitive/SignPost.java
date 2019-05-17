package codemetropolis.toolchain.rendering.model.primitive;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.model.BasicBlock;

public class SignPost implements Primitive {

	public enum Orientation {
		SOUTH(0), SOUTHWEST(2), WEST(4), NORTHWEST(6), NORTH(8), NORTHEAST(10), EAST(12), SOUTHEAST(14);

		private final int value;

		Orientation(int v) {
			value = v;
		}

		public int getValue() {
			return value;
		}
	}

	private Point position;
	private Orientation orientation;
	private String text;

	public SignPost(int x, int y, int z, Orientation orientation, String text) {
		super();
		this.position = new Point(x, y, z);
		this.orientation = orientation;
		this.text = text;
	}

	@Override
	public int toCSVFile(File directory) {

		Map<String, String> properties = new HashMap<>();
		properties.put("rotation", orientation.getValue() + "");

		BasicBlock signPost = new BasicBlock(BasicBlock.SIGN.getId(), properties);

		new Boxel(signPost, position, text).toCSVFile(directory);
		return 1;
	}

	@Override
	public int getNumberOfBlocks() {
		return 1;
	}

}
