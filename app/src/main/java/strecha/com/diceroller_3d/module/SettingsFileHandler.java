package strecha.com.diceroller_3d.module;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by admin on 12.09.2017.
 */

public class SettingsFileHandler {

    private static final String SETTING_SOUND = "isSoundEnabled";
    private static final String SETTINGS_3D = "is3dEnabled";
    private static final String FILEPATH = "/diceRoller_3d.properties";

    private Settings defaultSettings;
    private File configProperties;

    public SettingsFileHandler(Context context){
        configProperties = new File(context.getFilesDir(), FILEPATH);
        defaultSettings = new Settings(true, false);
    }

    public boolean hasSettingsFile(){
        return configProperties.exists();
    }

    //read config.properties file, if exception -> return defaultSettings
    public Settings readSettings(){
        try {
            FileInputStream fis = new FileInputStream(configProperties);
            Properties prop = new Properties();
            prop.load(fis);
            String isSoundEnabled = prop.getProperty(SETTING_SOUND);
            String is3dEnabled = prop.getProperty(SETTINGS_3D);
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
            prop.setProperty(SETTING_SOUND, String.valueOf(settings.isSoundEnabled()));
            prop.setProperty(SETTINGS_3D, String.valueOf(settings.is3dEnabled()));
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
