package comp20050.qssboard;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StrategyVisualizer {
    private static final double CONNECTOR_SPACING = 12.0;
    private static final double RIGHT_MARGIN_PADDING = 20.0;
    private static final double TOP_MARGIN_PADDING = 40.0;
    private static final double LABEL_OFFSET_FROM_RIGHT_MARGIN = 6.0;
    private static final double LABEL_BASELINE_OFFSET = 5.0;
    private static final double LABEL_DROP_FROM_TOP = 15.0;
    private static final double CONNECTOR_STROKE_WIDTH = 1.5;
    private static final String LABEL_STYLE = "-fx-font-size: 14px; -fx-font-weight: bold;";

    private final Pane overlayPane;
    private final Group shapeLayout;
    private final Color bestMoveColor;
    private final Color otherMoveColor;
    private final ArrayList<Node> visuals = new ArrayList<>();

    public StrategyVisualizer(Pane overlayPane, Group shapeLayout, Color bestMoveColor, Color otherMoveColor) {
        this.overlayPane = overlayPane;
        this.shapeLayout = shapeLayout;
        this.bestMoveColor = bestMoveColor;
        this.otherMoveColor = otherMoveColor;
    }

    public Polygon render(ArrayList<Bot.ScoredMove> moves, boolean horizontalPaths) {
        Map<Integer, Integer> trackCounts = countMovesPerTrack(moves, horizontalPaths);
        Map<Integer, Integer> trackIndices = new HashMap<>();
        Polygon bestCell = null;

        for (int index = moves.size() - 1; index >= 0; index--) {
            Bot.ScoredMove move = moves.get(index);
            if (move.getMove() == null) continue;
            Polygon rendered = renderMove(move, index == 0, horizontalPaths, trackCounts, trackIndices);
            if (index == 0) bestCell = rendered;
        }
        return bestCell;
    }

    public void clear() {
        for (Node visual : visuals) overlayPane.getChildren().remove(visual);
        visuals.clear();
    }

    private Map<Integer, Integer> countMovesPerTrack(ArrayList<Bot.ScoredMove> moves, boolean horizontalPaths) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Bot.ScoredMove move : moves) {
            if (move.getMove() == null) continue;
            counts.merge(trackOf(move, horizontalPaths), 1, Integer::sum);
        }
        return counts;
    }

    private int trackOf(Bot.ScoredMove move, boolean horizontalPaths) {
        return horizontalPaths ? move.getMove().getRow() : move.getMove().getCol();
    }

    private Polygon renderMove(Bot.ScoredMove move, boolean isBest, boolean horizontalPaths,
                               Map<Integer, Integer> trackCounts, Map<Integer, Integer> trackIndices) {
        int track = trackOf(move, horizontalPaths);
        int groupSize = trackCounts.get(track);
        int positionInGroup = trackIndices.merge(track, 1, Integer::sum) - 1;
        double lateralOffset = (positionInGroup - (groupSize - 1) / 2.0) * CONNECTOR_SPACING;

        Color color = isBest ? bestMoveColor : otherMoveColor;
        // removed: Polygon highlightedCell = isBest ? highlightBestCell(move) : null;

        Point2D cellCenter = cellCenterInOverlay(move.getMove());
        if (cellCenter != null) {
            drawWeightWithConnector(cellCenter, move.getScore(), color, horizontalPaths, lateralOffset);
        }
        return null; // we don't paint cells anymore
    }

    private void drawWeightWithConnector(Point2D endpoint, int score, Color color,
                                         boolean horizontalPaths, double lateralOffset) {
        ConnectorGeometry geometry = horizontalPaths
                ? horizontalConnector(endpoint, lateralOffset)
                : verticalConnector(endpoint, lateralOffset);

        addConnectorLine(geometry, color);
        addWeightLabel(geometry, score, color);
    }

    private ConnectorGeometry horizontalConnector(Point2D endpoint, double lateralOffset) {
        double y = endpoint.getY() + lateralOffset;
        double rightMarginX = computeRightMarginX();
        return new ConnectorGeometry(
                endpoint.getX(), y,
                rightMarginX, y,
                rightMarginX + LABEL_OFFSET_FROM_RIGHT_MARGIN, y + LABEL_BASELINE_OFFSET);
    }

    private ConnectorGeometry verticalConnector(Point2D endpoint, double lateralOffset) {
        double x = endpoint.getX() + lateralOffset;
        double topMarginY = computeTopMarginY();
        return new ConnectorGeometry(
                x, endpoint.getY(),
                x, topMarginY,
                x, topMarginY - LABEL_DROP_FROM_TOP);
    }

    private void addConnectorLine(ConnectorGeometry geometry, Color color) {
        Line line = new Line(geometry.lineStartX, geometry.lineStartY, geometry.lineEndX, geometry.lineEndY);
        line.setStroke(color);
        line.setStrokeWidth(CONNECTOR_STROKE_WIDTH);
        line.setMouseTransparent(true);
        overlayPane.getChildren().add(line);
        visuals.add(line);
    }

    private void addWeightLabel(ConnectorGeometry geometry, int score, Color color) {
        Text label = new Text(String.valueOf(score));
        label.setStyle(LABEL_STYLE);
        label.setFill(color);
        label.setX(geometry.labelX - label.getLayoutBounds().getWidth() / 2);
        label.setY(geometry.labelY);
        label.setMouseTransparent(true);
        overlayPane.getChildren().add(label);
        visuals.add(label);
    }

    private Point2D cellCenterInOverlay(Position position) {
        Polygon cell = (Polygon) shapeLayout.lookup("#" + position.getRawPosition());
        if (cell == null) return null;
        Point2D scene = cell.localToScene(
                cell.getBoundsInLocal().getCenterX(),
                cell.getBoundsInLocal().getCenterY());
        return overlayPane.sceneToLocal(scene);
    }

    private double computeRightMarginX() {
        double maxX = 0;
        for (Node node : shapeLayout.getChildren()) {
            if (!(node instanceof Polygon polygon)) continue;
            double x = rightEdgeXInOverlay(polygon);
            if (x > maxX) maxX = x;
        }
        return maxX + RIGHT_MARGIN_PADDING;
    }

    private double computeTopMarginY() {
        double minY = Double.MAX_VALUE;
        for (Node node : shapeLayout.getChildren()) {
            if (!(node instanceof Polygon polygon)) continue;
            double y = topEdgeYInOverlay(polygon);
            if (y < minY) minY = y;
        }
        return minY - TOP_MARGIN_PADDING;
    }

    private double rightEdgeXInOverlay(Polygon polygon) {
        Point2D scene = polygon.localToScene(
                polygon.getBoundsInLocal().getMaxX(),
                polygon.getBoundsInLocal().getCenterY());
        return overlayPane.sceneToLocal(scene).getX();
    }

    private double topEdgeYInOverlay(Polygon polygon) {
        Point2D scene = polygon.localToScene(
                polygon.getBoundsInLocal().getCenterX(),
                polygon.getBoundsInLocal().getMinY());
        return overlayPane.sceneToLocal(scene).getY();
    }

    private static class ConnectorGeometry {
        final double lineStartX, lineStartY;
        final double lineEndX, lineEndY;
        final double labelX, labelY;

        ConnectorGeometry(double lineStartX, double lineStartY,
                          double lineEndX, double lineEndY,
                          double labelX, double labelY) {
            this.lineStartX = lineStartX;
            this.lineStartY = lineStartY;
            this.lineEndX = lineEndX;
            this.lineEndY = lineEndY;
            this.labelX = labelX;
            this.labelY = labelY;
        }
    }
}