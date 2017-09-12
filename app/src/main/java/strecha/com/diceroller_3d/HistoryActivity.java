package strecha.com.diceroller_3d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        ArrayList<Integer> history = intent.getIntegerArrayListExtra(RollActivity.EXTRA_HISTORY);
        GridLayout historyGrid = (GridLayout) findViewById(R.id.historyGrid);

        for (Integer i: history){
            Button b = new Button(this);
            b.setText(String.valueOf(i));
            assert historyGrid != null;
            historyGrid.addView(b);
        }
    }
}
