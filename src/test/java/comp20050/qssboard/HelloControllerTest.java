package comp20050.qssboard;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {
    HelloController controller;
    Button activatePieButton;
    Polygon octCellTurn;
    Polygon rhombusTurn;
    Label turnLabel;

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

        controller.activatePieButton = activatePieButton;
        controller.OctCell_turn = octCellTurn;
        controller.Rhombus_turn = rhombusTurn;
        controller.turnLabel = turnLabel;

        controller.initialize();
    }

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
    void handleCellSelection_invalidMove_doesNotIncrementMovesMade() {
        Polygon clicked = new Polygon();
        clicked.setId("O_0_0");

        controller.handleCellSelection(clicked);
        int movesAfterValid = controller.moves_made;

        controller.handleCellSelection(clicked);

        assertEquals(movesAfterValid, controller.moves_made);
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

}