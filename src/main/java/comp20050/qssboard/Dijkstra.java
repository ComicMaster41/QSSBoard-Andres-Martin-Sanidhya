package comp20050.qssboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private static final int INF = 1_000_000;
    // Dijkstra shortest path:
    // own tile = cost 0
    // empty tile = cost 1
    // opponent tile = blocked
    public static int computeDistance(GameState simState, QuaxBoard.TileOwner colour) {
        int rows = Tile.NUM_ROWS;
        int cols = Tile.NUM_COLS;

        int[][] dist = new int[rows][cols];

        // QUESTION: what does this do?
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dist[i][j] = INF;
            }
        }

        PriorityQueue<Bot.Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));

        // Seed starting side
        if (colour == QuaxBoard.TileOwner.BLACK) {
            // BLACK tries top -> bottom
            for (int col = 0; col < cols; col++) {
                int cost = tileCost(simState, 0, col, colour);
                if (cost < INF) {
                    dist[0][col] = cost;
                    pq.add(new Bot.Node(0, col, cost));
                }
            }
        } else {
            // WHITE tries left -> right
            for (int row = 0; row < rows; row++) {
                int cost = tileCost(simState, row, 0, colour);
                if (cost < INF) {
                    dist[row][0] = cost;

                    pq.add(new Bot.Node(row, 0, cost));
                }
            }
        }

        while (!pq.isEmpty()) {
            Bot.Node cur = pq.poll();

            if (cur.dist != dist[cur.row][cur.col]) {
                continue; // stale entry
            }

            for (int[] n : simState.getNeighbours(cur.row, cur.col)) {
                int nr = n[0];
                int nc = n[1];

                int stepCost = tileCost(simState, nr, nc, colour);
                if (stepCost >= INF) continue;

                int newDist = cur.dist + stepCost;

                if (newDist < dist[nr][nc]) {
                    dist[nr][nc] = newDist;
                    pq.add(new Bot.Node(nr, nc, newDist));
                }
            }

            if (colour == QuaxBoard.TileOwner.BLACK && cur.row == rows - 1) {
                return cur.dist;
            }
            if (colour == QuaxBoard.TileOwner.WHITE && cur.col == cols - 1) {
                return cur.dist;
            }
        }

        return INF;
    }

    private static int tileCost(GameState simState, int row, int col, QuaxBoard.TileOwner colour) {
        QuaxBoard.TileOwner owner = simState.game_board.getTileOwner(row, col);

        if (owner == colour) {
            return 0;
        }

        if (simState.game_board.isTileEmpty(row, col)) {
            return 1;
        }

        return INF;
    }
}
