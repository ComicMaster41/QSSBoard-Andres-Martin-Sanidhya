package comp20050.qssboard;

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
        if(checkWin(current_player)) {
            System.out.println(current_player + " wins!");
        }

        switchPlayerTurn();
        return true;
    }

    public boolean checkWin(Player player) {
        visited = new boolean[Tile.NUM_ROWS][Tile.NUM_COLS]; // RESET HERE

        if (player.equals(Player.P1)) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                if (game_board.getTileOwner(0, col) == player) { // this tile is occupied by the player, so we start dfs
                    if (dfs(0, col, player)) {
                        return true;
                    }
                }
            }
        }
        else {
            for (int row = 0; row < Tile.NUM_ROWS; row++) {
                if (game_board.getTileOwner(row, 0) == player) { // this tile is occupied by the player, so we start dfs
                    if (dfs(row, 0, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean dfs(int row, int col, Player player) {
        if (player.equals(Player.P1) && row == 10) {
            return true;
        }
        if (player.equals(Player.P2) && col == 20) {
            return true;
        }

        visited[row][col] = true;

        for (int[] n : getNeighbours(row, col) ) {
            int n_row = n[0];
            int n_col = n[1];

            if (!visited[n_row][n_col] && game_board.getTileOwner(n_row, n_col) == player) {
                if (dfs(n_row, n_col, player)) {
                    return true;
                }
            }
        }

        return false;
    }
    public ArrayList<int[]> getNeighbours(int row, int col) {
        ArrayList<int[]> neighbours = new ArrayList<>();

        int[][] directions = {
                {-1, 0}, {1, 0},   // up, down
                {0, -1}, {0, 1},   // left, right - rhomubs
                {0, -2}, {0, 2},   // left, right - octagon
                {-1, -1}, {1, 1}   // diagonals (adjust later if needed)
        };

        // these are all the directions, now find all corresponding tiles that neighbour given tile
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

        return neighbours;
    }
    public void switchPlayerTurn() {
        current_player = (current_player == Player.P1) ? (Player.P2) : (Player.P1);
    }

    public Player getCurrentPlayer() {
        return current_player;
    }




}
