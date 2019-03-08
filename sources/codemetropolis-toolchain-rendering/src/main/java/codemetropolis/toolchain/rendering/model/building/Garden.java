package codemetropolis.toolchain.rendering.model.building;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.BasicBlock;
import codemetropolis.toolchain.rendering.model.pattern.RandomPattern;
import codemetropolis.toolchain.rendering.model.pattern.RepeationPattern;
import codemetropolis.toolchain.rendering.model.pattern.YSplitPattern;
import codemetropolis.toolchain.rendering.model.primitive.SignPost;
import codemetropolis.toolchain.rendering.model.primitive.Spawner;
import codemetropolis.toolchain.rendering.model.primitive.SolidBox;
import codemetropolis.toolchain.rendering.util.Orientation;

public class Garden extends Building {
	
	int monster_count;

	public Garden(Buildable innerBuildable) throws BuildingTypeMismatchException {
		super(innerBuildable);
		
		if ( innerBuildable.getType() != Type.GARDEN )
			throw new BuildingTypeMismatchException(innerBuildable.getType(), getClass());

		prepareBase();
		prepareDoor();
		prepareSigns();
		prepareSpawners();
		prepareSignsForSpawners();
	}
	
	private void prepareBase( ) {
		BasicBlock _fnc = new BasicBlock( "minecraft:fence" );
		BasicBlock _sns = new BasicBlock( "minecraft:sandstone" );
		
		monster_count = innerBuildable.hasAttribute("monster-count") 
				? Integer.parseInt( innerBuildable.getAttributeValue ("monster-count") )
				: 0;
		
		RandomPattern _flowers = new RandomPattern( new RepeationPattern(  new BasicBlock[][][]{ { { BasicBlock.NonBlock } } } ) );
		
		RandomPattern _redOrYellow = new RandomPattern( new RepeationPattern(  new BasicBlock[][][]{ { { new BasicBlock( "minecraft:yellow_flower" ) } } } ) );
		_redOrYellow.add(new RepeationPattern(  new BasicBlock[][][]{ { { new BasicBlock( "minecraft:red_flower" ) } } } ), 0.5);
		_flowers.add(
			_redOrYellow,
			innerBuildable.hasAttribute( "flower-ratio" )
				? Double.parseDouble( innerBuildable.getAttributeValue("flower-ratio") )
				: 0 );
		_flowers.add(
			new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:brown_mushroom" ) } } } ),
			innerBuildable.hasAttribute( "mushroom-ratio" )
				? Double.parseDouble( innerBuildable.getAttributeValue("mushroom-ratio") )
				: 0 );
		_flowers.add(
			new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:sapling" ) } } } ),
			innerBuildable.hasAttribute( "tree-ratio" )
				? Double.parseDouble( innerBuildable.getAttributeValue("tree-ratio") )
				: 0 );
		primitives.add(
			new SolidBox(
				position, new Point( size.getX(), 2, size.getZ() ),
				new YSplitPattern(
					0,
					new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:grass" ) } } } ),
					_flowers ),
				new RepeationPattern(
					new BasicBlock[][][]
					{
						{
							{ _fnc },
							{ _sns }
						}
					} ),
				Orientation.NearX ) );
	}
	
	protected void prepareDoor( )
	{
		BasicBlock _fnc = new BasicBlock( "minecraft:fence" );
		BasicBlock _rwl = new BasicBlock( "minecraft:wool", 14 );
		BasicBlock _gwl = new BasicBlock( "minecraft:wool", 5 );
		BasicBlock _bwl = new BasicBlock( "minecraft:wool", 3 );
		BasicBlock _ywl = new BasicBlock( "minecraft:wool", 4 );
		primitives.add(
			new SolidBox(
				position.translate( new Point( center.getX() - 1, 0, 0 ) ), new Point( 3, 4, 1 ),
				new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:air", 2 ) } } } ),
				new RepeationPattern(
					new BasicBlock[][][]
					{
						{
							{ _fnc },
							{ _fnc },
							{ _fnc },
							{ _rwl }
						}
					} ),
				Orientation.NearX )
			);
		primitives.add(
			new SolidBox(
				position.translate( new Point( center.getX() - 1, 0, size.getZ() - 1 ) ), new Point( 3, 4, 1 ),
				new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:air", 2 ) } } } ),
				new RepeationPattern(
					new BasicBlock[][][]
					{
						{
							{ _fnc },
							{ _fnc },
							{ _fnc },
							{ _gwl }
						}
					} ),
				Orientation.NearX )
			);
		primitives.add(
			new SolidBox(
				position.translate( new Point( 0, 0, center.getZ()-1 ) ), new Point( 1, 4, 3 ),
				new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:air", 2 ) } } } ),
				new RepeationPattern(
					new BasicBlock[][][]
					{
						{
							{ _fnc },
							{ _fnc },
							{ _fnc },
							{ _bwl }
						}
					} ),
				Orientation.NearX )
			);
		primitives.add(
			new SolidBox(
				position.translate( new Point( size.getX()-1, 0, center.getZ() - 1 ) ), new Point( 1, 4, 3 ),
				new RepeationPattern( new BasicBlock[][][] { { { new BasicBlock( "minecraft:air", 2 ) } } } ),
				new RepeationPattern(
					new BasicBlock[][][]
					{
						{
							{ _fnc },
							{ _fnc },
							{ _fnc },
							{ _ywl }
						}
					} ),
				Orientation.NearX )
			);
	}
	
	private void prepareSigns( ) {
		primitives.add(new SignPost(position.getX(), position.getY() + 2, position.getZ(), SignPost.Orientation.NORTHWEST, innerBuildable.getName()));
		primitives.add(new SignPost(position.getX() + size.getX() - 1, position.getY() + 2, position.getZ(), SignPost.Orientation.NORTHEAST, innerBuildable.getName()));
		primitives.add(new SignPost(position.getX(), position.getY() + 2, position.getZ() + size.getZ() - 1, SignPost.Orientation.SOUTHWEST, innerBuildable.getName()));
		primitives.add(new SignPost(position.getX() + size.getX() - 1, position.getY() + 2, position.getZ() + size.getZ() - 1, SignPost.Orientation.SOUTHEAST, innerBuildable.getName()));
	}
	
	private void prepareSpawners( ) {
		if (monster_count > 0) primitives.add(new Spawner(position.getX() + size.getX() / 2, position.getY(), position.getZ() - 3));
		if (monster_count > 1) primitives.add(new Spawner(position.getX() + size.getX() / 2, position.getY(), position.getZ() + size.getZ() + 2));
		if (monster_count > 2) primitives.add(new Spawner(position.getX() - 3, position.getY(), position.getZ() + size.getZ() / 2));
		if (monster_count > 3) primitives.add(new Spawner(position.getX() + size.getX() + 2, position.getY(), position.getZ() + size.getZ() / 2));
	}
	
	private void prepareSignsForSpawners() {
		String signName = innerBuildable.hasAttribute("monster-label") ? (innerBuildable.getAttributeValue("monster-label")) : "";
		
		switch (monster_count) {
			case 4:
				primitives.add(new SignPost(position.getX() + size.getX() + 2, position.getY(), position.getZ() + size.getZ() / 2 + 1, SignPost.Orientation.EAST, signName));
				primitives.add(new SignPost(position.getX() + size.getX() + 2, position.getY(), position.getZ() + size.getZ() / 2 - 1, SignPost.Orientation.EAST, signName));
			case 3:
				primitives.add(new SignPost(position.getX() - 3, position.getY(), position.getZ() + size.getZ() / 2 + 1, SignPost.Orientation.WEST, signName));
				primitives.add(new SignPost(position.getX() - 3, position.getY(), position.getZ() + size.getZ() / 2 - 1, SignPost.Orientation.WEST, signName));
			case 2:
				primitives.add(new SignPost(position.getX() + size.getX() / 2 - 1, position.getY(), position.getZ() + size.getZ() + 2, SignPost.Orientation.SOUTH, signName));
				primitives.add(new SignPost(position.getX() + size.getX() / 2 + 1, position.getY(), position.getZ() + size.getZ() + 2, SignPost.Orientation.SOUTH, signName));
			case 1:
				primitives.add(new SignPost(position.getX() + size.getX() / 2 - 1, position.getY(), position.getZ() - 3, SignPost.Orientation.NORTH, signName));
				primitives.add(new SignPost(position.getX() + size.getX() / 2 + 1, position.getY(), position.getZ() - 3, SignPost.Orientation.NORTH, signName));
		}
	}

}
