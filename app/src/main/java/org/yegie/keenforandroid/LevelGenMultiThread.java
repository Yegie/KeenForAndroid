package org.yegie.keenforandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

/**
 * Handles multithreading of level generation to prevent
 * frame drops in the ui thread
 *
 * Created by Sergey on 9/21/2016.
 */
public class LevelGenMultiThread implements Runnable {

    private KeenActivity gameActivity;
    private Handler mHandler;

    //constructor sets up parent and multithread handler
    public LevelGenMultiThread(KeenActivity gameActivity, Handler mHandler)
    {

        this.gameActivity = gameActivity;
        this.mHandler = mHandler;

    }

    //creates a game model, and when it is done runs a method on the
    //gameActivity that tells it to start the game
    @Override
    public void run() {

        Bundle gameData = gameActivity.getGameData();
        int size = gameData.getInt("size");
        int diff = gameData.getInt("diff");
        int mult = gameData.getInt("mult");
        long seed = gameData.getLong("seed");
        KeenModelBuilder builder=new KeenModelBuilder();
        final KeenModel gameModel = builder.build(size,diff,mult,seed);


        mHandler.post(new Runnable() {
            public void run() {
                gameActivity.runGameModel(gameModel);
            }
        });
    }
}
