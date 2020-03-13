package com.kavurivinay.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Button startButton, correctButton, wrongButton1, wrongButton2, wrongButton3;
    TextView myText, score, question;
    int counter;
    boolean doLoop = true;
    final Handler h = new Handler();
    Gson gson;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Trivia> triviaQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        triviaQuestions = new ArrayList<Trivia>();
        triviaQuestions.add(new Trivia("Established in 1607, what settlement was founded by John Smith in Virginia?", "Jamestown", "Roanoke", "Plymouth", "New York"));
        triviaQuestions.add(new Trivia("Who is the only US President to serve two non-consecutive terms?", "Grover Cleveland", "Franklin D Roosevelt", "Theodore Roosevelt", "William Taft"));
        triviaQuestions.add(new Trivia("What wonder of the ancient world was destroyed by arson the same night Alexander the Great was born?", "Temple of Artemis", "Lighthouse of Alexandria", "Colossus of Rhodes", "Hanging Gardens of Babylon"));
        triviaQuestions.add(new Trivia("Who was the second man to set foot on the moon?", "Edwin Aldrin", "Neil Armstrong", "Michael Collins", "Charles Conrad"));

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
        Trivia randQuestion = triviaQuestions.get((int)(triviaQuestions.size() * Math.random()));
        question.setText(randQuestion.myQuestion);
        correctButton.setText(randQuestion.myCorrect);
        wrongButton1.setText(randQuestion.myWrong1);
        wrongButton2.setText(randQuestion.myWrong2);
        wrongButton3.setText(randQuestion.myWrong3);
    }

    public void startGame(View view) {
        setContentView(R.layout.game_scren);
        question = findViewById(R.id.question);
        correctButton = findViewById(R.id.correct_answer);
        wrongButton1 = findViewById(R.id.wrong1);
        wrongButton2 = findViewById(R.id.wrong2);
        wrongButton3 = findViewById(R.id.wrong3);

        Trivia randQuestion = triviaQuestions.get((int)(triviaQuestions.size() * Math.random()));
        question.setText(randQuestion.myQuestion);
        correctButton.setText(randQuestion.myCorrect);
        wrongButton1.setText(randQuestion.myWrong1);
        wrongButton2.setText(randQuestion.myWrong2);
        wrongButton3.setText(randQuestion.myWrong3);

        doLoop = true;
        doAction(view);
    }
    public class Score {
        public int myScore = 0;
    }
    public class Trivia {
        public String myQuestion, myCorrect, myWrong1, myWrong2, myWrong3;
        public Trivia(String q, String c, String w1, String w2, String w3){
            myCorrect = c;
            myQuestion = q;
            myWrong1 = w1;
            myWrong2 = w2;
            myWrong3 = w3;
        }
    }
}
