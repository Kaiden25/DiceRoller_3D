package strecha.com.diceroller_3d;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import strecha.com.diceroller_3d.module.DiceRollerApplication;
import strecha.com.diceroller_3d.module.DiceType;
import strecha.com.diceroller_3d.module.Settings;
import strecha.com.diceroller_3d.module.SettingsFileHandler;

public class RollActivity extends AppCompatActivity implements SensorEventListener {

    private DiceType diceType;
    private int numberOfDiceSites;
    private int diceNumber;
    private Settings settings;
    private final int rollAnimations = 50;
    private final int delayTime = 15;
    private HashMap<DiceType, int[]> diceImagesMap;
    private Drawable[] dice;
    private final Random randomGen = new Random();
    private int[] roll = new int[9];
    private SparseArray<ImageView> diceImageViewArray;
    private SensorManager sensorMgr;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private boolean paused = false;
    private static final int UPDATE_DELAY = 50;
    private static final double SHAKE_THRESHOLD = 5000;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        paused = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);

        // create list with all imageViews for the dices
        diceImageViewArray = new SparseArray<>();
        diceImageViewArray.put(0, (ImageView) findViewById(R.id.die1));
        diceImageViewArray.put(1, (ImageView) findViewById(R.id.die2));
        diceImageViewArray.put(2, (ImageView) findViewById(R.id.die3));
        diceImageViewArray.put(3, (ImageView) findViewById(R.id.die4));
        diceImageViewArray.put(4, (ImageView) findViewById(R.id.die5));
        diceImageViewArray.put(5, (ImageView) findViewById(R.id.die6));
        diceImageViewArray.put(6, (ImageView) findViewById(R.id.die7));
        diceImageViewArray.put(7, (ImageView) findViewById(R.id.die8));
        diceImageViewArray.put(8, (ImageView) findViewById(R.id.die9));

        // set all images of imageViews to null
        for (int i = 0; i < diceImageViewArray.size(); i++){
            diceImageViewArray.get(i).setImageDrawable(null);
        }

        // diceImagesMap contains int-arrays for all diceTypes
        diceImagesMap = new HashMap<>();
        diceImagesMap.put(DiceType.D2, new int[] { R.drawable.d2_1, R.drawable.d2_2 });
        diceImagesMap.put(DiceType.D4, new int[] { R.drawable.d4_1, R.drawable.d4_2, R.drawable.d4_3, R.drawable.d4_4 });
        diceImagesMap.put(DiceType.D6, new int[] { R.drawable.d6_1, R.drawable.d6_2, R.drawable.d6_3, R.drawable.d6_4, R.drawable.d6_5, R.drawable.d6_6 });
        diceImagesMap.put(DiceType.D8, new int[] { R.drawable.d8_1, R.drawable.d8_2, R.drawable.d8_3, R.drawable.d8_4, R.drawable.d8_5, R.drawable.d8_6, R.drawable.d8_7, R.drawable.d8_8 });
        diceImagesMap.put(DiceType.D10, new int[] { R.drawable.d10_1, R.drawable.d10_2, R.drawable.d10_3, R.drawable.d10_4, R.drawable.d10_5, R.drawable.d10_6, R.drawable.d10_7, R.drawable.d10_8, R.drawable.d10_9, R.drawable.d10_10 });
        diceImagesMap.put(DiceType.D12, new int[] { R.drawable.d12_1, R.drawable.d12_2, R.drawable.d12_3, R.drawable.d12_4, R.drawable.d12_5, R.drawable.d12_6, R.drawable.d12_7, R.drawable.d12_8, R.drawable.d12_9, R.drawable.d12_10, R.drawable.d12_11, R.drawable.d12_12 });
        diceImagesMap.put(DiceType.D20, new int[] { R.drawable.d20_1, R.drawable.d20_2, R.drawable.d20_3, R.drawable.d20_4, R.drawable.d20_5, R.drawable.d20_6, R.drawable.d20_7, R.drawable.d20_8, R.drawable.d20_9, R.drawable.d20_10, R.drawable.d20_11, R.drawable.d20_12,R.drawable.d20_13, R.drawable.d20_14, R.drawable.d20_15, R.drawable.d20_16, R.drawable.d20_17, R.drawable.d20_18, R.drawable.d20_19, R.drawable.d20_20 });

        // check of diceNumber values (min: 1, max:9)
        if (((DiceRollerApplication) getApplicationContext()).getDiceNumber() > 9) {
            ((DiceRollerApplication) getApplicationContext()).setDiceNumber(9);
            Toast toast = Toast.makeText(this, "Maximum of dices is 9", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (((DiceRollerApplication) getApplicationContext()).getDiceNumber() < 1){
            ((DiceRollerApplication) getApplicationContext()).setDiceNumber(1);
            Toast toast = Toast.makeText(this, "Minimum of dices is 2", Toast.LENGTH_SHORT);
            toast.show();
        }

        diceNumber = ((DiceRollerApplication) getApplicationContext()).getDiceNumber();
        diceType = ((DiceRollerApplication) getApplicationContext()).getDiceType();

        String[] s = diceType.toString().split("D");
        numberOfDiceSites = Integer.parseInt(s[1]);
        dice = new Drawable[numberOfDiceSites];

        // reads or creates (if not available) settings file
        SettingsFileHandler sfh = new SettingsFileHandler(this);
        if (sfh.hasSettingsFile()){
            settings = sfh.readSettings();
        }
        else {
            settings = sfh.createDefaultSettings();
        }

        for (int i = 0; i < numberOfDiceSites; i++) {
            dice[i] = getDrawable(diceImagesMap.get(diceType)[i]);
        }

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        boolean accelSupported = sensorMgr.registerListener((SensorEventListener) this,
                sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),	SensorManager.SENSOR_DELAY_GAME);
        if (!accelSupported) sensorMgr.unregisterListener((SensorEventListener) this); //no accelerometer on the device
        rollDice();
    }

    // rolls all dices
    private void rollDice() {
        if (paused) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rollAnimations; i++) {
                    doRoll();
                }

                // add rolled numbers to history
                for (int i = 0; i < diceNumber; i++){
                    int rolled = roll[i];
                    ((DiceRollerApplication) getApplicationContext()).addRolledNumberToHistory(rolled + 1);
                }
            }
        }).start();

        // plays sound if enabled
        if(settings.isSoundEnabled()) {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.roll);
            mp.start();
        }
    }

    private void doRoll() { // only does a single roll

        for (int i = 0; i < diceNumber; i++){
            roll[i] = randomGen.nextInt(numberOfDiceSites);
        }

        synchronized (getLayoutInflater()) {

            for (int i = 0; i < diceNumber; i++){
                final ImageView imageView = diceImageViewArray.get(i);
                final Drawable drawable = dice[roll[i]];

                // sets image of rolled number to imageView
                imageView.post(new Runnable() {
                    public void run() {
                        imageView.setImageDrawable(drawable);
                    }
                });
            }
        }
        try { // delay to allow for smooth animation
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

    // triggers roll on recorded sensor changes
    @Override
    public void onSensorChanged(SensorEvent event) {
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

    // not needed, only for implement SensorEventListener
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
