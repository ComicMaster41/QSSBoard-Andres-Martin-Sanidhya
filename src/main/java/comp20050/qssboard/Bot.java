package comp20050.qssboard;

import java.util.Random;

public class Bot {
    public GameState state;
    public Bot(GameState state) {
        this.state = state;
    }
    public Position makeMove() {
        // a bot that randomly chooses tiles
        Boolean[][] valid_moves = state.game_board.getValidMoves();
        Random r = new Random();
        int x;
        int y;
        do {
            x = r.nextInt(Tile.NUM_ROWS);
            y = r.nextInt(Tile.NUM_COLS);
        } while (!valid_moves[x][y]);

        Tile tile = state.game_board.getTile(x, y);
        String botMoveId = (tile.type.toString().equals("RHOMBUS") ? "R" : "O") + "_" + x + "_" + y;
        Position pos = new Position(botMoveId);
        return pos;
    }

    // Scores a board position from the bot's perspective

    // Returns true when minimax should stop expanding this branch

    // Creates a deep copy of the game state / board for simulation

    // Applies a move to a copied state during simulation

    // Gets all legal moves available in the current state

    // Optional: convert board coordinates into a Position object

    // Optional: helper to determine whose turn it is in simulation
}
