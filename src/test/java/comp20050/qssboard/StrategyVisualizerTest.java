package comp20050.qssboard;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StrategyVisualizerTest {

    private Pane overlayPane;
    private Group shapeLayout;
    private StrategyVisualizer visualizer;
    private GameState state;


    @BeforeEach
    void setUp() {
        overlayPane = new Pane();
        shapeLayout = new Group();
        state = new GameState();
        visualizer = new StrategyVisualizer(overlayPane, shapeLayout, () -> GameState.Player.P2);
    }

    private Polygon addTile(String id) {
        Polygon p = new Polygon();
        p.setId(id);
        shapeLayout.getChildren().add(p);
        return p;
    }

    private Bot botWithMoves(String... moveIds) {
        Bot bot = new Bot(state, null, GameState.Player.P2);
        for (int i = 0; i < moveIds.length; i++) {
            bot.getScoredMoves().add(new Bot.ScoredMove(new Position(moveIds[i]), i + 1));
        }
        return bot;
    }

    @Test
    @DisplayName("hintMove is null before computeAndShow is called")
    void hintMoveInitiallyNull() {
        assertNull(visualizer.getHintMove());
    }

    @Test
    @DisplayName("drawStrategy returns null when moveMadeId is null")
    void drawStrategyNullMoveMadeId() {
        Bot bot = new Bot(state, null, GameState.Player.P2);
        assertNull(visualizer.drawStrategy(bot, state, null));
    }

    @Test
    @DisplayName("drawStrategy returns null when there are no scored moves")
    void drawStrategyNoScoredMoves() {
        addTile("O_0_0");
        Bot bot = new Bot(state, null, GameState.Player.P2);
        assertNull(visualizer.drawStrategy(bot, state, new Position("O_0_0")));
    }

    @Test
    @DisplayName("drawStrategy highlights best move tile green")
    void drawStrategyHighlightsBestMoveGreen() {
        Polygon tile = addTile("O_0_0");
        Bot bot = botWithMoves("O_0_0");
        visualizer.drawStrategy(bot, state, new Position("O_0_0"));
        assertEquals(Color.GREEN, tile.getFill());
    }

    @Test
    @DisplayName("clear resets hintMove to null")
    void clearResetsHintMove() {
        addTile("O_0_0");
        Bot bot = botWithMoves("O_0_0");
        visualizer.computeAndShow(bot, state, new Position("O_0_0"), false);
        visualizer.clear(state, Color.BLACK, Color.WHITE);
        assertNull(visualizer.getHintMove());
    }

    @Test
    @DisplayName("clearVisuals removes overlay children")
    void clearVisualsRemovesOverlayChildren() {
        addTile("O_0_0");
        addTile("O_2_0");
        Bot bot = botWithMoves("O_0_0", "O_2_0");
        visualizer.drawStrategy(bot, state, new Position("O_0_0"));
        assertFalse(overlayPane.getChildren().isEmpty());
        visualizer.clearVisuals();
        assertTrue(overlayPane.getChildren().isEmpty());
    }

    @Test
    @DisplayName("computeAndShow sets hintMove without showing when show is false")
    void computeAndShowSetsHintMoveOnly() {
        Bot bot = new Bot(state, null, GameState.Player.P2);
        visualizer.computeAndShow(bot, state, new Position("O_0_0"), false);
        assertTrue(overlayPane.getChildren().isEmpty());
    }
}