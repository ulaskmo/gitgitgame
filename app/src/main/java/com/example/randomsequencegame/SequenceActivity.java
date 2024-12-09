package com.example.randomsequencegame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class SequenceActivity extends AppCompatActivity {
    private ArrayList<Integer> sequence;
    private Random random;
    private TextView sequenceText;
    private Handler handler;
    private int score; // Current score
    private int round; // Current round

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);

        sequenceText = findViewById(R.id.sequenceText);
        handler = new Handler();

        random = new Random();
        sequence = new ArrayList<>();

        // Retrieve score and round from Intent
        score = getIntent().getIntExtra("score", 0);
        round = getIntent().getIntExtra("round", 1);

        generateSequence();
        showSequence();
    }

    private void generateSequence() {
        sequence.clear();
        int sequenceLength = 4 + (round - 1) * 2; // Sequence length increases by 2 for each round
        for (int i = 0; i < sequenceLength; i++) {
            sequence.add(random.nextInt(4)); // Random numbers representing colors
        }
    }

    private void showSequence() {
        sequenceText.setText("Watch the sequence!");
        handler.postDelayed(() -> displaySequenceStep(0), 1000); // Start after 1 second
    }

    private void displaySequenceStep(int step) {
        if (step < sequence.size()) {
            // Update UI to show the current sequence step (e.g., color)
            sequenceText.setText("Color: " + getColorName(sequence.get(step)));

            // Continue showing the next step after a delay
            handler.postDelayed(() -> displaySequenceStep(step + 1), 1000);
        } else {
            // Transition to PlayActivity after showing the sequence
            startPlayActivity();
        }
    }

    private String getColorName(int colorIndex) {
        switch (colorIndex) {
            case 0: return "Red";
            case 1: return "Blue";
            case 2: return "Green";
            case 3: return "Yellow";
            default: return "Unknown";
        }
    }

    private void startPlayActivity() {
        handler.postDelayed(() -> {
            Intent intent = new Intent(SequenceActivity.this, PlayActivity.class);
            intent.putIntegerArrayListExtra("sequence", sequence); // Pass updated sequence
            intent.putExtra("score", score); // Pass current score
            intent.putExtra("round", round); // Pass current round
            startActivity(intent);
            finish();
        }, 1000);
    }
}
