package codemetropolis.toolchain.rendering.model.building;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.BasicBlock;
import codemetropolis.toolchain.rendering.model.pattern.RepeationPattern;
import codemetropolis.toolchain.rendering.model.primitive.SolidBox;
import codemetropolis.toolchain.rendering.util.Orientation;

public class Cellar extends Floor {

	public Cellar(Buildable innerBuildable) throws BuildingTypeMismatchException {
		super(innerBuildable);

		if ( innerBuildable.getType() != Type.CELLAR )
			throw new BuildingTypeMismatchException(innerBuildable.getType(), getClass());
		
		primitives.add(
				0,
				new SolidBox(
					position.translate( new Point( 1, 1, 1 ) ),
					size.translate( new Point( -2, -2, -2 ) ),
					new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:air" ) } } } ),
					new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:air" ) } } } ),
					Orientation.NearX ) );
		
		primitives.add(
				new SolidBox(
					position.translate( new Point( center.getX() - 3, size.getY() + 1, center.getZ() - 3 ) ),
					new Point( 7, 1, 7 ),
					new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:air" ) } } } ),
					new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:air" ) } } } ),
					Orientation.NearX ) );
	}

}
