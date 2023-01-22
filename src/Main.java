import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;

//Add popup/confirmation box
    //confirm game winning point
    //show popup if error

//make statistics pretty
//   add more stats
//fix ping pong stats
//Add big stats to first page
//   add recent stats too

//refactor menu for simplicity and to easily reset to default
//make input button reset on entering
//refactor input button coding for entering

//fix show button so only reloads needed buttons kinda?
//add page number to bottom of stack
//make menu all self contained
//fully integrate scoreboard into menu
//combine layouts playerselect and leaderboard


//compare player stats function
//optimize player stats by updateing only new scores not fully recalculating
//fix score grabber updating array code

//add date useage timeline/bar graph


//pi graph for people played against
//pi graph for most wins in top 10

//separate input and logic with events
//cleanup code
//comment code

public class Main extends Application {
    File configFile = new File("config.properties");
    ArrayList<String> config = new ArrayList<>();

    static Scoreboard sb;
    static Game game;
    static Stage pStage;
    static Scene mmScene, sbScene, psScene, lbScene, giScene, sScene;

    static ArrayList<ButtonMenu> buttonMenus;
    static int currentMenu;
    static ArrayList<Player> players;
    static int currentPlayerSelect;
    static String keyBoardInput;
    static boolean buttonChanged = false;
    static LeaderBoardService service;
    static Controller fxmlController;
    static int lbGameType = 0;
    static int statMode = 0;
    static boolean lbMode;
    static ArrayList<Score> scores = new ArrayList<>();
    static Score[] standOutStats = new Score[4];
    static NumberFormat myFormat;
    static int[] comparePlayerId = {-1, -1};
    static int[] generalStats = new int[4];

    static Color c1 = new Color(0.2, 0.5, 0.9, 1.0);
    static Color c2 = new Color(1.0, 0.0, 0.0, 1.0);
    static Color c3 = new Color(0.5, 0.2, 0.4, 1.0);


    private static final List<sbActionListener> listeners = new ArrayList<sbActionListener>();


    @Override
    public void start(Stage primaryStage) throws Exception{
        pStage = primaryStage;

        fxmlController = new Controller();
        buttonMenus = new ArrayList<>();
        
        myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        scores = new ArrayList<>();

        /*try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String host = props.getProperty("host");
            int i = 0;
            while (i < props.size()) {
                config.add(props.get(i).toString());
                i++;
            }

            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("file does not exist");
        } catch (IOException ex) {
            System.out.println("I/O error:");
        }*/

        //creates the different scenes for each page
        mmScene = getScene("MainMenuLayout.fxml", fxmlController); //main menu
        buttonMenus.add(new ButtonMenu(false, 10));
        sbScene = getScene("ScoreBoardLayout.fxml", fxmlController); //scoreboard
        buttonMenus.add(new ButtonMenu(false, 10));
        psScene = getScene("PlayerSelectLayout.fxml", fxmlController); //player select
        buttonMenus.add(new ButtonMenu(true, 10));
        lbScene = getScene("LeaderBoardLayout.fxml", fxmlController); //leaderboard
        buttonMenus.add(new ButtonMenu(false, 10));
        giScene = getScene("GenGameInputLayout.fxml", fxmlController); //game input
        buttonMenus.add(new ButtonMenu(false, 10));
        sScene = getScene("StatisticsLayout.fxml", fxmlController); //Statistics
        buttonMenus.add(new ButtonMenu(false, 10));
        setScene(0);
        primaryStage.show();

        addListener(fxmlController);

        service = new LeaderBoardService();

        //adds every button to their respective page

        //Main Menu
        buttonMenus.get(0).addEmptyStack(false, true, fxmlController.getPane(0));
        buttonMenus.get(0).addTextBoxInStack(0, "Main Menu", 60);
        buttonMenus.get(0).addButtonInStack(0,"New PingPong Game", 40, c1, i -> {startNewGame(0);});
        buttonMenus.get(0).addButtonInStack(0,"New PinBall Game", 40, c1, i -> {startNewGame(1);});
        buttonMenus.get(0).addButtonInStack(0,"New Pool Game", 40, c1, i -> {startNewGame(2);});
        buttonMenus.get(0).addButtonInStack(0,"New AirHockey Game", 40, c1, i -> {startNewGame(3);});
        buttonMenus.get(0).addButtonInStack(0,"View All Scores", 40, c1, i -> {startLeaderBoard(true);});
        buttonMenus.get(0).addButtonInStack(0,"View LeaderBoard", 40, c1, i -> {grabAllPlayers(); calculatePlayerStats(); startLeaderBoard(false);});
        buttonMenus.get(0).addButtonInStack(0,"View Statistics", 40, c1, i -> {startStatisticsPage();});
        buttonMenus.get(0).addTextBoxInStack(0,"", 40);
        buttonMenus.get(0).addEmptyStack(false, true, fxmlController.getPane(0));

        //Player Select
        buttonMenus.get(1).addEmptyStack(false, true, fxmlController.getPane(1));
        buttonMenus.get(1).addButtonInStack(0,"Quit", 40, c1, i -> {setScene(0);});
        buttonMenus.get(1).addButtonInStack(0, "Start SOLO", 40, c1, i -> {startGenInput(); });
        buttonMenus.get(1).hideButton(0, 1);
        buttonMenus.get(1).addEmptyStack(true, true, fxmlController.getPane(1));
        buttonMenus.get(1).addEmptyStack(false, true, fxmlController.getPane(1));
        buttonMenus.get(1).addTextBoxInStack(2, "First Name", 40);
        buttonMenus.get(1).addInputButtonInStack(2,10, 40, "FirstName Input", c1, c2, i -> {});
        buttonMenus.get(1).addTextBoxInStack(2, "Last Name", 40);
        buttonMenus.get(1).addInputButtonInStack(2, 10, 40, "LastName Input", c1, c2, i -> {});
        buttonMenus.get(1).addTextBoxInStack(2, "NickName", 40);
        buttonMenus.get(1).addInputButtonInStack(2, 3, 40, "NickName Input", c1, c2, i -> {});
        buttonMenus.get(1).addTextBoxInStack(2, "Color", 40);
        buttonMenus.get(1).addInputButtonInStack(2, 6, 40, "Color Input", c1, c2, i -> {});
        buttonMenus.get(1).addButtonInStack(2, "Submit", 40, c1, i -> {playerCreate();});

        //Scoreboard
        buttonMenus.get(2).addEmptyStack(false);
        buttonMenus.get(2).addButtonInStack(0,"Quit", 40, c1, i -> {setScene(0);}, fxmlController.getPane(2));
        buttonMenus.get(2).addEmptyStack(false);
        buttonMenus.get(2).addButtonInStack(1,"Restart", 40, c1, i -> {saveScoreBoard(); startScoreBoard(true);}, fxmlController.getPane(3));
        buttonMenus.get(2).addButtonInStack(1,"Reset", 40, c1, i -> {saveScoreBoard(); startScoreBoard(false);}, fxmlController.getPane(3));

        //LeaderBoard
        buttonMenus.get(3).addEmptyStack(false, true, fxmlController.getPane(4));
        buttonMenus.get(3).addButtonInStack(0,"Quit", 40, c1, i -> {setScene(0);});
        buttonMenus.get(3).addButtonInStack(0,"Ping Pong", 40, c1, true, c3, i -> {setGameType(0); changeButtonColor(3);});
        buttonMenus.get(3).addButtonInStack(0,"PinBall", 40, c1, true, c3, i -> {setGameType(1); changeButtonColor(3);});
        buttonMenus.get(3).addButtonInStack(0,"Pool", 40, c1, true, c3, i -> {setGameType(2); changeButtonColor(3);});
        buttonMenus.get(3).addButtonInStack(0,"AirHockey", 40, c1, true, c3, i -> {setGameType(3); changeButtonColor(3);});
        buttonMenus.get(3).addEmptyStack(true, true, fxmlController.getPane(4));
        buttonMenus.get(3).addEmptyStack(false, true, fxmlController.getPane(4));
        buttonMenus.get(3).addTextBoxInStack(2, "", 40);
        buttonMenus.get(3).addTextBoxInStack(2, "", 40);
        buttonMenus.get(3).addTextBoxInStack(2, "", 40);
        buttonMenus.get(3).addTextBoxInStack(2, "", 40);
        buttonMenus.get(3).addTextBoxInStack(2, "", 40);

        //General Game Input
        buttonMenus.get(4).addEmptyStack(false, true, fxmlController.getPane(5));
        buttonMenus.get(4).addButtonInStack(0,"Quit", 40, c1, i -> {setScene(0);});
        buttonMenus.get(4).addEmptyStack(false, true, fxmlController.getPane(5));
        buttonMenus.get(4).addTextBoxInStack(1, "Player 1", 40);
        buttonMenus.get(4).addInputButtonInStack(1,6, 40, "P1Score Input", c1, c2, i -> {});
        buttonMenus.get(4).addTextBoxInStack(1,"Player 2", 40);
        buttonMenus.get(4).addInputButtonInStack(1,6, 40, "P2Score Input", c1, c2, i -> {});
        buttonMenus.get(4).addTextBoxInStack(1,"Duration", 40);
        buttonMenus.get(4).addInputButtonInStack(1,6, 40, "Duration Input", c1, c2, i -> {});
        buttonMenus.get(4).addButtonInStack(1,"Submit", 40, c1, i -> {addGenGameScore();});

        //Stat page
        buttonMenus.get(5).addEmptyStack(false, true, fxmlController.getPane(6));
        buttonMenus.get(5).addButtonInStack(0,"Quit", 40, c1, i -> {setScene(0);});
        buttonMenus.get(5).addButtonInStack(0,"General Stats", 40, c1, i -> {setStatMode(0);});
        buttonMenus.get(5).addButtonInStack(0,"Player Stats", 40, c1, i -> {setStatMode(1);});
        buttonMenus.get(5).addEmptyStack(true, true, fxmlController.getPane(6));
        buttonMenus.get(5).addEmptyStack(false, true, fxmlController.getPane(6));
        buttonMenus.get(5).addTextBoxInStack(2, "", 40);
        buttonMenus.get(5).addTextBoxInStack(2, "", 40);
        buttonMenus.get(5).addTextBoxInStack(2, "", 40);
        buttonMenus.get(5).addTextBoxInStack(2, "", 40);
        buttonMenus.get(5).addTextBoxInStack(2, "", 40);
        buttonMenus.get(5).addEmptyStack(false, true, fxmlController.getPane(6));

        //buttonMenus.get(0).addGraphInStack(1, "Total Wins", 100, i -> {System.out.println("Graph");}, fxmlController.getPane(0));
        /*buttonMenus.get(0).updateGraphData(1, 0, FXCollections.observableArrayList(
                //new PieChart.Data("Jake", 25),
                new PieChart.Data("Derek", 75),
                //new PieChart.Data("Conor", 25),
                new PieChart.Data("Juliana", 25)));

         */

        /*ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Jake", 25),
                        new PieChart.Data("Derek", 25),
                        new PieChart.Data("Conor", 25),
                        new PieChart.Data("Juliana", 25));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Imported Fruits");*/
        //fxmlController.getPane(14).getChildren().add(chart);

        resetMenu(0);

        //handles user input
        EventHandler<KeyEvent> keyEventEventHandler = keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {buttonMenus.get(currentMenu).runButton();}
            else {
                if (!buttonMenus.get(currentMenu).getInputMode()) {
                    //normal operation - Runs Scoreboard and menu movement
                    switch (keyEvent.getCode()) {
                        case DIVIDE: sb.score(true); break;
                        case MULTIPLY: sb.score(false); break;
                        case SUBTRACT: sb.activateSubtractMode(); break;
                        case NUMPAD2: buttonMenus.get(currentMenu).moveMenuSelection(0); buttonChanged = true; break;
                        case NUMPAD4: buttonMenus.get(currentMenu).moveMenuSelection(1); buttonChanged = true; break;
                        case NUMPAD6: buttonMenus.get(currentMenu).moveMenuSelection(2); buttonChanged = true; break;
                        case NUMPAD8: buttonMenus.get(currentMenu).moveMenuSelection(3); buttonChanged = true; break;
                    }
                    keyBoardInput = "";
                } else {
                    //runs when input button is "activated"
                    keyBoardInput += keyEvent.getText();
                    buttonMenus.get(currentMenu).setImpText(keyBoardInput);
                }
                if (currentMenu == 2) {notifyListeners(0, sb.getP1Score(), sb.getP2Score(), sb.getMaxScoreString(), sb.getGameUpdate(), "");}
                buttonChanged = false;
            }
        };
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, keyEventEventHandler);
    }

    //Runs on start launches new class for special javafx stuff
    public static void main(String[] args) {
        launch();
    }

    public void addListener(sbActionListener listener) {
        listeners.add(listener);
    }

    public static void deleteStackFromMenu(int m, int s) {
        ButtonMenu menu = buttonMenus.get(m);
        if (!menu.getIfStackEmpty(s)) {
            menu.clearStack(s);
        }
    }

    //notifies the scoreboard to update the main labels
    private static void notifyListeners(int action, int v, int v2, String v3, String v4, String v5) {
        for (sbActionListener listener : listeners) {
            listener.updateText(action, v, v2, v3, v4, v5);
        }
    }
    //################################################################################################################################################
    private Scene getScene(String fxmlPath, Controller controller) {
        FXMLLoader loader;
        Parent parent;
        Scene scene;
        try {
            //not FXMLLoader.load(getClass().getResource(fxmlPath)
            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);
            parent = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        scene = new Scene(parent);

        return scene;

    }

    public static void startScoreBoard(boolean reusePlayers) {
        if (!reusePlayers) {
            currentPlayerSelect = 0;
            sb = new Scoreboard(21);
            setScene(1);
            populatePlayers();
        } else {
            sb = new Scoreboard(sb.getP1(), sb.getP2(), 21);
        }
        fxmlController.updateSB(sb.getP1Score(), sb.getP2Score(), sb.getMaxScoreString(), sb.getGameUpdate());
    }

    //runs both the leaderboard and score page
    public static void startLeaderBoard(boolean mode) {
        int menuID = 3;
        int stackID = 1;
        lbMode = mode;

        if (currentMenu != menuID) {
            setScene(menuID);
            lbGameType = 0;
        }
        deleteStackFromMenu(menuID, stackID);
        ArrayList<Score> scores = service.getAllScores(lbGameType);

        //mode true is all scores, false is players
        if (mode) {
            /*for (int i = 1; i < scores.size(); i++) {
                Score key = scores.get(i);
                int j = i - 1;
                while (j >= 0 && scores.get(j).getWinningScore() < key.getWinningScore()) {
                    //scores.set(j+1, scores.get(j));
                    j--;
                }
                System.out.println(j);
                //scores.set(j, key);
                scores.add(j + 1, key);
                scores.remove(i + 1);
            } */
            Collections.reverse(scores);
            final Score[] scoresFinal = scores.toArray(new Score[0]);

            for (Score score : scoresFinal) {

                Player p1 = service.getPlayerByID(score.getP1Id()).get(0);
                Player p2;
                if (score.getP2Id() > 0) {
                    p2 = service.getPlayerByID(score.getP2Id()).get(0);
                } else {
                    p2 = null;
                }

                Player winningPlayer = p1;
                Player losingPlayer = p2;
                int p1score = score.getP1Score();
                int p2score = score.getP2Score();
                if (!score.isWinnerP1()) {
                    winningPlayer = p2;
                    losingPlayer = p1;
                    int t2 = p1score;
                    p1score = p2score;
                    p2score = t2;
                }
                if (p2 != null) {
                    buttonMenus.get(menuID).addButtonInStack(stackID, winningPlayer.getNickname() + ":" + myFormat.format(p1score) + " - " + losingPlayer.getNickname() + ":" + p2score, 40, c1, true, c3, i -> {
                        System.out.println("Score");
                        expandScore(score, p1, p2);
                        changeButtonColor(menuID);
                    });
                } else {
                    buttonMenus.get(menuID).addButtonInStack(stackID, p1.getNickname() + ":" + myFormat.format(p1score), 40, c1, true, c3, i -> {
                        System.out.println("Score");
                        expandScore(score, p1, p2);
                        changeButtonColor(menuID);
                    });
                }
            }
        } else {
            //sorts player array by stat
            ArrayList<Player> p = players;                          //how do I separate????
            for (int i = 1; i < p.size(); i++) {
                Player key = p.get(i);
                int j = i - 1;
                if (lbGameType == 0) {
                    while (j >= 0 && p.get(j).getWins(lbGameType) < key.getWins(lbGameType)) {
                        j--;
                    }
                } else {
                    while (j >= 0 && p.get(j).getHighScore(lbGameType) < key.getHighScore(lbGameType)) {
                        j--;
                    }
                }
                p.add(j + 1, key);
                p.remove(i + 1);
            }
            int sortStat;
            for (Player p2 : players) {
                if (lbGameType == 0) {sortStat = p2.getWins(lbGameType);}
                else {sortStat = p2.getHighScore(lbGameType);}
                buttonMenus.get(menuID).addButtonInStack(stackID, p2.getNickname() + ":" + myFormat.format(sortStat), 40, c1, true, c3, i -> {
                    System.out.println("Player"); changeButtonColor(menuID); expandPlayer(p2);});
            }
        }
    }

    public static void startStatisticsPage() {
        int menuID = 5;
        int stackID = 1;
        int stackID2 = 3;

        setScene(5);
        buttonMenus.get(menuID).clearStack(stackID);
        buttonMenus.get(menuID).clearStack(stackID2);
        grabAllPlayers();
        calculatePlayerStats();

        switch (statMode) {
            case 0:
               //buttonMenus.get(menuID).addButtonInStack(stackID, "Total time played", 40, c1, true, c3, i -> {changeButtonColor(menuID);});
                displayGeneralStats();
                break;
            case 1:

                final Player[] playersFinal = players.toArray(new Player[0]);

                for (Player player : playersFinal) {
                    buttonMenus.get(menuID).addButtonInStack(stackID, player.getNickname(), 40, c1, true, c3, i -> {changeButtonColor(menuID); setPlayerToCompare(true, player.getId());});
                    buttonMenus.get(menuID).addButtonInStack(stackID2, player.getNickname(), 40, c1, true, c3, i -> {changeButtonColor(menuID); setPlayerToCompare(false, player.getId());});
                }
                break;
        }
    }

    public static void startGenInput() {
        buttonMenus.get(4).setTextBoxText(1, 0, game.getP1().getNickname());
        resetMenu(4);

        if (game.getP2() != null) {
            buttonMenus.get(4).showButton(1, 2);
            buttonMenus.get(4).showButton(1, 3);
            buttonMenus.get(4).setTextBoxText(1, 1, game.getP2().getNickname());
        } else {
            buttonMenus.get(4).hideButton(1, 2);
            buttonMenus.get(4).hideButton(1, 3);}
        if (game.getGameType() == 3) {
            buttonMenus.get(4).showButton(1, 4);
            buttonMenus.get(4).showButton(1, 5);
        } else {
            buttonMenus.get(4).hideButton(1, 4);
            buttonMenus.get(4).hideButton(1, 5);
        }
        setScene(4);
    }

    //#################################################################################################################################################
    public static void setGameType(int g) {
        lbGameType = g;
        startLeaderBoard(lbMode);
    }

    public static void expandScore(Score s, Player p1, Player p2) {
        buttonMenus.get(3).setTextBoxText(2, 0, "Date: " + s.getDate().toString());
        buttonMenus.get(3).setTextBoxText(2, 1, p1.getFirstName() + " " + p1.getLastName() + ": " + myFormat.format(s.getP1Score()));
        if (p2 != null) {buttonMenus.get(3).setTextBoxText(2, 2, p2.getFirstName() + " " + p2.getLastName() + ": " + myFormat.format(s.getP2Score()));}
        else {buttonMenus.get(3).setTextBoxText(2, 2, "Single Player");}
        buttonMenus.get(3).setTextBoxText(2, 3, "Duration:" + parseTime(s.getGameDuration()));
        buttonMenus.get(3).setTextBoxText(2, 4, "");
    }

    public static void expandPlayer(Player p) {
        buttonMenus.get(3).setTextBoxText(2, 0, "Total Games: " + myFormat.format(p.getPlayedGames(lbGameType)));
        buttonMenus.get(3).setTextBoxText(2, 1, "HS: " + myFormat.format(p.getHighScore(lbGameType)));
        if (p.getPlayedGames(lbGameType) > 0) {
            buttonMenus.get(3).setTextBoxText(2, 2, "WP: " + (100 * p.getWins(lbGameType) / (p.getWins(lbGameType) + p.getLosses(lbGameType))) + "%");
            buttonMenus.get(3).setTextBoxText(2, 3, "Avg: " + myFormat.format((p.getTotalScore(lbGameType) / p.getPlayedGames(lbGameType))));
        }
        buttonMenus.get(3).setTextBoxText(2, 4, "Total Duration:" + parseTime(p.getTimePlayed(lbGameType)));
    }

    public static void calculatePlayerStats() {
        generalStats = new int[4];
        for (Score s : getAllScores()) {
            Player p1 = getPlayerById(s.getP1Id());
            Player p2 = null;
            int gameId = s.getGameType();
            if (s.getP2Id() != 0) {p2 = getPlayerById(s.getP2Id());
                if (s.isWinnerP1()) {p1.addWin(gameId); p2.addLoss(gameId);}
                else {p2.addWin(gameId); p1.addLoss(gameId);}
                p2.addPlayedGame(gameId);
                p2.addTimePlayed(gameId, s.getGameDuration());
                p2.addScore(gameId, s.getP2Score());
                p2.compareHighScore(gameId, s.getP2Score());
            }
            p1.addPlayedGame(gameId);
            p1.addTimePlayed(gameId, s.getGameDuration());
            p1.addScore(gameId, s.getP1Score());
            p1.compareHighScore(gameId, s.getP1Score());
            //if (s.getWinningScore() > standOutStats[0].getWinningScore()) {standOutStats[0] = s;}
            generalStats[0]++;
            generalStats[1] = generalStats[1] + s.getGameDuration().intValue();
        }
        /*
        int gameType = 1;
        for (Player p : players) {
            System.out.println(p.getNickname() + ": " + p.getPlayedGames(gameType) + "-" + p.getWins(gameType) + "-" + p.getLosses(gameType) + "-" + p.getTimePlayed(gameType));
            if (p.getPlayedGames(gameType) > 0) {
            System.out.println("    HS: " + p.getHighScore(gameType) + " WP: " + (100*p.getWins(gameType)/(p.getWins(gameType)+p.getLosses(gameType))) + "% Avg: " + (p.getTotalScore(gameType)/p.getPlayedGames(gameType)) );}
        }
         */
    }

    public static void displayGeneralStats() {
        buttonMenus.get(5).setTextBoxText(2, 0, "Games Played: " + generalStats[0]);
        buttonMenus.get(5).setTextBoxText(2, 1, "Time Played: " + parseTime(generalStats[1]));
    }

    public static void setPlayerToCompare(boolean p1, int pId) {
        if (p1) {
            comparePlayerId[0] = pId;
        } else {
            comparePlayerId[1] = pId;
        }
        if (comparePlayerId[0] != -1 && comparePlayerId[1] != -1) {
            comparePlayers(comparePlayerId[0], comparePlayerId[1]);
        }
    }

    public static void comparePlayers(int p1Id, int p2Id) {
        ArrayList<Integer> stats = new ArrayList<Integer>();
        stats.add(0);
        stats.add(0);
        for (Score s : getAllScores()) {
            if ((s.getP1Id() == p1Id || s.getP1Id() == p2Id) && (s.getP2Id() == p1Id || s.getP2Id() == p2Id) ) {
                stats.set(0, stats.get(0) + 1);
                stats.set(1, stats.get(1) + s.getGameDuration().intValue());
            }
        }
        buttonMenus.get(5).setTextBoxText(2, 0, "Games Played: " + stats.get(0).toString());
        buttonMenus.get(5).setTextBoxText(2, 1,"Time Played: " + parseTime(stats.get(1)));
    }

    public static void startNewGame(int gameType) {
        setScene(1);
        populatePlayers();
        currentPlayerSelect = 0;

        if (gameType == 0) {
            game = new Scoreboard(21);
        } else {
            game = new Game(gameType);
        }
    }

    public static void addGenGameScore() {
        ArrayList<String> input = buttonMenus.get(4).getUserInputs();
        if (!input.get(0).equals("")) {game.setP1Score(Integer.parseInt(input.get(0)));}
        if (input.get(1) != null && !input.get(1).equals("")) {game.setP2Score(Integer.parseInt(input.get(1)));}
        if (game.getGameType() == 3) {game.setGameDuration(Integer.parseInt(input.get(2)) * 60L);}
        game.getScoreObj().printScore();                                                        //DeBug
        if (game.getScoreObj().getP1Score() > 0) {
            if (!service.addNewScore(game.getScoreObj())) {
                displayErrorMessage("Saving Score");}}
        setScene(0);
    }

    public static void displayErrorMessage(String msg) {
        String disptext = "Error: " + msg;
        System.out.println(disptext);
        buttonMenus.get(0).setTextBoxText(0,0, disptext);
        showPopup(false, disptext);
    }

    public static void showPopup(boolean requireConfirmation, String msg) {
        if (requireConfirmation) {
            //shows message and waits for response from yes/no buttons
        }
        else {
            //shows for 4 seconds then disappears
        }
    }


    public static ArrayList<Score> getAllScores() {
        if (scores.size() == 0) {
            scores = service.getAllScores(0);
            scores.addAll(service.getAllScores(1));
            scores.addAll(service.getAllScores(2));
            scores.addAll(service.getAllScores(3));
        } else {
            //Compares highest score id if less than database increasingly retrieves scores till caught up
            int i = scores.size();
            Score newScore = service.getScore(i);
            while (scores.get(i - 1).getId() < newScore.getId()) {
                scores.add(newScore);
                i++;
                newScore = service.getScore(i);
            }
        }
        return scores;
    }

    public static Player getPlayerById(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public static void setStatMode(int mode) {
        statMode = mode;
        startStatisticsPage();
    }

    public static void saveScoreBoard() {
        sb.getScoreObj().printScore();
        if (!service.addNewScore(sb.getScoreObj())) {
            displayErrorMessage("Saving Score");}
    }

    public static void grabAllPlayers() {players = service.getAllPlayers();}

    public static void populatePlayers() {
        int menuID = 1;
        int stackID = 1;

        grabAllPlayers();

        deleteStackFromMenu(menuID, stackID);

        final Player[] playersFinal = players.toArray(new Player[0]);

        buttonMenus.get(1).addTextBoxInStack(1, "Select a player", 50);
        for (Player player : playersFinal) {
            buttonMenus.get(menuID).addButtonInStack(stackID, player.getNickname(), 40, c1, true, c3, i -> {
                selectPlayer(player); changeButtonColor(menuID);});
        }
    }

    public static void setScene(int v) {
        resetMenu(v);
        switch (v) {
            case 0: pStage.setScene(mmScene); break;
            case 1: pStage.setScene(psScene); break;
            case 2: pStage.setScene(sbScene); break;
            case 3: pStage.setScene(lbScene); break;
            case 4: pStage.setScene(giScene); break;
            case 5: pStage.setScene(sScene); break;
        }
         currentMenu = v;
    }

    //adds the player given to the game then continues defending on game type
    public static void selectPlayer(Player p) {
        buttonMenus.get(1).hideButton(0, 1);
        System.out.println(p.getNickname());
        switch (currentPlayerSelect) {
            case 0 : game.addPlayer(true, p);
                currentPlayerSelect++;
                if (game.getGameType() != 0) {
                    buttonMenus.get(1).showButton(0, 1);
                }
                break;
            case 1 : game.addPlayer(false, p); currentPlayerSelect++;
                if (game.getGameType() == 0) {
                    setScene(2);
                    notifyListeners(2, 0, 0, "", game.getP1().getNickname(), game.getP2().getNickname());
                    sb = game.toScoreBoard(); //not how i want to really do it
                    notifyListeners(0, sb.getP1Score(), sb.getP2Score(), sb.getMaxScoreString(), sb.getGameUpdate(), "");
                } else {
                    startGenInput();
                }
            break;
            case 2 : System.out.println("Both players have already been selected"); break;
        }
    }



    //creates player and adds them to the game
    public static void playerCreate() {
        ArrayList<String> input = buttonMenus.get(1).getUserInputs();
        if (!service.addNewPlayer(input.get(0), input.get(1), input.get(2))) {displayErrorMessage("Saving Player");}
        ArrayList<Player> p = service.getAllPlayers();
        selectPlayer(p.get(p.size() - 1));
        buttonMenus.get(1).resetMenu();
    }

    public static void changeButtonColor(int menu) {
        //if (menu == 3 || menu == 5) {buttonMenus.get(menu).resetButtonColors();}
        //buttonMenus.get(menu).chooseButtonColor(false);
    }

    public static String parseTime(long sec) {
        return " " + sec/3600 + ":" + (sec%3600)/60 + ":" + sec%60;
    }

    public static void resetMenu(int m) {
        buttonMenus.get(m).resetMenu();

        switch (m) {
            case 3: buttonMenus.get(3).setButtonColor(false, 0, 1); break;
        }
    }


}