import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Scoreboard extends Game{
    int combinedScore;
    int maxScore;
    String gameUpdate = "";
    boolean subtractMode = false;

    public Scoreboard(Player p1Obj, Player p2Obj, int max) {
        p1 = p1Obj;
        p2 = p2Obj;
        maxScore = max;
        gameOver = false;
        startTime = LocalDateTime.now();
        gameType = 0;
        System.out.println(maxScore + "A");
    }

    public Scoreboard(int max) {
        maxScore = max;
        gameOver = false;
        startTime = LocalDateTime.now();
        gameType = 0;
        System.out.println(maxScore);
    }

    public void addPlayer(boolean isP1, Player p) { if (isP1) {p1 = p;} else {p2 = p;} }

    //adds to the score and then calculates if other if any stats changed/game over
    public boolean score(boolean p) {
        if (!gameOver) {
            if (!subtractMode) {
                if (p) { p1Score++;}
                else { p2Score++; }
            } else {
                if (p) { p1Score--; }
                else { p2Score--; }
                subtractMode = false;
            }

            if (p1Score == p2Score && p1Score == maxScore - 1) {
                maxScore++;
            }

            if (p1Score == maxScore) {
                p1Win = true;
                gameOver = true;
            } else if (p2Score == maxScore) {
                p1Win = false;
                gameOver = true;
            }
            combinedScore = p1Score + p2Score;

            if (combinedScore % 5 == 0) {
                gameUpdate = "Switch Server";
            } else if(p1Score == maxScore - 1 || p2Score == maxScore - 1) {
                gameUpdate = "Game Point";
            } else {
                gameUpdate = "";
            }

            if (gameOver) {
                endTime = LocalDateTime.now();
                gameDuration = startTime.until(endTime, ChronoUnit.SECONDS);
                gameUpdate = "Game Over";
            }
        }
        return gameOver;
    }

    public Score getScoreObj() {
        return new Score(0, true, p1.getId(), p2.getId(), p1Score, p2Score, gameDuration, startTime);
    }

    public void activateSubtractMode() { subtractMode = true; }

    public int getP1Score() {return p1Score;}
    public int getP2Score() {return p2Score;}

    public int getMaxScore() {return maxScore;}
    public String getMaxScoreString() {return "" + maxScore;}

    public String getGameUpdate() {return gameUpdate;}

    public Player getP1() {return p1;}
    public Player getP2() {return p2;}
}