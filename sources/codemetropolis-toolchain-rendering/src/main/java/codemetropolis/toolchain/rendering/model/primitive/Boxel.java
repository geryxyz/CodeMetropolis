package codemetropolis.toolchain.rendering.model.primitive;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import codemetropolis.blockmodifier.World;
import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.model.BasicBlock;

public class Boxel implements Primitive {

	public BasicBlock block;
	public Point position;
	public String info;

	public Boxel(BasicBlock block, Point position) {
		super();
		this.block = block;
		this.position = position;
	}

	public Boxel(BasicBlock block, Point position, String info) {
		this(block, position);
		this.info = info;

	}

	public void render(World world) {
		if (position.getY() < 0 || position.getY() >= 255)
			return;

		switch (block.getId()) {

		case "minecraft:sign":
			world.setSignPost(position.getX(), position.getY(), position.getZ(), block.getProperties(), info);
			break;
		case "minecraft:wall_sign":
			world.setWallSign(position.getX(), position.getY(), position.getZ(), block.getProperties(), info);
			break;
		case "minecraft:white_banner":
			world.setBanner(position.getX(), position.getY(), position.getZ(), block.getProperties(),
					World.BannerColor.valueOf(info.toUpperCase()));
			break;
		default:
			world.setBlock(position.getX(), position.getY(), position.getZ(), block.getId(), block.getProperties());
		}
	}

	public String toCSV() {

		if (block.getId().equals("")) {
			return null;
		}

		String fancyProperties = block.getProperties().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
				.collect(Collectors.joining("&"));

		return String.format("%s;%s;%d;%d;%d;%s", block.getId(), fancyProperties, position.getX(), position.getY(),
				position.getZ(), (info == null || info.equals("") ? "NULL" : info));
	}

	public static Boxel parseCSV(String csv) {
		String[] parts = csv.split(";");
		Map<String, String> properties = Collections.emptyMap();
		try {
			String[] rawProperties = parts[1].split("&");
			properties = Arrays.stream(rawProperties)
					.collect(Collectors.toMap(e -> e.split("=")[0], e -> e.split("=")[1]));
		} catch (Exception e2) {

		}

		return new Boxel(new BasicBlock(parts[0], properties),
				new Point(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])),
				(parts[5].equals("NULL") ? "" : parts[5]));
	}

	@Override
	public int toCSVFile(File directory) {
		int x = position.getX() >> 9;
		int z = position.getZ() >> 9;

		directory.mkdirs();
		String filename = String.format("blocks.%d.%d.csv", x, z);
		File file = new File(directory, filename);

		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
			String csv = toCSV();
			if (csv != null)
				writer.println(csv);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return 1;
	}

	@Override
	public int getNumberOfBlocks() {
		return 1;
	}

	@Override
	public String toString() {
		return "Boxel [block=" + block + ", position=" + position + ", info=" + info + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Boxel other = (Boxel) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}
	
	

}
