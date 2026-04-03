package comp20050.qssboard;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GameState {
    public enum Player {
        P1,
        P2
    };
    public Player current_player;
    public QuaxBoard game_board = new QuaxBoard(); // allow all classes to access the board
    public boolean[][] visited = new boolean[11][21];

    public GameState() {
        this.game_board = new QuaxBoard();
        this.current_player = Player.P1;
    }

    // QUESTION: Why do we have two constructors?
    public GameState(QuaxBoard game_board, Player current_player) {
        this.game_board = game_board; // QUESTION: wouldn't it be better to put this in quaxboard?
        this.current_player = current_player; // BUG SUGGESTION: changing Player.p1 to currentplayer
    }
    public boolean makeMove(Position pos, QuaxBoard.TileType tileType) { // isMoveValid is called here
        pos.extractPosition();
        int row = pos.getRow();
        int col = pos.getCol();


        if (!game_board.isMoveValid(row, col, tileType)){
            throw new IllegalArgumentException("Invalid move - " + row + ", " + col);
        }

        game_board.makeMove(row, col, current_player, tileType);

        switchPlayerTurn();
        return true;
    }

    public boolean checkWin(QuaxBoard.TileOwner colour) {
        visited = new boolean[Tile.NUM_ROWS][Tile.NUM_COLS]; // RESET HERE

        if (colour == QuaxBoard.TileOwner.BLACK) {

            for (int col = 0; col < Tile.NUM_COLS; col++) {
                if (game_board.getTileOwner(0, col) == colour) { // this tile is occupied by the player, so we start dfs
                    if (dfs(0, col, colour)) {
                        return true;
                    }
                }
            }
        }
        else {
            for (int row = 0; row < Tile.NUM_ROWS; row++) {
                if (game_board.getTileOwner(row, 0) == colour) { // this tile is occupied by the player, so we start dfs
                    if (dfs(row, 0, colour  )) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean dfs(int row, int col, QuaxBoard.TileOwner colour) {
        if (colour == QuaxBoard.TileOwner.BLACK && row == 10) {
            return true;
        }
        if (colour == QuaxBoard.TileOwner.WHITE && col == 20) {
            return true;
        }

        visited[row][col] = true;

        for (int[] n : getNeighbours(row, col) ) {
            int n_row = n[0];
            int n_col = n[1];

            if (!visited[n_row][n_col] && game_board.getTileOwner(n_row, n_col) == colour) {
                if (dfs(n_row, n_col, colour)) {
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<Position> getLegalMoves() {
        ArrayList<Position> moves = new ArrayList<>();

        for (int i = 0; i < Tile.NUM_ROWS; i++) {
            for (int j = 0; j < Tile.NUM_COLS; j++) {
                if (game_board.isTileEmpty(i, j)) {
                    Tile tile = game_board.getTile(i, j);
                    String prefix = (tile.type == QuaxBoard.TileType.RHOMBUS) ? "R" : "O";
                    moves.add(new Position(prefix + "_" + i + "_" + j));
                }
            }
        }

        /*
        * My thoughts: I think that we can get legal moves from a certain chunk of the board, so we iterate over a certain chunk of the map
        * we can consider the entire board but might need to sort or reorder what are good conditions, but then the question becomes
        * how do we sort the legal paths according to the best ones? maybe we can calculate the costs for all the paths and sort by the shortest
        * */

        return moves;
    }

    public ArrayList<int[]> getNeighbours(int row, int col) {
        ArrayList<int[]> neighbours = new ArrayList<>();

        if (game_board.getTileType(row, col) == QuaxBoard.TileType.OCTAGON) {
            // Octagon connections
            int[][] dirs = {
                    {-1, 0}, {1, 0},   // UP, DOWN - OCTAGON
                    {0, -2}, {0, 2},   // LEFT, RIGHT - OCTAGON
                    {-1, -1}, {-1, 1}, // B_LEFT DIAGONAL, B_RIGHT DIAGONAL - RHOMBUS
                    {0, -1}, {0, 1}    // U_LEFT DIAGONAL, U_RIGHT DIAGONAL - RHOMBUS
            };

            addValid(neighbours, row, col, dirs);
        }
        else {
            // Rhombus connections
            int[][] dirs = {
                    {0, -1}, {0, 1}, // B_LEFT, B_RIGHT - OCTAGON
                    {1, -1}, {1, 1}  // U_LEFT, U_RIGHT - OCTAGON
            };

            addValid(neighbours, row, col, dirs);
        }

        return neighbours;
    }
    public void addValid(ArrayList<int[]> neighbours, int row, int col, int[][] directions) {
        for (int i = 0; i < directions.length; i++) {
            int new_row = row + directions[i][0];
            int new_col = col + directions[i][1];

            if (new_row < 0 || new_row > 10 || new_col < 0 || new_col > 20) {
                continue;
            }
            else {
                neighbours.add(new int[]{new_row, new_col});
            }
        }
    }
    public void switchPlayerTurn() {
        current_player = (current_player == Player.P1) ? (Player.P2) : (Player.P1);
    }

    public GameState copyState() {
        QuaxBoard copiedBoard = this.game_board.copyBoard();
        return new GameState(copiedBoard, this.current_player);
    }

    public Player getCurrentPlayer() {
        return current_player;
    }
}
