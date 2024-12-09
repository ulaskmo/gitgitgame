package com.example.randomsequencegame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper; // Helper to manage the database
    private int playerScore; // The final score of the player

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_game_over);

        // Initialize database helper and retrieve final score
        dbHelper = new DatabaseHelper(this);
        playerScore = getIntent().getIntExtra("score", 0);

        // UI elements
        TextView scoreDisplay = findViewById(R.id.scoreView);
        EditText playerNameInput = findViewById(R.id.nameInput);
        Button saveScoreButton = findViewById(R.id.saveButton);

        // Display the final score
        scoreDisplay.setText("Final Score: " + playerScore);

        // Save button listener to save player's name and score
        saveScoreButton.setOnClickListener(v -> {
            String playerName = playerNameInput.getText().toString().trim();
            if (!playerName.isEmpty()) {
                dbHelper.savePlayerScore(playerName, playerScore);
                Toast.makeText(this, "Your score has been saved!", Toast.LENGTH_SHORT).show();

                // Redirect to High Score screen
                Intent intent = new Intent(GameOverActivity.this, HighScoreActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Prompt user to enter a valid name
                Toast.makeText(this, "Please enter a valid name!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
