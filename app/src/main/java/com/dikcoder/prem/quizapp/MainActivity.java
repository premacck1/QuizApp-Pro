package com.dikcoder.prem.quizapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kobakei.ratethisapp.RateThisApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Field.OnFragmentInteractionListener,
        Difficulty.OnFragmentInteractionListener {

    private String JSONString = null;
    boolean doubleBackToExitPressedOnce = false;
    protected static ArrayList<QuestionBean> QUESTION = null;
    public static Typeface fontTypefaceSemiLight, fontTypefaceLight;
    private String version;
    private DatabaseHolder dbHandler;

    @Override
    public boolean releaseInstance() {
        return super.releaseInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        // If the criteria is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
//        Stop showing 'rate this' dialog
        RateThisApp.stopRateDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            return;
        }
        fontTypefaceSemiLight = Typeface.createFromAsset(getAssets(), "fonts/seguisl.ttf");
        fontTypefaceLight = Typeface.createFromAsset(getAssets(), "fonts/seguil.ttf");

//        initializing db handler
        dbHandler = new DatabaseHolder(this);
        dbHandler.open();
        Cursor versionCursor = dbHandler.getQuestionVersion();
        versionCursor.moveToFirst();
        if (!versionCursor.isAfterLast()) {
            version = versionCursor.getString(versionCursor.getColumnIndex("version"));
        }
        dbHandler.close();

        Field mField = new Field();
        mField.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment_container, mField).commit();

//        APP-RATING DIALOG CODE
//        Custom criteria: 3 days and 5 launches
        RateThisApp.Config config = new RateThisApp.Config(3, 5);
//        Custom title
        config.setTitle(R.string.rate_us);
        RateThisApp.init(config);

//        FIRST GET THE VERSION
        if(isConnected()){
            new HttpAsyncTask().execute("http://json-956.appspot.com/version.txt");
        }

        try{
            String string = readFromFile();
            if(string != null)
                JSONString = string;
            else{
                if(isConnected()){
//                 call AsyncTask to perform network operation on separate thread
//                new HttpAsyncTask().execute("http://json-956.appspot.com/version.txt");
                    new HttpAsyncTask().execute("https://json-956.appspot.com/json-1.txt");
                }
                else {
                    readFromFileAnyways();
                }
            }
        }
        catch (IOException e){
            readFromFileAnyways();
        }
    }

    public void readFromFileAnyways(){
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onResumeFragments() {
        if (Difficulty.BACK_FROM_RESULTS == 2){
            getSupportFragmentManager().popBackStack();
        }
        super.onResumeFragments();
    }
    //    Write to JSON file in Internal Memory
    public void writeToFile(String data){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(
                            new File(
                                    String.valueOf(getAssets().open("json.txt"))
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
                new InputStreamReader(
                        getAssets().open("json.txt")
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
        if (JSONString == null){
            return null;
        }
        else {
            ArrayList<QuestionBean> fieldList;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(JSONString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /** Getting the parsed data as a List construct */
            fieldList = new QuestionJSONParser().parse(jsonObject, field, difficulty);
            return fieldList;
        }
    }

    public static String getStringFromInputStream(InputStream inputStream){
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
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

    public static String getVersionFromInputStream(InputStream inputStream){
        BufferedReader bufferedReader = null;
        String versionStr = "";
        String line;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            while((line = bufferedReader.readLine()) != null){
                versionStr = line;
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
        return versionStr;
    }

    public String GET(String URLString){
        InputStream inputStream = null;
        String result = null;
        try{
            URL url = new URL(URLString);
//           create HttpClient
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            receive response as inputStream
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            }catch (Exception uhe){
                uhe.printStackTrace();
            }
            if (inputStream !=null) {
                if (URLString.contains("version")) {
                    result = getVersionFromInputStream(inputStream);
                    if (result.codePointAt(0) == 0xfeff) {
                        result = result.substring(1, result.length());
                    }
                } else if (URLString.contains("json")){
                    result = getStringFromInputStream(inputStream);
                }
                else{
                    result = readFromFile();
                }
            }
            else{
                result = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isConnected() {
        ConnectivityManager conman = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conman.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            if(!(version.equals(GET(params[0]).trim()))) {
                // DB Update with new Version
                dbHandler.open();
                dbHandler.updateVersion(GET(params[0].trim()),"1");
                dbHandler.close();
                return GET("https://json-956.appspot.com/json.txt");
            }
            else {
                try {
                    result = readFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null){
                writeToFile(s);
                JSONString = s;
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
            case R.id.action_account:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.action_donate:
                startActivity(new Intent(this, Results.class));
                break;
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
                break;
            case R.id.action_help:
                Dialog d1 = new Dialog(this);
                d1.setContentView(R.layout.help);
                d1.setTitle("Help");
                d1.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(View v, int pos) {
        Difficulty.BACK_FROM_RESULTS = 0;
        Difficulty mdifficulty = new Difficulty();
        Bundle args = new Bundle();
        args.putInt(Difficulty.ARG_POSITION, pos);
        mdifficulty.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_fragment_container, mdifficulty);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(int selection, int pos) {
        if (Difficulty.BACK_FROM_RESULTS == 1 || Difficulty.BACK_FROM_RESULTS == 2){
            getSupportFragmentManager().popBackStack();
        }
        else {
            final String[] questionArgs = getArgs(selection, pos);
            QUESTION = doInBackground(JSONString, questionArgs[0], questionArgs[1]);
            if(QUESTION == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Sorry, but the questions couldn't be loaded.");
                builder.setCancelable(false);
                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else {
                Intent i = new Intent(MainActivity.this, Questions.class);
                i.putExtra(Questions.FIELD_ARG, questionArgs[0]);
                i.putExtra(Questions.DIFFICULTY_ARG, questionArgs[1]);
                i.putExtra("Question", QUESTION);
                startActivity(i);
            }
        }
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
            Toast.makeText(this, "Hit back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}