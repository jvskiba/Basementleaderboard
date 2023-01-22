import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ButtonMenu {
    private int buttonStack = 0;
    private int buttonItem = 0;
    private final ArrayList<ArrayList<KeyBoardSelectableInputs>> kbsiArray;
    private final boolean inverted;
    private final ArrayList<Integer> currentStackPage;
    private final ArrayList<Boolean> dispStackPage;
    private final int buttonsPerPage;
    private boolean inputMode = false;
    private ArrayList<Pane> stackPanes;
    private ArrayList<Boolean> usePreMadePane;

    public ButtonMenu(boolean i, int bPP) {
        kbsiArray = new ArrayList<>();
        currentStackPage = new ArrayList<>();
        dispStackPage = new ArrayList<>();
        stackPanes = new ArrayList<>();
        usePreMadePane = new ArrayList<>();
        inverted = i; //Stacks by default are columns, when inverted the stack becomes a row
        buttonsPerPage = bPP;
    }

    public void addButtonInStack(int stack, String text, int f, Color c, Consumer<Integer> a, Pane lp) {
        kbsiArray.get(stack).add(new KSButton(text, f, c, a, lp));
        addToDisplay(stack, kbsiArray.get(stack).size() - 1);
    }
    public void addButtonInStack(int stack, String text, int f, Color c, Consumer<Integer> a) {
        addButtonInStack(stack, text, f, c, a, stackPanes.get(stack));
    }
    public void addButtonInStack(int stack, String text, int f, Color c, boolean permaSelect, Color c2, Consumer<Integer> a, Pane lp) {
        addButtonInStack(stack, text, f, c, a, lp);
        kbsiArray.get(stack).get(kbsiArray.get(stack).size() - 1).setColor2(c2);
        kbsiArray.get(stack).get(kbsiArray.get(stack).size() - 1).setPermaSelectability(permaSelect);
    }
    public void addButtonInStack(int stack, String text, int f, Color c, boolean permaSelect, Color c2, Consumer<Integer> a) {
        addButtonInStack(stack, text, f, c, permaSelect, c2, a, stackPanes.get(stack));
    }

    public void addInputButtonInStack(int stack, int v, int f, String text, Color c, Color c2, Consumer<Integer> a, Pane lp) {
        kbsiArray.get(stack).add(new KSInputButton( v, f, text, c, c2, a, lp));
        addToDisplay(stack, kbsiArray.get(stack).size() - 1);
    }
    public void addInputButtonInStack(int stack, int v, int f, String n, Color c, Color c2, Consumer<Integer> a) {
        addInputButtonInStack(stack, v, f, n, c, c2, a, stackPanes.get(stack));
    }

    public void addTextBoxInStack(int stack, String text, int f, Pane lp) {
        kbsiArray.get(stack).add(new KSText(text, f, lp));
    }
    public void addTextBoxInStack(int stack, String text, int f) {
        addTextBoxInStack(stack, text, f, stackPanes.get(stack));
    }

    public void addPermanentTextBoxInStack(int stack, int item, String text, int f) {
        
    }

    public void addGraphInStack(int stack, String text, int r, Consumer<Integer> a, Pane lp) {
        kbsiArray.get(stack).add(new KSGraph(true, text, r, FXCollections.observableArrayList(), a, lp));
        addToDisplay(stack, kbsiArray.get(stack).size() - 1);
    }
    public void addGraphInStack(int stack, String text, int r, Consumer<Integer> a) {
        addGraphInStack(stack, text, r, a, stackPanes.get(stack));
    }
    public void updateGraphData(int stack, int item, ObservableList<PieChart.Data> pieChartData) {
        kbsiArray.get(stack).get(item).updateGraph(pieChartData);
    }

    public void addEmptyStack(boolean dispPageNumber, boolean createPane, Pane mainPane) {
        kbsiArray.add(new ArrayList<>());
        currentStackPage.add(0);
        dispStackPage.add(dispPageNumber);
        usePreMadePane.add(createPane);
        if (!inverted) {stackPanes.add(new VBox());}
        else {stackPanes.add(new HBox());}
        if (createPane) {mainPane.getChildren().add(stackPanes.get(stackPanes.size()-1));}
    }

    public void addEmptyStack(boolean dispPageNumber) {
        addEmptyStack(dispPageNumber, false, new Pane());
    }

    public void clearStack(int stack){
        if (kbsiArray.get(stack).size() >= 1) {
            for (KeyBoardSelectableInputs button : kbsiArray.get(stack)) {
                button.deleteFromScene();
            }
            kbsiArray.get(stack).clear();
        }
    }

    public void hideButton(int stack, int item) {
        kbsiArray.get(stack).get(item).deleteFromScene();
        kbsiArray.get(stack).get(item).setSelectability(false);
    }
    //DOESN'T WORK IF MENU IS INVERTED
    public void showButton(int stack, int item) {
        if (kbsiArray.get(stack).get(item).getIfHidden()) {
            kbsiArray.get(stack).get(item).addToScene();
            kbsiArray.get(stack).get(item).setSelectability(true); //this will lead to issues
            //Places it in the right spot by shifting everything below in stack
            for (int i = 0; i < kbsiArray.get(stack).size(); i++) {
                if (!kbsiArray.get(stack).get(i).getIfHidden()) {
                    kbsiArray.get(stack).get(i).deleteFromScene();
                    kbsiArray.get(stack).get(i).addToScene();
                }
            }
        }
    }

    public void runButton() {
        System.out.println(kbsiArray.get(buttonStack).get(buttonItem).getCurrentText());
        kbsiArray.get(buttonStack).get(buttonItem).runAction();
        resetButtonColors();
        if (kbsiArray.get(buttonStack).get(buttonItem).getClass() == KSInputButton.class) {
            inputMode = !inputMode;
            if (inputMode == true) {
                kbsiArray.get(buttonStack).get(buttonItem).setSelectedColor(new Color(1, 0, 1, 1));
            } else {kbsiArray.get(buttonStack).get(buttonItem).setSelectedColor(Color.WHITE);}
            kbsiArray.get(buttonStack).get(buttonItem).setSelected(true);
        }
        if (getCurrentButton().getPermaSelectability()) {
            chooseButtonColor(false);
        }
    }

    //takes direction then moves the menu accordingly, runs scroll
    public void moveMenuSelection(int d) {
        int previousStack = buttonStack;
        int previousItem = buttonItem;
        boolean selectionUp = false;

        while (true) {
            //flips button mapping when inverted
            //regular: (row, col), inverted: (col, row)
            if (inverted) {
                switch (d) {
                    case 0:
                        buttonStack++;
                        break;
                    case 1:
                        buttonItem++;
                        break;
                    case 2:
                        buttonItem--;
                        break;
                    case 3:
                        buttonStack--;
                        selectionUp = true;
                        break;
                }
            } else {
                switch (d) {
                    case 0:
                        buttonItem++;
                        break;
                    case 1:
                        buttonStack--;
                        break;
                    case 2:
                        buttonStack++;
                        break;
                    case 3:
                        buttonItem--;
                        selectionUp = true;
                        break;
                }
            }

            checkSelectionMove();
            checkSelectionMoveS1(selectionUp);

            if (kbsiArray.get(buttonStack).get(buttonItem).getSelectable()) {
                kbsiArray.get(previousStack).get(previousItem).setSelected(false);
                kbsiArray.get(buttonStack).get(buttonItem).setSelected(true);
                break;
            }
        }

    }

    private void checkSelectionMoveS1(boolean selectionUp) {
        int count = 0;
        for (int i = 2; i > 1; i--) {
            count++;
            if (!kbsiArray.get(buttonStack).get(buttonItem).getSelectable() && count < kbsiArray.get(buttonStack).size()) {
                if (selectionUp) {buttonItem--;
                } else {buttonItem++;}
                i++;
                checkSelectionMove();
            }
        }
    }

    private void checkSelectionMove() {
        //cycle stack selection
        if (buttonStack < 0) {
            buttonStack = kbsiArray.size() - 1;
        } else if (buttonStack >= kbsiArray.size()) {
            buttonStack = 0;
        }

        //cycle item selection
        if (buttonItem < 0) {
            buttonItem = kbsiArray.get(buttonStack).size() - 1;
        } else if (buttonItem >= kbsiArray.get(buttonStack).size()) {
            buttonItem = 0;
        }

        //cycle page
        if (buttonItem >= buttonsPerPage * (currentStackPage.get(buttonStack) + 1)) {
            setStackPage(buttonStack, currentStackPage.get(buttonStack) + 1);
        } else if (buttonItem < buttonsPerPage * (currentStackPage.get(buttonStack))) {
            setStackPage(buttonStack, currentStackPage.get(buttonStack) - 1);
        }
        //System.out.println("Curr: " + buttonStack + "/" + kbsiArray.size() + " : " + buttonItem + "/" + kbsiArray.get(buttonStack).size());
    }

    public boolean getIfSelectedIsInput() {
        return kbsiArray.get(buttonStack).get(buttonItem).getClass() == KSInputButton.class;
    }

    public void setTextBoxText(int stack, int item, String text) {
        kbsiArray.get(stack).get(item).setText(text);
    }

    public void setImpText(String text) {
        kbsiArray.get(buttonStack).get(buttonItem).setInpText(text);
    }

    /*public void updateGraph(int stack, int item, ArrayList<String> names, ArrayList<Integer> values) {
        kbsiArray.get(stack).get(item).updateGraph(names, values);
    }*/

    public KeyBoardSelectableInputs getCurrentButton() {
        return kbsiArray.get(buttonStack).get(buttonItem);
    }
    public KeyBoardSelectableInputs getButton(int stack, int item) {
        return kbsiArray.get(stack).get(item);
    }

    //Returns all inputted text in an array
    public ArrayList<String> getUserInputs() {
        ArrayList<String> inputs = new ArrayList<>();
        for (int i = 0; i < kbsiArray.size(); i++) {
            for (int j = 0; j < kbsiArray.get(i).size(); j++) {
                if (kbsiArray.get(i).get(j).getClass() == KSInputButton.class) {
                    inputs.add(kbsiArray.get(i).get(j).getInputtedText());
                }
            }
        }
        return inputs;
    }

    public boolean getIfStackEmpty(int stack) {
        return kbsiArray.get(stack).size() == 0;
    }

    public void addToDisplay(int row, int col) {
        if (col < buttonsPerPage) {
            kbsiArray.get(row).get(col).addToScene();
        }
    }

    public void setStackPage(int stack, int page) {
        int j = 10*currentStackPage.get(stack);
        for (int i = j; i < j+10; i++) {
            if (i < kbsiArray.get(stack).size()) {
                kbsiArray.get(stack).get(i).deleteFromScene();
            }
        }
        int j2 = 10*page;
        for (int i = j2; i < j2+10; i++) {
            if (i < kbsiArray.get(stack).size()) {
                kbsiArray.get(stack).get(i).addToScene();
            }
        }
        currentStackPage.set(stack, page);
    }

    public void chooseButtonColor(boolean d) {
        kbsiArray.get(buttonStack).get(buttonItem).setColor(d);
    }

    public void setButtonColor(boolean d, int s, int i) {
        kbsiArray.get(s).get(i).setColor(d);
    }

    public void resetButtonColors() {
        for (int i = 0; i < kbsiArray.get(buttonStack).size(); i++) {
            kbsiArray.get(buttonStack).get(i).setColor(true);
            if (!kbsiArray.get(buttonStack).get(i).getIfSelected()) { //kinda gross
                kbsiArray.get(buttonStack).get(i).setSelected(false);
            }
        }
    }

    public int getPageNumberByStack(int stack) {return currentStackPage.get(stack);}

    public boolean getInputMode() {return inputMode;}

    public void resetMenu() {
        if (kbsiArray.size() != 0) {
            for (int i = 0; i < kbsiArray.size(); i++) {
                for (int j = 0; j < kbsiArray.get(i).size(); j++) {
                    KeyBoardSelectableInputs currentButton = kbsiArray.get(i).get(j);

                    currentButton.resetButton();

                    if (currentButton.getClass() == KSInputButton.class) {
                        currentButton.setInpText("");
                    }
                }
            }
            buttonStack = 0;
            buttonItem = 0;
            checkSelectionMoveS1(false);
            kbsiArray.get(buttonStack).get(buttonItem).setSelected(true);
        }
    }
}
