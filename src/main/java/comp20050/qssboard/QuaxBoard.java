package comp20050.qssboard;

import java.util.ArrayList;

public class QuaxBoard {
    public enum TileType {
        OCTAGON,
        RHOMBUS
    };
    public enum TileOwner { BLACK, WHITE, NONE }
    TileOwner p1Color = TileOwner.BLACK;
    TileOwner p2Color = TileOwner.WHITE;

    private Tile[][] state_board;

    public QuaxBoard() {

        this.state_board = new Tile[11][21];
        initialise_board();
    }

    private void initialise_board() {
        Tile new_tile;
        for (int i = 0; i < Tile.NUM_ROWS; i++) {
            for (int j = 0; j < Tile.NUM_COLS; j++) {
                if (j % 2 == 0) {
                    new_tile =  new Tile(TileType.OCTAGON);
                }
                else {
                    new_tile =  new Tile(TileType.RHOMBUS);
                }
                state_board[i][j] = new_tile;
            }
        }
    }
    TileOwner getColor(GameState.Player player) {
        if (player == GameState.Player.P1) return p1Color;
        else return p2Color;
    }

    public boolean isInBounds(int row, int col) {
        if (row >= 0 && row < Tile.NUM_ROWS && col >= 0 && col < Tile.NUM_COLS)
            return true;
        else return false;
    }

    public boolean isCorrectCellType (int row, int col, TileType t) {
        if (t == TileType.RHOMBUS)
            return col % 2 == 0;
        else return col % 2 != 0;
    }

    public boolean isMoveValid(int row, int col, TileType t) {
        if (t == null) return false;
        // QUESTION: UNSURE IF THIS IS THE SAME THING?
        if (state_board[row][col] == null || state_board[row][col].owner == null) {
            return true;
        }

        else return false;
    }

    public Boolean[][] getValidMoves() {
        Boolean[][] valid_moves = new Boolean[11][21];
        for (int i = 0; i < Tile.NUM_ROWS; i++) {
            for (int j = 0; j < Tile.NUM_COLS; j++) {
                if (state_board[i][j] == null || state_board[i][j].owner == null) {
                    valid_moves[i][j] = true;
                }
                else {
                    valid_moves[i][j] = false;
                }
            }
        }
        return valid_moves;
    }

    public QuaxBoard copyBoard() {
        QuaxBoard cpyBoard = new QuaxBoard();

    }

    public void makeMove(int row, int col, GameState.Player current_player, TileType t) {
        Tile tile = new Tile(t); // this sets TileType
        // state_board[row][col] = tile;
        state_board[row][col].owner = getColor(current_player);
       //  System.out.println("Row " + row + " Col " + col + " is of type " + state_board[row][col].type + " and belongs to " + state_board[row][col].owner);
    }

    public Tile[][] getStateBoard() {
        return state_board;
    }

    public void changeTileOwner(int row, int col, GameState.Player current_player) {
        state_board[row][col].owner = getColor(current_player);
    }

    public boolean isTileEmpty(int row, int col) {
        return state_board[row][col].owner == null;
    }
    public TileOwner getTileOwner(int row, int col) {
        return state_board[row][col].getOwner();
    }

    public TileType getTileType(int row, int col) {
        return state_board[row][col].type;
    }
    public Tile getTile(int row, int col) {
        return state_board[row][col];
    }

}
