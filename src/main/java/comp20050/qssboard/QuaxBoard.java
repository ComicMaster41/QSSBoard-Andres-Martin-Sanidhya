package comp20050.qssboard;

public class QuaxBoard {
    public enum TileType {
        OCTAGON,
        RHOMBUS
    };

    private GameState.Player[][] state_board_octagon;
    private GameState.Player[][] state_board_rhombus;



    public QuaxBoard() {
        this.state_board_octagon = new GameState.Player[11][11];
        this.state_board_rhombus = new GameState.Player[11][11];
    }

    public boolean isMoveValid(int row, int col, TileType t) { // returns if the board position is empty
       if (t == TileType.OCTAGON) {
           if (state_board_octagon[row][col] == null) {
               return true;
           } else {
               return false;
           }
       }
       else {
           if (state_board_rhombus[row][col] == null) {
               return true;
           } else {
               return false;
           }
       }
    }
    public void makeMove(int row, int col, GameState.Player current_player, TileType t) {
        if (t == TileType.OCTAGON) {
            state_board_octagon[row][col] = current_player;
        }
        else {
            state_board_rhombus[row][col] = current_player;
        }
        // logic for dsa to store the player's chain (i.e. a linked list for each player) will go here:
    }

    public GameState.Player[][] getStateBoard() {
        return state_board_octagon;
    }
}
