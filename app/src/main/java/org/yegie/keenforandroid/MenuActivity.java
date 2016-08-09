package org.yegie.keenforandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Sergey on 7/17/2016.
 */
public class MenuActivity extends Activity {
    protected static final String GAME_SIZE = "gameSize";

    private int gameSize=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button startButton=(Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.this.startGame();
            }
        });
    }

    private void startGame() {
        Intent intent=new Intent(this,KeenActivity.class);
        intent.putExtra(GAME_SIZE,gameSize);
        startActivity(intent);
    }
}
