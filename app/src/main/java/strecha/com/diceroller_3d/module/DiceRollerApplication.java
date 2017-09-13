package strecha.com.diceroller_3d.module;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by admin on 13.09.2017.
 */

public class DiceRollerApplication extends Application {

    private ArrayList<Integer> history = new ArrayList<>();
    private DiceType diceType = DiceType.D6;
    private int diceNumber = 1;

    public int getDiceNumber() {
        return diceNumber;
    }

    public void setDiceNumber(int diceNumber) {
        this.diceNumber = diceNumber;
    }

    public DiceType getDiceType() {
        return diceType;
    }

    public void setDiceType(DiceType diceType) {
        this.diceType = diceType;
    }

    public ArrayList<Integer> getHistory() {
        return history;
    }

    public void addRolledNumberToHistory(int i){
        history.add(i);
    }
}
