package codemetropolis.toolchain.rendering.model.building;

import java.util.LinkedList;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.BasicBlock;
import codemetropolis.toolchain.rendering.model.pattern.RepeationPattern;
import codemetropolis.toolchain.rendering.model.primitive.Primitive;
import codemetropolis.toolchain.rendering.model.primitive.SignPost;
import codemetropolis.toolchain.rendering.model.primitive.SolidBox;
import codemetropolis.toolchain.rendering.util.Orientation;

public class Ground extends Building {

	public Ground(Buildable innerBuildable) throws BuildingTypeMismatchException {
		super(innerBuildable);

		if (innerBuildable.getType() != Type.GROUND)
			throw new BuildingTypeMismatchException(innerBuildable.getType(), getClass());

		primitives.addAll(prepareBase());
		primitives.addAll(prepareSigns());
	}


	protected LinkedList<Primitive> prepareBase() {

		LinkedList<Primitive> base = new LinkedList<>();
		base.add(new SolidBox(position, new Point(size.getX(), 1, size.getZ()),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.STONE } } }),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.STONE_BRICKS } } }), Orientation.NearX));
		return base;
	}

	protected LinkedList<Primitive> prepareSigns() {
		LinkedList<Primitive> signs = new LinkedList<>();

		signs.add(new SignPost(position.getX(), position.getY() + 1, position.getZ(), SignPost.Orientation.NORTHWEST,
				innerBuildable.getName()));
		signs.add(new SignPost(position.getX() + size.getX() - 1, position.getY() + 1, position.getZ(),
				SignPost.Orientation.NORTHEAST, innerBuildable.getName()));
		signs.add(new SignPost(position.getX(), position.getY() + 1, position.getZ() + size.getZ() - 1,
				SignPost.Orientation.SOUTHWEST, innerBuildable.getName()));
		signs.add(new SignPost(position.getX() + size.getX() - 1, position.getY() + 1,
				position.getZ() + size.getZ() - 1, SignPost.Orientation.SOUTHEAST, innerBuildable.getName()));
		return signs;

	}

}
