import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.function.Consumer;

public class KeyBoardSelectableInputs {
    boolean permaSelectable = false;
    Color defaultColor;
    Color currentColor;
    Color defaultColor2;
    Color strokeColor = Color.WHITE;
    String defaultText;
    String currentText;
    String inputtedText = "";
    Text t;
    Rectangle r;

    boolean selected = false;
    boolean hidden = true;
    boolean selectable = true;

    Consumer<Integer> action;
    StackPane s;
    Pane locationPane;

    public StackPane getStackPane() {return s;}
    public Pane getLocationPane() {return locationPane;}

    //public String getText() {return defaultText;}

    public void addRequiredElements(int f) {
        t = new Text();
        r = new Rectangle();
        r.setStrokeWidth(5);
        setupText(t, f, Color.WHITE);
    }

    public void resetButton() {
        currentText = defaultText;
        currentColor = defaultColor;
        selected = false;

        t.setText(currentText);
        r.setFill(currentColor);
        r.setStroke(currentColor);
    }

    public void setSelected(boolean v) {
        selected = v;
        if (selected) {
            r.setStroke(strokeColor);}
        else {
            r.setStroke(currentColor);}
    }

    public boolean getIfSelected() {return selected;}

    public void runAction() {
        if (selected) {
            action.accept(1);
        }
    }

    public void setupText(Text t, int fontSize, Color c) {
        t.setFont(new Font(fontSize));
        t.setFill(c);
        t.setTextAlignment(TextAlignment.CENTER);
    }

    public void addToScene() { if (hidden) {locationPane.getChildren().add(s); hidden = false;}}
    public void deleteFromScene() { if (!hidden) {locationPane.getChildren().remove(s); hidden = true;}}

    public boolean getIfHidden() {return hidden;}
    public boolean getSelectable() {return selectable;}

    public void setText(String v) {currentText = v; t.setText(currentText);}

    public void setColor2(Color c) { defaultColor2 = c; }
    public void setColor(boolean d) {
        if (r != null) {
            if (d) {
                currentColor = defaultColor;
            } else {
                currentColor = defaultColor2;
            }
            r.setFill(currentColor);
            if (!selected) {
                r.setStroke(currentColor);
            }
        }
    }

    public void setSelectability(boolean v) {selectable = v;}
    public void setPermaSelectability(boolean v) {permaSelectable = v;}
    public boolean getPermaSelectability() {return permaSelectable;}

    public String getCurrentText() {return currentText;}

    public void setSelectedColor(Color c){
        strokeColor = c;
    }

    public int getMaxCharAmount() {return 0;}
    public void setInpText(String v) {inputtedText = v; t.setText(v);}
    public String getInputtedText() {return inputtedText;}
    public void updateGraph(ObservableList<PieChart.Data> pieChartData) {}
}