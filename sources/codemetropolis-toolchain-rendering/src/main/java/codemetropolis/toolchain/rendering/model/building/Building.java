package codemetropolis.toolchain.rendering.model.building;

import java.io.File;
import java.util.LinkedList;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.model.primitive.Primitive;
import codemetropolis.toolchain.rendering.model.primitive.SignPost;

public class Building {

	private static final int MIN_SIZE = 9;

	protected LinkedList<Primitive> primitives = new LinkedList<Primitive>();
	protected LinkedList<SignPost> signs = new LinkedList<SignPost>();
	protected Buildable innerBuildable;

	protected Point position;
	protected Point center;
	protected Point size;

	public Building(Buildable innerBuildable) {
		this.innerBuildable = innerBuildable;

		size = new Point(adjustSize(innerBuildable.getSizeX()), adjustSize(innerBuildable.getSizeY()),
				adjustSize(innerBuildable.getSizeZ()));

		position = new Point(innerBuildable.getPositionX(), innerBuildable.getPositionY(),
				innerBuildable.getPositionZ());

		center = new Point((int) (size.getX() * 0.5), (int) (size.getY() * 0.5), (int) (size.getZ() * 0.5));
	}

	public static int adjustSize(int x) {
		if (x < MIN_SIZE)
			return MIN_SIZE;
		if (x % 2 == 0)
			return x + 1;
		return x;
	}
	
	

	public int toCSVFile(File directory) {
		int count = 0;
		for (Primitive primitive : primitives) {
			primitive.toCSVFile(directory);
			count += primitive.getNumberOfBlocks();
		}
		return count;
	}

	public Buildable getInnerBuildable() {
		return innerBuildable;
	}

	public Point getPosition() {
		return position;
	}

	public Point getCenter() {
		return center;
	}

	public Point getSize() {
		return size;
	}

	public int getNumberOfBlocks() {
		int result = 0;
		for (Primitive p : primitives)
			result += p.getNumberOfBlocks();
		return result;
	}

}
