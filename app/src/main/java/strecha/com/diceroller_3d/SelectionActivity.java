package strecha.com.diceroller_3d;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import strecha.com.diceroller_3d.module.DiceRollerApplication;
import strecha.com.diceroller_3d.module.DiceType;

public class SelectionActivity extends AppCompatActivity {

    private GridLayout grid;
    private String diceType;
    private int numberOfDices;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        intent = new Intent(this, RollActivity.class);
        grid = (GridLayout) findViewById(R.id.grid);

        // create buttons for all diceType options and add them to grid
        for (DiceType d : DiceType.values()) {
            Button b = new Button(this);
            b.setText(d.toString());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDiceTypeButtonClick(v);
                }
            });

            assert grid != null;
            grid.addView(b);
        }
    }

    // onClick listener for diceType buttons
    public void onDiceTypeButtonClick(View view) {
        // get text of triggered button and remove all buttons from grid
        Button b = (Button) view;
        diceType = b.getText().toString();
        grid.removeAllViews();

        // create editText to get diceNumber and add it to grid
        EditText editText = new EditText(this);
        editText.setHint(R.string.diceNumberSettingText);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        //set listener to detect end of number input
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (
                    actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                ){
                    if (!v.getText().toString().equals("")) {
                        numberOfDices = Integer.valueOf(v.getText().toString());
                        ((DiceRollerApplication) getApplicationContext()).setDiceType(DiceType.valueOf(diceType));
                        ((DiceRollerApplication) getApplicationContext()).setDiceNumber(numberOfDices);

                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });
        grid.addView(editText);
    }
}

