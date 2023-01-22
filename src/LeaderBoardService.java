import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class LeaderBoardService {
    Connection conn1 = null;
    String url1 = "jdbc:mysql://localhost:3306/leaderboarddatabase";
    String user = "root";
    String password = "1234567890";
    Statement smt;

    boolean sendValues = true;

    public LeaderBoardService() {
        startConnection();
    }

    public void startConnection() {
        try {
             conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database");

                smt = conn1.createStatement();
                //statement.executeUpdate("INSERT INTO Game " + "VALUES (0, 'PingPong')");
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
    }

    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> p = new ArrayList<>();
        try {
            ResultSet rs = smt.executeQuery("SELECT * FROM player");
            while (rs.next()) {
                p.add(new Player(rs.getInt("PlayerID"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("NickName"), new Color(1.0, 1.0, 1.0, 1.0)));
            }
            return p;
        } catch (SQLException ex) {
            handleException(ex);
            return null;
        }
    }

    public ArrayList<Player> getPlayerByID(int id) {
        ArrayList<Player> p = new ArrayList<>();
        try {
            ResultSet rs = smt.executeQuery("SELECT * FROM player WHERE PlayerID = '" +id+ "'");
            while (rs.next()) {
                p.add(new Player(rs.getInt("PlayerID"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("NickName"), new Color(1.0, 1.0, 1.0, 1.0)));
            }
            return p;
        } catch (SQLException ex) {
            handleException(ex);
            return null;
        }
    }

    public boolean addNewPlayer(String fn, String ln, String nn) {
        if (fn != null && ln != null && nn != null && sendValues) {
            try {
                smt.executeUpdate("INSERT INTO player " + "VALUES (0, '" + fn + "', '" + ln + "', '" + nn + "', 0)");
                return true;
            } catch (SQLException ex) {
                handleException(ex);
            }
        }
        return false;
    }

    public boolean addNewScore(Score s) {
        if (s != null && sendValues) {
            try {
                PreparedStatement pStmt = conn1.prepareStatement("<query>");
                //pStmt.setDate(1, Date.valueOf(s.getDate()));
                smt.executeUpdate("INSERT INTO score " + "VALUES (" + s.getGameType() + ", '" + s.getDate().toString() + "', " + s.getGameDuration() + ", "
                        + s.getP1Id() + ", " + s.getP2Id() + ", " + s.getP1Score() + ", " + s.getP2Score() + ", 0)");
                return true;
            } catch (SQLException ex) {
                handleException(ex);
            }
        }
        return false;
    }

    public ArrayList<Score> getAllScores(int gameType) {
        ArrayList<Score> s = new ArrayList<>();
        try {
            ResultSet rs = smt.executeQuery("SELECT * FROM score WHERE GameID = '" + gameType +"'");
            while (rs.next()) {
                s.add(new Score(rs.getInt("GameID"), true, rs.getInt("P1ID"), rs.getInt("P2ID"), rs.getInt("P1Score"), rs.getInt("P2Score"), rs.getInt("Duration"), rs.getObject("Date", LocalDateTime.class)));
            }
            return s;
        } catch (SQLException ex) {
            handleException(ex);
            return null;
        }
    }

    public Score getScore(int scoreID) {
        Score s = null;
        try {
            ResultSet rs = smt.executeQuery("SELECT * FROM score WHERE ScoreId = '" + scoreID + "'");
            while (rs.next()) {
                s = new Score(rs.getInt("GameID"), true, rs.getInt("P1ID"), rs.getInt("P2ID"), rs.getInt("P1Score"), rs.getInt("P2Score"), rs.getInt("Duration"), rs.getObject("Date", LocalDateTime.class));
            }
            return s;
        } catch (SQLException ex) {
            handleException(ex);
            return null;
        }
    }

    public ArrayList<String> getAllGames() {
        ArrayList<String> g = new ArrayList<>();
        try {
            ResultSet rs = smt.executeQuery("SELECT * FROM game");
            while (rs.next()) {
                g.add(rs.getString("Name"));
            }
            return g;
        } catch (SQLException ex) {
            handleException(ex);
            return null;
        }
    }

    private void handleException(SQLException ex) {
        System.out.println("An error occurred.");
        ex.printStackTrace();
    }
}
