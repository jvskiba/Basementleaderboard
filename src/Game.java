import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Game {
    int p1Score;
    int p2Score;
    Player p1;
    Player p2;
    boolean p1Win;
    boolean gameOver;
    LocalDateTime startTime;
    LocalDateTime endTime;
    long gameDuration;
    int gameType;

    public Game() {}

    public Game(int game, Player p1Obj, Player p2Obj) {
        gameType = game;
        p1 = p1Obj;
        p2 = p2Obj;
        gameOver = false;
        startTime = LocalDateTime.now();
    }

    public Game(int game) {
        gameType = game;
        gameOver = false;
        startTime = LocalDateTime.now();
    }

    public void addPlayer(boolean isP1, Player p) { if (isP1) {p1 = p;} else {p2 = p;} }

    public void addNullPlayer(boolean isP1) {if (isP1) {p1 = null;} else {p2 = null;}}

    public Score getScoreObj() {
        System.out.println(gameDuration);
        if (gameDuration == 0) {endTime = LocalDateTime.now(); gameDuration = startTime.until(endTime, ChronoUnit.SECONDS);}
        int p2Id = 0;
        if (p2 != null) {p2Id = p2.getId();}
        return new Score(gameType, true, p1.getId(), p2Id, p1Score, p2Score, gameDuration, startTime);
    }

    public Player getP1() {return p1;}
    public Player getP2() {return p2;}

    public void setP1Score(int v) {p1Score = v;}
    public void setP2Score(int v) {p2Score = v;}

    public int getGameType() {return gameType;}

    public void setGameDuration(long s) {gameDuration = s;}

    public Scoreboard toScoreBoard() {return new Scoreboard(p1, p2, 21);}
}
