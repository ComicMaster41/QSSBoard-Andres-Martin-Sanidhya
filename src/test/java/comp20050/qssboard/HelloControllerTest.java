package comp20050.qssboard;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    HelloController controller;
    GameState state;
    Pane overlayPane;
    StrategyVisualizer strategyVisualizer;

    @BeforeAll
    static void initJfx() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
    }

    @BeforeEach
    void setUp() {
        controller = new HelloController();
        state = new GameState();
        overlayPane = new Pane();

        controller.ShapeLayout = new Group();
        controller.activatePieButton = new Button();
        controller.activateShowStrategyButton = new Button();
        controller.OctCell_turn = new Polygon();
        controller.Rhombus_turn = new Polygon();
        controller.green_oct = new Polygon();
        controller.turnLabel = new Label();
        controller.showLabel = new Label();
        controller.showLabel1 = new Label();
        controller.showLabel2 = new Label();
        controller.showLabel3 = new Label();
        controller.line1 = new Line();
        controller.line2 = new Line();
        controller.overlayPane = overlayPane;
        controller.state = state;
        controller.inputEnabled = true;

        controller.initialize();

        strategyVisualizer = new StrategyVisualizer(overlayPane, controller.ShapeLayout, () -> GameState.Player.P2);
    }


    private Polygon addTile(String id) {
        Polygon p = new Polygon();
        p.setId(id);
        controller.ShapeLayout.getChildren().add(p);
        return p;
    }

    private Bot.ScoredMove makeScoredMove(Bot bot, String moveId, int score) {
        Position move = new Position(moveId);
        return new Bot.ScoredMove(move, score);
    }

    private List<Line> getLines() {
        return overlayPane.getChildren().stream()
                .filter(n -> n instanceof Line)
                .map(n -> (Line) n)
                .collect(Collectors.toList());
    }

    private List<Text> getTexts() {
        return overlayPane.getChildren().stream()
                .filter(n -> n instanceof Text)
                .map(n -> (Text) n)
                .collect(Collectors.toList());
    }

    @Test
    void intializeHidePieButton() {
        assertFalse(controller.activatePieButton.isVisible());
    }

    @Test
    void intilizeBlackToPlayDisplay() {
        assertEquals("Black to play", controller.turnLabel.getText());
        assertEquals(Color.BLACK, controller.OctCell_turn.getFill());
        assertEquals(Color.BLACK, controller.Rhombus_turn.getFill());
    }

    @Test
    void getTileTypeFromId_returnsRhombusForRPrefix() {
        assertEquals(QuaxBoard.TileType.RHOMBUS, controller.getTileTypeFromId("R_0_0"));
    }

    @Test
    void getTileTypeFromId_returnsOctagonForOPrefix() {
        assertEquals(QuaxBoard.TileType.OCTAGON, controller.getTileTypeFromId("O_0_0"));
    }

    @Test
    void initialize_setsStartingTurnState() {
        assertFalse(controller.activatePieButton.isVisible());
        assertEquals("Black to play", controller.turnLabel.getText());
        assertEquals(Color.BLACK, controller.OctCell_turn.getFill());
        assertEquals(Color.BLACK, controller.Rhombus_turn.getFill());
    }

    @Test
    void handleCellSelection_incrementsMovesMade() {
        Polygon clicked = new Polygon();
        clicked.setId("O_0_0");
        int before = controller.moves_made;

        controller.handleCellSelection(clicked);

        assertEquals(before + 1, controller.moves_made);
    }

    @Test
    void handleCellSelection_firstMove_showsPieButton() {
        Polygon clicked = new Polygon();
        clicked.setId("O_0_0");

        controller.handleCellSelection(clicked);

        assertTrue(controller.activatePieButton.isVisible());
        assertEquals(1, controller.moves_made);
    }

    void placeTile(int row, int col, QuaxBoard.TileOwner owner) {
        state.getGameBoard().getTile(row, col).setOwner(owner);
    }

    @Test
    void testBlackNotWinningOnEmptyBoard() {
        assertFalse(state.checkWin(QuaxBoard.TileOwner.BLACK));
    }

    @Test
    void testWhiteNotWinningOnEmptyBoard() {
        assertFalse(state.checkWin(QuaxBoard.TileOwner.WHITE));
    }

    @Test
    void testBlackWinsWithStraightLine() {
        for (int row = 0; row <= 10; row++) {
            placeTile(row, 0, QuaxBoard.TileOwner.BLACK);
        }
        assertTrue(state.checkWin(QuaxBoard.TileOwner.BLACK));
    }

    @Test
    void testBlackDoesNotWinWithIncompletePath() {
        for (int row = 0; row <= 9; row++) {
            placeTile(row, 0, QuaxBoard.TileOwner.BLACK);
        }
        assertFalse(state.checkWin(QuaxBoard.TileOwner.BLACK));
    }

    @Test
    @DisplayName("drawStrategy returns null when moveMadeId is null")
    void drawStrategy_nullMoveMadeId_returnsNull() {
        Bot bot = new Bot(controller.state, null, GameState.Player.P2);

        Polygon result = strategyVisualizer.drawStrategy(bot, controller.state, controller.moveMadeId);

        assertNull(result);
    }

    @Test
    @DisplayName("drawStrategy returns null and adds nothing when there are no scored moves")
    void drawStrategy_noScoredMoves_returnsNullAndOverlayIsEmpty() {
        controller.moveMadeId = new Position("O_0_0");
        addTile("O_0_0");
        Bot bot = new Bot(controller.state, null, GameState.Player.P2);

        Polygon result = strategyVisualizer.drawStrategy(bot, controller.state, controller.moveMadeId);

        assertNull(result);
        assertTrue(overlayPane.getChildren().isEmpty());
    }

    @Test
    @DisplayName("drawStrategy adds a connector Line and Text label per scored move")
    void drawStrategy_addsLineAndTextPerMove() {
        controller.moveMadeId = new Position("O_0_0");
        addTile("O_0_0");
        addTile("O_2_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_2_0", 2));

        strategyVisualizer.drawStrategy(bot, controller.state, controller.moveMadeId);

        assertEquals(2, getLines().size());
        assertEquals(2, getTexts().size());
    }

    @Test
    @DisplayName("Exactly one text label is added per scored move")
    void drawStrategy_exactlyOneTextLabelPerMove() {
        controller.moveMadeId = new Position("O_0_0");
        addTile("O_0_0");
        addTile("O_2_0");
        addTile("O_4_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_2_0", 2));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_4_0", 3));

        strategyVisualizer.drawStrategy(bot, controller.state, controller.moveMadeId);

        assertEquals(3, getTexts().size());
    }

    @Test
    @DisplayName("Best move (index 0) label is green; all others are red")
    void drawStrategy_bestMoveLabelIsGreen_otherLabelsAreRed() {
        controller.moveMadeId = new Position("O_0_0");
        addTile("O_0_0");
        addTile("O_2_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_2_0", 2));

        strategyVisualizer.drawStrategy(bot, controller.state, controller.moveMadeId);

        List<Text> texts = getTexts();
        assertEquals(2, texts.size());
        assertEquals(Color.RED,   texts.get(0).getFill(), "Other move label should be red");
        assertEquals(Color.GREEN, texts.get(1).getFill(), "Best move label should be green");
    }

    @Test
    @DisplayName("Best move connector line is green; other connector lines are red")
    void drawStrategy_bestMoveLineIsGreen_otherLinesAreRed() {
        controller.moveMadeId = new Position("O_0_0");
        addTile("O_0_0");
        addTile("O_2_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_2_0", 2));

        strategyVisualizer.drawStrategy(bot, controller.state, controller.moveMadeId);
        List<Line> lines = getLines();
        assertEquals(2, lines.size());
        assertEquals(Color.RED,   lines.get(0).getStroke(), "Other move connector should be red");
        assertEquals(Color.GREEN, lines.get(1).getStroke(), "Best move connector should be green");
    }

    @Test
    @DisplayName("Best move tile is highlighted green in ShapeLayout")
    void drawStrategy_bestMoveTileIsHighlightedGreen() {
        controller.moveMadeId = new Position("O_0_0");
        Polygon bestTile = addTile("O_0_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1));

        Polygon result = strategyVisualizer.drawStrategy(bot, controller.state, controller.moveMadeId);

        assertEquals(bestTile, result);
        assertEquals(Color.GREEN, result.getFill());
    }

    @Test
    @DisplayName("drawStrategy returns the best move polygon")
    void drawStrategy_returnsBestMovePolygon() {
        controller.moveMadeId = new Position("O_0_0");
        Polygon bestTile = addTile("O_0_0");
        addTile("O_2_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_2_0", 2));

        Polygon result = strategyVisualizer.drawStrategy(bot, controller.state, controller.moveMadeId);

        assertEquals(bestTile, result);
    }

    @Test
    @DisplayName("Text labels display the score value as their content")
    void drawStrategy_textLabelsShowScoreValues() {
        controller.moveMadeId = new Position("O_0_0");
        addTile("O_0_0");
        addTile("O_2_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 5));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_2_0", 9));

        strategyVisualizer.drawStrategy(bot, controller.state, controller.moveMadeId);


        List<Text> texts = getTexts();
        assertEquals(2, texts.size());
        assertEquals("9", texts.get(0).getText());
        assertEquals("5", texts.get(1).getText());
    }
}