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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;


public class HelloController {

    // Functions from backend - logic for handling the player
    GameState state = new GameState();

    /**
     * If true, the bot plays white (P2) this game; if false, black (P1).
     * Flips each time a game ends and {@link #restartGame()} runs, so the bot alternates colours every round.
     */
    private boolean botHasWhiteStones = true;

    boolean inputEnabled = true;
    // colours of different players
    Color colorP1 = Color.BLACK;
    Color colorP2 = Color.WHITE;
    Color colorShowStrategy = Color.GREEN;

    private Polygon lastBestCell;

    Position moveMadeId;
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
    static boolean Show = false; // QUESTION: WHY IS IT STATIC?

    public boolean getShow() {
        return this.Show;
    }

    public void setShow(boolean show) {
        this.Show = show;
    }

    GameState.Player winner = null;

    private final ArrayList<Node> strategyVisuals = new ArrayList<>();


    private GameState.Player botSeat() {
        return botHasWhiteStones ? GameState.Player.P2 : GameState.Player.P1;
    }

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
        moveMadeId = pos;
        QuaxBoard.TileType tile_type = getTileTypeFromId(cell.getId());

        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        boolean success = state.makeMove(pos, tile_type);
        if (!success) {
            return;
        }

        paintCell(cell, playerBeforeMove);

        // check win using the player who just moved, before the turn switched
        if (state.checkWin(state.game_board.getColor(playerBeforeMove))) {
            gameOver = true;
            winner = playerBeforeMove;
            updateTurnDisplay();
            PauseTransition pause = new PauseTransition(Duration.millis(2000));
            pause.setOnFinished(event -> {restartGame();});
            pause.play();
            return;
        }

        moves_made++;
        if (moves_made == 1) {
            activatePieButton.setVisible(true);
        } else {
            activatePieButton.setVisible(false);
        }

        updateTurnDisplay();

        if (state.getCurrentPlayer() == botSeat()) {
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


    public Polygon drawStrategy(Bot bot) {
        int index = 0;
        boolean botIsBlack = (botSeat() == GameState.Player.P1);

        Polygon returnPoly = null;

        for (Bot.ScoredMove m : bot.getScoredMoves()) {
            // Green is best move, red is any move
            Color pathColor = (index == 0) ? Color.GREEN : Color.RED;

            // For the best path, highlight the first empty tile the bot is heading towards
            if (index == 0) {
                for (Position p : m.path) {
                    if (state.game_board.isTileEmpty(p.getRow(), p.getCol())) {
                        Polygon nextCell = (Polygon) ShapeLayout.lookup("#" + p.getRawPosition());
                        if (nextCell != null) {
                            nextCell.setFill(colorShowStrategy);
                            returnPoly = nextCell;
                        }
                        break;
                    }
                }
            }

            for (int i = 0; i < m.path.size() - 1; i++) {
                Position from = m.path.get(i);
                Position to = m.path.get(i + 1);

                // check if the bot is black or white to show arrow orientation
                if (botIsBlack) {
                    if (from.getRow() > to.getRow()) {
                        Position temp = from;
                        from = to;
                        to = temp;
                    }
                }

                else {
                    if (from.getCol() > to.getCol()) {
                        Position temp = from;
                        from = to;
                        to = temp;
                    }
                }

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

                // Only put an arrowhead on the last segment; use a plain line for all others
                if (i == m.path.size() - 2) {
                    Arrow arrow = new Arrow();
                    arrow.setColor(pathColor);
                    arrow.setStartX(startX);
                    arrow.setStartY(startY);
                    arrow.setEndX(endX);
                    arrow.setEndY(endY);
                    arrow.setMouseTransparent(true);
                    arrow.getChildren().forEach(child ->
                            child.setMouseTransparent(true));
                    overlayPane.getChildren().add(arrow);
                    strategyVisuals.add(arrow);
                } else {
                    Line segment = new Line(startX, startY, endX, endY);
                    segment.setStroke(pathColor);
                    segment.setStrokeWidth(3.0);
                    segment.setMouseTransparent(true);
                    overlayPane.getChildren().add(segment);
                    strategyVisuals.add(segment);
                }
            }

            index++;

            // Label at the candidate move cell so scores don't overlap
            Polygon moveCell = (Polygon) ShapeLayout.lookup("#" + m.move.getRawPosition());
            if (moveCell != null) {
                Point2D moveCellPoint = moveCell.localToScene(
                        moveCell.getBoundsInLocal().getCenterX(),
                        moveCell.getBoundsInLocal().getCenterY()
                );
                double textX = overlayPane.sceneToLocal(moveCellPoint).getX();
                double textY = overlayPane.sceneToLocal(moveCellPoint).getY();

                Text weightText = new Text(String.valueOf(m.score));
                weightText.setX(textX + 5);
                weightText.setY(textY - 10);
                weightText.setFill(pathColor);
                weightText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                weightText.setMouseTransparent(true);
                strategyVisuals.add(weightText);
                overlayPane.getChildren().add(weightText);
            }
        }

        return returnPoly;
    }

    public void makeBotMove() {
        if (gameOver) return;
        Bot bot = new Bot(state, moveMadeId, botSeat());

        Position botMove = bot.makeMove();
        if (botMove == null) {
            return;
        }

        moveMadeId = botMove;
        String botMoveID = botMove.getRawPosition();

        // Bot can activate pie button
        if (moves_made == 1) {
            boolean pressButton = bot.decideToPressPie();
            activatePieButton.setVisible(true);
            if (pressButton) {
                if (getShow()) lastBestCell = drawStrategy(bot); // added fix but not what we're looking for
                handlePieButtonClick();
                return;
            }
        }

        else activatePieButton.setVisible(false);


        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        if (!state.makeMove(botMove, QuaxBoard.TileType.OCTAGON)) { // QUESTION: should this also check for rhombus?
            throw new IllegalArgumentException("Error making bot move");
        }

        if (lastBestCell != null) { // Reset the highlighted green tile to its proper color
            Position p = new Position(lastBestCell.getId());
            QuaxBoard.TileOwner owner = state.game_board.getTileOwner(p.getRow(), p.getCol());
            if (owner == null) {
                lastBestCell.setFill(Paint.valueOf(p.getCol() % 2 == 0 ? "#c98c07" : "#ffb91f"));
            } else {
                lastBestCell.setFill(owner == state.game_board.p1Color ? colorP1 : colorP2);
            }
            lastBestCell = null;
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
            updateTurnDisplay();
            PauseTransition pause = new PauseTransition(Duration.millis(2000));
            pause.setOnFinished(event -> {restartGame();});
            pause.play();
        }

        // Show all paths and their weights
        paintCell(cell, playerBeforeMove);

        if (getShow()) {
            lastBestCell = drawStrategy(bot);
        }

        moves_made++;

        updateTurnDisplay();
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

        if (state.getCurrentPlayer() == botSeat()) {
            setInputEnabled(false); // lock UI

            PauseTransition pause = new PauseTransition(Duration.millis(1000));
            pause.setOnFinished(e ->  {
                makeBotMove();
                setInputEnabled(true); // unlock UI after bot moves
            });
            pause.play();
        }
    }


    @FXML
    public void handlePieButtonClick() {
        GameState.Player pieClaimant = state.getCurrentPlayer(); // whoever is pressing pie
        GameState.Player otherPlayer = (pieClaimant == GameState.Player.P1)
                ? GameState.Player.P2 : GameState.Player.P1;

        state.game_board.swapColors();

        // Swap UI colors
        Color temp = colorP1;
        colorP1 = colorP2;
        colorP2 = temp;

        // Repaint all node
        for (Node node : ShapeLayout.getChildren()) {
            if (node instanceof Polygon) {
                Position pos = new Position(node.getId());
                QuaxBoard.TileOwner owner = state.game_board.getTileOwner(pos.getRow(), pos.getCol());
                if (owner == QuaxBoard.TileOwner.BLACK) {
                    ((Polygon) node).setFill(colorP1);
                } else if (owner == QuaxBoard.TileOwner.WHITE) {
                    ((Polygon) node).setFill(colorP2);
                }
            }
        }

        state.current_player = otherPlayer;
        updateTurnDisplay();
        activatePieButton.setVisible(false);
    }
    @FXML
    public void handleShowStrategyButtonClick() {
        if (getShow() == !Show) { // If we want to stop showing strategy,
            overlayPane.setVisible(false);
        }

        setShow(!Show);
        activateShowStrategyButton.setText(Show ? "Hide Strategy" : "Show Strategy");
    }

    public void restartGame() {
        // reinitialise all variables
        botHasWhiteStones = !botHasWhiteStones;
        state = new GameState();
        inputEnabled = true;
        colorP1 = Color.BLACK;
        colorP2 = Color.WHITE;
        moves_made = 0;
        gameOver = false;
        Show = false;
        winner = null;
        state.current_player = GameState.Player.P1;
        moveMadeId = null;
        resetCellColours();
        initialize();
        System.out.println("restart game");
    }

    public void resetCellColours() {
        int col;
        for (Node node : ShapeLayout.getChildren()) {
            if (node instanceof Polygon) {
                Position pos = new Position(node.getId());
                col = pos.getCol();
                if (col % 2 == 0) {
                    ((Polygon) node).setFill(Paint.valueOf("#c98c07"));
                }
                else {
                    ((Polygon) node).setFill(Paint.valueOf("#ffb91f"));
                }
            }
        }
    }

    public void paintCell(Polygon cell, GameState.Player player) {
        cell.setFill(player == GameState.Player.P1 ? colorP1 : colorP2);
    }
}
