package com.quiz.quizappsport.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.quiz.quizappsport.User;
import com.quiz.quizappsport.interfaces.IDatabaseHandler;

public class UsersDbHelper extends SQLiteOpenHelper implements IDatabaseHandler {
    private static UsersDbHelper sInstance;

    private static final String TAG = UsersDbHelper.class.getSimpleName();
    private static final String DB_NAME = "QuizApp.db";
    private static final int DB_VERSION = 2;

    private static final String TABLE_USER = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_NICKNAME = "nickname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASS = "password";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_BEST_SCORE = "score";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NICKNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASS + " TEXT,"
            + COLUMN_AGE + " INTEGER,"
            + COLUMN_GENDER + " TEXT,"
            + COLUMN_BEST_SCORE + " INTEGER)";

    public static synchronized UsersDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UsersDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private UsersDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2)
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN " + COLUMN_BEST_SCORE + " INTEGER DEFAULT 0");
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NICKNAME, user.getNickName());
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_PASS, user.getPassword());
            values.put(COLUMN_AGE, user.getAge());
            values.put(COLUMN_GENDER, user.getGender());

            long id = db.insertOrThrow(TABLE_USER, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "User inserted " + id);
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add user to database");
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateBestScore(int id, int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] selectionArgs = {String.valueOf(id)};

        ContentValues values = new ContentValues();

        db.beginTransaction();

        try {
            values.put(COLUMN_BEST_SCORE, score);
            db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", selectionArgs);

            db.setTransactionSuccessful();
            Log.d(TAG, "User" + id + ": new score = " + score);
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update user best score to database");
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // selection arguments
        String[] selectionArgs = {String.valueOf(id)};

        String selectQuery = String.format("SELECT * FROM %s WHERE %s = ?",
                TABLE_USER, COLUMN_USER_ID);

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.getCount() < 1) {
            return null;
        }

        cursor.moveToFirst();

        User user = new User();
        user.setId(cursor.getInt(0));
        user.setNickName(cursor.getString(1));
        user.setAge(cursor.getInt(4));
        user.setGender(cursor.getString(5));
        user.setBestScore(cursor.getInt(6));

        cursor.close();
        db.close();

        return user;
    }

    @Override
    public int getIdUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        int idUser = -1;

        // selection arguments
        String[] selectionArgs = {email, pass};

        String selectQuery = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?",
                TABLE_USER, COLUMN_EMAIL, COLUMN_PASS);

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            idUser = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return idUser;
    }

    public boolean existUser(String name, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean existUser = false;

        // selection arguments
        String[] selectionArgs = {name, email};

        String selectQuery = String.format("SELECT * FROM %s WHERE %s = ? OR %s = ?",
                TABLE_USER, COLUMN_NICKNAME, COLUMN_EMAIL);

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.getCount() > 0) {
            existUser = true;
        }

        cursor.close();
        db.close();
        return existUser;
    }

}
