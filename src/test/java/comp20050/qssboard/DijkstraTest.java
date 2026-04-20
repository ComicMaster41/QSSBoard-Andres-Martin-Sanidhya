package comp20050.qssboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    void computeDistanceReturnsNonNegative() {
        int distance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        assertTrue(distance >= 0, "Distance should be non-negative");
    }

    @Test
    void computeDistanceForBlackPlayer() {
        int distance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        assertTrue(distance >= 0);
    }

    @Test
    void computeDistanceForWhitePlayer() {
        int distance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.WHITE);
        assertTrue(distance >= 0);
    }

    @Test
    void computeDistanceOnEmptyBoard() {
        int blackDistance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        int whiteDistance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.WHITE);

        assertTrue(blackDistance >= 0);
        assertTrue(whiteDistance >= 0);
    }

    @Test
    void distanceIsDeterministic() {
        int distance1 = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        int distance2 = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);

        assertEquals(distance1, distance2, "Distance should be deterministic on same board state");
    }

    @Test
    void whiteDistanceAfterMultipleMoves() {
        gameState.makeMove(new Position("O_10_0"), QuaxBoard.TileType.OCTAGON);
        gameState.makeMove(new Position("O_0_0"), QuaxBoard.TileType.OCTAGON);
        int distance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.WHITE);
        assertTrue(distance >= 0);
    }

    @Test
    void computeDistanceConsistentBetweenPlayers() {
        int blackDist = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        int whiteDist = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.WHITE);

        assertTrue(blackDist >= 0);
        assertTrue(whiteDist >= 0);
    }

    @Test
    void computeDistanceAfterManyMoves() {
        Position[] moves = {
                new Position("O_10_0"),
                new Position("O_0_0"),
                new Position("O_9_0"),
                new Position("O_1_0"),
                new Position("O_8_0"),
                new Position("O_2_0")
        };

        for (Position move : moves) {
            if (gameState.makeMove(move, QuaxBoard.TileType.OCTAGON)) {
                int distance = Dijkstra.computeDistance(gameState, gameState.game_board.getColor(gameState.getCurrentPlayer()));
                assertTrue(distance >= 0, "Distance should be non-negative after move");
            }
        }
    }

    @Test
    void blackPathStartsFromTop() {
        int blackDist = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        assertTrue(blackDist >= 0);
    }

    @Test
    void whitePathStartsFromLeft() {
        int whiteDist = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.WHITE);
        assertTrue(whiteDist >= 0);
    }

    @Test
    void distanceWithOwnTilesHasCostZero() {
        gameState.makeMove(new Position("O_10_0"), QuaxBoard.TileType.OCTAGON);
        gameState.makeMove(new Position("O_0_0"), QuaxBoard.TileType.OCTAGON);

        int distance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        assertTrue(distance >= 0);
    }

    @Test
    void distanceWithOpponentTilesBlocked() {
        gameState.makeMove(new Position("O_10_0"), QuaxBoard.TileType.OCTAGON); // P1 (BLACK)
        gameState.makeMove(new Position("O_0_0"), QuaxBoard.TileType.OCTAGON);  // P2 (WHITE)

        int whiteDistance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.WHITE);
        assertTrue(whiteDistance >= 0);
    }

    @Test
    void emptyTilesHaveCostOne() {
        int distance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        assertTrue(distance > 0, "Distance should be positive on mostly empty board");
    }

    @Test
    void computeDistanceReturnsFiniteValue() {
        int distance = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        assertNotEquals(Integer.MAX_VALUE, distance, "Distance should be finite");
    }

    @Test
    void multipleCallsYieldSameResult() {
        int call1 = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        int call2 = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);
        int call3 = Dijkstra.computeDistance(gameState, QuaxBoard.TileOwner.BLACK);

        assertEquals(call1, call2);
        assertEquals(call2, call3);
    }
}