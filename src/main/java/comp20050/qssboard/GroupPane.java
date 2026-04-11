package comp20050.qssboard;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Builds the Quax board (frame, labels, and clickable cells) in code instead of FXML.
 */
public class GroupPane extends Group {

    private static final double CELL_SCALE = 0.75;
    private static final Color OCT_FILL = Color.web("#c98c07");
    private static final Color RHO_FILL = Color.web("#ffb91f");

    private static final double[] OCT_POINTS = {
            157.1, 76.3, 157.1, 123.7, 123.7, 157.1, 76.3, 157.1,
            42.9, 123.7, 42.9, 76.3, 76.3, 42.9, 123.7, 42.9
    };
    private static final double[] RHO_POINTS = {
            100.0, 64.9, 135.1, 100.0, 100.0, 135.1, 64.9, 100.0
    };

    private final Pane shapeLayout = new Pane();

    public GroupPane() {
        setAutoSizeChildren(false);
    }

    public Pane getShapeLayout() {
        return shapeLayout;
    }

    public void buildBoard(EventHandler<MouseEvent> onCellClick) {
        getChildren().clear();
        shapeLayout.getChildren().clear();

        Label title = new Label("Quax (Human vs. Bot)");
        title.setLayoutX(450);
        title.setLayoutY(-1);
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        title.setTextFill(Color.web("#eb3b00"));

        Group topLetters = letterRow(75, -21, 120, "ABCDEFGHIJK");
        Group bottomLetters = letterRow(75, -41, 905, "ABCDEFGHIJK");

        Group frame = new Group();
        frame.setLayoutX(95);
        frame.setLayoutY(-21);
        Rectangle topBar = new Rectangle(140, 130, 773, 35);
        topBar.setFill(Color.BLACK);
        topBar.setStroke(Color.BLACK);
        Rectangle bottomBar = new Rectangle(140, 830, 773, 35);
        bottomBar.setFill(Color.BLACK);
        bottomBar.setStroke(Color.BLACK);
        Rectangle leftBar = new Rectangle(-241, 482, 735, 30);
        leftBar.setFill(Color.WHITE);
        leftBar.setStroke(Color.BLACK);
        leftBar.setRotate(90);
        Rectangle rightBar = new Rectangle(558, 482, 735, 30);
        rightBar.setFill(Color.WHITE);
        rightBar.setStroke(Color.BLACK);
        rightBar.setRotate(90);
        frame.getChildren().addAll(topBar, bottomBar, leftBar, rightBar);

        Group leftNums = numberColumn(95, -21, 120);
        Group rightNums = numberColumn(95, -21, 920);

        shapeLayout.setLayoutX(75);
        shapeLayout.setLayoutY(-41);
        shapeLayout.setScaleX(CELL_SCALE);
        shapeLayout.setScaleY(CELL_SCALE);

        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                if (col % 2 == 0) {
                    addOctagon(row, col, onCellClick);
                } else {
                    addRhombus(row, col, onCellClick);
                }
            }
        }

        getChildren().addAll(title, topLetters, bottomLetters, frame, leftNums, rightNums, shapeLayout);
    }

    private Group letterRow(double groupX, double groupY, double textY, String letters) {
        Group g = new Group();
        g.setLayoutX(groupX);
        g.setLayoutY(groupY);
        double x0 = 225;
        double step = 64;
        for (int i = 0; i < letters.length(); i++) {
            Text t = new Text(String.valueOf(letters.charAt(i)));
            t.setLayoutX(x0 + i * step);
            t.setLayoutY(textY);
            t.setFont(Font.font(36));
            t.setTextOrigin(VPos.CENTER);
            t.setTextAlignment(TextAlignment.CENTER);
            g.getChildren().add(t);
        }
        return g;
    }

    private Group numberColumn(double gx, double gy, double x) {
        Group g = new Group();
        g.setLayoutX(gx);
        g.setLayoutY(gy);
        double y0 = 190;
        double step = 62;
        for (int i = 0; i < 11; i++) {
            int label = 11 - i;
            Text t = new Text(String.valueOf(label));
            t.setLayoutX(x);
            t.setLayoutY(y0 + i * step);
            t.setFont(Font.font(18));
            t.setTextOrigin(VPos.CENTER);
            g.getChildren().add(t);
        }
        return g;
    }

    private void addOctagon(int row, int col, EventHandler<MouseEvent> onCellClick) {
        Polygon p = new Polygon(OCT_POINTS);
        p.setLayoutX(27 + (col / 2) * 84);
        p.setLayoutY(-8 + (Tile.NUM_ROWS - 1 - row) * 85);
        p.setId("O_" + row + "_" + col);
        p.setFill(OCT_FILL);
        p.setStroke(Color.BLACK);
        p.setScaleX(CELL_SCALE);
        p.setScaleY(CELL_SCALE);
        p.setOnMouseClicked(onCellClick);
        shapeLayout.getChildren().add(p);
    }

    private void addRhombus(int row, int col, EventHandler<MouseEvent> onCellClick) {
        Polygon p = new Polygon(RHO_POINTS);
        p.setLayoutX(69 + ((col - 1) / 2) * 84);
        p.setLayoutY(34.5 + (Tile.NUM_ROWS - 2 - row) * 85);
        p.setId("R_" + row + "_" + col);
        p.setFill(RHO_FILL);
        p.setStroke(Color.BLACK);
        p.setScaleX(CELL_SCALE);
        p.setScaleY(CELL_SCALE);
        p.setOnMouseClicked(onCellClick);
        shapeLayout.getChildren().add(p);
    }
}
