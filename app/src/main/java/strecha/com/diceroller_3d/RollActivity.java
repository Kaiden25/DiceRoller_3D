package strecha.com.diceroller_3d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.Button;

public class RollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
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
