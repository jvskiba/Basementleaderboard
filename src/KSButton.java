import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.function.Consumer;

class KSButton extends KeyBoardSelectableInputs{ //Keyboard Selectable Button

    public KSButton(String text, int f, Color c, Consumer<Integer> a, Pane lp) {
        defaultColor = c;
        currentColor = defaultColor;
        defaultText = text;
        currentText = defaultText;
        action = a;
        locationPane = lp;

        addRequiredElements(f);

        t.setText(currentText);

        double tWidth = t.getLayoutBounds().getWidth()+10;
        double tHeight = t.getLayoutBounds().getHeight()+1;

        r.setHeight(tHeight);
        r.setWidth(tWidth);
        r.setFill(currentColor);
        r.setStroke(currentColor);

        s = new StackPane();
        s.getChildren().addAll(r, t);
    }
}
