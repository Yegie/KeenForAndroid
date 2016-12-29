package org.yegie.keenforandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;

/**
 * Created by Sergey on 7/17/2016.
 */
public class MenuActivity extends Activity {
    protected static final String GAME_SIZE = "gameSize";
    protected static final String GAME_DIFF = "gameDiff";
    protected static final String GAME_MULT = "gameMultOnly";
    protected static final String GAME_SEED = "gameSeed";
    protected static final String GAME_CONT = "contPrev";

    private int gameSize=3;
    private int gameDiff=1;
    private int gameMult=0;
    private long gameSeed=1010101;

    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final Intent intent=new Intent(this,KeenActivity.class);

        if(KeenActivity.getGameInProg(getBaseContext())){
            startActivity(intent);
        }

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        if(mProgress!=null) {
            mProgress.setVisibility(View.GONE);
        }

        Button startButton= (Button) findViewById(R.id.button_start);
        Button contButton= (Button) findViewById(R.id.button_cont);
        if(KeenActivity.getCanCont(getBaseContext())){
            contButton.setVisibility(View.VISIBLE);
        }

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
                        gameDiff = i;
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

        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(GAME_CONT,true);
                startActivity(intent);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }


    private void startGame() {
        //gameSeed setup
        gameSeed = System.currentTimeMillis();

        mProgress.setVisibility(View.VISIBLE);
        Intent intent=new Intent(this,KeenActivity.class);
        intent.putExtra(GAME_SIZE,gameSize);
        intent.putExtra(GAME_DIFF,gameDiff);
        intent.putExtra(GAME_MULT,gameMult);
        intent.putExtra(GAME_SEED,gameSeed);
        Log.d("info dump","Size: "+gameSize+" Diff: "+gameDiff+" GameMult: "+gameMult);
        startActivity(intent);
    }
}
