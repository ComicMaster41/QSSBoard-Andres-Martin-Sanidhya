/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package comp20050.qssboard;

import java.util.ArrayList;


import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;


public class HelloController {

    // Functions from backend - logic for handling the player
    GameState state = new GameState();
    boolean inputEnabled = true;
    // colours of different players
    Color colorP1 = Color.BLACK;
    Color colorP2 = Color.WHITE;
    Color colorShowStrategy = Color.GREEN;

    private Polygon lastBestCell;

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
    protected Button activatePieButton; // Bug with bot where you can make a second choice...

    @FXML
    protected Button activateShowStrategyButton;

    @FXML
    protected Pane overlayPane;

    int moves_made = 0;
    boolean gameOver = false;
    static boolean Show = false;

    public static boolean getShow() {
        return Show;
    }

    GameState.Player winner = null;

    private final ArrayList<Node> strategyVisuals = new ArrayList<>();


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
        if (gameOver || !inputEnabled) return; // block clicks during bot move

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

        // check win using the player who just moved, before the turn switched
        if (state.checkWin(state.game_board.getColor(playerBeforeMove))) {
            gameOver = true;
            winner = playerBeforeMove;
        }

        moves_made++;
        if (moves_made == 1) {
            activatePieButton.setVisible(true);
        } else {
            activatePieButton.setVisible(false);
        }

        updateTurnDisplay();

        if (state.getCurrentPlayer() == GameState.Player.P2) {
            setInputEnabled(false); // lock UI

            PauseTransition pause = new PauseTransition(Duration.millis(1000));
            pause.setOnFinished(e -> {
                makeBotMove();
                setInputEnabled(true); // unlock UI after bot moves
            });
            pause.play();
        }
    }

    private void setInputEnabled(boolean enabled) {
        inputEnabled = enabled;
        // Disable all clickable cells
        for (Node node : ShapeLayout.getChildren()) {
            if (node instanceof Polygon) {
                node.setMouseTransparent(!enabled);
            }
        }
        // Disable pie button too
        activatePieButton.setMouseTransparent(!enabled);
    }

    public void drawLegend() {
        // anything green is best move
        // anything red is bad move

        // green tile is the move bot is going to make


    }

    public Polygon drawStrategy(Bot bot) {
        int index = 0;
        double boardSplit = ShapeLayout.getBoundsInLocal().getWidth() / 2;

        // So that we can display the text at the tip of the path
        double lastX = 0;
        double lastY = 0;
        
        Polygon returnPoly = null;

        for (Bot.ScoredMove m : bot.getScoredMoves()) {
            if (index == 0) {
                Polygon bestMoveCell = (Polygon) ShapeLayout.lookup("#" + m.move.getRawPosition());
                if (bestMoveCell != null) {
                    bestMoveCell.setFill(colorShowStrategy);
                    returnPoly = bestMoveCell;
                }
            }

            for (int i = 0; i < m.path.size() - 1; i++) {
                Position from = m.path.get(i);
                Position to = m.path.get(i + 1);

                Polygon fromCell = (Polygon) ShapeLayout.lookup("#" + from.getRawPosition());
                Point2D fromPoint = fromCell.localToScene(
                        fromCell.getBoundsInLocal().getCenterX(),
                        fromCell.getBoundsInLocal().getCenterY()
                );

                double startX = overlayPane.sceneToLocal(fromPoint).getX();
                double startY = overlayPane.sceneToLocal(fromPoint).getY();

                Polygon toCell = (Polygon) ShapeLayout.lookup("#" + to.getRawPosition());
                // Claude AI suggestion to convert the x-y coordinates to layer on the Pane
                Point2D toPoint = toCell.localToScene(
                        toCell.getBoundsInLocal().getCenterX(),
                        toCell.getBoundsInLocal().getCenterY()
                );

                double endX = overlayPane.sceneToLocal(toPoint).getX();
                double endY = overlayPane.sceneToLocal(toPoint).getY();

                lastX = endX;
                lastY = endY;

                Arrow arrow = new Arrow();

                arrow.setStartX(startX);
                arrow.setStartY(startY);
                arrow.setEndX(endX);
                arrow.setEndY(endY);

                overlayPane.getChildren().add(arrow);
                strategyVisuals.add(arrow);
            }

            index++;

            Text weightText = new Text(String.valueOf(m.score));
            weightText.setX(lastX + 5);
            weightText.setY(lastY - 10);
            weightText.setFill(Color.RED);
            weightText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            strategyVisuals.add(weightText);
            overlayPane.getChildren().add(weightText);
        }

        return returnPoly;
    }

    public void makeBotMove() {
        if (gameOver) return;

        Bot bot = new Bot(state);
        Position botMove = bot.makeMove();

        if (botMove == null) {
            return;
        }
        String botMoveID = botMove.getRawPosition();

        if (lastBestCell != null) {
            lastBestCell.setFill(colorP2);
            lastBestCell = null;
        }

        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        if (!state.makeMove(botMove, QuaxBoard.TileType.OCTAGON)) {
            throw new IllegalArgumentException("Error making bot move");
        }

        // Clear the old values
        for (Node node : strategyVisuals) {
            overlayPane.getChildren().remove(node);
        }
        strategyVisuals.clear();

        Polygon cell = (Polygon) ShapeLayout.lookup("#" + botMoveID);

        // check win using the player who just moved, before the turn switched
        if (state.checkWin(state.game_board.getColor(playerBeforeMove))) {
            gameOver = true;
            winner = playerBeforeMove;
        }

        // Show all paths and their weights
        if (HelloController.getShow()) {
            lastBestCell = drawStrategy(bot); // Function that draws the strategy
        }

        else {
            cell.setFill(colorP2);
        }

        moves_made++;
        if (moves_made == 1) {
            activatePieButton.setVisible(true);
        } else {
            activatePieButton.setVisible(false);
        }

        updateTurnDisplay();
    }

    @NotNull
    private static Label getLabel(Bot.ScoredMove m, double startX, double startY) {
        Label weightLabel = new Label(String.valueOf(m.score));
        weightLabel.setLayoutX(startX + 500 + 10);
        weightLabel.setLayoutY(startY - 10);
        // FIX OFFSET AND FIGURE OUT WHY NUMBERS ARE NEGATIVE

        // Highlight best move in green
        if (m.score >= 0) {
            weightLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 40px;");
        } else {
            weightLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 40px;");
        }
        return weightLabel;
    }

    private void updateTurnDisplay() {
        GameState.Player current = state.getCurrentPlayer();

        if (!gameOver) {
            if (current == GameState.Player.P1) {
                OctCell_turn.setFill(colorP1);
                Rhombus_turn.setFill(colorP1);
                turnLabel.setText((colorP1 == Color.BLACK ? "Black" : "White") + " to play");
            } else if (current == GameState.Player.P2) {
                OctCell_turn.setFill(colorP2);
                Rhombus_turn.setFill(colorP2);
                turnLabel.setText((colorP2 == Color.BLACK ? "Black" : "White") + " to play");
            }
        } else {
            Color winnerColor = (winner == GameState.Player.P1) ? colorP1 : colorP2;
            OctCell_turn.setFill(winnerColor);
            Rhombus_turn.setFill(winnerColor);
            turnLabel.setText((winnerColor == Color.BLACK ? "Black" : "White") + " is the winner");
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        OctCell_turn.setOnMouseClicked(null);
        Rhombus_turn.setOnMouseClicked(null);
        OctCell_turn.setMouseTransparent(true);
        Rhombus_turn.setMouseTransparent(true);
        activatePieButton.setVisible(false);
        overlayPane.setMouseTransparent(true);

        // just to give initla white colour
        OctCell_turn.setFill(colorP1);
        Rhombus_turn.setFill(colorP1);
        turnLabel.setText("Black to play");
    }


    @FXML
    public void handlePieButtonClick(ActionEvent actionEvent) {
        if (!inputEnabled || gameOver) return; // block during bot move

        // May have to move this to an update method
        // basically, if P2 clicks pie button, we want to have it so that the the only tile on the board
        // which was placed by P1, is now owned by P2
        Tile[][] board = state.game_board.getStateBoard();

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
    @FXML
    public void handleShowStrategyButtonClick(ActionEvent actionEvent) {
        Show = !Show;
        activateShowStrategyButton.setText(Show ? "Hide Strategy" : "Show Strategy");
    }
}
