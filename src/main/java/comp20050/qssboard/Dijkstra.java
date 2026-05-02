package comp20050.qssboard;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private static final int INF = 1000000;

    public static int computeDistance(GameState simState, QuaxBoard.TileOwner colour) {
        int rows = Tile.NUM_ROWS;
        int cols = Tile.NUM_COLS;

        int[][] dist = initialiseDist(rows, cols);
        PriorityQueue<Bot.Node> pq = new PriorityQueue<>(Comparator.comparingInt(Bot.Node::getDist));

        seedStartingSide(simState, colour, dist, pq, rows, cols);

        return runDijkstra(simState, colour, dist, pq, rows, cols);
    }

    private static int[][] initialiseDist(int rows, int cols) {
        int[][] dist = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dist[i][j] = INF;
            }
        }
        return dist;
    }

    private static void seedStartingSide(GameState simState, QuaxBoard.TileOwner colour,
                                         int[][] dist, PriorityQueue<Bot.Node> pq,
                                         int rows, int cols) {
        if (colour == QuaxBoard.TileOwner.BLACK) {
            for (int col = 0; col < cols; col++) {
                int cost = tileCost(simState, 0, col, colour);
                if (cost < INF) {
                    dist[0][col] = cost;
                    pq.add(new Bot.Node(0, col, cost));
                }
            }
        } else {
            for (int row = 0; row < rows; row++) {
                int cost = tileCost(simState, row, 0, colour);
                if (cost < INF) {
                    dist[row][0] = cost;
                    pq.add(new Bot.Node(row, 0, cost));
                }
            }
        }
    }

    private static int runDijkstra(GameState simState, QuaxBoard.TileOwner colour,
                                   int[][] dist, PriorityQueue<Bot.Node> pq,
                                   int rows, int cols) {
        while (!pq.isEmpty()) {
            Bot.Node cur = pq.poll();

            if (cur.getDist() == dist[cur.getRow()][cur.getCol()]) {
                if (hasReachedGoal(colour, cur, rows, cols)) return cur.getDist();
                relaxNeighbours(simState, colour, cur, dist, pq);
            }
        }
        return INF;
    }

    private static boolean hasReachedGoal(QuaxBoard.TileOwner colour, Bot.Node cur, int rows, int cols) {
        return (colour == QuaxBoard.TileOwner.BLACK && cur.getRow() == rows - 1)
                || (colour == QuaxBoard.TileOwner.WHITE && cur.getCol() == cols - 1);
    }

    private static void relaxNeighbours(GameState simState, QuaxBoard.TileOwner colour,
                                        Bot.Node cur, int[][] dist, PriorityQueue<Bot.Node> pq) {
        for (int[] n : simState.getNeighbours(cur.getRow(), cur.getCol())) {
            int nr = n[0];
            int nc = n[1];

            int stepCost = tileCost(simState, nr, nc, colour);
            int newDist = cur.getDist() + stepCost;

            if (stepCost < INF && newDist < dist[nr][nc]) {
                dist[nr][nc] = newDist;
                pq.add(new Bot.Node(nr, nc, newDist));
            }
        }
    }

    private static int tileCost(GameState simState, int row, int col, QuaxBoard.TileOwner colour) {
        QuaxBoard.TileOwner owner = simState.getGameBoard().getTileOwner(row, col);

        if (owner == colour) {
            return 0;
        }

        if (simState.getGameBoard().isTileEmpty(row, col)) {
            return 1;
        }

        return INF;
    }
}