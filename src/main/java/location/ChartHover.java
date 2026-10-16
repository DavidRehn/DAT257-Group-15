
package location;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.Locale;

public class ChartHover {

    public static StackPane create(
            XYChart<String, Number> chart,
            XYChart.Series<String, Number> series) {

        // Main chart container
        StackPane container = new StackPane();
        container.getChildren().add(chart);

        // Transparent layer above the chart
        Pane overlay = new Pane();
        overlay.setMouseTransparent(true);

        container.getChildren().add(overlay);

        StackPane.setAlignment(
                overlay,
                Pos.TOP_LEFT
        );

        // Vertical guide line
        Line guide = new Line();
        guide.getStyleClass().add("chart-hover-guide");
        guide.setVisible(false);
        guide.setManaged(false);

        // Tooltip labels
        Label timeLabel = new Label();
        timeLabel.getStyleClass().add("chart-hover-time");

        Label valueLabel = new Label();
        valueLabel.getStyleClass().add("chart-hover-value");

        // Tooltip card
        VBox popup = new VBox(4);
        popup.getChildren().addAll(
                timeLabel,
                valueLabel
        );

        popup.getStyleClass().add("chart-hover-card");
        popup.setPrefWidth(130);
        popup.setManaged(false);
        popup.setMouseTransparent(true);
        popup.setVisible(false);
        popup.setOpacity(0);

        overlay.getChildren().addAll(
                guide,
                popup
        );

        // Smooth tooltip animation
        FadeTransition fade = new FadeTransition(
                Duration.millis(160),
                popup
        );

        // Connect every chart point
        for (XYChart.Data<String, Number> item
                : series.getData()) {

            item.nodeProperty().addListener(
                    (obs, oldNode, newNode) -> {

                        if (newNode != null) {
                            setupPoint(
                                    newNode,
                                    item,
                                    chart,
                                    overlay,
                                    guide,
                                    popup,
                                    timeLabel,
                                    valueLabel,
                                    fade
                            );
                        }
                    }
            );

            if (item.getNode() != null) {

                setupPoint(
                        item.getNode(),
                        item,
                        chart,
                        overlay,
                        guide,
                        popup,
                        timeLabel,
                        valueLabel,
                        fade
                );
            }
        }

        return container;
    }

    private static void setupPoint(
            Node point,
            XYChart.Data<String, Number> item,
            XYChart<String, Number> chart,
            Pane overlay,
            Line guide,
            VBox popup,
            Label timeLabel,
            Label valueLabel,
            FadeTransition fade) {

        // Mouse enters a chart point
        point.setOnMouseEntered(event -> {

            // Show real time and watts
            timeLabel.setText(item.getXValue());

            valueLabel.setText(
                    String.format(
                            Locale.US,
                            "%.1f W",
                            item.getYValue().doubleValue()
                    )
            );

            // Find point location
            Bounds bounds = point.localToScene(
                    point.getBoundsInLocal()
            );

            double centerX =
                    (bounds.getMinX() + bounds.getMaxX()) / 2;

            double centerY =
                    (bounds.getMinY() + bounds.getMaxY()) / 2;

            Point2D position = overlay.sceneToLocal(
                    centerX,
                    centerY
            );

            double x = position.getX();
            double y = position.getY();

            // Keep tooltip inside chart
            double popupX = Math.max(
                    10,
                    Math.min(
                            x - 65,
                            overlay.getWidth() - 140
                    )
            );

            double popupY = Math.max(
                    10,
                    y - 85
            );

            popup.relocate(popupX, popupY);

            // Show vertical guide line
            Node plot = chart.lookup(
                    ".chart-plot-background"
            );

            if (plot != null) {

                Bounds plotBounds = plot.localToScene(
                        plot.getBoundsInLocal()
                );

                Point2D top = overlay.sceneToLocal(
                        plotBounds.getMinX(),
                        plotBounds.getMinY()
                );

                Point2D bottom = overlay.sceneToLocal(
                        plotBounds.getMinX(),
                        plotBounds.getMaxY()
                );

                guide.setStartX(x);
                guide.setEndX(x);
                guide.setStartY(top.getY());
                guide.setEndY(bottom.getY());

                guide.setVisible(true);
            }

            // Smooth tooltip appearance
            fade.stop();
            fade.setOnFinished(null);

            popup.setVisible(true);

            fade.setFromValue(popup.getOpacity());
            fade.setToValue(1);
            fade.play();

            // Enlarge the hovered point
            ScaleTransition grow = new ScaleTransition(
                    Duration.millis(150),
                    point
            );

            grow.setToX(1.5);
            grow.setToY(1.5);
            grow.play();
        });

        // Mouse leaves the point
        point.setOnMouseExited(event -> {

            // Hide guide line
            guide.setVisible(false);

            // Fade tooltip away
            fade.stop();

            fade.setFromValue(popup.getOpacity());
            fade.setToValue(0);

            fade.setOnFinished(e ->
                    popup.setVisible(false)
            );

            fade.play();

            // Return point to normal size
            ScaleTransition shrink = new ScaleTransition(
                    Duration.millis(150),
                    point
            );

            shrink.setToX(1);
            shrink.setToY(1);
            shrink.play();
        });
    }
}
