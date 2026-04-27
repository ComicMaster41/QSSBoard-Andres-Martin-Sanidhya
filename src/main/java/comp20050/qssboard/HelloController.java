package comp20050.qssboard;

import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class HelloController {
    private static final double BOT_THINK_DELAY_MS = 1000;
    private static final double POST_GAME_PAUSE_MS = 2000;
    private static final double LAYOUT_SETTLE_DELAY_MS = 100;
    private static final String OCTAGON_CELL_HEX = "#c98c07";
    private static final String RHOMBUS_CELL_HEX = "#ffb91f";

    private GameState state = new GameState();
    private boolean botHasWhiteStones = true;
    private boolean inputEnabled = true;
    private boolean show = false;
    private boolean gameOver = false;
    private int movesMade = 0;

    private final Color colorP1 = Color.BLACK;
    private final Color colorP2 = Color.WHITE;
    private final Color bestMoveColor = Color.GREEN;
    private final Color otherMoveColor = Color.RED;

    private GameState.Player winner = null;
    private Position moveMadeId;
    private Polygon botLastPlacedCell;
    private GameState.Player botLastPlacedPlayer;
    private StrategyVisualizer strategyVisualizer;

    @FXML protected Group ShapeLayout;
    @FXML protected Polygon OctCell_turn;
    @FXML protected Polygon Rhombus_turn;
    @FXML protected Polygon green_oct;
    @FXML protected Label turnLabel;
    @FXML protected Label showLabel;
    @FXML protected Label showLabel1;
    @FXML protected Label showLabel2;
    @FXML protected Label showLabel3;
    @FXML protected Line line1;
    @FXML protected Line line2;
    @FXML protected Button activatePieButton;
    @FXML protected Button activateShowStrategyButton;
    @FXML protected Pane overlayPane;

    @FXML
    void initialize() {
        strategyVisualizer = new StrategyVisualizer(overlayPane, ShapeLayout, bestMoveColor, otherMoveColor);
        configureNonInteractiveCells();
        hideStrategyLabels();
        resetTurnDisplay();
        if (state.getCurrentPlayer() == botSeat()) scheduleBotMove();
    }

    @FXML
    void getCellID(MouseEvent event) {
        handleCellSelection((Polygon) event.getSource());
    }

    @FXML
    void handleCellSelection(Polygon cell) {
        if (gameOver || !inputEnabled) return;

        Position pos = new Position(cell.getId());
        moveMadeId = pos;
        QuaxBoard.TileType tileType = tileTypeFromId(cell.getId());

        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        if (!state.makeMove(pos, tileType)) return;

        paintCell(cell, playerBeforeMove);

        if (didPlayerWin(playerBeforeMove)) {
            handleGameOver(playerBeforeMove);
            return;
        }

        movesMade++;
        updateTurnDisplay();
        refreshPieButtonVisibility();

        if (state.getCurrentPlayer() == botSeat()) scheduleBotMove();
    }

    public void makeBotMove() {
        if (gameOver) return;
        Bot bot = new Bot(state, moveMadeId, botSeat());

        if (shouldOfferPie()) {
            activatePieButton.setVisible(true);
            if (bot.decideToPressPie()) {
                handlePieButtonClick();
                if (show) botLastPlacedCell = renderStrategy(bot);
                return;
            }
        }

        Position botMove = bot.chooseMove();
        if (botMove == null) return;

        applyBotMoveToBoard(bot, botMove);
    }

    private void applyBotMoveToBoard(Bot bot, Position botMove) {
        moveMadeId = botMove;
        activatePieButton.setVisible(false);

        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        QuaxBoard.TileType tileType = tileTypeFromId(botMove.getRawPosition());
        if (!state.makeMove(botMove, tileType)) {
            throw new IllegalStateException("Bot produced an illegal move: " + botMove.getRawPosition());
        }

        strategyVisualizer.clear();

        // Repaint the previously-green bot cell back to bot's actual color
        if (botLastPlacedCell != null) {
            paintCell(botLastPlacedCell, playerBeforeMove);
        }


        Polygon cell = (Polygon) ShapeLayout.lookup("#" + botMove.getRawPosition());
        cell.setFill(bestMoveColor);
        botLastPlacedCell = cell;
        botLastPlacedPlayer = playerBeforeMove;


        if (didPlayerWin(playerBeforeMove)) {
            paintCell(cell, botLastPlacedPlayer); // restore actual color before showing winner
            handleGameOver(playerBeforeMove);
            return;
        }

        if (show) scheduleStrategyRender(bot);

        movesMade++;
        updateTurnDisplay();
        refreshPieButtonVisibility();
    }

    @FXML
    public void handlePieButtonClick() {
        Polygon cell = (Polygon) ShapeLayout.lookup("#" + moveMadeId.getRawPosition());
        state.getGameBoard().changeTileOwner(moveMadeId.getRow(), moveMadeId.getCol(), GameState.Player.P2);
        paintCell(cell, GameState.Player.P2);
        state.setCurrentPlayer(GameState.Player.P1);
        updateTurnDisplay();
        refreshPieButtonVisibility();

        if (state.getCurrentPlayer() == botSeat()) scheduleBotMove();
    }

    @FXML
    public void handleShowStrategyButtonClick() {
        show = !show;
        overlayPane.setVisible(show);
        activateShowStrategyButton.setText(show ? "Hide Strategy" : "Show Strategy");
        setStrategyLabelsVisible(show);
    }

    public void restartGame() {
        botHasWhiteStones = !botHasWhiteStones;
        state = new GameState();
        inputEnabled = true;
        movesMade = 0;
        gameOver = false;
        show = false;
        winner = null;
        moveMadeId = null;
        botLastPlacedCell = null; // add this
        activateShowStrategyButton.setText("Show Strategy");
        overlayPane.setVisible(true);
        botLastPlacedPlayer = null;
        resetCellColours();
        initialize();
    }

    private void scheduleBotMove() {
        setInputEnabled(false);
        PauseTransition pause = new PauseTransition(Duration.millis(BOT_THINK_DELAY_MS));
        pause.setOnFinished(event -> {
            makeBotMove();
            setInputEnabled(true);
        });
        pause.play();
    }

    private void scheduleStrategyRender(Bot bot) {
        PauseTransition pause = new PauseTransition(Duration.millis(LAYOUT_SETTLE_DELAY_MS));
        pause.setOnFinished(event -> renderStrategy(bot));
        pause.play();
    }

    private Polygon renderStrategy(Bot bot) {
        if (moveMadeId == null) return null;
        QuaxBoard.TileOwner botColor = state.getGameBoard().getColor(botSeat());
        boolean horizontalPaths = botColor == QuaxBoard.TileOwner.WHITE;
        return strategyVisualizer.render(bot.getScoredMoves(), horizontalPaths);
    }

    private void handleGameOver(GameState.Player victor) {
        gameOver = true;
        winner = victor;
        updateTurnDisplay();
        PauseTransition pause = new PauseTransition(Duration.millis(POST_GAME_PAUSE_MS));
        pause.setOnFinished(event -> restartGame());
        pause.play();
    }

    private boolean didPlayerWin(GameState.Player player) {
        return state.checkWin(state.getGameBoard().getColor(player));
    }

    private boolean shouldOfferPie() {
        return movesMade == 1 && state.getCurrentPlayer() == GameState.Player.P2;
    }

    private void refreshPieButtonVisibility() {
        activatePieButton.setVisible(shouldOfferPie());
    }

    private void setInputEnabled(boolean enabled) {
        inputEnabled = enabled;
        for (Node node : ShapeLayout.getChildren()) {
            if (node instanceof Polygon) node.setMouseTransparent(!enabled);
        }
        activatePieButton.setMouseTransparent(!enabled);
    }

    private void updateTurnDisplay() {
        if (gameOver) {
            displayWinner();
        } else {
            displayCurrentPlayerTurn();
        }
    }

    private void displayWinner() {
        Color winnerColor = winner == GameState.Player.P1 ? colorP1 : colorP2;
        OctCell_turn.setFill(winnerColor);
        Rhombus_turn.setFill(winnerColor);
        turnLabel.setText(colorName(winnerColor) + " is the winner");
    }

    private void displayCurrentPlayerTurn() {
        Color playerColor = state.getCurrentPlayer() == GameState.Player.P1 ? colorP1 : colorP2;
        OctCell_turn.setFill(playerColor);
        Rhombus_turn.setFill(playerColor);
        turnLabel.setText(colorName(playerColor) + " to play");
    }

    private String colorName(Color color) {
        return color == Color.BLACK ? "Black" : "White";
    }

    private void configureNonInteractiveCells() {
        OctCell_turn.setOnMouseClicked(null);
        Rhombus_turn.setOnMouseClicked(null);
        OctCell_turn.setMouseTransparent(true);
        Rhombus_turn.setMouseTransparent(true);
        green_oct.setOnMouseClicked(null);
        green_oct.setVisible(false);
        activatePieButton.setVisible(false);
        overlayPane.setMouseTransparent(true);
    }

    private void resetTurnDisplay() {
        OctCell_turn.setFill(colorP1);
        Rhombus_turn.setFill(colorP1);
        turnLabel.setText("Black to play");
    }

    private void hideStrategyLabels() {
        setStrategyLabelsVisible(false);
    }

    private void setStrategyLabelsVisible(boolean visible) {
        showLabel.setVisible(visible);
        showLabel1.setVisible(visible);
        showLabel2.setVisible(visible);
        showLabel3.setVisible(visible);
        line1.setVisible(visible);
        line2.setVisible(visible);
        green_oct.setVisible(visible);
    }

    public void resetCellColours() {
        for (Node node : ShapeLayout.getChildren()) {
            if (!(node instanceof Polygon polygon)) continue;
            Position pos = new Position(node.getId());
            String hex = pos.getCol() % 2 == 0 ? OCTAGON_CELL_HEX : RHOMBUS_CELL_HEX;
            polygon.setFill(Paint.valueOf(hex));
        }
    }

    public void paintCell(Polygon cell, GameState.Player player) {
        cell.setFill(player == GameState.Player.P1 ? colorP1 : colorP2);
    }

    private GameState.Player botSeat() {
        return botHasWhiteStones ? GameState.Player.P2 : GameState.Player.P1;
    }

    private QuaxBoard.TileType tileTypeFromId(String id) {
        return id.charAt(0) == 'R' ? QuaxBoard.TileType.RHOMBUS : QuaxBoard.TileType.OCTAGON;
    }
}