package strecha.com.diceroller_3d.module;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by admin on 12.09.2017.
 */

public class SettingsFileHandler {

    private static final String FILEPATH = "/diceRoller_3d.properties";
    private Settings defaultSettings;
    private Context context;
    private File configProperties;

    public SettingsFileHandler(Context context){
        this.context = context;
        configProperties = new File(context.getFilesDir(), FILEPATH);
        defaultSettings = new Settings(true, false);
    }

    public boolean hasSettingsFile(){
        return configProperties.exists();
    }

    public Settings readSettings(){
        try {
            FileInputStream fis = new FileInputStream(configProperties);
            /*InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }*/
            Properties prop = new Properties();
            prop.load(fis);
            String isSoundEnabled = prop.getProperty("isSoundEnabled");
            String is3dEnabled = prop.getProperty("is3dEnabled");
            return new Settings(Boolean.parseBoolean(isSoundEnabled), Boolean.parseBoolean(is3dEnabled));
        }
        catch (IOException ex){
            ex.printStackTrace();
            return defaultSettings;
        }
    }

    public void writeSettings(Settings settings){
        try {
            FileOutputStream fos = new FileOutputStream(configProperties);
            Properties prop = new Properties();
            prop.setProperty("isSoundEnabled", String.valueOf(settings.isSoundEnabled()));
            prop.setProperty("is3dEnabled", String.valueOf(settings.is3dEnabled()));
            prop.store(fos, "");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public Settings createDefaultSettings(){
        writeSettings(defaultSettings);
        return defaultSettings;
    }
}
