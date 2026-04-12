package comp20050.qssboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Bot {
    public GameState state;

    public class ScoredMove {
        public Position move;
        public int score;
        ArrayList<Position> path;

        public ScoredMove(Position move, int score, ArrayList<Position> path) {
            this.move = move;
            this.score = score;
            this.path = path;
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

    public Bot(GameState state) {
        this.state = state;
    }

    public ArrayList<ScoredMove> getScoredMoves() {return scoredMoves;}

    public void setBestmove(Position bestMove) {this.bestMove = bestMove;}

    public Position makeMove() {
        ArrayList<Position> legalMoves = state.getLegalMoves();

        if (legalMoves.isEmpty()) {
            return null;
        }

        int bestValue = Integer.MIN_VALUE;
        int depth = 2; // or 2, increasing depth means it looks ahead more

        scoredMoves.clear();
        for (Position move : legalMoves) {
            GameState child = state.copyState();
            applyMove(child, move);

            int eval = minmax(child, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            int dist = Dijkstra.computeDistance(child, child.game_board.p2Color); // Slows the game down
            ArrayList<Position> path = Dijkstra.computePath(child, child.game_board.p2Color);
            scoredMoves.add(new ScoredMove(move, dist, path));
            if (eval > bestValue) {
                bestValue = eval;
                setBestmove(move);
            }

        }

        scoredMoves.sort(Comparator.comparingInt((Bot.ScoredMove a) -> a.score));
        scoredMoves = new ArrayList<>(scoredMoves.subList(0, Math.min(5, scoredMoves.size())));

        return bestMove;
    }

    public int heuristic(GameState simState) {
        QuaxBoard.TileOwner botColor = simState.game_board.p2Color;
        QuaxBoard.TileOwner playerColor = simState.game_board.p1Color;

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
