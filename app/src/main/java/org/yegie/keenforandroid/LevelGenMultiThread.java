package org.yegie.keenforandroid;

import android.os.Bundle;

/**
 * Created by Sergey on 9/21/2016.
 */
public class LevelGenMultiThread implements Runnable {

    private KeenActivity gameActivity;

    public LevelGenMultiThread(KeenActivity gameActivity)
    {

        this.gameActivity = gameActivity;

    }

    @Override
    public void run() {

        Bundle gameData = gameActivity.getGameData();
        int size = gameData.getInt("size");
        int diff = gameData.getInt("diff");
        int mult = gameData.getInt("mult");
        long seed = gameData.getLong("seed");
        KeenModelBuilder builder=new KeenModelBuilder();
        KeenModel gameModel = gameActivity.getGameModel();
        gameModel = builder.build(size,diff,mult,seed);
        gameActivity.setGameModel(gameModel);

    }
}
