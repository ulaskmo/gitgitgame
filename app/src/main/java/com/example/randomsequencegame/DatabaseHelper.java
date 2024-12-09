package com.example.randomsequencegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Leaderboard.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_HIGH_SCORES = "tbl_high_scores";
    private static final String COLUMN_PLAYER_ID = "player_id";
    private static final String COLUMN_PLAYER_NAME = "player_name";
    private static final String COLUMN_PLAYER_SCORE = "player_score";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_HIGH_SCORES + " (" +
                COLUMN_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYER_NAME + " TEXT, " +
                COLUMN_PLAYER_SCORE + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGH_SCORES);
        onCreate(db);
    }

    public void savePlayerScore(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, name);
        values.put(COLUMN_PLAYER_SCORE, score);
        db.insert(TABLE_HIGH_SCORES, null, values);
        db.close();
    }

    public ArrayList<String> fetchTopScores() {
        ArrayList<String> topScores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_PLAYER_NAME + ", " + COLUMN_PLAYER_SCORE +
                " FROM " + TABLE_HIGH_SCORES +
                " ORDER BY " + COLUMN_PLAYER_SCORE + " DESC LIMIT 5";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String playerName = cursor.getString(0);
                int playerScore = cursor.getInt(1);
                topScores.add(playerName + ": " + playerScore);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return topScores;
    }
}
