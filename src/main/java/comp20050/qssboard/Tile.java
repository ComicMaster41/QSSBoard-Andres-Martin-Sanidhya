package comp20050.qssboard;

public class Tile {
    public QuaxBoard.TileType type;
    public QuaxBoard.TileOwner owner;
    public static final int NUM_ROWS = 11;
    public static final int NUM_COLS = 21;

    public Tile(QuaxBoard.TileType type) {
        this.type = type;
        this.owner = null;
    }

    public QuaxBoard.TileOwner getOwner() {
        return owner;
    }

    public void setOwner(QuaxBoard.TileOwner new_owner) {
        owner = new_owner;
    }

    public QuaxBoard.TileType getType() {
        return type;
    }
}

