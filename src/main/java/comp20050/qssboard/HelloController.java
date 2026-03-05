/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package comp20050.qssboard;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import kotlin.NotImplementedError;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;



public class HelloController {

    // Functions from backend - logic for handling the player
    GameState state = new GameState();

    // colours of different players
    Color colorP1 = Color.BLACK;
    Color colorP2 = Color.WHITE;

    @FXML // fx:id="OctCell_r0_c0"
    private Polygon OctCell_turn;

    @FXML // fx:id="OctCell_r0_c0"
    private Polygon Rhombus_turn;

    @FXML
    private Label turnLabel;

    int moves_made = 0;

    @FXML
    void getCellID(MouseEvent event) {
        Polygon cell = (Polygon) event.getSource();

        // get row and col
        Position pos = new Position(cell.getId());
        QuaxBoard.TileType tile_type;

        // get tile type:
        if (cell.getId().charAt(0) == 'R'){
            tile_type = QuaxBoard.TileType.RHOMBUS;
        }
        else {
            tile_type = QuaxBoard.TileType.OCTAGON;
        }

        // make the move:
        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        boolean success = state.makeMove(pos, tile_type);
        if (!success) {
            return;
        }


        if (playerBeforeMove == GameState.Player.P1) {
            cell.setFill(colorP1);
        }
        else if (playerBeforeMove == GameState.Player.P2) {
            cell.setFill(colorP2);
        }
        else { // player must be either P1 or P2. Otherwise, we have an error
            throw new IllegalArgumentException("Error from Controller in getCellID - NO player option given");
        }

        moves_made++;
        // isWhiteTurn = !isWhiteTurn;

        // deactivate pie button:
        if (moves_made == 1) {
            activatePieButton.setVisible(true);
        }
        else {
            activatePieButton.setVisible(false);
        }

        updateTurnDisplay();

    }

    private void updateTurnDisplay() {
        GameState.Player current = state.getCurrentPlayer();

        if (current == GameState.Player.P1) {
            // P1 is black in your original mapping
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
    // Create button variable
    @FXML
    private Button activatePieButton;

    @FXML
    public void handlePieButtonClick(ActionEvent actionEvent) {
        // May have to move this to an update method
        // basically, if P2 clicks pie button, we want to have it so that the the only tile on the board
        // which was placed by P1, is now owned by P2
        Tile[][] board = state.game_board.getStateBoard();


        // find only occupied cell:
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                if (board[row][col] != null && board[row][col].owner == GameState.Player.P1){
                    board[row][col].owner = GameState.Player.P2;
                    System.out.println("AFTER PIE RULE: Row " + row + " Col " + col + " is of type " + board[row][col].type + " and belongs to " + board[row][col].owner);
                }
            }
        }

        state.current_player =  GameState.Player.P1;
        Color temp;
        temp = colorP1;
        colorP1 = colorP2;
        colorP2 = temp;

        activatePieButton.setVisible(false);
    }
}
