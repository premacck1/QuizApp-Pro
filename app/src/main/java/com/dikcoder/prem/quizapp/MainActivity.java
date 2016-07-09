package com.dikcoder.prem.quizapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Field.OnFragmentInteractionListener, Difficulty.OnFragmentInteractionListener {

    private ListView listView;
    private String JSONString = null;
    private String newJSONToWrite;
    boolean doubleBackToExitPressedOnce = false;
    protected static ArrayList<QuestionBean> QUESTION = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            return;
        }

        Field mField = new Field();
        mField.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment_container, mField).commit();

        if(isConnected()){
            // call AsyncTask to perform network operation on separate thread
            new HttpAsyncTask().execute("https://json-956.appspot.com/json.txt");
        }
        try{
            String string = readFromFile();
            if(string != null)
                JSONString = string;
            else{
                AssetManager assetManager = getResources().getAssets();
                InputStream inputStream;
                try{
                    inputStream = assetManager.open("json.txt");
                    JSONString = getStringFromInputStream(inputStream);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e){
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream;
            try{
                inputStream = assetManager.open("json.txt");
                JSONString = getStringFromInputStream(inputStream);
            }
            catch (IOException io){
                io.printStackTrace();
            }
        }
    }

//    Write to JSON file in Internal Memory
    public void writeToFile(String data){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(
                            new File(
                                    getFilesDir() + File.separator + "json.txt"
                            )
                    )
            );
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    Read from JSON file in Internal Memory
    public String readFromFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(
                        new File(
                                getFilesDir() + File.separator + "json.txt"
                        )
                )
        );
        String read;
        StringBuilder builder = new StringBuilder("");

        while((read = bufferedReader.readLine()) != null){
            builder.append(read);
        }
        bufferedReader.close();
        return builder.toString();
    }

//    Doing parsing of JSON data
    public ArrayList<QuestionBean> doInBackground(String JSONString,String field, String difficulty){
        ArrayList<QuestionBean> fieldList = null;
        JSONObject jObject;
        /** Getting the parsed data as a List construct */
        try{
            jObject = new JSONObject(JSONString);
            fieldList = new QuestionJSONParser().parse(jObject, field, difficulty);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fieldList;
    }

    public static String getStringFromInputStream(InputStream inputStream){
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bufferedReader !=null){
                try{
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public static String GET(String URLString){
        InputStream inputStream;
        String result = null;
        try{
            URL url = new URL(URLString);
//           create HttpClient
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            receive response as inputStream
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
//            convert inputStream to string
            result = getStringFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isConnected() {
        ConnectivityManager conman = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conman.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return GET(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            newJSONToWrite = s;
            if (newJSONToWrite != null){
                writeToFile(newJSONToWrite);
                JSONString = newJSONToWrite;
            }
        }
    }

    public static String[] getArgs(int field, int difficulty){
        String [] args = new String[2];
        switch (field){
            case 0:
                args[0] = "iOS";
                break;
            case 1:
                args[0] = "Java";
                break;
            case 2:
                args[0] = "HTML";
                break;
            case 3:
                args[0] = "JavaScript";
                break;
            default:
                break;
        }
        switch (difficulty){
            case 0:
                args[1] = "Rookie";
                break;
            case 1:
                args[1] = "Apprentice";
                break;
            case 2:
                args[1] = "Pro";
                break;
            case 3:
                args[1] = "Hitman";
                break;
            default:
                break;
        }
        return args;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_bookmark:
                startActivity(new Intent(this, Bookmarks.class));
                break;
            case R.id.action_about:
                Dialog d = new Dialog(this);
                d.setContentView(R.layout.about);
                d.setTitle("About us");
                d.show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFragmentInteraction(View v, int pos) {
        Difficulty mdifficulty = new Difficulty();
        Bundle args = new Bundle();
        args.putInt(Difficulty.ARG_POSITION, pos);
        mdifficulty.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_fragment_container, mdifficulty);
        transaction.addToBackStack(null);
        transaction.commit();
/*
        listView = (ListView) findViewById(android.R.id.list);
        listView.setLayoutAnimation(
                new LayoutAnimationController(
                        AnimationUtils.loadAnimation(this, R.anim.front_exit),
                        0.2F
                )
        );
*/
/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                setActionBarTitle(position);
            }
        }, 200);
*/
    }

    @Override
    public void onFragmentInteraction(int selection, int pos) {
        final String[] questionArgs = getArgs(selection, pos);
        QUESTION = doInBackground(JSONString, questionArgs[0], questionArgs[1]);
        Intent i = new Intent(MainActivity.this, Questions.class);
        i.putExtra(Questions.FIELD_ARG, questionArgs[0]);
        i.putExtra(Questions.DIFFICULTY_ARG, questionArgs[1]);
        i.putExtra("Question", QUESTION);
        startActivity(i);
        MainActivity.this.finish();

/*
        listView = (ListView) findViewById(R.id.listView1);
        listView.setLayoutAnimation(
                new LayoutAnimationController(
                        AnimationUtils.loadAnimation(this, R.anim.front_exit),
                        0.2F
                )
        );
*/
/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setVisibility(View.INVISIBLE);
            }
        }, 200);
*/
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backStackCount == 1){
            getSupportFragmentManager().popBackStack();
        }
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}