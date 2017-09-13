package strecha.com.diceroller_3d;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ToggleButton;

import strecha.com.diceroller_3d.module.Settings;
import strecha.com.diceroller_3d.module.SettingsFileHandler;

public class SettingsActivity extends AppCompatActivity {

    private ToggleButton tglButSound;
    private ToggleButton tglBut3d;

    private SettingsFileHandler sgh;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tglButSound = (ToggleButton) findViewById(R.id.tglButSound);
        tglBut3d = (ToggleButton) findViewById(R.id.tglBut3D);
        assert tglButSound != null;
        assert tglBut3d != null;

        // 3D option not available
        tglBut3d.setEnabled(false);
        tglBut3d.setHint("3D isn't available yet");

        sgh = new SettingsFileHandler(this);
        settings = sgh.readSettings();

        tglButSound.setChecked(settings.isSoundEnabled());
        tglBut3d.setChecked(settings.is3dEnabled());
    }

    public void onSaveButtonClick(View view){

        assert tglButSound != null && tglBut3d != null;
        if (tglButSound.isChecked() != settings.isSoundEnabled() || tglBut3d.isChecked() != settings.is3dEnabled()) {
            Settings settings = new Settings(tglButSound.isChecked(), tglBut3d.isChecked());
            sgh.writeSettings(settings);
        }

        Intent intent = new Intent(this, RollActivity.class);
        startActivity(intent);
    }
}
