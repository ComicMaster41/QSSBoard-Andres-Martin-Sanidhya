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

    public class ScoredMove {
        public Position move;
        public int score;

        public ScoredMove(Position move, int score) {
            this.move = move;
            this.score = score;
        }
    };

    static class Node {
        int row;
        int col;
        int dist;

        Node(int row, int col, int dist) {
            this.row = row;
            this.col = col;
            this.dist = dist;
        }
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

    public ArrayList<ScoredMove> getScoredMoves() {return scoredMoves;}

    public void setBestmove(Position bestMove) {this.bestMove = bestMove;}

    public Position makeMove() {
        ArrayList<Position> legalMoves = state.getLegalMoves();


        if (legalMoves.isEmpty()) {
            return null;
        }

        Position bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int depth = 2; // or 2, increasing depth means it looks ahead more

        scoredMoves.clear();
        for (Position move : legalMoves) {
            GameState child = state.copyState();
            applyMove(child, move);

            int eval = minmax(child, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            int dist = Dijkstra.computeDistance(child,  child.game_board.getColor(botPlayer)); // Slows the game down
            scoredMoves.add(new ScoredMove(move, dist));
            if (eval > bestValue) {
                bestValue = eval;
                setBestmove(move);
            }

        }

        scoredMoves.sort(Comparator.comparingInt((Bot.ScoredMove a) -> a.score));
        scoredMoves = new ArrayList<>(scoredMoves.subList(0, Math.min(5, scoredMoves.size())));

        return this.bestMove;
    }

    public boolean decideToPressPie() {
        int row = lastMoveMadeId.getRow();
        int col = lastMoveMadeId.getCol();

        // if the first move made by Black is anywhere on the edge of the board -> then bot should press the pie button
        if (row == 0 || row == 10 || col == 0 || col == 20) {
            return true;
        }
        return false;
    }

    public int heuristic(GameState simState) {
        QuaxBoard.TileOwner botColor = simState.game_board.getColor(botPlayer);
        QuaxBoard.TileOwner playerColor = simState.game_board.getColor(opponent(botPlayer));

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

        QuaxBoard.TileType tileType = simState.game_board.getTileType(row, col);
        simState.makeMove(move, tileType);
    }

    public int minmax(GameState simState, int depth, int alpha, int beta, boolean isMax) {
        if (depth == 0 ) { // LATER: check terminal wins, || simState.getLegalMoves().isEmpty()
            return heuristic(simState);
        }
        ArrayList<Position> legalMoves = simState.getLegalMoves();


        if (isMax) {
            int bestValue = Integer.MIN_VALUE;

            for (Position move : legalMoves) {

                GameState child = simState.copyState();

                // Make sure that it's doing a deep copy and not a shallow copy. So we need to find what kind of copy it's doing
                applyMove(child, move);

                int eval = minmax(child, depth - 1, alpha, beta, false);
                // System.out.println("Depth " + depth + " moves: " + legalMoves.size());
                bestValue = Math.max(bestValue, eval);
                alpha = Math.max(alpha, bestValue);

                if (beta <= alpha) break;
            }

            return bestValue;
        }

        // maybe we can check the max inside so we don't do the copy twice if we're in max?
        else {
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
