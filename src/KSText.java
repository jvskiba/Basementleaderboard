import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class KSText extends KeyBoardSelectableInputs{
    public KSText(String text, int f, Pane lp) {
        defaultText = text;
        locationPane = lp;

        addRequiredElements(f);

        currentText = defaultText;
        t.setText(currentText);

        selectable = false;

        s = new StackPane();
        s.getChildren().addAll(t);

        hidden = false;
        selectable = false;
        locationPane.getChildren().add(s);
    }
}
