package comp20050.qssboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Bot {
    public GameState state;
    private static final int INF = 1_000_000;

    private static class Node {
        int row;
        int col;
        int dist;

        Node(int row, int col, int dist) {
            this.row = row;
            this.col = col;
            this.dist = dist;
        }
    }


    public Bot(GameState state) {
        this.state = state;
    }

    // bot doesnt make move after player
    // board representation can be off. getNeighbour->addValid
    // account for activate pie button

    public Position makeMove() {
        ArrayList<Position> legalMoves = state.getLegalMoves();


        if (legalMoves.isEmpty()) {
            return null;
        }

        Position bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int depth = 2; // or 2, increasing depth means it looks ahead more

        for (Position move : legalMoves) {
            GameState child = state.copyState();
            applyMove(child, move);
            // there was apply here but im thinking it should be in minmax insteead

            int eval = minmax(child, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (eval > bestValue) {
                bestValue = eval;
                bestMove = move;
            }
        }

        return bestMove;
    }

    public int heuristic(GameState simState) {
        QuaxBoard.TileOwner botColor = simState.game_board.p2Color;
        QuaxBoard.TileOwner playerColor = simState.game_board.p1Color;

        if (simState.checkWin(botColor)) return 100000;
        if (simState.checkWin(playerColor)) return -100000;

        int myDist = computeDistance(simState, botColor);
        int oppDist = computeDistance(simState, playerColor);

        return oppDist - myDist;
    }

    private void applyMove(GameState simState, Position move) {
        move.extractPosition();
        int row = move.getRow();
        int col = move.getCol();

        QuaxBoard.TileType tileType = simState.game_board.getTileType(row, col);
        simState.makeMove(move, tileType);
    }

    private int tileCost(GameState simState, int row, int col, QuaxBoard.TileOwner colour) {
        QuaxBoard.TileOwner owner = simState.game_board.getTileOwner(row, col);

        if (owner == colour) {
            return 0;
        }

        if (simState.game_board.isTileEmpty(row, col)) {
            return 1;
        }

        return INF;
    }

    // Dijkstra shortest path:
    // own tile = cost 0
    // empty tile = cost 1
    // opponent tile = blocked
    public int computeDistance(GameState simState, QuaxBoard.TileOwner colour) {
        int rows = Tile.NUM_ROWS;
        int cols = Tile.NUM_COLS;

        int[][] dist = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dist[i][j] = INF;
            }
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));

        // Seed starting side
        if (colour == QuaxBoard.TileOwner.BLACK) {
            // BLACK tries top -> bottom
            for (int col = 0; col < cols; col++) {
                int cost = tileCost(simState, 0, col, colour);
                if (cost < INF) {
                    dist[0][col] = cost;
                    pq.add(new Node(0, col, cost));
                }
            }
        } else {
            // WHITE tries left -> right
            for (int row = 0; row < rows; row++) {
                int cost = tileCost(simState, row, 0, colour);
                if (cost < INF) {
                    dist[row][0] = cost;
                    pq.add(new Node(row, 0, cost));
                }
            }
        }

        while (!pq.isEmpty()) {
            Node cur = pq.poll();

            if (cur.dist != dist[cur.row][cur.col]) {
                continue; // stale entry
            }

            // Goal check
            if (colour == QuaxBoard.TileOwner.BLACK && cur.row == rows - 1) {
                return cur.dist;
            }
            if (colour == QuaxBoard.TileOwner.WHITE && cur.col == cols - 1) {
                return cur.dist;
            }

            for (int[] n : simState.getNeighbours(cur.row, cur.col)) {
                int nr = n[0];
                int nc = n[1];

                int stepCost = tileCost(simState, nr, nc, colour);
                if (stepCost >= INF) continue;

                int newDist = cur.dist + stepCost;

                if (newDist < dist[nr][nc]) {
                    dist[nr][nc] = newDist;
                    pq.add(new Node(nr, nc, newDist));
                }
            }
        }

        return INF;
    }

    // QUESTION: changed simState from GameState to QuaxBoard bc. getValidMoves was in that class
    int minmax(GameState simState, int depth, int alpha, int beta, boolean isMax) {
        if (depth == 0 ) { // LATER: check terminal wins, || simState.getLegalMoves().isEmpty()
            return heuristic(simState);
            // ^^ We have compute distance in evaluate, but I don't see that working for the rest of minmax, making me think that its extra work
        }

        ArrayList<Position> legalMoves = simState.getLegalMoves();

        if (isMax) {
            int bestValue = Integer.MIN_VALUE;

            for (Position move : legalMoves) {
                GameState child = simState.copyState();
                // Make sure that it's doing a deep copy and not a shallow copy. So we need to find what kind of copy it's doing
                applyMove(child, move);

                int eval = minmax(child, depth - 1, alpha, beta, false);
                System.out.println("Depth " + depth + " moves: " + legalMoves.size());
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
