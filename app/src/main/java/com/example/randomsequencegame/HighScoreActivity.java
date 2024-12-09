package com.example.randomsequencegame;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        dbHelper = new DatabaseHelper(this);
        ArrayList<String> highScores = dbHelper.fetchTopScores();

        ListView highScoresListView = findViewById(R.id.highScoreList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highScores);
        highScoresListView.setAdapter(adapter);
    }
}
