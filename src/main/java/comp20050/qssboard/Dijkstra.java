package comp20050.qssboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private static final int INF = 1_000_000;

    // Pulled from Claude, which does the same functionality as computeDistance, just the return type is differnet
    public static ArrayList<Position> computePath(GameState simState, QuaxBoard.TileOwner colour) {
        int rows = Tile.NUM_ROWS;
        int cols = Tile.NUM_COLS;

        int[][] dist = new int[rows][cols];
        int[][] prevRow = new int[rows][cols];
        int[][] prevCol = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dist[i][j] = INF;
                prevRow[i][j] = -1;
                prevCol[i][j] = -1;
            }
        }

        PriorityQueue<Bot.Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));

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

        int goalRow = -1;
        int goalCol = -1;

        while (!pq.isEmpty()) {
            Bot.Node cur = pq.poll();

            if (cur.dist != dist[cur.row][cur.col]) continue;

            if ((colour == QuaxBoard.TileOwner.BLACK && cur.row == rows - 1) ||
                    (colour == QuaxBoard.TileOwner.WHITE && cur.col == cols - 1)) {
                goalRow = cur.row;
                goalCol = cur.col;
                break;
            }

            for (int[] n : simState.getNeighbours(cur.row, cur.col)) {
                int nr = n[0];
                int nc = n[1];

                int stepCost = tileCost(simState, nr, nc, colour);
                if (stepCost >= INF) continue;

                int newDist = cur.dist + stepCost;

                if (newDist < dist[nr][nc]) {
                    dist[nr][nc] = newDist;
                    prevRow[nr][nc] = cur.row;
                    prevCol[nr][nc] = cur.col;
                    pq.add(new Bot.Node(nr, nc, newDist));
                }
            }
        }

        ArrayList<Position> path = new ArrayList<>();
        if (goalRow == -1) return path;

        int curRow = goalRow;
        int curCol = goalCol;

        while (curRow != -1 && curCol != -1) {
            Tile tile = simState.game_board.getTile(curRow, curCol);
            String prefix = (tile.type == QuaxBoard.TileType.RHOMBUS) ? "R" : "O";
            path.add(new Position(prefix + "_" + curRow + "_" + curCol));
            int nextRow = prevRow[curRow][curCol];
            int nextCol = prevCol[curRow][curCol];
            curRow = nextRow;
            curCol = nextCol;
        }

        java.util.Collections.reverse(path);
        return path;
    }

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
