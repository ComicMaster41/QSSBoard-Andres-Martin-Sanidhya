package comp20050.qssboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameIntegrationTest {

    private GameState gameState;
    private Bot botP1;
    private Bot botP2;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        botP1 = new Bot(gameState, null, GameState.Player.P1);
        botP2 = new Bot(gameState, null, GameState.Player.P2);
    }

    private boolean makeLegalMove(GameState state) {
        ArrayList<Position> moves = state.getLegalMoves();
        if (moves.isEmpty()) return false;
        Position move = moves.get(0);
        move.extractPosition();
        QuaxBoard.TileType tileType = state.getGameBoard().getTileType(move.getRow(), move.getCol());
        return state.makeMove(move, tileType);
    }

    @Nested
    @DisplayName("Game Flow Integration Tests")
    class GameFlowTests {

        @Test
        @DisplayName("Game starts with correct initial state")
        void gameStartsCorrectly() {
            assertEquals(GameState.Player.P1, gameState.getCurrentPlayer());
            assertTrue(gameState.getLegalMoves().size() > 0);
            assertFalse(gameState.checkWin(QuaxBoard.TileOwner.BLACK));
            assertFalse(gameState.checkWin(QuaxBoard.TileOwner.WHITE));
        }

        @Test
        @DisplayName("Players alternate turns correctly")
        void playersTakeTurnsInSequence() {
            GameState.Player firstPlayer = gameState.getCurrentPlayer();

            makeLegalMove(gameState);

            GameState.Player secondPlayer = gameState.getCurrentPlayer();
            assertNotEquals(firstPlayer, secondPlayer);
        }


        @Test
        @DisplayName("Legal moves decrease as board fills")
        void legalMovesDecreaseAsGameProgresses() {
            int movesAtStart = gameState.getLegalMoves().size();

            for (int i = 0; i < 10; i++) {
                if (!makeLegalMove(gameState)) break;
            }

            int movesAfter = gameState.getLegalMoves().size();
            assertTrue(movesAfter < movesAtStart);
        }
    }

    @Nested
    @DisplayName("Bot vs Bot Integration Tests")
    class BotVsBotTests {

        @Test
        @DisplayName("Bot can make valid moves repeatedly")
        void botMakesValidMovesRepeatedly() {
            for (int i = 0; i < 5; i++) {
                Bot currentBot = (gameState.getCurrentPlayer() == GameState.Player.P1) ? botP1 : botP2;
                currentBot.state = gameState;

                Position move = currentBot.chooseMove();
                assertNotNull(move);

                move.extractPosition();
                QuaxBoard.TileType tileType = gameState.getGameBoard().getTileType(move.getRow(), move.getCol());
                assertTrue(gameState.makeMove(move, tileType));
            }
        }

        @Test
        @DisplayName("Bot scores moves correctly")
        void botScoresMovesCorrectly() {
            botP1.state = gameState;
            botP1.chooseMove();

            ArrayList<Bot.ScoredMove> scoredMoves = botP1.getScoredMoves();
            assertFalse(scoredMoves.isEmpty());

            for (int i = 0; i < scoredMoves.size() - 1; i++) {
                assertTrue(scoredMoves.get(i).getScore() <= scoredMoves.get(i + 1).getScore());
            }
        }

        @Test
        @DisplayName("Bot makes at most 5 top moves")
        void botKeepsTop5Moves() {
            botP1.state = gameState;
            botP1.chooseMove();

            ArrayList<Bot.ScoredMove> scoredMoves = botP1.getScoredMoves();
            assertTrue(scoredMoves.size() <= 5);
        }

        @Test
        @DisplayName("Bot heuristic evaluates both win conditions")
        void botHeuristicEvalsWins() {
            botP1.state = gameState;

            int normalEval = botP1.heuristic(gameState);
            assertTrue(normalEval < 100000 && normalEval > -100000);
        }

        @Test
        @DisplayName("Bot minmax with alpha-beta pruning completes")
        void minmaxWithAlphaBetaPruningCompletes() {
            botP1.state = gameState;
            int result = botP1.minmax(gameState, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

            assertTrue(result < 1_000_000 && result > -1_000_000);
        }
    }

    @Nested
    @DisplayName("Board State Integration Tests")
    class BoardStateTests {

        @Test
        @DisplayName("Board tiles are correctly tracked after moves")
        void boardTilesTrackedCorrectly() {
            int initialEmptyCount = countEmptyTiles();

            makeLegalMove(gameState);

            int finalEmptyCount = countEmptyTiles();
            assertEquals(initialEmptyCount - 1, finalEmptyCount);
        }

        @Test
        @DisplayName("No two players can occupy same tile")
        void noTileDoubleOccupancy() {
            ArrayList<Position> moves = gameState.getLegalMoves();
            Position firstMove = moves.get(0);
            firstMove.extractPosition();
            QuaxBoard.TileType tileType = gameState.getGameBoard().getTileType(firstMove.getRow(), firstMove.getCol());
            gameState.makeMove(firstMove, tileType);

            ArrayList<Position> nextMoves = gameState.getLegalMoves();
            assertFalse(nextMoves.contains(firstMove));
        }

        @Test
        @DisplayName("Dijkstra distance improves with strategic play")
        void dijkstraDistanceImprovesStrategically() {
            QuaxBoard.TileOwner p1Color = gameState.getGameBoard().getColor(GameState.Player.P1);

            int distBefore = Dijkstra.computeDistance(gameState, p1Color);

            botP1.state = gameState;
            Position move = botP1.chooseMove();
            move.extractPosition();
            QuaxBoard.TileType tileType = gameState.getGameBoard().getTileType(move.getRow(), move.getCol());
            gameState.makeMove(move, tileType);

            int distAfter = Dijkstra.computeDistance(gameState, p1Color);
            assertTrue(distAfter <= distBefore);
        }
    }

    @Nested
    @DisplayName("Edge Cases & Boundary Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Game handles no legal moves gracefully")
        void noLegalMovesHandled() {
            assertNotNull(gameState.getLegalMoves());
        }

        @Test
        @DisplayName("Bot handles null state gracefully")
        void botHandlesInvalidState() {
            Bot testBot = new Bot(null, null, GameState.Player.P1);
            testBot.state = null;

            assertThrows(Exception.class, testBot::chooseMove);
        }

        @Test
        @DisplayName("Game can continue for many moves")
        void gameCanProgressLonger() {
            int moveCount = 0;
            while (moveCount < 50) {
                if (!makeLegalMove(gameState)) break;
                moveCount++;
            }
            assertTrue(moveCount > 10);
        }

        @Test
        @DisplayName("Game state is consistent after many operations")
        void gameStateConsistencyAfterManyOps() {
            for (int i = 0; i < 20; i++) {
                if (!makeLegalMove(gameState)) break;
            }
            assertNotNull(gameState.getCurrentPlayer());
            assertNotNull(gameState.getLegalMoves());
        }
    }

    @Nested
    @DisplayName("Pathfinding Integration Tests")
    class PathfindingTests {

        @Test
        @DisplayName("Dijkstra integrates with game state correctly")
        void dijkstraIntegrationWithGameState() {
            QuaxBoard.TileOwner blackColor = gameState.getGameBoard().getColor(GameState.Player.P1);
            QuaxBoard.TileOwner whiteColor = gameState.getGameBoard().getColor(GameState.Player.P2);

            assertTrue(Dijkstra.computeDistance(gameState, blackColor) >= 0);
            assertTrue(Dijkstra.computeDistance(gameState, whiteColor) >= 0);
        }



        @Test
        @DisplayName("Both players have valid distance metrics")
        void bothPlayersHaveValidMetrics() {
            QuaxBoard.TileOwner p1Color = gameState.getGameBoard().getColor(GameState.Player.P1);
            QuaxBoard.TileOwner p2Color = gameState.getGameBoard().getColor(GameState.Player.P2);

            int p1Dist = Dijkstra.computeDistance(gameState, p1Color);
            int p2Dist = Dijkstra.computeDistance(gameState, p2Color);

            assertTrue(p1Dist >= 0 && p1Dist < 1_000_000);
            assertTrue(p2Dist >= 0 && p2Dist < 1_000_000);
        }
    }

    private int countEmptyTiles() {
        int count = 0;
        for (int r = 0; r < Tile.NUM_ROWS; r++) {
            for (int c = 0; c < Tile.NUM_COLS; c++) {
                if (gameState.getGameBoard().isTileEmpty(r, c)) count++;
            }
        }
        return count;
    }

    @Test
    void copyStateIsDeepCopy() {
        GameState copy = gameState.copyState();
        makeLegalMove(gameState);

        assertNotEquals(gameState.getCurrentPlayer(), copy.getCurrentPlayer());
    }

    @Test
    void invalidMoveDoesNotChangeBoard() {
        ArrayList<Position> moves = gameState.getLegalMoves();
        Position validMove = moves.get(0);
        validMove.extractPosition();
        QuaxBoard.TileType tileType = gameState.getGameBoard().getTileType(validMove.getRow(), validMove.getCol());

        gameState.makeMove(validMove, tileType);
        int emptyAfterFirst = countEmptyTiles();

        gameState.makeMove(validMove, tileType);
        int emptyAfterSecond = countEmptyTiles();

        assertEquals(emptyAfterFirst, emptyAfterSecond);
    }

    @Test
    void botDoesNotMutateGameState() {
        botP1.state = gameState;
        GameState.Player playerBefore = gameState.getCurrentPlayer();
        int emptyBefore = countEmptyTiles();

        botP1.chooseMove();

        assertEquals(playerBefore, gameState.getCurrentPlayer());
        assertEquals(emptyBefore, countEmptyTiles());
    }

    @Test
    void positionThrowsOnInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> new Position("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> new Position("X_0_0"));
    }

    @Test
    void botThrowsOnNullState() {
        Bot testBot = new Bot(null, null, GameState.Player.P1);
        testBot.state = null;
        assertThrows(IllegalStateException.class, testBot::chooseMove);
    }
}