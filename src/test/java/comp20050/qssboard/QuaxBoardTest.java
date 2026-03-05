package comp20050.qssboard;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class QuaxBoardTest {

    QuaxBoard board;

    @BeforeEach
    void setUp() {
        board = new QuaxBoard();
    }

    @Nested
    @DisplayName("Tests for isMoveValid")
    class IsMoveValidTests {
        @Test
        @DisplayName("Empty OCTAGON position should be valid")
        void emptyOctagonValid() {
            assertTrue(board.isMoveValid(0, 0, QuaxBoard.TileType.OCTAGON));
        }

        @Test
        @DisplayName("Empty RHOMBUS position should be valid")
        void emptyRhombusValid() {
            assertTrue(board.isMoveValid(0, 0, QuaxBoard.TileType.RHOMBUS));
        }

        @Test
        @DisplayName("Occupied OCTAGON position should be invalid")
        void occupiedOctagonInvalid() {
            board.makeMove(1, 1, GameState.Player.P1, QuaxBoard.TileType.OCTAGON);
            assertFalse(board.isMoveValid(1, 1, QuaxBoard.TileType.OCTAGON));
        }

        @Test
        @DisplayName("Occupied RHOMBUS position should be invalid")
        void occupiedRhombusInvalid() {
            board.makeMove(2, 2, GameState.Player.P1, QuaxBoard.TileType.RHOMBUS);
            assertFalse(board.isMoveValid(2, 2, QuaxBoard.TileType.RHOMBUS));
        }

        @Test
        @DisplayName("Different board types are independent")
        void octagonAndRhombusIndependence() {
            board.makeMove(3, 3, GameState.Player.P1, QuaxBoard.TileType.OCTAGON);
            assertTrue(board.isMoveValid(3, 3, QuaxBoard.TileType.RHOMBUS));
        }

        @Test
        @DisplayName("Out-of-bounds move throws exception")
        void outOfBoundsMove() {
            assertThrows(ArrayIndexOutOfBoundsException.class,
                    () -> board.isMoveValid(11, 0, QuaxBoard.TileType.OCTAGON));
            assertThrows(ArrayIndexOutOfBoundsException.class,
                    () -> board.isMoveValid(0, 11, QuaxBoard.TileType.RHOMBUS));
        }
    }

    @Nested
    @DisplayName("Tests for makeMove")
    class MakeMoveTests {
        @Test
        @DisplayName("Making a move on occupied position overwrites")
        void makeMoveOverwrite() {
            board.makeMove(0, 0, GameState.Player.P1, QuaxBoard.TileType.OCTAGON);
            board.makeMove(0, 0, GameState.Player.P2, QuaxBoard.TileType.OCTAGON);
            assertEquals(GameState.Player.P2, board.getStateBoard()[0][0]);
        }

        @Test
        @DisplayName("Out-of-bounds move throws exception")
        void makeMoveOutOfBounds() {
            assertThrows(ArrayIndexOutOfBoundsException.class,
                    () -> board.makeMove(11, 0, GameState.Player.P1, QuaxBoard.TileType.OCTAGON));
        }
    }

//    @Nested
//    @DisplayName("Tests for getStateBoard")
//    class GetStateBoardTests {
//        @Test
//        @DisplayName("OCTAGON board is empty initially")
//        void octagonEmptyInitially() {
//            GameState.Player[][] boardState = board.getStateBoard();
//            for (int r = 0; r < 11; r++) {
//                for (int c = 0; c < 11; c++) {
//                    assertNull(boardState[r][c]);
//                }
//            }
//        }
//    }
}

// need to add logic for handing rhombus' more effectively - will then add code to test that