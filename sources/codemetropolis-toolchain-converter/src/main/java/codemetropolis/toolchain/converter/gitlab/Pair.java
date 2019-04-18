package codemetropolis.toolchain.converter.gitlab;

public class Pair {
    private String ID;
    private Type type;

    public Pair(String ID, Type type) {
        this.ID = ID;
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public Type getType() {
        return type;
    }
}
