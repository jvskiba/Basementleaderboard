import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

interface sbActionListener {
    void updateText(int action, int v, int v2, String v3, String v4, String v5);
}

public class Controller implements sbActionListener {
    //Main Menu
    @FXML private HBox mmMainHBox;
    //Player Select
    @FXML private HBox psMainHBox;
    //Scoreboard
    @FXML private HBox quitHBox;
    @FXML private HBox restartButtonHBox;
    //LeaderBoard
    @FXML private HBox lbMainHBox;
    //General Game Input
    @FXML private HBox ggiMainHBox;

    @FXML private HBox spMainHBox;

    @FXML private Text updateBox;
    @FXML private Text p1Name;
    @FXML private Text p1Score;
    @FXML private Text p2Name;
    @FXML private Text p2Score;
    @FXML private Text maxScore;


    private ArrayList<Rectangle> buttonRects;

    @Override
    public void updateText(int action, int v, int v2, String v3, String v4, String v5) {
        switch (action) {
            case 0: updateSB(v, v2, v3, v4); break;
            case 2: p1Name.setText(v4); p2Name.setText(v5); break;
        }
    }

    public void updateSB(int p1S, int p2S, String max, String update) {
        p1Score.setText(String.valueOf(p1S));
        p2Score.setText(String.valueOf(p2S));
        maxScore.setText(String.valueOf(max));
        updateBox.setText(update);
    }

    public Pane getPane(int pane) {
        switch (pane) {
            case 0: return mmMainHBox;
            case 1: return psMainHBox;

            case 2: return quitHBox;
            case 3: return restartButtonHBox;

            case 4: return lbMainHBox;

            case 5: return ggiMainHBox;

            case 6: return spMainHBox;
        }
        return null;
    }
}
