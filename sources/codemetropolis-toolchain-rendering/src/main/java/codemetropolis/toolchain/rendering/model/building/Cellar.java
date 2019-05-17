package codemetropolis.toolchain.rendering.model.building;

import java.util.LinkedList;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.BasicBlock;
import codemetropolis.toolchain.rendering.model.pattern.RepeationPattern;
import codemetropolis.toolchain.rendering.model.primitive.Primitive;
import codemetropolis.toolchain.rendering.model.primitive.SolidBox;
import codemetropolis.toolchain.rendering.util.Orientation;

public class Cellar extends Floor {

	public Cellar(Buildable innerBuildable) throws Exception {
		super(innerBuildable);

		if (innerBuildable.getType() != Type.CELLAR) {
			throw new BuildingTypeMismatchException(innerBuildable.getType(), getClass());
		}

		primitives.addAll(makePrimitives());

	}

	public LinkedList<Primitive> makePrimitives() {
		LinkedList<Primitive> primitive = new LinkedList<>();

		primitive.add(0,
				new SolidBox(position.translate(new Point(1, 1, 1)), size.translate(new Point(-2, -2, -2)),
						new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.AIR } } }),
						new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.AIR } } }), Orientation.NearX));

		primitive.add(new SolidBox(position.translate(new Point(center.getX() - 3, size.getY() + 1, center.getZ() - 3)),
				new Point(7, 1, 7), new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.AIR } } }),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.AIR } } }), Orientation.NearX));

		return primitive;
	}

}
