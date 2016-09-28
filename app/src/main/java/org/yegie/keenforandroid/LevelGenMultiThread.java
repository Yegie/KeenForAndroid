package org.yegie.keenforandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

/**
 * Created by Sergey on 9/21/2016.
 */
public class LevelGenMultiThread implements Runnable {

    private KeenActivity gameActivity;
    private Handler mHandler;

    public LevelGenMultiThread(KeenActivity gameActivity, Handler mHandler)
    {

        this.gameActivity = gameActivity;
        this.mHandler = mHandler;

    }

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
