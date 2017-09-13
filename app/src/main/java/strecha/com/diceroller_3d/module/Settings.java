package strecha.com.diceroller_3d.module;

/**
 * Created by admin on 12.09.2017.
 */

public class Settings {

    private boolean isSoundEnabled;
    private boolean is3dEnabled;

    public Settings (boolean isSoundEnabled, boolean is3dEnabled){
        this.isSoundEnabled = isSoundEnabled;
        this.is3dEnabled = is3dEnabled;
    }

    public boolean is3dEnabled() {
        return is3dEnabled;
    }

    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }

}
