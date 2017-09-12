package strecha.com.diceroller_3d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import strecha.com.diceroller_3d.module.Settings;
import strecha.com.diceroller_3d.module.SettingsFileHandler;

public class SettingsActivity extends AppCompatActivity {

    public static final String EXTRA_SETTINGS_SOUND = "strecha.com.diceroller_3d.SettingsActivity.Settings-Sound";
    public static final String EXTRA_SETTINGS_3D = "strecha.com.diceroller_3d.SettingsActivity.Settings-3D";

    private ToggleButton tglButSound;
    private ToggleButton tglBut3d;

    private boolean hasSound;
    private boolean is3d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tglButSound = (ToggleButton) findViewById(R.id.tglButSound);
        tglBut3d = (ToggleButton) findViewById(R.id.tglBut3D);

        Intent intent = getIntent();

        hasSound = intent.getBooleanExtra(EXTRA_SETTINGS_SOUND, true);
        is3d = intent.getBooleanExtra(EXTRA_SETTINGS_3D, false);

        tglButSound.setChecked(hasSound);
        tglBut3d.setChecked(is3d);
    }

    public void onSaveButtonClick(View view){

        assert tglButSound != null && tglBut3d != null;
        if (tglButSound.isChecked() != hasSound && tglBut3d.isChecked() != is3d) {
            Settings settings = new Settings(tglButSound.isChecked(), tglBut3d.isChecked());
            SettingsFileHandler sgh = new SettingsFileHandler(this);
            sgh.writeSettings(settings);
        }

        Intent intent = new Intent(this, RollActivity.class);
        startActivity(intent);
    }
}
