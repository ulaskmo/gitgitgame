package com.example.randomsequencegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Play button
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SequenceActivity.class);
            startActivity(intent);
        });

        // High Score button
        Button highScoreButton = findViewById(R.id.highScoreButton);
        highScoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
            startActivity(intent);
        });
    }
}
