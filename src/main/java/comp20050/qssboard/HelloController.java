package comp20050.qssboard;

import java.util.ArrayList;


import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
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
    protected Polygon green_oct;

    @FXML
    protected Label turnLabel;

    @FXML
    protected Label showLabel;

    @FXML
    protected Label showLabel1;

    @FXML
    protected Label showLabel2;
    @FXML
    protected Label showLabel3;
    @FXML
    protected Line line1;
    @FXML
    protected Line line2;

    // Create button variable
    @FXML
    protected Button activatePieButton;

    @FXML
    protected Button activateShowStrategyButton;

    @FXML
    protected Pane overlayPane;


    int moves_made = 0;
    boolean gameOver = false;
    static boolean Show = false;

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
        updateTurnDisplay();
        refreshPieButtonVisibility();

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
        Polygon returnPoly = null;

        if (moveMadeId == null) return null;

        QuaxBoard.TileOwner botColor = state.game_board.getColor(botSeat());
        boolean horizontalPaths = (botColor == QuaxBoard.TileOwner.WHITE);

        double rightMarginX = computeRightMarginX();
        double topMarginY   = computeTopMarginY();

        Point2D originCenter = cellCenterInOverlay(moveMadeId);
        if (originCenter == null) return null;

        ArrayList<Bot.ScoredMove> moves = bot.getScoredMoves();
        double spacing = 12.0;

        // Pass 1: count how many candidates share each track (column for black-bot, row for white-bot)
        java.util.Map<Integer, Integer> trackCount = new java.util.HashMap<>();
        for (Bot.ScoredMove m : moves) {
            if (m.move == null) continue;
            int track = horizontalPaths ? m.move.getRow() : m.move.getCol();
            trackCount.merge(track, 1, Integer::sum);
        }

        // Pass 2: render, assigning each candidate an index within its track group
        java.util.Map<Integer, Integer> trackIndex = new java.util.HashMap<>();

        for (int index = moves.size() - 1; index >= 0; index--) {
            Bot.ScoredMove m = moves.get(index);
            if (m.move == null) continue;

            int track = horizontalPaths ? m.move.getRow() : m.move.getCol();
            int groupSize = trackCount.get(track);
            int posInGroup = trackIndex.merge(track, 1, Integer::sum) - 1; // 0-based

            // Symmetric spread around 0: singleton -> 0, pair -> ±spacing/2, triple -> -s, 0, +s
            double lateralOffset = (posInGroup - (groupSize - 1) / 2.0) * spacing;

            boolean isBest = (index == 0);
            Color pathColor = isBest ? Color.GREEN : Color.RED;

            if (isBest) {
                Polygon bestCell = (Polygon) ShapeLayout.lookup("#" + m.move.getRawPosition());
                if (bestCell != null) {
                    bestCell.setFill(colorShowStrategy);
                    bestCell.setMouseTransparent(true);
                    returnPoly = bestCell;
                }
                Point2D bestCenter = cellCenterInOverlay(m.move);
                if (bestCenter != null) {
                    drawWeightWithConnector(bestCenter, m.score, pathColor,
                            horizontalPaths, rightMarginX, topMarginY, lateralOffset);
                }
                continue;
            }

            Point2D toCenter = cellCenterInOverlay(m.move);
            if (toCenter == null) continue;

            drawWeightWithConnector(toCenter, m.score, pathColor,
                    horizontalPaths, rightMarginX, topMarginY, lateralOffset);
        }

        return returnPoly;
    }

    private void drawWeightWithConnector(Point2D endpoint, int score, Color color,
                                         boolean horizontalPaths,
                                         double rightMarginX, double topMarginY,
                                         double lateralOffset) {
        double labelX, labelY;
        double lineStartX, lineStartY;
        double lineEndX, lineEndY;

        if (horizontalPaths) {
            // White bot: connector runs horizontally, offset vertically
            lineStartX = endpoint.getX();
            lineStartY = endpoint.getY() + lateralOffset;
            lineEndX   = rightMarginX;
            lineEndY   = endpoint.getY() + lateralOffset;
            labelX     = rightMarginX + 6;
            labelY     = endpoint.getY() + lateralOffset + 5;
        } else {
            // Black bot: connector runs vertically, offset horizontally
            lineStartX = endpoint.getX() + lateralOffset;
            lineStartY = endpoint.getY();
            lineEndX   = endpoint.getX() + lateralOffset;
            lineEndY   = topMarginY;
            labelX     = endpoint.getX() + lateralOffset;
            labelY     = topMarginY - 15;
        }

        Line connector = new Line(lineStartX, lineStartY, lineEndX, lineEndY);
        connector.setStroke(color);
        connector.setStrokeWidth(1.5);
        connector.setMouseTransparent(true);
        overlayPane.getChildren().add(connector);
        strategyVisuals.add(connector);

        Text weightText = new Text(String.valueOf(score));
        weightText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        weightText.setFill(color);
        double textWidth = weightText.getLayoutBounds().getWidth();
        weightText.setX(labelX - textWidth / 2);
        weightText.setY(labelY);
        weightText.setMouseTransparent(true);
        overlayPane.getChildren().add(weightText);
        strategyVisuals.add(weightText);
    }

    private Point2D cellCenterInOverlay(Position p) {
        Polygon cell = (Polygon) ShapeLayout.lookup("#" + p.getRawPosition());
        if (cell == null) return null;
        Point2D scene = cell.localToScene(
                cell.getBoundsInLocal().getCenterX(),
                cell.getBoundsInLocal().getCenterY()
        );
        return overlayPane.sceneToLocal(scene);
    }

    private double computeRightMarginX() {
        double maxX = 0;
        for (Node node : ShapeLayout.getChildren()) {
            if (node instanceof Polygon) {
                Polygon p = (Polygon) node;
                Point2D scene = p.localToScene(
                        p.getBoundsInLocal().getMaxX(),
                        p.getBoundsInLocal().getCenterY()
                );
                double x = overlayPane.sceneToLocal(scene).getX();
                if (x > maxX) maxX = x;
            }
        }
        return maxX + 20; // padding past the board
    }

    private double computeTopMarginY() {
        double minY = Double.MAX_VALUE;
        for (Node node : ShapeLayout.getChildren()) {
            if (node instanceof Polygon) {
                Polygon p = (Polygon) node;
                Point2D scene = p.localToScene(
                        p.getBoundsInLocal().getCenterX(),
                        p.getBoundsInLocal().getMinY()
                );
                double y = overlayPane.sceneToLocal(scene).getY();
                if (y < minY) minY = y;
            }
        }
        return minY - 40;
    }

    public void makeBotMove() {
        if (gameOver) return;
        Bot bot = new Bot(state, moveMadeId, botSeat());

        // Pie is only for White (P2) after Black's opening; do not treat P1's first reply as a pie turn.
        if (moves_made == 1 && state.getCurrentPlayer() == GameState.Player.P2) {
            boolean pressButton = bot.decideToPressPie();
            activatePieButton.setVisible(true);
            if (pressButton) {
                handlePieButtonClick();
                if (getShow()) {
                    lastBestCell = drawStrategy(bot);
                }
                return;
            }
        }


        Position botMove = bot.makeMove();
        if (botMove == null) {
            return;
        }

        moveMadeId = botMove;
        String botMoveID = botMove.getRawPosition();

        activatePieButton.setVisible(false);


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
                lastBestCell.setFill(owner == state.game_board.getColor(colorP1) ? colorP1 : colorP2);
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

        paintCell(cell, playerBeforeMove);

        if (getShow()) {
            PauseTransition layoutWait = new PauseTransition(Duration.millis(100));
            Bot pieBot = bot;
            layoutWait.setOnFinished(ev -> lastBestCell = drawStrategy(pieBot));
            layoutWait.play();
        }

        moves_made++;

        updateTurnDisplay();
        refreshPieButtonVisibility();
    }

    /** Pie is offered to White (P2) once Black's (P1's) opening stone is on the board. */
    private void refreshPieButtonVisibility() {
        activatePieButton.setVisible(
                moves_made == 1 && state.getCurrentPlayer() == GameState.Player.P2);
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
        green_oct.setOnMouseClicked(null);
        green_oct.setVisible(false);
        // just to give initla white colour
        OctCell_turn.setFill(colorP1);
        Rhombus_turn.setFill(colorP1);
        turnLabel.setText("Black to play");
        showLabel.setVisible(false);
        showLabel1.setVisible(false);
        showLabel2.setVisible(false);
        showLabel3.setVisible(false);
        line1.setVisible(false);
        line2.setVisible(false);

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


        Polygon cell = (Polygon) ShapeLayout.lookup("#" + moveMadeId.getRawPosition());
        state.game_board.changeTileOwner(moveMadeId.getRow(), moveMadeId.getCol(), GameState.Player.P2);
        paintCell(cell, GameState.Player.P2);
        state.current_player = GameState.Player.P1; // Black to play (opening stone is now White's)
        updateTurnDisplay();
        refreshPieButtonVisibility();

        // if it's bot turn after white presses pie button, then make bot move
        if (state.getCurrentPlayer() == botSeat()) {
            setInputEnabled(false);
            PauseTransition pause = new PauseTransition(Duration.millis(1000));
            pause.setOnFinished(e -> {
                makeBotMove();
                setInputEnabled(true);
            });
            pause.play();
        }
    }
    @FXML
    public void handleShowStrategyButtonClick() {
        setShow(!Show);
        overlayPane.setVisible(Show);
        activateShowStrategyButton.setText(Show ? "Hide Strategy" : "Show Strategy");
        if (Show){
            green_oct.setVisible(true);
            showLabel.setVisible(true);
            showLabel1.setVisible(true);
            showLabel2.setVisible(true);
            showLabel3.setVisible(true);
            line1.setVisible(true);
            line2.setVisible(true);

        }
        else{
            green_oct.setVisible(false);
            showLabel.setVisible(false);
            showLabel1.setVisible(false);
            showLabel2.setVisible(false);
            showLabel3.setVisible(false);
            line1.setVisible(false);
            line2.setVisible(false);
        }

    }


    public void restartGame() {
        botHasWhiteStones = !botHasWhiteStones;
        state = new GameState();
        inputEnabled = true;
        colorP1 = Color.BLACK;
        colorP2 = Color.WHITE;
        moves_made = 0;
        gameOver = false;
        Show = false;
        activateShowStrategyButton.setText("Show Strategy");
        overlayPane.setVisible(true);
        winner = null;
        state.current_player = GameState.Player.P1;
        moveMadeId = null;
        resetCellColours();
        initialize();
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
