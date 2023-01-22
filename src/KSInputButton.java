import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.function.Consumer;

class KSInputButton extends KeyBoardSelectableInputs { //Keyboard Selectable Button
    Color defaultColor3;
    int maxCharAmount;

    public KSInputButton(int width, int font, String n, Color c, Color c2, Consumer<Integer> a, Pane lp) {
        defaultColor = c;
        currentColor = defaultColor;
        defaultColor3 = c2;
        action = a;
        defaultText = n;
        currentText = "";
        maxCharAmount = width;
        locationPane = lp;

        addRequiredElements(font);

        t.setText("G");

        r.setHeight(t.getLayoutBounds().getHeight()+1);
        r.setWidth(maxCharAmount * t.getLayoutBounds().getWidth()+10);
        r.setFill(defaultColor);

        r.setStroke(defaultColor3);

        t.setText(currentText);
        s = new StackPane();
        s.getChildren().addAll(r, t);
    }

    public void setInpText(String v) {
        if (v.length() <= maxCharAmount) {
            inputtedText = v; t.setText(v);
        }
    }

    public int getMaxCharAmount() {return maxCharAmount;}

    public String getInputtedText() {return inputtedText;}

    public void runAction() {
        if (selected) {
            action.accept(1);
        } else {
            inputtedText = "";
            t.setText(inputtedText);
        }
    }
}
