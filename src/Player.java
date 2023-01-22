import javafx.scene.paint.Color;

public class Player {
    private long timePlayed;
    private final String firstName;
    private final String lastName;
    private final String nickname;
    private final Color color;
    private final int id;

    private final int[] winsPerGame = new int[4];
    private final int[] lossesPerGame = new int[4];
    private final int[] totalPlaysPerGame = new int[4];
    private final long[] totalTimePerGame = new long[4];
    private final int[] totalScore = new int[4];
    private final int[] highScore = new int[4];

    public Player(int idNum, String fn, String ln, String nn, Color c) {
        timePlayed = 0;
        firstName = fn;
        lastName = ln;
        nickname = nn;
        color = c;
        id = idNum;
    }

    public void updateStats(long gameDuration) {
        timePlayed += gameDuration;
    }

    public int getId() {return id;}
    public String getNickname() {return nickname;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}

    public void addWin(int gameId) {winsPerGame[gameId]++;}
    public void addLoss(int gameId) {lossesPerGame[gameId]++;}
    public void addPlayedGame(int gameId) {totalPlaysPerGame[gameId]++;}
    public void addTimePlayed(int gameId, long timePlayed) {totalTimePerGame[gameId] += timePlayed;}
    public void addScore(int gameId, int score) {totalScore[gameId] += score;}
    public void compareHighScore(int gameId, int score) {if (score > highScore[gameId]) {highScore[gameId] = score;}}

    public int getWins(int gameId) {return winsPerGame[gameId];}
    public int getLosses(int gameId) {return lossesPerGame[gameId];}
    public int getPlayedGames(int gameId) {return totalPlaysPerGame[gameId];}
    public long getTimePlayed(int gameId) {return totalTimePerGame[gameId];}
    public int getTotalScore(int gameId) {return totalScore[gameId];}
    public int getHighScore(int gameId) {return highScore[gameId];}
}
