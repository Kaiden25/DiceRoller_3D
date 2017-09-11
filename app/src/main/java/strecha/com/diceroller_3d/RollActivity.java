package strecha.com.diceroller_3d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.Button;

import strecha.com.diceroller_3d.module.DiceType;

public class RollActivity extends AppCompatActivity {

    private static final String EXTRA_DICE_TYPE = "strecha.com.diceroller_3d.RollActivity.DiceType";
    private static final String EXTRA_DICE_NUMBER = "strecha.com.diceroller_3d.RollActivity.DiceNumber";

    private DiceType diceType = DiceType.D4;
    private int diceNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_DICE_TYPE)){
            String s = intent.getStringExtra(EXTRA_DICE_TYPE);
            diceType = DiceType.valueOf(s);
        }

        if (intent.hasExtra(EXTRA_DICE_NUMBER)){
            diceNumber = intent.getIntExtra(EXTRA_DICE_NUMBER, 1);
        }
    }


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
}
