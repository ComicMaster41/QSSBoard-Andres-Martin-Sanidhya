package comp20050.qssboard;

import java.util.ArrayList;

public class GameState {
    public enum Player { P1, P2 }

    private static final int[][] OCTAGON_DIRECTIONS = {
            {-1, 0}, {1, 0},
            {0, -2}, {0, 2},
            {-1, -1}, {-1, 1},
            {0, -1}, {0, 1}
    };

    private static final int[][] RHOMBUS_DIRECTIONS = {
            {0, -1}, {0, 1},
            {1, -1}, {1, 1}
    };

    private Player currentPlayer;
    private QuaxBoard gameBoard;

    public GameState() {
        this(new QuaxBoard(), Player.P1);
    }

    public GameState(QuaxBoard gameBoard, Player currentPlayer) {
        this.gameBoard = gameBoard;
        this.currentPlayer = currentPlayer;
    }

    public QuaxBoard getGameBoard() {
        return gameBoard;
    }

    public void setCurrentPlayer(GameState.Player player) {
        this.currentPlayer = player;
    }

    public boolean makeMove(Position pos, QuaxBoard.TileType tileType) {
        pos.extractPosition();
        int row = pos.getRow();
        int col = pos.getCol();

        if (!gameBoard.isMoveValid(row, col, tileType)) return false;

        gameBoard.makeMove(row, col, currentPlayer, tileType);
        switchPlayerTurn();
        return true;
    }

    public boolean checkWin(QuaxBoard.TileOwner colour) {
        boolean[][] visited = new boolean[Tile.NUM_ROWS][Tile.NUM_COLS];
        return colour == QuaxBoard.TileOwner.BLACK
                ? hasPathFromTopEdge(colour, visited)
                : hasPathFromLeftEdge(colour, visited);
    }

    private boolean hasPathFromTopEdge(QuaxBoard.TileOwner colour, boolean[][] visited) {
        for (int col = 0; col < Tile.NUM_COLS; col++) {
            if (isStartingTile(0, col, colour) && dfs(0, col, colour, visited)) return true;
        }
        return false;
    }

    private boolean hasPathFromLeftEdge(QuaxBoard.TileOwner colour, boolean[][] visited) {
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            if (isStartingTile(row, 0, colour) && dfs(row, 0, colour, visited)) return true;
        }
        return false;
    }

    private boolean isStartingTile(int row, int col, QuaxBoard.TileOwner colour) {
        return gameBoard.getTileOwner(row, col) == colour
                && gameBoard.getTileType(row, col) == QuaxBoard.TileType.OCTAGON;
    }

    private boolean dfs(int row, int col, QuaxBoard.TileOwner colour, boolean[][] visited) {
        if (hasReachedGoalEdge(row, col, colour)) return true;

        visited[row][col] = true;

        for (int[] neighbour : getNeighbours(row, col)) {
            int neighbourRow = neighbour[0];
            int neighbourCol = neighbour[1];
            if (visited[neighbourRow][neighbourCol]) continue;
            if (gameBoard.getTileOwner(neighbourRow, neighbourCol) != colour) continue;
            if (dfs(neighbourRow, neighbourCol, colour, visited)) return true;
        }
        return false;
    }

    private boolean hasReachedGoalEdge(int row, int col, QuaxBoard.TileOwner colour) {
        if (gameBoard.getTileType(row, col) != QuaxBoard.TileType.OCTAGON) return false;
        if (colour == QuaxBoard.TileOwner.BLACK) return row == Tile.NUM_ROWS - 1;
        return col == Tile.NUM_COLS - 1;
    }

    public ArrayList<Position> getLegalMoves() {
        ArrayList<Position> moves = findMovesAdjacentToOccupiedTiles();
        if (moves.isEmpty()) return findAllEmptyTiles();
        return moves;
    }

    private ArrayList<Position> findMovesAdjacentToOccupiedTiles() {
        ArrayList<Position> moves = new ArrayList<>();
        boolean[][] seen = new boolean[Tile.NUM_ROWS][Tile.NUM_COLS];
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                if (gameBoard.isTileEmpty(row, col)) continue;
                collectEmptyNeighboursAsMoves(row, col, moves, seen);
            }
        }
        return moves;
    }

    private void collectEmptyNeighboursAsMoves(int row, int col, ArrayList<Position> moves, boolean[][] seen) {
        for (int[] neighbour : getNeighbours(row, col)) {
            int neighbourRow = neighbour[0];
            int neighbourCol = neighbour[1];
            if (!gameBoard.isTileEmpty(neighbourRow, neighbourCol)) continue;
            if (seen[neighbourRow][neighbourCol]) continue;
            seen[neighbourRow][neighbourCol] = true;
            moves.add(positionFor(neighbourRow, neighbourCol));
        }
    }

    private ArrayList<Position> findAllEmptyTiles() {
        ArrayList<Position> moves = new ArrayList<>();
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                if (gameBoard.isTileEmpty(row, col)) moves.add(positionFor(row, col));
            }
        }
        return moves;
    }

    private Position positionFor(int row, int col) {
        String prefix = gameBoard.getTileType(row, col) == QuaxBoard.TileType.RHOMBUS ? "R" : "O";
        return new Position(prefix + "_" + row + "_" + col);
    }

    public ArrayList<int[]> getNeighbours(int row, int col) {
        int[][] directions = gameBoard.getTileType(row, col) == QuaxBoard.TileType.OCTAGON
                ? OCTAGON_DIRECTIONS
                : RHOMBUS_DIRECTIONS;
        return collectInBoundsNeighbours(row, col, directions);
    }

    private ArrayList<int[]> collectInBoundsNeighbours(int row, int col, int[][] directions) {
        ArrayList<int[]> neighbours = new ArrayList<>();
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (gameBoard.isInBounds(newRow, newCol)) {
                neighbours.add(new int[]{newRow, newCol});
            }
        }
        return neighbours;
    }

    public void switchPlayerTurn() {
        currentPlayer = currentPlayer == Player.P1 ? Player.P2 : Player.P1;
    }

    public GameState copyState() {
        return new GameState(gameBoard.copyBoard(), currentPlayer);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}