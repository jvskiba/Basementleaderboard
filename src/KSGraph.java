import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.function.Consumer;

public class KSGraph extends KeyBoardSelectableInputs {
    Circle c;
    PieChart chart;

    public KSGraph(boolean type, String text, int r, ObservableList<PieChart.Data> pieChartData, Consumer<Integer> a, Pane lp) {
        action = a;
        locationPane = lp;
        chart = new PieChart(pieChartData);
        chart.setTitle(text);
        chart.resize(50, 50);

        selectable = false;

        c = new Circle();
        c.setRadius(r);

        s = new StackPane();
        s.getChildren().addAll(c, chart);
    }

    public void setSelected(boolean v) {
        selected = v;
        if (selected) {
            c.setStroke(Color.WHITE);}
        else {
            c.setStroke(currentColor);}
    }

    public void updateGraph(ObservableList<PieChart.Data> pieChartData) {
        chart.setData(pieChartData);
    }

    public void resetButton() {
        selected = false;

        c.setFill(currentColor);
        c.setStroke(currentColor);
    }
}
