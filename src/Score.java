import java.time.LocalDateTime;

public class Score {
    private final int gameType; //0-PingPong  1-Pool  2-Pinball   3-AirHockey
    private final boolean twoPlayers;
    private final int p1Score;
    private final int p2Score;
    private final int p1Id;
    private final int p2Id;
    private final long gameDuration;
    private final LocalDateTime date;
    private final int scoreId;

    //for newly created scores, -1 = assigned yet
    public Score(int gt, boolean tp, int p1Id, int p2Id, int p1S, int p2S, long gd, LocalDateTime d) {
        gameType = gt;
        twoPlayers = tp;
        this.p1Id = p1Id;
        this.p2Id = p2Id;
        p1Score = p1S;
        p2Score = p2S;
        gameDuration = gd;
        date = d;
        scoreId = -1;
    }
    //For loaded in scores
    public Score(int gt, boolean tp, int p1Id, int p2Id, int p1S, int p2S, long gd, LocalDateTime d, int sId) {
        gameType = gt;
        twoPlayers = tp;
        this.p1Id = p1Id;
        this.p2Id = p2Id;
        p1Score = p1S;
        p2Score = p2S;
        gameDuration = gd;
        date = d;
        scoreId = sId;
    }

    public void printScore() {
        System.out.println(p1Id + "-" + p1Score + "  " + p2Id + "-" + p2Score + "  " + gameDuration + " d-" + date);
    }

    public int getP1Id() {return p1Id;}
    public int getP2Id() {return p2Id;}
    public int getP1Score() {return p1Score;}
    public int getP2Score() {return p2Score;}
    public int getId() {return scoreId;}
    public int getGameType() {return gameType;}
    public Long getGameDuration() {return gameDuration;}
    public LocalDateTime getDate() {return date;}
    public boolean isWinnerP1() {return p1Score >= p2Score;}
    public int getWinningScore() {
        if (isWinnerP1()) {return p1Score;}
        else {return p2Score;}
    }
}

/*public class PingPongScore extends Score {
    public PingPongScore(int gt, boolean tp, int p1Id, int p2Id, int p1S, int p2S, long gd, LocalDateTime d) {
        super(gt, tp, p1Id, p2Id, p1S, p2S, gd, d);
    }
}*/
