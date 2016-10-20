package org.yegie.keenforandroid;

import android.content.Context;
import android.util.Log;

/**
 * Created by Sergey on 5/19/2016.
 */
public class KeenController implements KeenView.OnGridClickListener {
    private static final String TAG = "KeenController";

    private KeenModel gameModel;
    private KeenView gameView;
    private KeenActivity parent;

    public KeenController(KeenModel gameModel,KeenView gameView,KeenActivity parent)
    {

        this.gameModel = gameModel;
        this.gameView = gameView;
        this.parent = parent;

        gameView.setOnGridClickListener(this);

    }

    @Override
    public void onEndScreenClick()
    {
        parent.returnToMainMenu();
    }

    @Override
    public void onGridClick(int x, int y) {
        //Log.d(TAG,"Grid "+x+","+y+" clicked.");

        if(x == gameModel.getActiveX() && y == gameModel.getActiveY())
        {

            gameModel.toggleFinalGuess();
        } else
        {
            gameModel.setActiveX(x);
            gameModel.setActiveY(y);
        }

        gameView.invalidate();
    }

    @Override
    public void onButtonClicked(int i) {
        Log.d(TAG,"Button "+i+" clicked.");
        int x = gameModel.getActiveX();
        int y = gameModel.getActiveY();

        if(x != -1 && y != -1)
        {
            if(gameModel.getFinalGuess())
            {
                gameModel.clearGuesses(x,y);
                gameModel.setCellFinalGuess(x,y,i);
            }else
            {
                gameModel.clearFinal(x,y);
                gameModel.addToCellGuesses(x,y,i);
            }

        }

        gameModel.puzzleWon();

        gameView.invalidate();

    }
}
