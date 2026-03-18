package comp20050.qssboard;

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

    public boolean isMoveValid(int row, int col, TileType t) { // returns if the board position is empty
        if (state_board[row][col] == null || state_board[row][col].owner == null) {
           return true;
       } else {
           return false;
       }
    }
    public void makeMove(int row, int col, GameState.Player current_player, TileType t) {
        Tile tile = new Tile(t); // this sets TileType
        state_board[row][col] = tile;
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

}
