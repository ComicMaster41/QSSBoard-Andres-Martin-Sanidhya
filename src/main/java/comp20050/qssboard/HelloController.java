package comp20050.qssboard;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;


public class HelloController {

    GameState state = new GameState();

    private boolean botHasWhiteStones = true;

    boolean inputEnabled = true;
    Color colorP1 = Color.BLACK;
    Color colorP2 = Color.WHITE;

    private StrategyVisualizer strategyVisualizer;

    Position moveMadeId;
    @FXML
    protected Group ShapeLayout;
    @FXML
    protected Polygon OctCell_turn;
    @FXML
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

    private GameState.Player botSeat() {
        return botHasWhiteStones ? GameState.Player.P2 : GameState.Player.P1;
    }

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
        if (gameOver || !inputEnabled) return;

        strategyVisualizer.clear(state, colorP1, colorP2);

        Position pos = new Position(cell.getId());
        moveMadeId = pos;
        QuaxBoard.TileType tile_type = getTileTypeFromId(cell.getId());

        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        boolean success = state.makeMove(pos, tile_type);
        if (!success) return;

        paintCell(cell, playerBeforeMove);

        if (state.checkWin(state.getGameBoard().getColor(playerBeforeMove))) {
            resetGame(playerBeforeMove);
            return;
        }

        moves_made++;
        updateTurnDisplay();
        refreshPieButtonVisibility();
        triggerBotTurn();
    }

    private void resetGame(GameState.Player playerBeforeMove) {
        gameOver = true;
        winner = playerBeforeMove;
        updateTurnDisplay();
        PauseTransition pause = new PauseTransition(Duration.millis(2000));
        pause.setOnFinished(event -> restartGame());
        pause.play();
    }

    private void triggerBotTurn() {
        if (state.getCurrentPlayer() == botSeat()) {
            setInputEnabled(false);
            PauseTransition pause = new PauseTransition(Duration.millis(3000));
            pause.setOnFinished(e -> {
                makeBotMove();
                setInputEnabled(true);
            });
            pause.play();
        }
    }

    private void setInputEnabled(boolean enabled) {
        inputEnabled = enabled;
        for (Node node : ShapeLayout.getChildren()) {
            if (node instanceof Polygon) {
                node.setMouseTransparent(!enabled);
            }
        }
        activatePieButton.setMouseTransparent(!enabled);
    }

    private void computeAndShowBotStrategy() {
        Bot hintBot = new Bot(state, moveMadeId, botSeat());
        strategyVisualizer.computeAndShow(hintBot, state, moveMadeId, getShow());
    }


    public void makeBotMove() {
        if (gameOver) return;
        Bot bot = new Bot(state, moveMadeId, botSeat());

        if (moves_made == 1 && state.getCurrentPlayer() == GameState.Player.P2) {
            boolean pressButton = bot.decideToPressPie();
            activatePieButton.setVisible(true);
            if (pressButton) {
                handlePieButtonClick();
                computeAndShowBotStrategy();
                return;
            }
        }

        Position botMove = bot.chooseMove();
        if (botMove == null) return;

        moveMadeId = botMove;
        String botMoveID = botMove.getRawPosition();
        activatePieButton.setVisible(false);

        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        QuaxBoard.TileType botTileType = getTileTypeFromId(botMove.getRawPosition());
        if (!state.makeMove(botMove, botTileType)) {
            throw new IllegalArgumentException("Error making bot move");
        }

        strategyVisualizer.clearVisuals();

        Polygon cell = (Polygon) ShapeLayout.lookup("#" + botMoveID);
        paintCell(cell, playerBeforeMove);

        if (state.checkWin(state.getGameBoard().getColor(playerBeforeMove))) {
            resetGame(playerBeforeMove);
            return;
        }

        moves_made++;
        updateTurnDisplay();
        refreshPieButtonVisibility();
        computeAndShowBotStrategy();
    }


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

    @FXML
    void initialize() {
        strategyVisualizer = new StrategyVisualizer(overlayPane, ShapeLayout, this::botSeat);

        OctCell_turn.setOnMouseClicked(null);
        Rhombus_turn.setOnMouseClicked(null);
        OctCell_turn.setMouseTransparent(true);
        Rhombus_turn.setMouseTransparent(true);
        activatePieButton.setVisible(false);
        overlayPane.setMouseTransparent(true);
        green_oct.setOnMouseClicked(null);
        green_oct.setVisible(false);
        OctCell_turn.setFill(colorP1);
        Rhombus_turn.setFill(colorP1);
        turnLabel.setText("Black to play");
        showLabel.setVisible(false);
        showLabel1.setVisible(false);
        showLabel2.setVisible(false);
        showLabel3.setVisible(false);
        line1.setVisible(false);
        line2.setVisible(false);

        triggerBotTurn();
    }


    @FXML
    public void handlePieButtonClick() {
        Polygon cell = (Polygon) ShapeLayout.lookup("#" + moveMadeId.getRawPosition());
        state.getGameBoard().changeTileOwner(moveMadeId.getRow(), moveMadeId.getCol(), GameState.Player.P2);
        paintCell(cell, GameState.Player.P2);
        state.setCurrentPlayer(GameState.Player.P1);
        updateTurnDisplay();
        refreshPieButtonVisibility();

        triggerBotTurn();
    }

    @FXML
    public void handleShowStrategyButtonClick() {
        setShow(!Show);
        overlayPane.setVisible(Show);
        activateShowStrategyButton.setText(Show ? "Hide Strategy" : "Show Strategy");
        if (Show) {
            green_oct.setVisible(true);
            showLabel.setVisible(true);
            showLabel1.setVisible(true);
            showLabel2.setVisible(true);
            showLabel3.setVisible(true);
            line1.setVisible(true);
            line2.setVisible(true);
            if (strategyVisualizer.getHintMove() != null && state.getCurrentPlayer() != botSeat()) {
                strategyVisualizer.show(state);
            }
        } else {
            green_oct.setVisible(false);
            showLabel.setVisible(false);
            showLabel1.setVisible(false);
            showLabel2.setVisible(false);
            showLabel3.setVisible(false);
            line1.setVisible(false);
            line2.setVisible(false);
            strategyVisualizer.clear(state, colorP1, colorP2);
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
        state.setCurrentPlayer(GameState.Player.P1);
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