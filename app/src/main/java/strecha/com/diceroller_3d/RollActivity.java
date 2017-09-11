package strecha.com.diceroller_3d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.Button;

import strecha.com.diceroller_3d.module.DiceType;

public class RollActivity extends AppCompatActivity {

    //Identifiers for extras in intents
    public static final String EXTRA_DICE_TYPE = "strecha.com.diceroller_3d.RollActivity.DiceType";
    public static final String EXTRA_DICE_NUMBER = "strecha.com.diceroller_3d.RollActivity.DiceNumber";

    //diceType and diceNumber initialized with Default Values
    private DiceType diceType = DiceType.D4;
    private int diceNumber = 1;

    //TODO: store rolled numbers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);

        // check for intent extra values
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_DICE_TYPE)){
            String s = intent.getStringExtra(EXTRA_DICE_TYPE);
            diceType = DiceType.valueOf(s);
        }

        if (intent.hasExtra(EXTRA_DICE_NUMBER)){
            diceNumber = intent.getIntExtra(EXTRA_DICE_NUMBER, 1);
        }
    }


    // onClick listener for menu buttons selection, settings and history
    public void onMenuButtonClick(View v){
        Intent intent = null;
        if (v.getId() == R.id.butSelection){
            intent = new Intent(this, SelectionActivity.class);
        }
        else if (v.getId() == R.id.butSettings){
            intent = new Intent(this, SettingsActivity.class);
        }
        else if (v.getId() == R.id.butHistory){
            intent = new Intent(this, HistoryActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    //TODO: implement roll logic, roll animation and acceleration event
}
