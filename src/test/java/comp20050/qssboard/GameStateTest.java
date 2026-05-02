package comp20050.qssboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

class GameStateTest {
    GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    @DisplayName("getCurrentPLayer just tests if the getter method returns what we want")
    void testGetCurrentPlayer() {
        GameState.Player p1 = gameState.getCurrentPlayer();
        GameState.Player returnedPlayer = gameState.getCurrentPlayer();

        assertEquals(p1, returnedPlayer);
    }
    @Test
    @DisplayName("makeMove updates board and switches player")
    void makeMoveSwitchesPlayer() {
        Position pos = new Position("O_0_0");
        GameState.Player currentBefore = gameState.getCurrentPlayer();
        boolean result = gameState.makeMove(pos, QuaxBoard.TileType.OCTAGON);

        assertTrue(result);
        assertNotEquals(currentBefore, gameState.getCurrentPlayer(), "Player should switch after move");
    }

    @Test
    @DisplayName("switchPlayerTurn flips current player")
    void switchPlayerTurnTest() {
        GameState.Player p1 = gameState.getCurrentPlayer();
        gameState.switchPlayerTurn();
        assertNotEquals(p1, gameState.getCurrentPlayer());
        gameState.switchPlayerTurn();
        assertEquals(p1, gameState.getCurrentPlayer());
    }

    @Test
    @DisplayName("getLegalMoves returns correct positions for empty tiles")
    void testGetLegalMoves() {

        ArrayList<Position> legalMoves = gameState.getLegalMoves();
        assertNotNull(legalMoves);
        assertEquals(Tile.NUM_ROWS * Tile.NUM_COLS, legalMoves.size(),
                "All tiles should be legal on empty board");

        // Check that each Position has correct row, col, and prefix
        for (Position p : legalMoves) {
            int row = p.getRow();
            int col = p.getCol();
            // Row/col within bounds
            assertTrue(row >= 0 && row < Tile.NUM_ROWS, "Row in bounds");
            assertTrue(col >= 0 && col < Tile.NUM_COLS, "Col in bounds");
        }
    }

    @Test
    @DisplayName("checkWin returns false when no tiles occupied for Black")
    void testCheckWinBlackEmpty() {
        assertFalse(gameState.checkWin(QuaxBoard.TileOwner.BLACK));
    }

    @Test
    @DisplayName("checkWin returns false when no tiles occupied for White")
    void testCheckWinWhiteEmpty() {
        assertFalse(gameState.checkWin(QuaxBoard.TileOwner.WHITE));
    }

    @Test
    @DisplayName("checkWin returns true for Black when first column fully occupied")
    void testCheckWinBlackPath() {
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            gameState.getGameBoard().changeTileOwner(row, 0, GameState.Player.P1); // P1 is Black
        }
        assertTrue(gameState.checkWin(QuaxBoard.TileOwner.BLACK));
    }
    @Test
    @DisplayName("checkWin returns true for Black with staggered octagon-rhombus path")
    void testBlackWinsStaggeredPath() {
        // we get a chain which has to be made of octagons and rhombusess
        for (int i = 0;  i < Tile.NUM_ROWS; i++) {
            for (int col = 0; col < Tile.NUM_COLS/2; col++) {
                gameState.getGameBoard().changeTileOwner(i, col, GameState.Player.P1);
            }
        }
        assertTrue(gameState.checkWin(QuaxBoard.TileOwner.BLACK));
    }
    @Test
    @DisplayName("checkWin returns true for White when first row fully occupied")
    void testCheckWinWhitePath() {
        for (int col = 0; col < Tile.NUM_COLS; col++) {
            gameState.getGameBoard().changeTileOwner(0, col, GameState.Player.P2); // P2 is White
        }
        assertTrue(gameState.checkWin(QuaxBoard.TileOwner.WHITE));
    }

    @Test
    @DisplayName("checkWin returns true for White with staggered octagon-rhombus path")
    void testWhiteWinsStaggeredPath() {
        // we get a chain which has to be made of octagons and rhombgusess
        for (int i = 0; i < Tile.NUM_ROWS/2; i++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                gameState.getGameBoard().changeTileOwner(i, col, GameState.Player.P2);
            }
        }
        assertTrue(gameState.checkWin(QuaxBoard.TileOwner.WHITE));
    }

    @Test
    @DisplayName("dfs returns true when Black path reaches bottom row")
    void testDfsBlack() {
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            gameState.getGameBoard().changeTileOwner(row, 0, GameState.Player.P1);
        }
        assertTrue(gameState.checkWin(QuaxBoard.TileOwner.BLACK));
    }

    @Test
    @DisplayName("getNeighbours returns non-empty list for Octagon tile")
    void testGetNeighboursOctagon() {
        // Pick an even col = OCTAGON
        var neighbours = gameState.getNeighbours(5, 0);
        assertFalse(neighbours.isEmpty());
    }

    @Test
    @DisplayName("getNeighbours returns non-empty list for Rhombus tile")
    void testGetNeighboursRhombus() {
        // Pick an odd col = RHOMBUS
        var neighbours = gameState.getNeighbours(5, 1);
        assertFalse(neighbours.isEmpty());
    }

    @Test
    @DisplayName("getNeighbours only returns in-bounds positions")
    void testAddValid() {
        ArrayList<int[]> cornerNeighbours = gameState.getNeighbours(0, 0);
        ArrayList<int[]> middleNeighbours = gameState.getNeighbours(5, 4);

        assertTrue(cornerNeighbours.size() < middleNeighbours.size());

        for (int[] n : cornerNeighbours) {
            assertTrue(n[0] >= 0 && n[0] < Tile.NUM_ROWS);
            assertTrue(n[1] >= 0 && n[1] < Tile.NUM_COLS);
        }
    }



}