package comp20050.qssboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Bot {
    public GameState state;
    private Position lastMoveMadeId;
    private final GameState.Player botPlayer;
    private static final int INF = 1_000_000;

    public Bot(GameState.Player botPlayer) {
        this.botPlayer = botPlayer;
    }

    public static class ScoredMove {
        private final Position move;
        private final int score;

        public ScoredMove(Position move, int score) {
            this.move = move;
            this.score = score;
        }

        public Position getMove() { return move; }
        public int getScore() { return score; }
    }

    static class Node {
        final int row;
        final int col;
        final int dist;

        Node(int row, int col, int dist) {
            this.row = row;
            this.col = col;
            this.dist = dist;
        }

        public int getRow() { return row; }
        public int getCol() { return col; }
        public int getDist() { return dist; }
    }

    private ArrayList<ScoredMove> scoredMoves = new ArrayList<>();
    private Position bestMove;

    public Bot(GameState state, Position lastMoveMadeId, GameState.Player botPlayer) {
        this.state = state;
        this.lastMoveMadeId = lastMoveMadeId;
        this.botPlayer = botPlayer;
    }

    private static GameState.Player opponent(GameState.Player p) {
        return p == GameState.Player.P1 ? GameState.Player.P2 : GameState.Player.P1;
    }

    public ArrayList<ScoredMove> getScoredMoves() { return scoredMoves; }

    public void setBestmove(Position bestMove) { this.bestMove = bestMove; }

    public Position chooseMove() {
        if (state == null) {
            throw new IllegalStateException("Bot cannot choose a move: game state is null.");
        }

        ArrayList<Position> legalMoves = state.getLegalMoves();

        if (legalMoves.isEmpty()) return null;

        int bestValue = Integer.MIN_VALUE;
        int depth = 2;

        scoredMoves.clear();
        setBestmove(null);
        for (Position move : legalMoves) {
            GameState child = state.copyState();
            applyMove(child, move);

            int eval = minmax(child, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            int dist = Dijkstra.computeDistance(child, child.getGameBoard().getColor(botPlayer));
            scoredMoves.add(new ScoredMove(move, dist));
            if (this.bestMove == null || eval > bestValue) {
                bestValue = eval;
                setBestmove(move);
            }
        }

        scoredMoves.sort(Comparator.comparingInt(ScoredMove::getScore));
        scoredMoves = new ArrayList<>(scoredMoves.subList(0, Math.min(5, scoredMoves.size())));

        return this.bestMove;
    }

    public boolean decideToPressPie() {
        int row = lastMoveMadeId.getRow();
        int col = lastMoveMadeId.getCol();

        if (row == 0 || row == 10 || col == 0 || col == 20 || col % 2 != 0) {
            return false;
        }
        return true;
    }

    public int heuristic(GameState simState) {
        QuaxBoard.TileOwner botColor = simState.getGameBoard().getColor(botPlayer);
        QuaxBoard.TileOwner playerColor = simState.getGameBoard().getColor(opponent(botPlayer));

        if (simState.checkWin(botColor)) return 100000;
        if (simState.checkWin(playerColor)) return -100000;

        int myDist = Dijkstra.computeDistance(simState, botColor);
        int oppDist = Dijkstra.computeDistance(simState, playerColor);

        return oppDist - myDist;
    }

    public void applyMove(GameState simState, Position move) {
        move.extractPosition();
        int row = move.getRow();
        int col = move.getCol();

        QuaxBoard.TileType tileType = simState.getGameBoard().getTileType(row, col);
        simState.makeMove(move, tileType);
    }

    public int minmax(GameState simState, int depth, int alpha, int beta, boolean isMax) {
        if (depth == 0) {
            return heuristic(simState);
        }
        ArrayList<Position> legalMoves = simState.getLegalMoves();
        if (legalMoves.isEmpty()) {
            return heuristic(simState);
        }

        if (isMax) {
            int bestValue = Integer.MIN_VALUE;

            for (Position move : legalMoves) {
                GameState child = simState.copyState();
                applyMove(child, move);

                int eval = minmax(child, depth - 1, alpha, beta, false);
                bestValue = Math.max(bestValue, eval);
                alpha = Math.max(alpha, bestValue);

                if (beta <= alpha) break;
            }

            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;

            for (Position move : legalMoves) {
                GameState child = simState.copyState();
                applyMove(child, move);
                int eval = minmax(child, depth - 1, alpha, beta, true);
                bestValue = Math.min(bestValue, eval);
                beta = Math.min(beta, bestValue);

                if (beta <= alpha) break;
            }

            return bestValue;
        }
    }
}