package org.yegie.keenforandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewGroup;

import com.google.gson.Gson;

public class KeenActivity extends Activity {

    private int size = 3;
    private int diff = 1;
    private int multOnly = 0;
    private long seed = 10101;
    private KeenModel gameModel;
    private static final String IS_IN_GAME = "game_was_in_prog";
    private final String SAVE_MODEL = "save_model";

    private SharedPreferences sharedPref;

    private Handler mHandler = new Handler();

    public Bundle getGameData()
    {
        Bundle data = new Bundle();
        data.putInt("size",size);
        data.putInt("diff",diff);
        data.putInt("mult",multOnly);
        data.putLong("seed",seed);
        return data;
    }

    public static boolean getGameInProg(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(IS_IN_GAME, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keen);
        //    Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
        //    setSupportActionBar(Toolbar);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (!sharedPref.getBoolean(IS_IN_GAME, false))
        {
            size = getIntent().getExtras().getInt(MenuActivity.GAME_SIZE, 0);
            diff = getIntent().getExtras().getInt(MenuActivity.GAME_DIFF, 0);
            multOnly = getIntent().getExtras().getInt(MenuActivity.GAME_MULT, 0);
            seed = getIntent().getExtras().getLong(MenuActivity.GAME_SEED, 0);

            if (size < 3 || size > 9) {
                Log.e("KEEN", "Got invalid game size, quitting...");
                setResult(Activity.RESULT_CANCELED);
                finish();
            }

            runGame();
        }
    }

    public void returnToMainMenu()
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IS_IN_GAME,false);
        editor.commit();
        Intent intent=new Intent(this,MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        returnToMainMenu();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        boolean defaultValue = false;
        if(sharedPref.getBoolean(IS_IN_GAME, defaultValue))
        {
            String jsonGameModel = sharedPref.getString(SAVE_MODEL,"");

            KeenModel gameModel = new Gson().fromJson(jsonGameModel, KeenModel.class);
            runGameModel(gameModel);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {

        String modelAsString = new Gson().toJson(gameModel);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IS_IN_GAME,true);
        editor.putString(SAVE_MODEL,modelAsString);
        editor.commit();


    }

    @SuppressWarnings("unused")//it is used because it does stuff within itself
    public void runGameModel(KeenModel gameModel) {
        this.gameModel = gameModel;
        KeenView gameView = new KeenView(this,gameModel);
        KeenController gameController = new KeenController(gameModel,gameView,this);
        ViewGroup container = (ViewGroup) findViewById(R.id.keen_container);
        container.addView(gameView);
    }

    public void runGame()
    {

        Thread gameGenThread = new Thread(new LevelGenMultiThread(this,mHandler));
        gameGenThread.start();
    }


}
