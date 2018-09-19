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
    protected static final String DARK_MODE = "darkMode";

    //default values for game launch
    @SuppressWarnings("FieldCanBeLocal")
    private long gameSeed=1010101;
    private ApplicationCore app;

    //the buttons that are created in onCreate
    private Button contButton;
    private Spinner sizeButton;
    private Spinner diffButton;

    // initialize all the buttons and
    // create and assign listeners for all the buttons and spinners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = (ApplicationCore) getApplication();
        if(app.isDarkMode())
            setTheme(R.style.AppTheme_Base_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
                        app.setGameSize(i + 3);
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
                        app.setGameDiff(i);
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

        contButton.setVisibility(app.isCanCont() ? View.VISIBLE : View.GONE);

        diffButton.setSelection(app.getGameDiff());

        sizeButton.setSelection(app.getGameSize()-3);

//        CheckBox ckboxD = findViewById(R.id.button_dark_mode);
//        ckboxD.setChecked(app.isDarkMode());

        CheckBox ckbox = findViewById(R.id.button_mult);
        ckbox.setChecked(app.getGameMult()!=0);
    }

    //save the current menu selections to be restored at a later point
    @Override
    public void onPause(){
        app.savePrefs();
        super.onPause();
    }

    //handler and listener for checkbox clicks (mult only button)
    public void onCheckboxClicked(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.button_mult:
                app.setGameMult(checked ? 1 : 0);
                break;
//            case R.id.button_dark_mode:
//                app.setDarkMode(checked);
//                break;
        }
    }

    //start the game activity with the correct parameters
    private void startGame() {
        //gameSeed setup
        gameSeed = System.currentTimeMillis();
        Intent intent=new Intent(this,KeenActivity.class);
        intent.putExtra(GAME_CONT, false);
        intent.putExtra(GAME_SIZE,app.getGameSize());
        intent.putExtra(GAME_DIFF,app.getGameDiff());
        intent.putExtra(GAME_MULT,app.getGameMult());
        intent.putExtra(GAME_SEED,gameSeed);
        startActivity(intent);
    }
}
