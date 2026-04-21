package comp20050.qssboard;

import javafx.scene.Group;
import javafx.scene.Node;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {
    HelloController controller;
    Button activatePieButton;
    Polygon octCellTurn;
    Polygon rhombusTurn;
    Label turnLabel;
    Pane overlayPane;

    @BeforeAll
    static void initJfx() { // Needed to get the tests to work
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
    }

    @BeforeEach
    void setUp() {
        controller = new HelloController();
        activatePieButton = new Button();
        octCellTurn = new Polygon();
        rhombusTurn = new Polygon();
        turnLabel = new Label();
        state = new GameState();
        overlayPane = new Pane();

        controller.ShapeLayout = new Group();
        controller.activatePieButton = new Button();
        controller.state = state;
        controller.activatePieButton = activatePieButton;
        controller.OctCell_turn = octCellTurn;
        controller.Rhombus_turn = rhombusTurn;
        controller.turnLabel = turnLabel;
        controller.overlayPane = overlayPane;
        controller.activateShowStrategyButton = new Button();
        controller.inputEnabled = true;
        controller.initialize();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Adds a blank polygon with the given tile ID to ShapeLayout so lookup() can find it. */
    private Polygon addTile(String id) {
        Polygon p = new Polygon();
        p.setId(id);
        controller.ShapeLayout.getChildren().add(p);
        return p;
    }

    /** Builds a ScoredMove with a given move tile and ordered path tile IDs. */
    private Bot.ScoredMove makeScoredMove(Bot bot, String moveId, int score, String... pathIds) {
        Position move = new Position(moveId);
        ArrayList<Position> path = new ArrayList<>();
        for (String id : pathIds) path.add(new Position(id));
        return bot.new ScoredMove(move, score);
    }

    /** Returns the stroke color of the first (main) line inside an Arrow group. */
    private Color getArrowColor(Arrow arrow) {
        Line mainLine = (Line) arrow.getChildren().get(0);
        return (Color) mainLine.getStroke();
    }

    private List<Arrow> getArrows() {
        return overlayPane.getChildren().stream()
                .filter(n -> n instanceof Arrow)
                .map(n -> (Arrow) n)
                .collect(Collectors.toList());
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

    // ── Existing tests ────────────────────────────────────────────────────────

    @Test
    void intializeHidePieButton() {
        assertFalse(controller.activatePieButton.isVisible());
    }

    @Test
    void intilizeBlackToPlayDisplay() {
        assertEquals("Black to play", turnLabel.getText());
        assertEquals(Color.BLACK, controller.OctCell_turn.getFill());
        assertEquals(Color.BLACK, controller.Rhombus_turn.getFill());
    }

    @Test
    void getTileTypeFromId_returnsRhombusForRPrefix() {
        QuaxBoard.TileType result = controller.getTileTypeFromId("R_0_0");
        assertEquals(QuaxBoard.TileType.RHOMBUS, result);
    }

    @Test
    void getTileTypeFromId_returnsOctagonForOPrefix() {
        QuaxBoard.TileType result = controller.getTileTypeFromId("O_0_0");
        assertEquals(QuaxBoard.TileType.OCTAGON, result);
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


    @Test
    void initialize_setsStartingTurnState() {
        assertFalse(controller.activatePieButton.isVisible());
        assertEquals("Black to play", controller.turnLabel.getText());
        assertEquals(Color.BLACK, controller.OctCell_turn.getFill());
        assertEquals(Color.BLACK, controller.Rhombus_turn.getFill());
    }

    GameState state;
    void placeTile(int row, int col, QuaxBoard.TileOwner owner) {
        state.game_board.getTile(row, col).setOwner(owner);
    }@Test
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
    void testBlackDoesNotWinWithIncompletePath() { //column 1 row 0- 9
        for (int row = 0; row <= 9; row++) {
            placeTile(row, 0, QuaxBoard.TileOwner.BLACK);
        }
        assertFalse(state.checkWin(QuaxBoard.TileOwner.BLACK));
    }

    // ── drawStrategy: no scored moves ─────────────────────────────────────────

    @Test
    @DisplayName("drawStrategy returns null and adds nothing when there are no scored moves")
    void drawStrategy_noScoredMoves_returnsNullAndOverlayIsEmpty() {
        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        // scoredMoves list is empty because makeMove() was never called

        Polygon result = controller.drawStrategy(bot);

        assertNull(result, "Should return null when there are no paths to draw");
        assertTrue(overlayPane.getChildren().isEmpty(), "Overlay should have no nodes added");
    }

    // ── drawStrategy: arrow colors ────────────────────────────────────────────

    @Test
    @DisplayName("The best path (index 0) arrow is drawn in green")
    void drawStrategy_bestPathArrowIsGreen() {
        addTile("O_0_0");
        addTile("O_1_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1, "O_0_0", "O_1_0"));

        controller.drawStrategy(bot);

        List<Arrow> arrows = getArrows();
        assertFalse(arrows.isEmpty(), "Expected at least one Arrow node");
        assertEquals(Color.GREEN, getArrowColor(arrows.get(0)), "Best path arrow should be green");
    }

    @Test
    @DisplayName("Non-best path arrows (index > 0) are drawn in red")
    void drawStrategy_nonBestPathArrowsAreRed() {
        addTile("O_0_0");
        addTile("O_1_0");
        addTile("O_2_0");
        addTile("O_3_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1, "O_0_0", "O_1_0"));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_2_0", 2, "O_2_0", "O_3_0"));

        controller.drawStrategy(bot);

        List<Arrow> arrows = getArrows();
        assertEquals(2, arrows.size(), "Expected one arrow per scored move");
        assertEquals(Color.GREEN, getArrowColor(arrows.get(0)), "First arrow should be green");
        assertEquals(Color.RED,   getArrowColor(arrows.get(1)), "Second arrow should be red");
    }

    @Test
    @DisplayName("Mid-path line segments respect green/red coloring for best vs other paths")
    void drawStrategy_lineSegmentsRespectPathColor() {
        // 3-tile path produces: 1 Line (segment 0→1) + 1 Arrow (segment 1→2)
        addTile("O_0_0");
        addTile("O_1_0");
        addTile("O_2_0");
        addTile("O_3_0");
        addTile("O_4_0");
        addTile("O_5_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1, "O_0_0", "O_1_0", "O_2_0"));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_3_0", 2, "O_3_0", "O_4_0", "O_5_0"));

        controller.drawStrategy(bot);

        List<Line> lines = getLines();
        assertEquals(2, lines.size(), "Expected one mid-segment Line per scored move");
        assertEquals(Color.GREEN, lines.get(0).getStroke(), "Best path line segment should be green");
        assertEquals(Color.RED,   lines.get(1).getStroke(), "Other path line segment should be red");
    }

    // ── drawStrategy: green tile selection ────────────────────────────────────

    @Test
    @DisplayName("Green tile is the first empty tile in the best path, not the bot's move tile")
    void drawStrategy_greenTileIsFirstEmptyTileInPath() {
        // Bot (P2 = WHITE) already owns O_0_0; O_1_0 is the next empty tile
        state.game_board.getTile(0, 0).setOwner(QuaxBoard.TileOwner.WHITE);

        addTile("O_0_0");
        Polygon emptyTile = addTile("O_1_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        // Path starts on bot's own tile, then steps onto the empty tile
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1, "O_0_0", "O_1_0"));

        Polygon result = controller.drawStrategy(bot);

        assertEquals(emptyTile, result, "Returned polygon should be the first empty tile in the path");
        assertEquals(Color.GREEN, result.getFill(), "That tile should be filled green");
    }

    @Test
    @DisplayName("No green tile is highlighted when every tile in the best path is already owned")
    void drawStrategy_allPathTilesOwned_noGreenTileHighlighted() {
        // Both tiles in the path are already owned by the bot
        state.game_board.getTile(0, 0).setOwner(QuaxBoard.TileOwner.WHITE);
        state.game_board.getTile(1, 0).setOwner(QuaxBoard.TileOwner.WHITE);

        addTile("O_0_0");
        addTile("O_1_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 0, "O_0_0", "O_1_0"));

        Polygon result = controller.drawStrategy(bot);

        assertNull(result, "Should return null when no empty tile exists in the best path");
    }

    // ── drawStrategy: green arrow not overridden by red ───────────────────────

    @Test
    @DisplayName("Green arrow is still present when a red arrow covers the same tiles")
    void drawStrategy_greenArrowNotOverriddenWhenPathsOverlap() {
        // Both scored moves share the same two tiles — red path drawn on top of green
        addTile("O_0_0");
        addTile("O_1_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1, "O_0_0", "O_1_0")); // best → green
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 2, "O_0_0", "O_1_0")); // other → red

        controller.drawStrategy(bot);

        List<Arrow> arrows = getArrows();
        assertEquals(2, arrows.size(), "Both arrows should exist even when paths overlap");
        assertTrue(arrows.stream().anyMatch(a -> Color.GREEN.equals(getArrowColor(a))),
                "Green arrow must still be present in the overlay");
        assertTrue(arrows.stream().anyMatch(a -> Color.RED.equals(getArrowColor(a))),
                "Red arrow must also be present in the overlay");
    }

    // ── drawStrategy: weight text labels ─────────────────────────────────────

    @Test
    @DisplayName("Exactly one weight label is added per scored move — no duplicates or omissions")
    void drawStrategy_exactlyOneTextLabelPerMove() {
        addTile("O_0_0");
        addTile("O_1_0");
        addTile("O_2_0");
        addTile("O_3_0");
        addTile("O_4_0");
        addTile("O_5_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1, "O_0_0", "O_1_0"));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_2_0", 2, "O_2_0", "O_3_0"));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_4_0", 3, "O_4_0", "O_5_0"));

        controller.drawStrategy(bot);

        assertEquals(3, getTexts().size(),
                "There should be exactly one text label per scored move");
    }

    @Test
    @DisplayName("Best move label is green; all other move labels are red")
    void drawStrategy_bestMoveLabelIsGreen_otherLabelsAreRed() {
        addTile("O_0_0");
        addTile("O_1_0");
        addTile("O_2_0");
        addTile("O_3_0");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0", 1, "O_0_0", "O_1_0"));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_2_0", 2, "O_2_0", "O_3_0"));

        controller.drawStrategy(bot);

        List<Text> texts = getTexts();
        assertEquals(2, texts.size());
        assertEquals(Color.GREEN, texts.get(0).getFill(), "Best move label should be green");
        assertEquals(Color.RED,   texts.get(1).getFill(), "Other move label should be red");
    }

    @Test
    @DisplayName("Weight labels for different move tiles are placed at distinct positions")
    void drawStrategy_textLabelsForDifferentMovesDoNotSharePosition() {
        // Give polygons real coordinates so their bounds are non-zero
        Polygon tileA = new Polygon(0, 0, 20, 0, 20, 20, 0, 20);
        tileA.setId("O_0_0");
        controller.ShapeLayout.getChildren().add(tileA);

        Polygon tileB = new Polygon(100, 100, 120, 100, 120, 120, 100, 120);
        tileB.setId("O_5_10");
        controller.ShapeLayout.getChildren().add(tileB);

        // Path tiles (just need to exist for arrow drawing)
        addTile("O_1_0");
        addTile("O_6_10");

        Bot bot = new Bot(controller.state, null, GameState.Player.P2);
        bot.getScoredMoves().add(makeScoredMove(bot, "O_0_0",  1, "O_0_0",  "O_1_0"));
        bot.getScoredMoves().add(makeScoredMove(bot, "O_5_10", 2, "O_5_10", "O_6_10"));

        controller.drawStrategy(bot);

        List<Text> texts = getTexts();
        assertEquals(2, texts.size());
        // Labels anchored to different cells must end up at different X positions
        assertNotEquals(texts.get(0).getX(), texts.get(1).getX(),
                "Text labels for different move tiles should have different X positions");
    }
}
