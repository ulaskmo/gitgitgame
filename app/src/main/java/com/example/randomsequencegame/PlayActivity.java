package com.example.randomsequencegame;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ArrayList<Integer> sequence;
    private int currentStep = 0;
    private int score = 0; // Total score
    private int round = 1; // Current round
    private boolean inputEnabled = false; // Prevent accidental inputs
    private boolean movementDetected = false; // To prevent multiple detections
    private static final float THRESHOLD = 6.0f; // Minimum tilt to consider
    private static final float DOMINANCE_FACTOR = 1.5f; // Axis dominance threshold
    private TextView goTextView, scoreTextView, roundTextView; // UI elements
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sequence = getIntent().getIntegerArrayListExtra("sequence");
        score = getIntent().getIntExtra("score", 0); // Retrieve current score
        round = getIntent().getIntExtra("round", 1); // Retrieve current round

        goTextView = findViewById(R.id.goTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        roundTextView = findViewById(R.id.roundTextView);
        handler = new Handler();

        updateUI(); // Display current score and round

        Toast.makeText(this, "Match the sequence by tilting the phone!", Toast.LENGTH_SHORT).show();

        // Start the game with the first "GO!"
        showGoForNextColor();
    }

    private void showGoForNextColor() {
        if (currentStep < sequence.size()) {
            goTextView.setText("GO!");
            inputEnabled = false;

            handler.postDelayed(() -> {
                goTextView.setText(""); // Clear the "GO!" message
                inputEnabled = true;   // Enable input for the current step
                movementDetected = false; // Reset movement detection for this step
            }, 1000); // 1-second delay before enabling input
        }
    }

    private void updateUI() {
        // Update the score and round display
        scoreTextView.setText("Score: " + score);
        roundTextView.setText("Round: " + round);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!inputEnabled || movementDetected) return; // Ignore input until enabled or if movement already detected

        float x = event.values[0];
        float y = event.values[1];

        int direction = determineDirection(x, y);

        if (direction != -1 && direction == sequence.get(currentStep)) {
            movementDetected = true; // Lock input until the next "GO!"
            currentStep++;
            if (currentStep < sequence.size()) {
                showGoForNextColor(); // Show "GO" for the next step
            } else {
                // Round Complete
                score += 4; // Add 4 points for successfully completing the round
                round++; // Increment the round
                updateUI();

                Toast.makeText(this, "Round Complete! Starting Round " + round, Toast.LENGTH_SHORT).show();

                // Start a new round with an extended sequence
                handler.postDelayed(() -> {
                    Intent intent = new Intent(this, SequenceActivity.class);
                    intent.putExtra("score", score); // Pass updated score
                    intent.putExtra("round", round); // Pass updated round
                    startActivity(intent);
                    finish();
                }, 1000);
            }
        } else if (direction != -1) {
            // Incorrect tilt
            Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
            inputEnabled = false;

            handler.postDelayed(() -> {
                Intent intent = new Intent(this, GameOverActivity.class);
                intent.putExtra("score", score); // Pass the final score to GameOverActivity
                startActivity(intent);
                finish();
            }, 1000);
        }
    }

    private int determineDirection(float x, float y) {
        if (Math.abs(x) > THRESHOLD && Math.abs(x) > Math.abs(y) * DOMINANCE_FACTOR) {
            if (x > 0) return 3; // Down (Yellow)
            else return 2;       // Up (Green)
        }

        if (Math.abs(y) > THRESHOLD && Math.abs(y) > Math.abs(x) * DOMINANCE_FACTOR) {
            if (y > 0) return 0; // Right (Red)
            else return 1;       // Left (Blue)
        }

        return -1; // No significant movement
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
