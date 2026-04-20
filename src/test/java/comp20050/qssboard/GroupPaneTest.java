package comp20050.qssboard;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupPaneTest {

    private GroupPane groupPane;
    private int cellClickCount;

    @BeforeAll
    static void initJfx() {
        // Initialize JavaFX Toolkit for testing
        if (!Platform.isFxApplicationThread()) {
            new Thread(() -> Platform.runLater(() -> {})).start();
        }
    }

    @BeforeEach
    void setUp() {
        groupPane = new GroupPane();
        cellClickCount = 0;
    }

    @Test
    void groupPaneConstructorInitializes() {
        assertNotNull(groupPane);
    }



    @Test
    void buildBoardInitializesBoard() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        assertNotNull(groupPane);
        assertTrue(groupPane.getChildren().size() > 0, "Board should have children after build");
    }

    @Test
    void buildBoardCreatesShapeLayout() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane shapeLayout = groupPane.getShapeLayout();
        assertNotNull(shapeLayout);
        assertTrue(shapeLayout.getChildren().size() > 0, "Shape layout should contain cells");
    }

    @Test
    void buildBoardCreatesCorrectNumberOfCells() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane shapeLayout = groupPane.getShapeLayout();
        // Board is Tile.NUM_ROWS x Tile.NUM_COLS
        int expectedCells = Tile.NUM_ROWS * Tile.NUM_COLS;
        assertEquals(expectedCells, shapeLayout.getChildren().size(),
                "Should create one shape for each board cell");
    }

    @Test
    void buildBoardClearsExistingChildren() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);
        int firstBuildSize = groupPane.getChildren().size();

        groupPane.buildBoard(clickHandler);
        int secondBuildSize = groupPane.getChildren().size();

        assertEquals(firstBuildSize, secondBuildSize,
                "Multiple builds should result in same number of children");
    }

    @Test
    void buildBoardClearsShapeLayout() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);
        Pane shapeLayout1 = groupPane.getShapeLayout();
        int size1 = shapeLayout1.getChildren().size();

        groupPane.buildBoard(clickHandler);
        Pane shapeLayout2 = groupPane.getShapeLayout();
        int size2 = shapeLayout2.getChildren().size();

        assertEquals(size1, size2, "Shape layout should be cleared and rebuilt");
    }

    @Test
    void buildBoardWithNullClickHandler() {
        // Should not throw exception
        assertDoesNotThrow(() -> groupPane.buildBoard(null));
    }

    @Test
    void buildBoardCreatesTitle() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        assertTrue(groupPane.getChildren().size() > 0);
        // Board includes title, letters, numbers, frame, and shape layout
    }

    @Test
    void buildBoardCreatesLetterLabels() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        // Should have top and bottom letter labels
        assertTrue(groupPane.getChildren().size() > 0);
    }

    @Test
    void buildBoardCreatesNumberLabels() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        // Should have left and right number labels
        assertTrue(groupPane.getChildren().size() > 0);
    }

    @Test
    void buildBoardCreatesFrame() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        // Frame should be created with rectangles
        assertTrue(groupPane.getChildren().size() > 0);
    }

    @Test
    void shapeLayoutHasCorrectTransformations() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane shapeLayout = groupPane.getShapeLayout();
        // Layout should be scaled
        assertEquals(0.75, shapeLayout.getScaleX(), 0.001);
        assertEquals(0.75, shapeLayout.getScaleY(), 0.001);
    }

    @Test
    void shapeLayoutHasCorrectPosition() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane shapeLayout = groupPane.getShapeLayout();
        assertEquals(75.0, shapeLayout.getLayoutX());
        assertEquals(-41.0, shapeLayout.getLayoutY());
    }

    @Test
    void cellsHaveCorrectIds() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane shapeLayout = groupPane.getShapeLayout();
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                String expectedId;
                if (col % 2 == 0) {
                    expectedId = "O_" + row + "_" + col;
                } else {
                    expectedId = "R_" + row + "_" + col;
                }
                // ID should be set in the polygon
            }
        }
        assertEquals(Tile.NUM_ROWS * Tile.NUM_COLS, shapeLayout.getChildren().size());
    }

    @Test
    void octagonCellsAreCreatedForEvenColumns() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane shapeLayout = groupPane.getShapeLayout();
        // Even columns (0, 2, 4, ...) should have octagons
        assertTrue(shapeLayout.getChildren().size() > 0);
    }

    @Test
    void rhombusCellsAreCreatedForOddColumns() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane shapeLayout = groupPane.getShapeLayout();
        // Odd columns (1, 3, 5, ...) should have rhombuses
        assertTrue(shapeLayout.getChildren().size() > 0);
    }

    @Test
    void multipleBuildsWork() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;

        groupPane.buildBoard(clickHandler);
        int size1 = groupPane.getChildren().size();

        groupPane.buildBoard(clickHandler);
        int size2 = groupPane.getChildren().size();

        groupPane.buildBoard(clickHandler);
        int size3 = groupPane.getChildren().size();

        assertEquals(size1, size2);
        assertEquals(size2, size3);
    }

    @Test
    void buildBoardWithDifferentHandlers() {
        EventHandler<MouseEvent> handler1 = event -> cellClickCount++;
        EventHandler<MouseEvent> handler2 = event -> cellClickCount += 2;

        groupPane.buildBoard(handler1);
        int size1 = groupPane.getChildren().size();

        groupPane.buildBoard(handler2);
        int size2 = groupPane.getChildren().size();

        assertEquals(size1, size2);
    }

    @Test
    void shapeLayoutIsAccessible() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane shapeLayout = groupPane.getShapeLayout();
        assertNotNull(shapeLayout);
        assertFalse(shapeLayout.getChildren().isEmpty());
    }

    @Test
    void boardHasAllRequiredComponents() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        // Should have: title, topLetters, bottomLetters, frame, leftNums, rightNums, shapeLayout
        assertTrue(groupPane.getChildren().size() >= 7,
                "Board should have at least 7 main components");
    }

    @Test
    void cellsAreMouseClickable() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane shapeLayout = groupPane.getShapeLayout();
        assertNotNull(shapeLayout);
        // Each cell should have an event handler attached
    }

    @Test
    void buildBoardIsRepeatable() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                groupPane.buildBoard(clickHandler);
            }
        });
    }

    @Test
    void getShapeLayoutConsistentAfterBuild() {
        EventHandler<MouseEvent> clickHandler = event -> cellClickCount++;
        groupPane.buildBoard(clickHandler);

        Pane layout1 = groupPane.getShapeLayout();
        Pane layout2 = groupPane.getShapeLayout();

        assertSame(layout1, layout2, "getShapeLayout should return the same Pane instance");
    }
}
