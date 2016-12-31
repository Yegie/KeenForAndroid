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
    private boolean continuing = false;
    private long seed = 10101;
    private KeenModel gameModel;
    private static final String IS_IN_GAME = "game_was_in_prog";
    private final String SAVE_MODEL = "save_model";
    private static final String CAN_CONT = "can_continue";

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

    public static boolean getCanCont(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(CAN_CONT, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keen);
        //    Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
        //    setSupportActionBar(Toolbar);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            continuing = extras.getBoolean(MenuActivity.GAME_CONT, false);
        }

        if (!sharedPref.getBoolean(IS_IN_GAME, false) && !continuing)
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

    public void returnToMainMenu(boolean canCont)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IS_IN_GAME,false);
        String modelAsString = new Gson().toJson(gameModel);
        editor.putBoolean(CAN_CONT,canCont);
        editor.putString(SAVE_MODEL,modelAsString);
        editor.commit();
        Intent intent=new Intent(this,MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        returnToMainMenu(true);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        boolean defaultValue = false;
        if(sharedPref.getBoolean(IS_IN_GAME, defaultValue) || continuing)
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
        editor.putBoolean(CAN_CONT,true);
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
