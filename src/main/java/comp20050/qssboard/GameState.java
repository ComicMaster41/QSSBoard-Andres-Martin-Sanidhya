package comp20050.qssboard;

public class GameState {
    public enum Player {
        P1,
        P2
    };
    public Player current_player;
    public QuaxBoard game_board = new QuaxBoard(); // allow all classes to access the board

    public GameState() {
        current_player = Player.P1;
    }

    public boolean makeMove(Position pos, QuaxBoard.TileType tileType) { // isMoveValid is called here
        pos.extractPosition();
        int row = pos.getRow();
        int col = pos.getCol();

        if (!game_board.isMoveValid(row, col, tileType)){
            return false;
        }

        game_board.makeMove(row, col, current_player, tileType);
        switchPlayerTurn();
        return true;
    }

    public void switchPlayerTurn() {
        current_player = (current_player == Player.P1) ? (Player.P2) : (Player.P1);
    }

    public Player getCurrentPlayer() {
        return current_player;
    }



}
