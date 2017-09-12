package strecha.com.diceroller_3d;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import strecha.com.diceroller_3d.module.DiceType;
import strecha.com.diceroller_3d.module.Settings;
import strecha.com.diceroller_3d.module.SettingsFileHandler;

public class RollActivity extends AppCompatActivity implements SensorEventListener {

    //Identifiers for extras in intents
    public static final String EXTRA_DICE_TYPE = "strecha.com.diceroller_3d.RollActivity.DiceType";
    public static final String EXTRA_DICE_NUMBER = "strecha.com.diceroller_3d.RollActivity.DiceNumber";

    //diceType and diceNumber initialized with Default Values
    private DiceType diceType = DiceType.D6;
    private int diceNumber = 10;
    private Settings settings;
    private ArrayList<Integer> history;

    private final int rollAnimations = 50;
    private final int delayTime = 15;
    private Resources res;
    private HashMap<DiceType, int[]> diceImagesMap;
    private Drawable dice[] = new Drawable[6];
    private final Random randomGen = new Random();
    @SuppressWarnings("unused")
    private int diceSum;
    private int roll[] = new int[diceNumber];
    private ImageView die1;
    private ImageView die2;
    private ImageView die3;
    private ImageView die4;
    private ImageView die5;
    private ImageView die6;
    private ImageView die7;
    private ImageView die8;
    private ImageView die9;
    private SensorManager sensorMgr;
    private Handler animationHandler;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private boolean paused = false;
    private static final int UPDATE_DELAY = 50;
    private static final double SHAKE_THRESHOLD = 5000;

    /** Called when the activity is first created. */
    //TODO: store rolled numbers

    @Override
    public void onCreate(Bundle savedInstanceState) {
        paused = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
        res = getResources();
        history = new ArrayList<>();
        diceImagesMap = new HashMap<>();
        diceImagesMap.put(DiceType.D6, new int[] { R.drawable.d6_1, R.drawable.d6_2, R.drawable.d6_3, R.drawable.d6_4, R.drawable.d6_5, R.drawable.d6_6 });

        // check for intent extra values
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_DICE_TYPE)){
            String s = intent.getStringExtra(EXTRA_DICE_TYPE);
            diceType = DiceType.valueOf(s);
        }

        if (intent.hasExtra(EXTRA_DICE_NUMBER)){
            int nbr = intent.getIntExtra(EXTRA_DICE_NUMBER, 1);
            if (nbr > 9) {
                diceNumber = 9;
                Toast toast = Toast.makeText(this, "Maximum of dices is 9", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (nbr < 1){
                diceNumber = 1;
                Toast toast = Toast.makeText(this, "Minimum of dices is 1", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                diceNumber = nbr;
            }
        }

        SettingsFileHandler sfh = new SettingsFileHandler(this);
        if (sfh.hasSettingsFile()){
            settings = sfh.readSettings();
        }
        else {
            settings = sfh.createDefaultSettings();
        }

        for (int i = 0; i < 6; i++) {
            dice[i] = res.getDrawable(diceImagesMap.get(diceType)[i]);
        }

        die1 = (ImageView) findViewById(R.id.die1);
        die2 = (ImageView) findViewById(R.id.die2);
        die3 = (ImageView) findViewById(R.id.die3);
        die4 = (ImageView) findViewById(R.id.die4);
        die5 = (ImageView) findViewById(R.id.die5);
        die6 = (ImageView) findViewById(R.id.die6);
        die7 = (ImageView) findViewById(R.id.die7);
        die8 = (ImageView) findViewById(R.id.die8);
        die9 = (ImageView) findViewById(R.id.die9);
        animationHandler = new Handler() {
            public void handleMessage(Message msg) {
                die1.setImageDrawable(dice[roll[0]]);
                die2.setImageDrawable(dice[roll[1]]);
                die3.setImageDrawable(dice[roll[2]]);
                die4.setImageDrawable(dice[roll[3]]);
                die5.setImageDrawable(dice[roll[4]]);
                die6.setImageDrawable(dice[roll[5]]);
                die7.setImageDrawable(dice[roll[6]]);
                die8.setImageDrawable(dice[roll[7]]);
                die9.setImageDrawable(dice[roll[8]]);
            }
        };
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        boolean accelSupported = sensorMgr.registerListener((SensorEventListener) this,
                sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),	SensorManager.SENSOR_DELAY_GAME);
        if (!accelSupported) sensorMgr.unregisterListener((SensorEventListener) this); //no accelerometer on the device
        rollDice();
    }

    private void rollDice() {
        if (paused) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rollAnimations; i++) {
                    doRoll();
                }
            }
        }).start();
        MediaPlayer mp = MediaPlayer.create(this, R.raw.roll);
        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
    }

    private void doRoll() { // only does a single roll

        for (int i = 0; i < diceNumber; i++){
            roll[i] = randomGen.nextInt(6);
            history.add(roll[i]);
        }

        //diceSum = roll[0] + roll[1] + 2; // 2 is added because the values of the rolls start with 0 not 1
        synchronized (getLayoutInflater()) {
            animationHandler.sendEmptyMessage(0);
        }
        try { // delay to alloy for smooth animation
            Thread.sleep(delayTime);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        paused = false;
    }

    public void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Sensor mySensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > UPDATE_DELAY) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) { //the screen was shaked
                    rollDice();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // onClick listener for menu buttons selection, settings and history
    public void onMenuButtonClick(View v){
        Intent intent = null;
        if (v.getId() == R.id.butSelection){
            intent = new Intent(this, SelectionActivity.class);
        }
        else if (v.getId() == R.id.butSettings){
            intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(SettingsActivity.EXTRA_SETTINGS_SOUND, settings.isSoundEnabled());
            intent.putExtra(SettingsActivity.EXTRA_SETTINGS_3D, settings.is3dEnabled());
        }
        else if (v.getId() == R.id.butHistory){
            intent = new Intent(this, HistoryActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    public void onDiceLinearLayoutClick(View v){
        rollDice();
    }
}
