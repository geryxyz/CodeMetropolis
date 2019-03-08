package codemetropolis.toolchain.rendering.model.primitive;

import java.io.File;

import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.rendering.model.BasicBlock;

public class Spawner implements Primitive {

    private Point position;

    public Spawner(int x, int y, int z) {
        super();
        this.position = new Point(x, y, z);
    }

    @Override
    public int toCSVFile(File directory) {
        new Boxel(new BasicBlock((short) 52), position).toCSVFile(directory);
        return 1;
    }
    @Override
    public int getNumberOfBlocks() {
        return 1;
    }

}
