/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package comp20050.qssboard;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import kotlin.NotImplementedError;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;



public class HelloController {

    // Functions from backend - logic for handling the player
    GameState state = new GameState();

    // colours of different players
    Color colorP1 = Color.BLACK;
    Color colorP2 = Color.WHITE;

    @FXML
    protected Group ShapeLayout;

    @FXML // fx:id="OctCell_r0_c0"
    protected Polygon OctCell_turn;

    @FXML // fx:id="OctCell_r0_c0"
    protected Polygon Rhombus_turn;

    @FXML
    protected Label turnLabel;

    // Create button variable
    @FXML
    protected Button activatePieButton;
    int moves_made = 0;


    // AI-Gen suggestion so that we can test this functionality
    QuaxBoard.TileType getTileTypeFromId(String id) {
        if (id.charAt(0) == 'R') {
            return QuaxBoard.TileType.RHOMBUS;
        }
        return QuaxBoard.TileType.OCTAGON;
    }

    @FXML
    void getCellID(MouseEvent event) {
        Polygon cell = (Polygon) event.getSource();
        handleCellSelection(cell);
    }

    @FXML
    void handleCellSelection(Polygon cell) {
        Position pos = new Position(cell.getId());
        QuaxBoard.TileType tile_type = getTileTypeFromId(cell.getId());

        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        boolean success = state.makeMove(pos, tile_type);
        if (!success) {
            return;
        }

        if (playerBeforeMove == GameState.Player.P1) {
            cell.setFill(colorP1);
        } else if (playerBeforeMove == GameState.Player.P2) {
            cell.setFill(colorP2);
        } else {
            throw new IllegalArgumentException("Error from Controller in getCellID - NO player option given");
        }

        moves_made++;

        if (moves_made == 1) {
            activatePieButton.setVisible(true);
        } else {
            activatePieButton.setVisible(false);
        }
        updateTurnDisplay();

        //BOT LOGIC:
        // after Black has made their move, white just automatically makes next move
        if (state.getCurrentPlayer() == GameState.Player.P2) {
            // AI CODE TO ADD LITTLE PAUSE
            PauseTransition pause = new PauseTransition(Duration.millis(500)); // 0.5 sec delay
            pause.setOnFinished(e -> makeBotMove());
            pause.play();
            // END AI CODE
        }
    }

    public void makePlayerMove() {

    }
    public void makeBotMove() {
        Bot bot = new Bot(state);
        Position botMove = bot.makeMove(); // need to implement a bot class

        String botMoveID = botMove.getRawPosition();
        if (botMove == null) {
            return;
        }
        if (!state.makeMove(botMove, QuaxBoard.TileType.OCTAGON)) { // dont need to pass in tile type
            throw new IllegalArgumentException("Error making bot move");
        }
        System.out.println("#" + botMoveID);
        Polygon cell = (Polygon) ShapeLayout.lookup("#" + botMoveID);
        cell.setFill(colorP2);
        moves_made++;
        if (moves_made == 1) {
            activatePieButton.setVisible(true);
        } else {
            activatePieButton.setVisible(false);
        }

        updateTurnDisplay();
    }

    private void updateTurnDisplay() {
        GameState.Player current = state.getCurrentPlayer();

        if (current == GameState.Player.P1) {
            // P1 is black in original mapping
            OctCell_turn.setFill(colorP1);
            Rhombus_turn.setFill(colorP1);
            turnLabel.setText((colorP1 == Color.BLACK ? "Black" : "White") + " to play");
        } else if (current == GameState.Player.P2) {
            OctCell_turn.setFill(colorP2);
            Rhombus_turn.setFill(colorP2);
            turnLabel.setText((colorP2 == Color.BLACK ? "Black" : "White") + " to play");
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        activatePieButton.setVisible(false);

        // just to give initla white colour
        OctCell_turn.setFill(colorP1);
        Rhombus_turn.setFill(colorP1);
        turnLabel.setText("Black to play");

    }


    @FXML
    public void handlePieButtonClick(ActionEvent actionEvent) {
        // May have to move this to an update method
        // basically, if P2 clicks pie button, we want to have it so that the the only tile on the board
        // which was placed by P1, is now owned by P2
        Tile[][] board = state.game_board.getStateBoard();


        // find only occupied cell:
//        for (int row = 0; row < Tile.NUM_ROWS; row++) {
//            for (int col = 0; col < Tile.NUM_COLS; col++) {
//                if (board[row][col] != null && board[row][col].owner == GameState.Player.P1){
//                    board[row][col].owner = GameState.Player.P2;
//                    // System.out.println("AFTER PIE RULE: Row " + row + " Col " + col + " is of type " + board[row][col].type + " and belongs to " + board[row][col].owner);
//                }
//            }
//        }

//        state.current_player =  GameState.Player.P1;
//        Color temp;
//        temp = colorP1;
//        colorP1 = colorP2;
//        colorP2 = temp;
        // swap LOGICAL ownership
        QuaxBoard.TileOwner temp = state.game_board.p1Color;
        state.game_board.p1Color = state.game_board.p2Color;
        state.game_board.p2Color = temp;

        // swap UI colors (what user sees)
        Color tempColor = colorP1;
        colorP1 = colorP2;
        colorP2 = tempColor;

        state.current_player = GameState.Player.P1;

        activatePieButton.setVisible(false);
    }
}
