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

    public void swapColors() {
        TileOwner temp = p1Color;
        p1Color = p2Color;
        p2Color = temp;
    }

    public boolean isMoveValid(int row, int col, TileType t) { // returns if the board position is empty
        if (t == null) return false;
        if (state_board[row][col] == null || state_board[row][col].owner == null) return true;
        else  return false;
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


    public QuaxBoard copyBoard() {
        QuaxBoard cpyBoard = new QuaxBoard();

        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                Tile originalTile = this.state_board[row][col];
                Tile copiedTile = new Tile(originalTile.type);
                copiedTile.owner = originalTile.owner;
                cpyBoard.state_board[row][col] = copiedTile;
            }
        }

        return cpyBoard;
    }

    public void makeMove(int row, int col, GameState.Player current_player, TileType t) {
        Tile tile = new Tile(t);
        state_board[row][col].owner = getColor(current_player);
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
