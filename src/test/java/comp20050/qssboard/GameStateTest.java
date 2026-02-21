package comp20050.qssboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

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
        Position pos = new Position("OctCell_r0_c0");
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

}