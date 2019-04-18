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

	protected int BuiltMetric1 , BuiltMetric2 , BuiltMetric3;
	
	public Building( Buildable innerBuildable ) {
		this.innerBuildable = innerBuildable;

		size = new Point(
				adjustSize(innerBuildable.getSizeX()),
				adjustSize(innerBuildable.getSizeY()),
				adjustSize(innerBuildable.getSizeZ())
		);
		if (innerBuildable.getType() == Buildable.Type.FLOOR) {
			BuiltMetric1 = Integer.parseInt(this.innerBuildable.getAttributeValue("BuiltMetric1"));
			BuiltMetric2 = Integer.parseInt(this.innerBuildable.getAttributeValue("BuiltMetric2"));
			BuiltMetric3 = Integer.parseInt(this.innerBuildable.getAttributeValue("BuiltMetric3"));
			
			
		} else{
			BuiltMetric1 = 0;
			BuiltMetric2 = 0;
			BuiltMetric3 = 0;
	}
		
		position = new Point(
				innerBuildable.getPositionX(),
				innerBuildable.getPositionY(),
				innerBuildable.getPositionZ()
				);
		
		center = new Point(
				(int)(size.getX() * 0.5),
				(int)(size.getY() * 0.5),
				(int)(size.getZ() * 0.5)
				);

	}
	public LinkedList<Primitive> getPrimitives() {
		return primitives;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public void setSize(Point size) {
		this.size = size;
	}

	private static int adjustSize(int x ) {
		if(x < MIN_SIZE) return MIN_SIZE;
		if(x % 2 == 0) return x + 1;
		return x;
	}
	
	public int toCSVFile(File directory) {
		int count = 0;
		for(Primitive primitive : primitives) {
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

	public int getBuiltMetric1() {
		return BuiltMetric1;
	}

	public void setBuiltMetric1(int builtMetric1) {
		BuiltMetric1 = builtMetric1;
	}

	public int getBuiltMetric2() {
		return BuiltMetric2;
	}

	public void setBuiltMetric2(int builtMetric2) {
		BuiltMetric2 = builtMetric2;
	}

	public int getBuiltMetric3() {
		return BuiltMetric3;
	}

	public void setBuiltMetric3(int builtMetric3) {
		BuiltMetric3 = builtMetric3;
	}

	public int getNumberOfBlocks() {
		int result = 0;
		for(Primitive p : primitives)
			result += p.getNumberOfBlocks();
		return result;
	}
	
}
