package com.kavurivinay.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {
    Button startButton, correctButton, wrongButton1, wrongButton2, wrongButton3;
    TextView myText, score;
    int counter;
    boolean doLoop = true;
    final Handler h = new Handler();
    Gson gson;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences= getSharedPreferences("clickValues", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson(); //converts class to a string to store in shared preferences
        Score sc = gson.fromJson(sharedPreferences.getString("scores", "{\"myScore\": 0}"), Score.class);
        counter = 0;
        startButton = findViewById(R.id.start_button);
        myText = findViewById(R.id.textView);
        score = findViewById(R.id.score);
        score.setText(""+sc.myScore);
    }
    public void doAction(View v)
    {
        final int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        final int height = Resources.getSystem().getDisplayMetrics().heightPixels - 200;
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                correctButton.setX((float) Math.random() * (width - correctButton.getWidth()));
                correctButton.setY((float) Math.random() * (height - correctButton.getHeight()));

                wrongButton1.setX((float) Math.random() * (width - wrongButton1.getWidth()));
                wrongButton1.setY((float) Math.random() * (height - wrongButton1.getHeight()));

                wrongButton2.setX((float) Math.random() * (width - wrongButton2.getWidth()));
                wrongButton2.setY((float) Math.random() * (height - wrongButton2.getHeight()));

                wrongButton3.setX((float) Math.random() * (width - wrongButton3.getWidth()));
                wrongButton3.setY((float) Math.random() * (height - wrongButton3.getHeight()));
                if(doLoop)
                {
                    h.postDelayed(this, 1200);
                }
                else{
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("app", "Waiting 2 Second");
                        }
                    }, 2000);
                }
            }
        }, 1200);
    }

    public void wrongAnswer(View view){
        doLoop = false;
        Gson gson = new Gson();
        Score sc = gson.fromJson(sharedPreferences.getString("scores", "{\"myScore\": 0}"), Score.class);
        sc.myScore = Math.max(sc.myScore, counter);
        editor.putString("scores", gson.toJson(sc));
        editor.apply();
        setContentView(R.layout.activity_main);
        sharedPreferences= getSharedPreferences("clickValues", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sc = gson.fromJson(sharedPreferences.getString("scores", "{\"myScore\": 0}"), Score.class);
        counter = 0;
        startButton = findViewById(R.id.start_button);
        myText = findViewById(R.id.textView);
        score = findViewById(R.id.score);
        score.setText(""+sc.myScore);
    }

    public void correctAnswer(View view){
        counter += 1;
    }

    public void startGame(View view) {
        setContentView(R.layout.game_scren);
        correctButton = findViewById(R.id.correct_answer);
        wrongButton1 = findViewById(R.id.wrong1);
        wrongButton2 = findViewById(R.id.wrong2);
        wrongButton3 = findViewById(R.id.wrong3);
        doLoop = true;
        doAction(view);
    }
    public class Score {
        public int myScore = 0;
    }
}
