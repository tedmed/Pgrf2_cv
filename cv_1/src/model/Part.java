package model;

public class Part {
    private final TopologyType type;
    private final int index; // na kterém indexu začíná
    private final int count; // kolik je tam primitiv

    public Part(TopologyType type, int index, int count) {
        this.type = type;
        this.index = index;
        this.count = count;
    }

    public TopologyType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }
}
