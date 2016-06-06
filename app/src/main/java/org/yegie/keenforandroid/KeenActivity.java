package org.yegie.keenforandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

public class KeenActivity extends Activity {

    private int size = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keen);
        runGame();
    }



    public void runGame()
    {
        KeenModelBuilder builder=new KeenModelBuilder();

        KeenModel gameModel = builder.build(size);

        KeenView gameView = new KeenView(this,gameModel);
        KeenController gameController = new KeenController(gameModel,gameView);

        ViewGroup container = (ViewGroup) findViewById(R.id.keen_container);
        container.addView(gameView);
    }
}
