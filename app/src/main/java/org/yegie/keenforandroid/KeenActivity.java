package org.yegie.keenforandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import java.util.Random;

public class KeenActivity extends Activity {
    private static final String TAG = "KeenActivity";

    private int size = 4;
    private int diff = 2;
    private int multOnly = 0;
    private long seed = 10101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keen);
    //    Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
    //    setSupportActionBar(Toolbar);

        size=getIntent().getExtras().getInt(MenuActivity.GAME_SIZE,0);
        diff=getIntent().getExtras().getInt(MenuActivity.GAME_DIFF,0);
        multOnly=getIntent().getExtras().getInt(MenuActivity.GAME_MULT,0);
        seed=getIntent().getExtras().getLong(MenuActivity.GAME_SEED,0);

        if(size<3 || size>9) {
            Log.e("KEEN","Got invalid game size, quitting...");
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        runGame();
    }

    @Override
    public void onBackPressed() {
        /// super.onBackPressed();
        Log.d(TAG,"TODO: Implement undo/redo here");
    }

    public void runGame()
    {
        KeenModelBuilder builder=new KeenModelBuilder();

        KeenModel gameModel = builder.build(size,diff,multOnly,seed);

        KeenView gameView = new KeenView(this,gameModel);
        KeenController gameController = new KeenController(gameModel,gameView);

        ViewGroup container = (ViewGroup) findViewById(R.id.keen_container);
        container.addView(gameView);
    }


}
