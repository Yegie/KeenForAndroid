package org.yegie.keenforandroid;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static org.yegie.keenforandroid.MenuActivity.DARK_MODE;
import static org.yegie.keenforandroid.MenuActivity.MENU_DIFF;
import static org.yegie.keenforandroid.MenuActivity.MENU_MULT;
import static org.yegie.keenforandroid.MenuActivity.MENU_SIZE;

public class ApplicationCore extends Application {

    private boolean isDarkMode;
    private int gameSize=3;
    private int gameDiff=1;
    private int gameMult=0;
    private boolean canCont=false;

    private SharedPreferences sharedPref;

    public boolean isDarkMode() {
//        return isDarkMode;
        return false;
    }

    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public int getGameDiff() {
        return gameDiff;
    }

    public void setGameDiff(int gameDiff) {
        this.gameDiff = gameDiff;
    }

    public int getGameMult() {
        return gameMult;
    }

    public void setGameMult(int gameMult) {
        this.gameMult = gameMult;
    }

    public boolean isCanCont() {
        return canCont;
    }

    public void setCanCont(boolean canCont) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KeenActivity.CAN_CONT,canCont);
        editor.apply();
        this.canCont = canCont;
    }

    public void savePrefs() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(MENU_MULT,gameMult!=0);
        editor.putInt(MENU_DIFF,gameDiff);
        editor.putInt(MENU_SIZE,gameSize);
        editor.putBoolean(DARK_MODE, isDarkMode);
        editor.apply();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        canCont= sharedPref.getBoolean(KeenActivity.CAN_CONT,false);

        gameDiff = sharedPref.getInt(MENU_DIFF,0);

        gameSize = sharedPref.getInt(MENU_SIZE,3);

        isDarkMode = sharedPref.getBoolean(DARK_MODE, false);

        gameMult = sharedPref.getBoolean(MENU_MULT,false) ? 1 : 0;
    }

}
