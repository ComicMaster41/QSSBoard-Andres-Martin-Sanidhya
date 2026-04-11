package comp20050.qssboard;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {

    GameState gameState;
    Bot bot;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        bot = new Bot(gameState, null);
    }


    @Test
    @DisplayName("makeMove does not modify original game state")
    void makeMoveDoesNotModifyState() {
        int before = gameState.getLegalMoves().size();

        bot.makeMove();

        int after = gameState.getLegalMoves().size();

        assertEquals(before, after);
    }

    @Test
    @DisplayName("heuristic returns a finite value for initial board")
    void heuristicInitialBoard() {
        int value = bot.heuristic(gameState);

        assertTrue(value < 1_000_000 && value > -1_000_000);
    }

    @Test
    @DisplayName("heuristic is consistent on same state")
    void heuristicIsDeterministic() {
        int val1 = bot.heuristic(gameState);
        int val2 = bot.heuristic(gameState);

        assertEquals(val1, val2);
    }

    @Test
    @DisplayName("computeDistance returns non-negative value")
    void computeDistanceNonNegative() {
        int dist = bot.computeDistance(gameState, gameState.game_board.p1Color);

        assertTrue(dist >= 0);
    }


    @Test
    @DisplayName("minmax returns heuristic at depth 0")
    void minmaxDepthZero() {
        int result = bot.minmax(gameState, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        int expected = bot.heuristic(gameState);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("minmax runs without crashing at depth 2")
    void minmaxRuns() {
        int result = bot.minmax(gameState, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

        assertTrue(result < 1_000_000 && result > -1_000_000);
    }

    @Test
    @DisplayName("applyMove changes the board state")
    void applyMoveChangesBoard() {
        Position move = gameState.getLegalMoves().get(0);
        GameState copy = gameState.copyState();

        bot.applyMove(copy, move);

        assertFalse(copy.game_board.isTileEmpty(move.getRow(), move.getCol()));
    }


    @Test
    @DisplayName("computeDistance decreases after placing own tile")
    void computeDistanceImprovesAfterMove() {
        int before = bot.computeDistance(gameState, gameState.game_board.p2Color);

        Position move = bot.makeMove();
        gameState.makeMove(move, gameState.game_board.getTileType(move.getRow(), move.getCol()));

        int after = bot.computeDistance(gameState, gameState.game_board.p2Color);

        assertTrue(after <= before);
    } 
}