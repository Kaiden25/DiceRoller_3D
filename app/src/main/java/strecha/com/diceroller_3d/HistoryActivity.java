package strecha.com.diceroller_3d;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;

import strecha.com.diceroller_3d.module.DiceRollerApplication;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ArrayList<Integer> history = ((DiceRollerApplication) getApplicationContext()).getHistory();

        // adds roll history to grid
        GridLayout historyGrid = (GridLayout) findViewById(R.id.historyGrid);
        for (Integer i: history){
            Button b = new Button(this);
            b.setText(String.valueOf(i));
            b.setClickable(false);
            assert historyGrid != null;
            historyGrid.addView(b);
        }
    }
}
