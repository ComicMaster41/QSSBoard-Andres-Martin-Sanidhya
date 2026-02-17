package comp20050.qssboard;

public class Tile {
    public enum TileType { OCTAGON, RHOMBUS }

    public TileType type;
    public GameState.Player owner;

    public Tile(TileType type) {
        this.type = type;
        this.owner = null;
    }

    public GameState.Player getOwner() {
        return owner;
    }

    public void setOwner(GameState.Player new_owner) {
        owner = new_owner;
    }

    public TileType getType() {
        return type;
    }
}

