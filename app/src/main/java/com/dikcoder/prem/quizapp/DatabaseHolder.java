package com.dikcoder.prem.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Prem $ on 7/4/2016.
 */
public class DatabaseHolder {
    public static final String field = "field";
    public static final String difficulty = "difficulty";
    public static final String question = "question";
    public static final String option1 = "option1";
    public static final String option2 = "option2";
    public static final String option3 = "option3";
    public static final String option4 = "option4";
    public static final String answer = "answer";
    public static final String tableName = "QuizTable";
    public static final String databaseName = "quizDatabase";
    public static final int database_version = 1;
    public static final String Table_Create = "create table QuizTable (field text not null, difficulty text not null,question text not null, option1 text not null, option2 text not null, option3 text not null, option4 text not null, answer text not null);";

    DatabaseHelper dbHelper;
    Context context;
    SQLiteDatabase db;
    public DatabaseHolder(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public DatabaseHolder open() {
        db  = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }
    public long insertData(String field_l, String difficulty_l,
                           String question_l,
                           String option1_l, String option2_l, String option3_l, String option4_l,
                           String answer_l) {
        ContentValues content = new ContentValues();
        content.put(field, field_l);
        content.put(difficulty, difficulty_l);
        content.put(question, question_l);
        content.put(option1, option1_l);
        content.put(option2, option2_l);
        content.put(option3, option3_l);
        content.put(option4, option4_l);
        content.put(answer, answer_l);
        return db.insertOrThrow(tableName, null, content);
    }

    public long deleteData(String question_l) {
        return db.delete(tableName, "question='"+question_l+"'", null);
    }
    public Cursor returnData() {
        return db.query(tableName, new String[] {field,difficulty,question,option1,option2,option3,option4,answer}, null, null, null, null, null);
    }
    public Cursor returnQuestion(){
        return db.query(tableName, new String[]{question}, null, null, null, null, null);
    }
    public Cursor returnBookmarkedQuestion(String field){
        return db.query(true, tableName, new String[]{DatabaseHolder.field, question, answer}, DatabaseHolder.field + " = '"+ field +"'", null, null, null, null, null);
    }

    public void dropTable(){
        db.execSQL("DROP TABLE IF EXISTS QuizTable");
        try{
            db.execSQL(Table_Create);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, field, null, database_version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            try{
                db.execSQL(Table_Create);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS QuizTable");
            onCreate(db);
        }

    }
}
