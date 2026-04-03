package comp20050.qssboard;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class TileTest {


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