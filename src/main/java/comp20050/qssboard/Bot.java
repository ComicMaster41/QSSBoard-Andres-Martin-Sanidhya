package comp20050.qssboard;

import java.util.ArrayList;
import java.util.Comparator;

public class Bot {
    private static final int WIN_SCORE = 1_000_000;
    private static final int LOSS_SCORE = -1_000_000;
    private static final int SEARCH_DEPTH = 2;
    private static final int TOP_MOVES_TO_KEEP = 5;

    private final GameState.Player botPlayer;
    private GameState state;
    private Position lastMoveMadeId;
    private ArrayList<ScoredMove> scoredMoves = new ArrayList<>();
    private Position bestMove;

    public Bot(GameState.Player botPlayer) {
        this.botPlayer = botPlayer;
    }

    public Bot(GameState state, Position lastMoveMadeId, GameState.Player botPlayer) {
        this(botPlayer);
        this.state = state;
        this.lastMoveMadeId = lastMoveMadeId;
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
        private final int row;
        private final int col;
        private final int dist;

        Node(int row, int col, int dist) {
            this.row = row;
            this.col = col;
            this.dist = dist;
        }

        public int getRow() { return row; }
        public int getCol() { return col; }
        public int getDist() { return dist; }
    }

    private static GameState.Player opponent(GameState.Player player) {
        return player == GameState.Player.P1 ? GameState.Player.P2 : GameState.Player.P1;
    }

    public ArrayList<ScoredMove> getScoredMoves() {
        return scoredMoves;
    }

    public Position chooseMove() {
        ArrayList<Position> legalMoves = state.getLegalMoves();
        if (legalMoves.isEmpty()) return null;

        scoredMoves.clear();
        bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (Position move : legalMoves) {
            int eval = evaluateMove(move);
            if (bestMove == null || eval > bestValue) {
                bestValue = eval;
                bestMove = move;
            }
        }

        keepTopScoredMoves();
        return bestMove;
    }

    private int evaluateMove(Position move) {
        GameState child = state.copyState();
        applyMove(child, move);
        int eval = negamax(child, SEARCH_DEPTH - 1, -WIN_SCORE, WIN_SCORE, -1);
        int distance = Dijkstra.computeDistance(child, child.getGameBoard().getColor(botPlayer));
        scoredMoves.add(new ScoredMove(move, distance));
        return eval;
    }

    private void keepTopScoredMoves() {
        scoredMoves.sort(Comparator.comparingInt(ScoredMove::getScore));
        int keep = Math.min(TOP_MOVES_TO_KEEP, scoredMoves.size());
        scoredMoves = new ArrayList<>(scoredMoves.subList(0, keep));
    }

    public boolean decideToPressPie() {
        int row = lastMoveMadeId.getRow();
        int col = lastMoveMadeId.getCol();
        return !isLastMoveOnEdgeOrRhombus(row, col);
    }

    private boolean isLastMoveOnEdgeOrRhombus(int row, int col) {
        boolean onTopOrBottomEdge = row == 0 || row == Tile.NUM_ROWS - 1;
        boolean onLeftOrRightEdge = col == 0 || col == Tile.NUM_COLS - 1;
        boolean onRhombusColumn = col % 2 != 0;
        return onTopOrBottomEdge || onLeftOrRightEdge || onRhombusColumn;
    }

    private int heuristic(GameState simState) {
        QuaxBoard.TileOwner botColor = simState.getGameBoard().getColor(botPlayer);
        QuaxBoard.TileOwner opponentColor = simState.getGameBoard().getColor(opponent(botPlayer));

        if (simState.checkWin(botColor)) return WIN_SCORE;
        if (simState.checkWin(opponentColor)) return LOSS_SCORE;

        int myDistance = Dijkstra.computeDistance(simState, botColor);
        int opponentDistance = Dijkstra.computeDistance(simState, opponentColor);
        return opponentDistance - myDistance;
    }

    private void applyMove(GameState simState, Position move) {
        move.extractPosition();
        int row = move.getRow();
        int col = move.getCol();
        QuaxBoard.TileType tileType = simState.getGameBoard().getTileType(row, col);
        simState.makeMove(move, tileType);
    }

    private int negamax(GameState simState, int depth, int alpha, int beta, int colorSign) {
        if (depth == 0) return colorSign * heuristic(simState);

        ArrayList<Position> legalMoves = simState.getLegalMoves();
        if (legalMoves.isEmpty()) return colorSign * heuristic(simState);

        int bestValue = -WIN_SCORE;
        for (Position move : legalMoves) {
            int eval = -negamaxChild(simState, move, depth, alpha, beta, colorSign);
            bestValue = Math.max(bestValue, eval);
            alpha = Math.max(alpha, bestValue);
            if (alpha >= beta) break;
        }
        return bestValue;
    }

    private int negamaxChild(GameState simState, Position move, int depth, int alpha, int beta, int colorSign) {
        GameState child = simState.copyState();
        applyMove(child, move);
        return negamax(child, depth - 1, -beta, -alpha, -colorSign);
    }
}