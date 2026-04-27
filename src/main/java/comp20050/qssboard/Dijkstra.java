package comp20050.qssboard;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private static final int INF = 1_000_000;
    private static final int OWN_TILE_COST = 0;
    private static final int EMPTY_TILE_COST = 1;
    private static final int BLOCKED = INF;

    private static class SearchContext {
        final GameState state;
        final QuaxBoard.TileOwner colour;
        final int[][] distance;
        final PriorityQueue<Bot.Node> queue;

        SearchContext(GameState state, QuaxBoard.TileOwner colour) {
            this.state = state;
            this.colour = colour;
            this.distance = initialiseDistanceGrid();
            this.queue = new PriorityQueue<>(Comparator.comparingInt(Bot.Node::getDist));
        }
    }

    public static int computeDistance(GameState simState, QuaxBoard.TileOwner colour) {
        SearchContext ctx = new SearchContext(simState, colour);
        seedStartingEdge(ctx);
        return runDijkstra(ctx);
    }

    private static int[][] initialiseDistanceGrid() {
        int[][] distance = new int[Tile.NUM_ROWS][Tile.NUM_COLS];
        for (int[] row : distance) Arrays.fill(row, INF);
        return distance;
    }

    private static void seedStartingEdge(SearchContext ctx) {
        if (ctx.colour == QuaxBoard.TileOwner.BLACK) {
            for (int col = 0; col < Tile.NUM_COLS; col += 2) {
                seedTile(ctx, 0, col);
            }
            // Flood fill BLACK's connected component from row 0
            floodFillOwnedTiles(ctx, true);
        } else {
            for (int row = 0; row < Tile.NUM_ROWS; row++) {
                seedTile(ctx, row, 0);
            }
            // Flood fill WHITE's connected component from col 0
            floodFillOwnedTiles(ctx, false);
        }
    }

    private static void floodFillOwnedTiles(SearchContext ctx, boolean isBlack) {
        // Keep expanding through owned tiles at cost 0
        // using a simple BFS through the already-seeded queue entries
        PriorityQueue<Bot.Node> tempQueue = new PriorityQueue<>(
                Comparator.comparingInt(Bot.Node::getDist));

        // Find all already-seeded tiles that are owned
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                if (ctx.distance[row][col] == 0) {
                    tempQueue.add(new Bot.Node(row, col, 0));
                }
            }
        }

        // Expand through connected owned tiles
        while (!tempQueue.isEmpty()) {
            Bot.Node current = tempQueue.poll();
            for (int[] neighbour : ctx.state.getNeighbours(
                    current.getRow(), current.getCol())) {
                int nr = neighbour[0];
                int nc = neighbour[1];
                if (ctx.distance[nr][nc] == 0) continue; // already seeded
                QuaxBoard.TileOwner owner =
                        ctx.state.getGameBoard().getTileOwner(nr, nc);
                if (owner == ctx.colour) {
                    ctx.distance[nr][nc] = 0;
                    ctx.queue.add(new Bot.Node(nr, nc, 0));
                    tempQueue.add(new Bot.Node(nr, nc, 0));
                }
            }
        }
    }

    private static void seedTile(SearchContext ctx, int row, int col) {
        int cost = tileCost(ctx, row, col);
        if (cost >= BLOCKED) return;
        ctx.distance[row][col] = cost;
        ctx.queue.add(new Bot.Node(row, col, cost));
    }

    private static int runDijkstra(SearchContext ctx) {
        while (!ctx.queue.isEmpty()) {
            Bot.Node current = ctx.queue.poll();
            if (isStaleEntry(ctx, current)) continue;
            if (hasReachedGoalEdge(ctx, current)) return current.getDist();
            relaxNeighbours(ctx, current);
        }
        return INF;
    }

    private static boolean isStaleEntry(SearchContext ctx, Bot.Node current) {
        return current.getDist() != ctx.distance[current.getRow()][current.getCol()];
    }

    private static boolean hasReachedGoalEdge(SearchContext ctx, Bot.Node current) {
        if (ctx.colour == QuaxBoard.TileOwner.BLACK) return current.getRow() == Tile.NUM_ROWS - 1;
        return current.getCol() == Tile.NUM_COLS - 1;
    }

    private static void relaxNeighbours(SearchContext ctx, Bot.Node current) {
        for (int[] neighbour : ctx.state.getNeighbours(current.getRow(), current.getCol())) {
            relaxNeighbour(ctx, current, neighbour[0], neighbour[1]);
        }
    }

    private static void relaxNeighbour(SearchContext ctx, Bot.Node current, int row, int col) {
        int stepCost = tileCost(ctx, row, col);
        if (stepCost >= BLOCKED) return;

        int newDistance = current.getDist() + stepCost;
        if (newDistance >= ctx.distance[row][col]) return;

        ctx.distance[row][col] = newDistance;
        ctx.queue.add(new Bot.Node(row, col, newDistance));
    }

    private static int tileCost(SearchContext ctx, int row, int col) {
        QuaxBoard.TileOwner owner = ctx.state.getGameBoard().getTileOwner(row, col);
        if (owner == ctx.colour) return OWN_TILE_COST;
        if (ctx.state.getGameBoard().isTileEmpty(row, col)) return EMPTY_TILE_COST;
        return BLOCKED;
    }
}