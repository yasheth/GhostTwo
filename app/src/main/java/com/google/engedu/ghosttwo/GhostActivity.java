package com.google.engedu.ghosttwo;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;



public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private String superWord="";
    TextView label,ghostText;
    private int whoWentFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }


        label = (TextView) findViewById(R.id.gameStatus);
        ghostText = (TextView) findViewById(R.id.ghostText);

        //if instance saved get the values
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            Log.v("SavedInstanceSTATE","TRUE");
            superWord = savedInstanceState.getString("GHOST_TEXT");
            String labelContent = savedInstanceState.getString("LABEL");
            userTurn=savedInstanceState.getBoolean("TURN");
            ghostText.setText(superWord);
            label.setText(labelContent);
            //logging values of values from savedInstance
            Log.v(superWord," ");
            Log.v(labelContent," ");
            Log.v(String.valueOf(userTurn)," ");
        }
        else
            onStart(null);

    }

    //Saving State before exiting
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v("onSAVEINSTANCS...","TRUE");
        // Save the user's current game state
        savedInstanceState.putString("GHOST_TEXT", (String)ghostText.getText());
        savedInstanceState.putString("LABEL", (String)label.getText());
        savedInstanceState.putBoolean("TURN",userTurn);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    //method to control keyboard actions
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //converting to character
        char pressedKey = (char) event.getUnicodeChar();

        //making character lower case and only allowing values from a to z
        pressedKey=Character.toLowerCase(pressedKey);
        if(pressedKey>='a'&& pressedKey<='z') {
            superWord=superWord+String.valueOf(pressedKey);
            ghostText.setText(superWord);
            if(dictionary.isWord(superWord)){
                label.setText("Complete Word. Bravo!");
            }
            else{
                label.setText("");
            }
        }
        //since user has already pressed his alphabet it should be computers chance next
        userTurn=true;
        onStart(null);
        return false;
    }

    //method to handle computers functionality
    private void computerTurn() {
        //get good word that starts from current word
        String newWord=dictionary.getGoodWordStartingWith(superWord,whoWentFirst);
        Log.v("New word is",newWord);

        //if word received is same as current word then COMPUTER WINS as user ended the word
        if(newWord.equals("sameAsPrefix")){
            Toast toast=Toast.makeText(getApplicationContext(), "Computer Wins. You ended the word.", Toast.LENGTH_SHORT);
            toast.show();
            label.setText("Computer Wins. You ended the word.");
            superWord="";
            ghostText.setText("");
            onStart(null);
        }
        //if no such word is found
        else if(newWord.equals("noWord")){
            Toast toast=Toast.makeText(getApplicationContext(), "Computer Wins. No Such Word", Toast.LENGTH_SHORT);
            toast.show();
            label.setText("Computer Wins. No Such Word");
            superWord="";
            ghostText.setText("");
            onStart(null);
        }
        else{
            //if word is valid then append superWord and change TextView to display
            superWord=newWord.substring(0,(superWord.length()+1));
            ghostText.setText(superWord);
            Toast toast=Toast.makeText(getApplicationContext(), "Your Turn", Toast.LENGTH_SHORT);
            toast.show();

            //computerTurn is over so its users turn now
            userTurn = false;
            onStart(null);
        }
    }

    public void onclick(View v){
        //when challenge button is pressed
        if(v.getId()==R.id.btnchallenge){
            superWord=(String)ghostText.getText();

            //check only if word's length is 4 or more
            if(superWord.length()>=4){
                //same logic as computerTurn just checking for User
                String newWord=dictionary.getAnyWordStartingWith(superWord);
                Log.v("New Word is ",newWord);
                if(newWord.equals("sameAsPrefix")){
                    Toast toast=Toast.makeText(getApplicationContext(), "You Win. Computer ended the word.", Toast.LENGTH_SHORT);
                    toast.show();
                    label.setText("You Wins. C ended the word.");
                    superWord="";
                    ghostText.setText("");
                    onStart(null);
                }
                else if(newWord.equals("noWord")){
                    Toast toast=Toast.makeText(getApplicationContext(), "You Win. No Such Word", Toast.LENGTH_SHORT);
                    toast.show();
                    label.setText("You Wins. No Such Word");
                    superWord="";
                    ghostText.setText("");
                    onStart(null);
                }
                else{
                    Toast toast=Toast.makeText(getApplicationContext(), "You Lose. Word Exists.\n"+newWord, Toast.LENGTH_SHORT);
                    toast.show();
                    label.setText("You Lose Longer word exist");
                    superWord="";
                    ghostText.setText("");
                    onStart(null);
                }
            }
            //if word is less than 4 characters and user challenged
            else{
                Toast toast=Toast.makeText(getApplicationContext(), "Computer Wins.\nWord is still less than 4 chars", Toast.LENGTH_SHORT);
                toast.show();
                superWord="";
                ghostText.setText("");
                onStart(null);
            }


        }
        //when restart button is pressed
        if(v.getId()==R.id.btnrestart){
            Toast toast=Toast.makeText(getApplicationContext(), "Computer Wins. No Such Word", Toast.LENGTH_SHORT);
            toast.show();
            superWord="";
            ghostText.setText("");
            onStart(null);
        }
    }

    public boolean onStart(View view) {
        //switching turn values
        if(userTurn) {
            userTurn = false;
        }else{
            userTurn = true;
        }
        whoWentFirst = userTurn ? 1 : 0;
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
