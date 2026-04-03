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
        @DisplayName("Empty position should be valid")
        void emptyPositionValid() {
            assertTrue(board.isMoveValid(0, 0, QuaxBoard.TileType.OCTAGON));
            assertTrue(board.isMoveValid(0, 1, QuaxBoard.TileType.RHOMBUS));
        }

        @Test
        @DisplayName("Occupied position should be invalid")
        void occupiedPositionInvalid() {
            board.makeMove(0, 0, GameState.Player.P1, QuaxBoard.TileType.OCTAGON);
            board.makeMove(0, 1, GameState.Player.P2, QuaxBoard.TileType.RHOMBUS);

            assertFalse(board.isMoveValid(0, 0, QuaxBoard.TileType.OCTAGON));
            assertFalse(board.isMoveValid(0, 1, QuaxBoard.TileType.RHOMBUS));
        }

        @Test
        @DisplayName("Out-of-bounds move throws exception")
        void outOfBounds() {
            assertThrows(ArrayIndexOutOfBoundsException.class,
                    () -> board.isMoveValid(11, 0, QuaxBoard.TileType.OCTAGON));
            assertThrows(ArrayIndexOutOfBoundsException.class,
                    () -> board.isMoveValid(0, 21, QuaxBoard.TileType.RHOMBUS));
        }
    }

    @Nested
    @DisplayName("Tests for makeMove")
    class MakeMoveTests {
        @Test
        @DisplayName("Making a move sets tile owner correctly")
        void makeMoveSetsOwner() {
            board.makeMove(0, 0, GameState.Player.P1, QuaxBoard.TileType.OCTAGON);
            assertEquals(QuaxBoard.TileOwner.BLACK, board.getStateBoard()[0][0].owner);

            board.makeMove(0, 1, GameState.Player.P2, QuaxBoard.TileType.RHOMBUS);
            assertEquals(QuaxBoard.TileOwner.WHITE, board.getStateBoard()[0][1].owner);
        }

        @Test
        @DisplayName("Making a move on occupied tile overwrites owner")
        void makeMoveOverwrite() {
            board.makeMove(0, 0, GameState.Player.P1, QuaxBoard.TileType.OCTAGON);
            board.makeMove(0, 0, GameState.Player.P2, QuaxBoard.TileType.OCTAGON);

            assertEquals(QuaxBoard.TileOwner.WHITE, board.getStateBoard()[0][0].owner);
        }

        @Test
        @DisplayName("Out-of-bounds move throws exception")
        void makeMoveOutOfBounds() {
            assertThrows(ArrayIndexOutOfBoundsException.class,
                    () -> board.makeMove(11, 0, GameState.Player.P1, QuaxBoard.TileType.OCTAGON));
        }
    }

    @Nested
    @DisplayName("Tests for changeTileOwner")
    class ChangeTileOwnerTests {

        @Test
        @DisplayName("Changing tile owner updates tile correctly")
        void changeOwnerUpdatesTile() {
            board.makeMove(0, 0, GameState.Player.P1, QuaxBoard.TileType.OCTAGON);
            board.changeTileOwner(0, 0, GameState.Player.P2);

            assertEquals(QuaxBoard.TileOwner.WHITE, board.getStateBoard()[0][0].owner);
        }
    }


    @Nested
    @DisplayName("Tests for board initialization and getStateBoard")
    class BoardInitializationTests {
        @Test
        @DisplayName("getStateBoard returns correct board reference")
        void getStateBoardReturnsCorrectBoard() {
            Tile[][] state = board.getStateBoard();
            assertEquals(Tile.NUM_ROWS, state.length, "Number of rows should match");
            assertEquals(Tile.NUM_COLS, state[0].length, "Number of columns should match");
        }
    }
}