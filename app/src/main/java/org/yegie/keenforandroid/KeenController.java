package org.yegie.keenforandroid;

import android.util.Log;

/**
 * Not a perfect implementation of MVC Since the view can see the model,
 * but it functions pretty well.
 *
 * Created by Sergey on 5/19/2016.
 */
public class KeenController implements KeenView.OnGridClickListener {
    private static final String TAG = "KeenController";

    private KeenModel gameModel;
    private KeenView gameView;
    private KeenActivity parent;

    //constructor
    //sets up the controller as the listener for the game view
    public KeenController(KeenModel gameModel,KeenView gameView,KeenActivity parent)
    {

        this.gameModel = gameModel;
        this.gameView = gameView;
        this.parent = parent;

        gameView.setOnGridClickListener(this);

    }

    //handles a touch event anywhere on the screen after the game is won
    @Override
    public void onEndScreenClick()
    {
        parent.returnToMainMenu();
    }

    //handles a click on the grid by updating the active cords
    //or toggling between guess and answer
    @Override
    public void onGridClick(int x, int y) {

        if(x == gameModel.getActiveX() && y == gameModel.getActiveY())
        {

            gameModel.toggleFinalGuess();
        } else
        {
            gameModel.setActiveX((short) x);
            gameModel.setActiveY((short) y);
        }

        gameView.invalidate();
    }

    //handles a click of one of the number buttons
    @Override
    public void onButtonClicked(int i) {
        short x = gameModel.getActiveX();
        short y = gameModel.getActiveY();

        if(x != -1 && y != -1)
        {
            gameModel.addCurToUndo(x,y);
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

    //handles a click of the undo button
    @Override
    public void onUndoButtonClick() {
        gameModel.undoOneStep();
        gameView.invalidate();
    }
}
