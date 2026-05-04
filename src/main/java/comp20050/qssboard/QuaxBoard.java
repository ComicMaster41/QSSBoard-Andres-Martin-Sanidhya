package comp20050.qssboard;

public class QuaxBoard {
    public enum TileType { OCTAGON, RHOMBUS }
    public enum TileOwner { BLACK, WHITE, NONE }

    private TileOwner p1Color = TileOwner.BLACK;
    private TileOwner p2Color = TileOwner.WHITE;
    private Tile[][] stateBoard;

    public QuaxBoard() {
        stateBoard = new Tile[Tile.NUM_ROWS][Tile.NUM_COLS];
        initialiseBoard();
    }

    private void initialiseBoard() {
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                stateBoard[row][col] = new Tile(tileTypeForColumn(col));
            }
        }
    }

    private TileType tileTypeForColumn(int col) {
        return col % 2 == 0 ? TileType.OCTAGON : TileType.RHOMBUS;
    }

    public TileOwner getColor(GameState.Player player) {
        return player == GameState.Player.P1 ? p1Color : p2Color;
    }

    public void swapColors() {
        TileOwner temp = p1Color;
        p1Color = p2Color;
        p2Color = temp;
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < Tile.NUM_ROWS
                && col >= 0 && col < Tile.NUM_COLS;
    }

    public boolean isCorrectCellType(int row, int col, TileType tileType) {
        return tileTypeForColumn(col) == tileType;
    }

    public boolean isMoveValid(int row, int col, TileType tileType) {
        return isInBounds(row, col) && isCorrectCellType(row, col, tileType) && isTileEmpty(row, col);
    }

    public QuaxBoard copyBoard() {
        QuaxBoard boardCopy = new QuaxBoard();
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                boardCopy.stateBoard[row][col].setOwner(getTileOwner(row, col));
            }
        }
        return boardCopy;
    }

    public void makeMove(int row, int col, GameState.Player currentPlayer, TileType tileType) {
        stateBoard[row][col].setOwner(getColor(currentPlayer));
    }

    public void changeTileOwner(int row, int col, GameState.Player currentPlayer) {
        stateBoard[row][col].setOwner(getColor(currentPlayer));
    }

    public boolean isTileEmpty(int row, int col) {
        return stateBoard[row][col].getOwner() == null;
    }

    public TileOwner getTileOwner(int row, int col) {
        return stateBoard[row][col].getOwner();
    }

    public TileType getTileType(int row, int col) {
        return stateBoard[row][col].getType();
    }

    public Tile getTile(int row, int col) {
        return stateBoard[row][col];
    }

    public Tile[][] getStateBoard() {
        return stateBoard;
    }
}