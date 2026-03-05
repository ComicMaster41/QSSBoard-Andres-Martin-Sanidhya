package comp20050.qssboard;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    @Test
    @DisplayName("setOwner and getOwner work correctly")
    void ownerSetAndGet() {
        Tile t = new Tile(QuaxBoard.TileType.OCTAGON);
        t.setOwner(GameState.Player.P1);
        assertEquals(GameState.Player.P1, t.getOwner());

        t.setOwner(GameState.Player.P2);
        assertEquals(GameState.Player.P2, t.getOwner());
    }

    @Test
    @DisplayName("getType returns correct type")
    void getTypeReturnsType() {
        Tile t = new Tile(QuaxBoard.TileType.OCTAGON);
        assertEquals(QuaxBoard.TileType.OCTAGON, t.getType());

        Tile r = new Tile(QuaxBoard.TileType.RHOMBUS);
        assertEquals(QuaxBoard.TileType.RHOMBUS, r.getType());
    }

    @Test
    @DisplayName("NUM_ROWS and NUM_COLS constants are correct")
    void constantsAreCorrect() {
        assertEquals(11, Tile.NUM_ROWS);
        assertEquals(21, Tile.NUM_COLS);
    }
}