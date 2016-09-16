package org.yegie.keenforandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

/**
 * Created by Sergey on 7/17/2016.
 */
public class MenuActivity extends Activity {
    protected static final String GAME_SIZE = "gameSize";
    protected static final String GAME_DIFF = "gameDiff";
    protected static final String GAME_MULT = "gameMultOnly";
    protected static final String GAME_SEED = "gameSeed";

    private int gameSize=5;
    private int gameDiff=3;
    private int gameMult=0;
    private long gameSeed=1010101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button startButton= (Button) findViewById(R.id.button_start);

        //set up the size button
        Spinner sizeButton= (Spinner) findViewById(R.id.button_size);

        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.size_list, R.layout.menu_spinner_layout);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeButton.setAdapter(sizeAdapter);

        sizeButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String itemSelected = parentView.getItemAtPosition(position).toString();
                String diffs[] = getResources().getStringArray(R.array.size_list);
                for (int i = 0; i < diffs.length; ++i) {
                    if (diffs[i].equals(itemSelected)) {
                        gameSize = i + 3;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }

        });


        //set up the size button
        Spinner diffButton= (Spinner) findViewById(R.id.button_diff);
        ArrayAdapter<CharSequence> diffAdapter = ArrayAdapter.createFromResource(this,
                R.array.diff_list, R.layout.menu_spinner_layout);
        diffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffButton.setAdapter(diffAdapter);

        diffButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String itemSelected = parentView.getItemAtPosition(position).toString();
                String diffs[] = getResources().getStringArray(R.array.diff_list);
                for(int i = 0; i < diffs.length; ++i)
                {
                    if(diffs[i].equals(itemSelected))
                    {
                        gameDiff = i+1;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }

        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.this.startGame();
            }
        });
    }

    public void onCheckboxClicked(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();
        if(checked && view.getId()==R.id.button_mult)
            gameMult = 1;
        else
            gameMult = 0;
    }

    private void startGame() {
        Intent intent=new Intent(this,KeenActivity.class);
        intent.putExtra(GAME_SIZE,gameSize);
        intent.putExtra(GAME_DIFF,gameDiff);
        intent.putExtra(GAME_MULT,gameMult);
        intent.putExtra(GAME_SEED,gameSeed);
        Log.d("info dump","Size: "+gameSize+" Diff: "+gameDiff+" GameMult: "+gameMult);
        startActivity(intent);
    }
}