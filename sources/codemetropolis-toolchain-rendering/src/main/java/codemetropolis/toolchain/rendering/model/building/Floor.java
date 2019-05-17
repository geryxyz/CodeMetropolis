package codemetropolis.toolchain.rendering.model.building;

import java.util.LinkedList;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.BasicBlock;
import codemetropolis.toolchain.rendering.model.pattern.Pattern;
import codemetropolis.toolchain.rendering.model.pattern.RandomPattern;
import codemetropolis.toolchain.rendering.model.pattern.RepeationPattern;
import codemetropolis.toolchain.rendering.model.primitive.Door;
import codemetropolis.toolchain.rendering.model.primitive.EmptyBox;
import codemetropolis.toolchain.rendering.model.primitive.Primitive;
import codemetropolis.toolchain.rendering.model.primitive.Row;
import codemetropolis.toolchain.rendering.model.primitive.Row.BlockFacing;
import codemetropolis.toolchain.rendering.model.primitive.SolidBox;
import codemetropolis.toolchain.rendering.model.primitive.WallSign;
import codemetropolis.toolchain.rendering.util.Orientation;

public class Floor extends Building {

	public Floor(Buildable innerBuildable) throws Exception {
		super(innerBuildable);

		if (innerBuildable.getType() != Type.FLOOR && innerBuildable.getType() != Type.CELLAR)
			throw new BuildingTypeMismatchException(innerBuildable.getType(), getClass());

		primitives.addAll(prepareWalls());
		primitives.addAll(prepareStairs());
		primitives.addAll(prepareDoor());
		primitives.addAll(prepareSigns());
		primitives.addAll(prepareTorches());
	}

	protected LinkedList<Primitive> prepareDoor() {
		LinkedList<Primitive> doors = new LinkedList<>();

		doors.add(new SolidBox(position.translate(new Point(center.getX() - 1, 0, 0)), new Point(3, 4, 1),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.AIR } } }),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.RED_WOOL }, { BasicBlock.REDSTONE_LAMP },
						{ BasicBlock.REDSTONE_BLOCK }, { BasicBlock.RED_WOOL } } }),
				Orientation.NearX));
		doors.add(new SolidBox(position.translate(new Point(center.getX() - 1, 0, size.getZ() - 1)), new Point(3, 4, 1),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.AIR } } }),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.GREENWOOL }, { BasicBlock.REDSTONE_LAMP },
						{ BasicBlock.REDSTONE_BLOCK }, { BasicBlock.GREENWOOL } } }),
				Orientation.NearX));
		doors.add(new SolidBox(position.translate(new Point(0, 0, center.getZ() - 1)), new Point(1, 4, 3),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.AIR } } }),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.BLUE_WOOL }, { BasicBlock.REDSTONE_LAMP },
						{ BasicBlock.REDSTONE_BLOCK }, { BasicBlock.BLUE_WOOL } } }),
				Orientation.NearX));
		doors.add(new SolidBox(position.translate(new Point(size.getX() - 1, 0, center.getZ() - 1)), new Point(1, 4, 3),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.AIR } } }),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.YELLOW_WOOL }, { BasicBlock.REDSTONE_LAMP },
						{ BasicBlock.REDSTONE_BLOCK }, { BasicBlock.YELLOW_WOOL } } }),
				Orientation.NearX));

		doors.add(new Door(position.getX() + size.getX() / 2, position.getY() + 1, position.getZ(),
				Door.Orientation.SOUTH));
		doors.add(new Door(position.getX() + size.getX() / 2, position.getY() + 1, position.getZ() + size.getZ() - 1,
				Door.Orientation.NORTH));
		doors.add(new Door(position.getX(), position.getY() + 1, position.getZ() + size.getZ() / 2,
				Door.Orientation.EAST));
		doors.add(new Door(position.getX() + size.getX() - 1, position.getY() + 1, position.getZ() + size.getZ() / 2,
				Door.Orientation.WEST));

		return doors;
	}

	protected LinkedList<Primitive> prepareStairs() {

		LinkedList<Primitive> stairs = new LinkedList<>();

		stairs.add(new SolidBox(position.translate(new Point(center.getX() - 2, 0, center.getZ() - 2)),
				new Point(5, size.getY() + 1, 5), getStairRepetationPattern(),
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.FENCE } } }), Orientation.NearY));
		return stairs;
	}

	protected Pattern getStairRepetationPattern() {
		BasicBlock _air = BasicBlock.AIR;
		BasicBlock _str = BasicBlock.STONE;
		BasicBlock _cre = BasicBlock.FENCE;

		return new RepeationPattern(new BasicBlock[][][] {
				{ { _air, _air, _air, _air, _air }, { _air, _str, _air, _air, _air }, { _air, _air, _cre, _air, _air },
						{ _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air } },
				{ { _air, _air, _air, _air, _air }, { _air, _air, _str, _air, _air }, { _air, _air, _cre, _air, _air },
						{ _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air } },
				{ { _air, _air, _air, _air, _air }, { _air, _air, _air, _str, _air }, { _air, _air, _cre, _air, _air },
						{ _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air } },
				{ { _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air }, { _air, _air, _cre, _str, _air },
						{ _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air } },
				{ { _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air }, { _air, _air, _cre, _air, _air },
						{ _air, _air, _air, _str, _air }, { _air, _air, _air, _air, _air } },
				{ { _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air }, { _air, _air, _cre, _air, _air },
						{ _air, _air, _str, _air, _air }, { _air, _air, _air, _air, _air } },
				{ { _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air }, { _air, _air, _cre, _air, _air },
						{ _air, _str, _air, _air, _air }, { _air, _air, _air, _air, _air } },
				{ { _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air }, { _air, _str, _cre, _air, _air },
						{ _air, _air, _air, _air, _air }, { _air, _air, _air, _air, _air } } });
	}

	protected LinkedList<Primitive> prepareWalls() throws Exception {
		RepeationPattern _bottomFill;
		RepeationPattern _topFill;
		RandomPattern _sideFill;
		RepeationPattern _stroke;
		BasicBlock _sideBlock;
		BasicBlock _strcBlock;
		LinkedList<Primitive> walls = new LinkedList<>();

		if (innerBuildable.hasAttribute("character")) {
			_sideBlock = new BasicBlock(innerBuildable.getAttributeValue("character"));
			_topFill = new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.FENCE } } });
		} else {
			_sideBlock = BasicBlock.MAGENTA_WOOL;
			_topFill = new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.MAGENTA_WOOL } } });
		}

		if (innerBuildable.hasAttribute("external_character")) {
			BasicBlock block = new BasicBlock(innerBuildable.getAttributeValue("external_character"));
			_bottomFill = new RepeationPattern(new BasicBlock[][][] { { { block } } });
			_strcBlock = block;
			_stroke = new RepeationPattern(new BasicBlock[][][] { { { block } } });
		} else {
			_bottomFill = new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.MAGENTA_WOOL } } });
			_strcBlock = BasicBlock.PURPLE_WOOL;
			_stroke = new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.BLACK_WOOL } } });
		}

		RandomPattern _fallbackPattern = new RandomPattern(
				new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.NON_BLOCK } } }));
		_fallbackPattern.add(new RepeationPattern(new BasicBlock[][][] { { { BasicBlock.FENCE } } }), .5);
		_sideFill = new RandomPattern(_fallbackPattern);
		_sideFill.add(
				new RepeationPattern(
						new BasicBlock[][][] { { { _sideBlock, _sideBlock, _strcBlock, _sideBlock, _sideBlock },
								{ _sideBlock, _sideBlock, _strcBlock, _sideBlock, _sideBlock },
								{ _strcBlock, _strcBlock, _strcBlock, _strcBlock, _strcBlock },
								{ _sideBlock, _sideBlock, _strcBlock, _sideBlock, _sideBlock },
								{ _sideBlock, _sideBlock, _strcBlock, _sideBlock, _sideBlock } } }),
				innerBuildable.hasAttribute("completeness")
						? Double.parseDouble(innerBuildable.getAttributeValue("completeness"))
						: 1);
		walls.add(new EmptyBox(position, size, _bottomFill, _topFill, _sideFill, _stroke, new Point(1, 1, 1),
				new Point(1, 1, 1)));
		return walls;
	}

	protected LinkedList<Primitive> prepareSigns() {
		LinkedList<Primitive> signs = new LinkedList<>();

		// Wall signs outside
		signs.add(new WallSign(position.getX() + size.getX() / 2, position.getY() + 3, position.getZ() - 1,
				WallSign.Orientation.NORTH, innerBuildable.getName()));
		signs.add(new WallSign(position.getX() + size.getX() / 2, position.getY() + 3, position.getZ() + size.getZ(),
				WallSign.Orientation.SOUTH, innerBuildable.getName()));
		signs.add(new WallSign(position.getX() - 1, position.getY() + 3, position.getZ() + size.getZ() / 2,
				WallSign.Orientation.WEST, innerBuildable.getName()));
		signs.add(new WallSign(position.getX() + size.getX(), position.getY() + 3, position.getZ() + size.getZ() / 2,
				WallSign.Orientation.EAST, innerBuildable.getName()));
		// Wall signs inside
		signs.add(new WallSign(position.getX() + size.getX() / 2, position.getY() + 3, position.getZ() + 1,
				WallSign.Orientation.SOUTH, innerBuildable.getName()));
		signs.add(new WallSign(position.getX() + size.getX() / 2, position.getY() + 3,
				position.getZ() + size.getZ() - 2, WallSign.Orientation.NORTH, innerBuildable.getName()));
		signs.add(new WallSign(position.getX() + 1, position.getY() + 3, position.getZ() + size.getZ() / 2,
				WallSign.Orientation.EAST, innerBuildable.getName()));
		signs.add(new WallSign(position.getX() + size.getX() - 2, position.getY() + 3,
				position.getZ() + size.getZ() / 2, WallSign.Orientation.WEST, innerBuildable.getName()));
		return signs;
	}

	private LinkedList<Primitive> prepareTorches() {
		LinkedList<Primitive> torches = new LinkedList<>();

		if (!innerBuildable.hasAttribute("torches"))
			return torches;

		int numberOfTorches = Integer.parseInt(innerBuildable.getAttributeValue("torches"));
		BasicBlock[] pattern;

		pattern = createTorchPattern(numberOfTorches, 3);
		torches.add(new Row(new Point(position.getX() + size.getX() / 2 + 2, position.getY() + 2, position.getZ() + 1),
				size.getX() / 2 - 2, Row.Direction.WEST, pattern, BlockFacing.SOUTH));

		torches.add(new Row(new Point(position.getX() + size.getX() / 2 - 2, position.getY() + 2, position.getZ() + 1),
				size.getX() / 2 - 2, Row.Direction.EAST, pattern, BlockFacing.SOUTH));

		pattern = createTorchPattern(numberOfTorches, 4);
		torches.add(new Row(
				new Point(position.getX() + size.getX() / 2 + 2, position.getY() + 2,
						position.getZ() + size.getZ() - 2),
				size.getX() / 2 - 2, Row.Direction.WEST, pattern, BlockFacing.NORTH));

		torches.add(new Row(
				new Point(position.getX() + size.getX() / 2 - 2, position.getY() + 2,
						position.getZ() + size.getZ() - 2),
				size.getX() / 2 - 2, Row.Direction.EAST, pattern, BlockFacing.NORTH));

		pattern = createTorchPattern(numberOfTorches, 1);
		torches.add(new Row(new Point(position.getX() + 1, position.getY() + 2, position.getZ() + size.getZ() / 2 + 2),
				size.getZ() / 2 - 2, Row.Direction.NORTH, pattern, BlockFacing.EAST));

		torches.add(new Row(new Point(position.getX() + 1, position.getY() + 2, position.getZ() + size.getZ() / 2 - 2),
				size.getZ() / 2 - 2, Row.Direction.SOUTH, pattern, BlockFacing.EAST));

		pattern = createTorchPattern(numberOfTorches, 2);
		torches.add(new Row(
				new Point(position.getX() + size.getX() - 2, position.getY() + 2,
						position.getZ() + size.getZ() / 2 + 2),
				size.getZ() / 2 - 2, Row.Direction.NORTH, pattern, BlockFacing.WEST));

		torches.add(new Row(
				new Point(position.getX() + size.getX() - 2, position.getY() + 2,
						position.getZ() + size.getZ() / 2 - 2),
				size.getZ() / 2 - 2, Row.Direction.SOUTH, pattern, BlockFacing.WEST));

		return torches;
	}

	protected BasicBlock[] createTorchPattern(int number, int data) {
		BasicBlock[] pattern = null;
		BasicBlock torch = BasicBlock.TORCH;
		BasicBlock space = BasicBlock.NON_BLOCK;

		switch (number) {
		case 0:
			pattern = new BasicBlock[] { space };
			break;
		case 1:
			pattern = new BasicBlock[] { torch, space, space, space, space };
			break;
		case 2:
			pattern = new BasicBlock[] { torch, space, space, space };
			break;
		case 3:
			pattern = new BasicBlock[] { torch, space, space };
			break;
		case 4:
			pattern = new BasicBlock[] { torch, space };
			break;
		case 5:
			pattern = new BasicBlock[] { torch };
			break;
		}
		return pattern;
	}

}
