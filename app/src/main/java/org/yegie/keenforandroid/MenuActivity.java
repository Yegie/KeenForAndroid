package org.yegie.keenforandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

/**
 * Menu Activity that handles selecting and starting a level
 *
 * Created by Sergey on 7/17/2016.
 */
public class MenuActivity extends Activity {
    //names by which to read from saved prefs
    protected static final String GAME_SIZE = "gameSize";
    protected static final String GAME_DIFF = "gameDiff";
    protected static final String GAME_MULT = "gameMultOnly";
    protected static final String GAME_SEED = "gameSeed";
    protected static final String GAME_CONT = "contPrev";
    protected static final String MENU_SIZE = "menuSize";
    protected static final String MENU_DIFF = "menuDiff";
    protected static final String MENU_MULT = "menuMult";

    //default values for game launch
    private int gameSize=3;
    private int gameDiff=1;
    private int gameMult=0;
    @SuppressWarnings("FieldCanBeLocal")
    private long gameSeed=1010101;

    //pref file stored between games
    private SharedPreferences sharedPref;

    //the buttons that are created in onCreate
    private Button contButton;
    private Spinner sizeButton;
    private Spinner diffButton;

    // initialize all the buttons and
    // create and assign listeners for all the buttons and spinners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Button startButton = findViewById(R.id.button_start);
        contButton = findViewById(R.id.button_cont);

        //set up the size button
        sizeButton= findViewById(R.id.button_size);

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


        //set up the diff button
        diffButton = findViewById(R.id.button_diff);
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
                final Intent intent=new Intent(MenuActivity.this,KeenActivity.class);
                intent.putExtra(GAME_CONT,true);
                startActivity(intent);
            }
        });
    }

    //restore the menu selections from the saved prefs file
    @Override
    public void onResume() {
        super.onResume();

        boolean canCont=sharedPref.getBoolean(KeenActivity.CAN_CONT,false);
        contButton.setVisibility(canCont ? View.VISIBLE : View.GONE);

        gameDiff = sharedPref.getInt(MENU_DIFF,0);
        diffButton.setSelection(gameDiff);

        gameSize = sharedPref.getInt(MENU_SIZE,3);
        sizeButton.setSelection(gameSize-3);

        gameMult = sharedPref.getBoolean(MENU_MULT,false) ? 1 : 0;
        CheckBox ckbox = findViewById(R.id.button_mult);
        ckbox.setChecked(gameMult!=0);
    }

    //save the current menu selections to be restored at a later point
    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(MENU_MULT,gameMult!=0);
        editor.putInt(MENU_DIFF,gameDiff);
        editor.putInt(MENU_SIZE,gameSize);
        editor.apply();
    }

    //handler and listener for checkbox clicks (mult only button)
    public void onCheckboxClicked(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();
        if(checked && view.getId()==R.id.button_mult)
            gameMult = 1;
        else
            gameMult = 0;
    }

    //start the game activity with the correct parameters
    private void startGame() {
        //gameSeed setup
        gameSeed = System.currentTimeMillis();
        Intent intent=new Intent(this,KeenActivity.class);
        intent.putExtra(GAME_CONT, false);
        intent.putExtra(GAME_SIZE,gameSize);
        intent.putExtra(GAME_DIFF,gameDiff);
        intent.putExtra(GAME_MULT,gameMult);
        intent.putExtra(GAME_SEED,gameSeed);
        startActivity(intent);
    }
}
