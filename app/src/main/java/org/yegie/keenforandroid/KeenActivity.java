package org.yegie.keenforandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;

public class KeenActivity extends Activity {

    //default game data
    private int size = 3;
    private int diff = 1;
    private int multOnly = 0;
    private long seed = 10101;
    private boolean continuing;
    private KeenModel gameModel;

    //names by which to read from saved prefs
    private final static String SAVE_MODEL = "save_model";
    protected final static String CAN_CONT = "can_continue";
    private final static String IS_CONT = "is_continuing";

    //shared prefs file
    private SharedPreferences sharedPref;

    //handler for multithreading
    private Handler mHandler = new Handler();

    //returns the current game params as a bundle
    public Bundle getGameData()
    {
        Bundle data = new Bundle();
        data.putInt("size",size);
        data.putInt("diff",diff);
        data.putInt("mult",multOnly);
        data.putLong("seed",seed);
        return data;
    }

    //get the saved prefs file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keen);

        continuing = savedInstanceState != null && savedInstanceState.getBoolean(IS_CONT, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

    }

    //so that other classes can call the finish function
    public void returnToMainMenu()
    {
        finish();
    }

    // If a new game was started and then the process was killed the "continuing"
    // would default to false unless saved here.
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean(IS_CONT,continuing);
    }

    //check if resuming a previous game and then either restore that game from saved prefs
    //or start a multi thread process to create a level with current params
    @Override
    public void onResume()
    {
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if(!continuing && extras != null) {
            continuing = extras.getBoolean(MenuActivity.GAME_CONT, false);
        }

        if(continuing)
        {
            String jsonGameModel = sharedPref.getString(SAVE_MODEL,"");

            KeenModel gameModel = new Gson().fromJson(jsonGameModel, KeenModel.class);

            runGameModel(gameModel);
        }
        else
        {
            size = getIntent().getExtras().getInt(MenuActivity.GAME_SIZE, 0);
            diff = getIntent().getExtras().getInt(MenuActivity.GAME_DIFF, 0);
            multOnly = getIntent().getExtras().getInt(MenuActivity.GAME_MULT, 0);
            seed = getIntent().getExtras().getLong(MenuActivity.GAME_SEED, 0);

            //this should never happen
            if (size < 3 || size > 9) {
                Log.e("KEEN", "Got invalid game size, quitting...");
                setResult(Activity.RESULT_CANCELED);
                finish();
            }

            runGame();
        }
    }

    //save the current game to json and correctly update whether the game can be continued
    @Override
    public void onPause()
    {
        super.onPause();

        SharedPreferences.Editor editor = sharedPref.edit();

        if(gameModel!=null) {
            String modelAsString = new Gson().toJson(gameModel, KeenModel.class);
            editor.putString(SAVE_MODEL, modelAsString);
            editor.putBoolean(CAN_CONT, !gameModel.getPuzzleWon());
        }
        else {
            editor.putString(SAVE_MODEL, "");
            editor.putBoolean(CAN_CONT, false);
        }

        editor.apply();
    }

    //called by the game gen thread after it creates a game model
    //creates a view and a controller, removes the load screen, and adds the game view.
    @SuppressWarnings("unused")
    public void runGameModel(KeenModel gameModel) {
        this.gameModel = gameModel;
        KeenView gameView = new KeenView(this,gameModel);
        KeenController gameController = new KeenController(gameModel,gameView,this);
        ViewGroup container = (ViewGroup) findViewById(R.id.keen_container);

        ProgressBar prog = (ProgressBar) findViewById(R.id.progress_bar);
        prog.setVisibility(View.GONE);

        container.addView(gameView);

        // Allowing onResume to understand that the current game needs to be loaded
        continuing = true;

    }

    //starts a multi threaded process to generate a new level
    public void runGame()
    {

        Thread gameGenThread = new Thread(new LevelGenMultiThread(this,mHandler));
        gameGenThread.start();
    }


}
