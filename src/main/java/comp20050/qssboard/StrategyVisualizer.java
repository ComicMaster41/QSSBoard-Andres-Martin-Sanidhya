package comp20050.qssboard;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class StrategyVisualizer {

    private final Pane overlayPane;
    private final Group shapeLayout;
    private final java.util.function.Supplier<GameState.Player> botSeatSupplier;

    private Polygon lastBestCell;
    private Position hintMove;

    private final Color colorShowStrategy = Color.GREEN;

    private final ArrayList<Node> strategyVisuals = new ArrayList<>();

    public StrategyVisualizer(Pane overlayPane, Group shapeLayout,
                              java.util.function.Supplier<GameState.Player> botSeatSupplier) {
        this.overlayPane = overlayPane;
        this.shapeLayout = shapeLayout;
        this.botSeatSupplier = botSeatSupplier;
    }

    public Position getHintMove() {
        return hintMove;
    }

    public void computeAndShow(Bot bot, GameState state, Position moveMadeId, boolean show) {
        Position nextMove = bot.chooseMove();
        hintMove = nextMove;
        if (show && hintMove != null) {
            lastBestCell = drawStrategy(bot, state, moveMadeId);
        }
    }

    public void show(GameState state) {
        if (hintMove == null) return;
        Polygon cell = (Polygon) shapeLayout.lookup("#" + hintMove.getRawPosition());
        if (cell == null) return;
        cell.setFill(colorShowStrategy);
        lastBestCell = cell;
    }

    public void clear(GameState state, Color colorP1, Color colorP2) {
        if (lastBestCell != null) {
            Position p = new Position(lastBestCell.getId());
            QuaxBoard.TileOwner owner = state.getGameBoard().getTileOwner(p.getRow(), p.getCol());
            if (owner == null) {
                lastBestCell.setFill(javafx.scene.paint.Paint.valueOf(p.getCol() % 2 == 0 ? "#c98c07" : "#ffb91f"));
            } else {
                lastBestCell.setFill(owner == state.getGameBoard().getColor(GameState.Player.P1) ? colorP1 : colorP2);
            }
            lastBestCell = null;
        }
        hintMove = null;
        clearVisuals();
    }

    public void clearVisuals() {
        for (Node node : strategyVisuals) {
            overlayPane.getChildren().remove(node);
        }
        strategyVisuals.clear();
    }

    public Polygon drawStrategy(Bot bot, GameState state, Position moveMadeId) {
        if (moveMadeId == null) return null;

        QuaxBoard.TileOwner botColor = state.getGameBoard().getColor(botSeatSupplier.get());
        boolean horizontalPaths = (botColor == QuaxBoard.TileOwner.WHITE);

        double rightMarginX = computeRightMarginX();
        double topMarginY   = computeTopMarginY();

        ArrayList<Bot.ScoredMove> moves = bot.getScoredMoves();
        double spacing = 12.0;

        java.util.Map<Integer, Integer> trackCount = new java.util.HashMap<>();
        for (Bot.ScoredMove m : moves) {
            if (m.getMove() == null) continue;
            int track = horizontalPaths ? m.getMove().getRow() : m.getMove().getCol();
            trackCount.merge(track, 1, Integer::sum);
        }

        java.util.Map<Integer, Integer> trackIndex = new java.util.HashMap<>();
        Polygon returnPoly = null;

        for (int index = moves.size() - 1; index >= 0; index--) {
            Polygon drawn = drawScoredMove(
                    moves.get(index), index == 0,
                    horizontalPaths, trackCount, trackIndex,
                    spacing, rightMarginX, topMarginY
            );
            if (index == 0) returnPoly = drawn;
        }

        return returnPoly;
    }

    private Polygon drawScoredMove(Bot.ScoredMove m, boolean isBest,
                                   boolean horizontalPaths,
                                   java.util.Map<Integer, Integer> trackCount,
                                   java.util.Map<Integer, Integer> trackIndex,
                                   double spacing, double rightMarginX, double topMarginY) {
        if (m.getMove() == null) return null;

        int track = horizontalPaths ? m.getMove().getRow() : m.getMove().getCol();
        int groupSize = trackCount.get(track);
        int posInGroup = trackIndex.merge(track, 1, Integer::sum) - 1;
        double lateralOffset = (posInGroup - (groupSize - 1) / 2.0) * spacing;

        Color pathColor = isBest ? Color.GREEN : Color.RED;
        Polygon returnPoly = null;

        if (isBest) {
            Polygon bestCell = (Polygon) shapeLayout.lookup("#" + m.getMove().getRawPosition());
            if (bestCell != null) {
                bestCell.setFill(colorShowStrategy);
                returnPoly = bestCell;
            }
        }

        Point2D center = cellCenterInOverlay(m.getMove());
        if (center != null) {
            drawWeightWithConnector(center, m.getScore(), pathColor,
                    horizontalPaths, rightMarginX, topMarginY, lateralOffset);
        }

        return returnPoly;
    }

    private void drawWeightWithConnector(Point2D endpoint, int score, Color color, boolean horizontalPaths,
                                         double rightMarginX, double topMarginY, double lateralOffset) {
        double[] coords = computeConnectorCoordinates(endpoint, horizontalPaths, rightMarginX, topMarginY, lateralOffset);
        double lineStartX = coords[0], lineStartY = coords[1];
        double lineEndX   = coords[2], lineEndY   = coords[3];
        double labelX     = coords[4], labelY     = coords[5];

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

    private double[] computeConnectorCoordinates(Point2D endpoint, boolean horizontalPaths,
                                                 double rightMarginX, double topMarginY, double lateralOffset) {
        double lineStartX, lineStartY, lineEndX, lineEndY, labelX, labelY;

        if (horizontalPaths) {
            lineStartX = endpoint.getX();
            lineStartY = endpoint.getY() + lateralOffset;
            lineEndX   = rightMarginX;
            lineEndY   = endpoint.getY() + lateralOffset;
            labelX     = rightMarginX + 6;
            labelY     = endpoint.getY() + lateralOffset + 5;
        } else {
            lineStartX = endpoint.getX() + lateralOffset;
            lineStartY = endpoint.getY();
            lineEndX   = endpoint.getX() + lateralOffset;
            lineEndY   = topMarginY;
            labelX     = endpoint.getX() + lateralOffset;
            labelY     = topMarginY - 15;
        }

        return new double[]{ lineStartX, lineStartY, lineEndX, lineEndY, labelX, labelY };
    }

    private Point2D cellCenterInOverlay(Position p) {
        Polygon cell = (Polygon) shapeLayout.lookup("#" + p.getRawPosition());
        if (cell == null) return null;
        Point2D scene = cell.localToScene(
                cell.getBoundsInLocal().getCenterX(),
                cell.getBoundsInLocal().getCenterY()
        );
        return overlayPane.sceneToLocal(scene);
    }

    private double computeRightMarginX() {
        double maxX = 0;
        for (Node node : shapeLayout.getChildren()) {
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
        return maxX + 20;
    }

    private double computeTopMarginY() {
        double minY = Double.MAX_VALUE;
        for (Node node : shapeLayout.getChildren()) {
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
}
