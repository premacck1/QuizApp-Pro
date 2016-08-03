package com.prembros.programming.quizapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

public class LoginActivity extends AppCompatActivity implements OnConnectionFailedListener, View.OnClickListener, ConnectionCallbacks {

    public static Bitmap dp = null;
    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private boolean is_signOutBtn_clicked = false;
    private int request_code;
    ProgressDialog progress_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isConnected()) {
//        SET UP ACTION BAR
            android.support.v7.app.ActionBar ab = this.getSupportActionBar();
            assert ab != null;
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);

            if (checkPlayServices()) {
                buildNewGoogleApiClient();
                setContentView(R.layout.activity_login);

//        Customize sign-in button.a red button may be displayed when Google+ scopes are requested
                customizeSignBtn();
                setBtnClickListeners();
                progress_dialog = new ProgressDialog(this);
                progress_dialog.setCancelable(false);
                progress_dialog.setMessage("Signing in....");
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Sorry, but we couldn't find Google Play Services on your devices, make sure you have the latest one from play store and try again.");
                builder.setCancelable(false);
                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                    startActivity(new Intent(Questions.this, Results.class));
                        LoginActivity.this.finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("No Internet?");
            builder.setMessage("Sorry, but we couldn't find any internet connections.\nFirst connect to a network then come here again.");
            builder.setCancelable(false);
            builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    startActivity(new Intent(Questions.this, Results.class));
                    LoginActivity.this.finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager conman = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conman.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private boolean checkPlayServices() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }


    @SuppressWarnings("ConstantConditions")
    public void prepareUI() {
        if (google_api_client != null && google_api_client.isConnected()) {
            // signed in. Show the "sign out" button and explanation.
            changeUI(true);
        } else {
            // not signed in. Show the "sign in" button and explanation.
            changeUI(false);
        }

        if (dp != null) {
            ((ImageView) findViewById(R.id.profile_pic)).setImageBitmap(dp);
//                changeUI(true);
        }
    }

    /*
     Show and hide of the Views according to the user login status
     */
    @SuppressWarnings("ConstantConditions")
    private void changeUI(boolean signedIn) {
        if (signedIn) {
//            findViewById(R.id.view).setVisibility(View.GONE);
//            findViewById(R.id.view2).setVisibility(View.GONE);
            findViewById(R.id.username).setVisibility(View.VISIBLE);
            findViewById(R.id.emailId).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            findViewById(R.id.leaderboard_rippleView).setVisibility(View.VISIBLE);
            findViewById(R.id.achievements_rippleView).setVisibility(View.VISIBLE);
            findViewById(R.id.login_form).setVisibility(View.VISIBLE);
        }
        else {
            dp = Bitmap.createBitmap(new int[]{Color.argb(0, 255, 255, 255)}, 1, 1, Bitmap.Config.ALPHA_8);
            ((ImageView) findViewById(R.id.profile_pic)).setImageBitmap(dp);
//            findViewById(R.id.view).setVisibility(View.VISIBLE);
//            findViewById(R.id.view2).setVisibility(View.VISIBLE);
            findViewById(R.id.username).setVisibility(View.GONE);
            findViewById(R.id.emailId).setVisibility(View.GONE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            findViewById(R.id.leaderboard_rippleView).setVisibility(View.GONE);
            findViewById(R.id.achievements_rippleView).setVisibility(View.GONE);
            findViewById(R.id.login_form).setVisibility(View.VISIBLE);
        }
    }

    /*
    create and  initialize GoogleApiClient object to use Google Plus Api.
    While initializing the GoogleApiClient object, request the Plus.SCOPE_PLUS_LOGIN scope.
    */

    private void buildNewGoogleApiClient(){

        google_api_client =  new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    /*
      Customize sign-in button. The sign-in button can be displayed in
      multiple sizes and color schemes. It can also be contextually
      rendered based on the requested scopes. For example. a red button may
      be displayed when Google+ scopes are requested, but a white button
      may be displayed when only basic profile is requested. Try adding the
      Plus.SCOPE_PLUS_LOGIN scope to see the  difference.
    */

    @SuppressWarnings("ConstantConditions")
    private void customizeSignBtn(){
//        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
//        findViewById(R.id.disconnect_button).setVisibility(View.GONE);
        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn_btn.setSize(SignInButton.SIZE_STANDARD);
        signIn_btn.setScopes(new Scope[]{Plus.SCOPE_PLUS_LOGIN});
    }

    /*
      Set on click Listeners on the sign-in sign-out and disconnect buttons
     */

    @SuppressWarnings("ConstantConditions")
    private void setBtnClickListeners(){
        // Button listeners
        signIn_btn.setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.show_leaderboard).setOnClickListener(this);
        findViewById(R.id.show_achievements).setOnClickListener(this);
    }

    protected void onStart() {
        super.onStart();
        if (isConnected()) {
            google_api_client.connect();
        }
    }

    protected void onStop() {
        super.onStop();
        if (isConnected()) {
            google_api_client.disconnect();
        }
    }

    protected void onResume(){
        super.onResume();
        if (isConnected()) {
            google_api_client.connect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (Leaderboard_Achievements.isFragmentActive) {
                    Leaderboard_Achievements.rootView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fragment_anim_out));
                    getAndRemoveActiveFragment();
                }
                else this.finish();
                break;
            case R.id.action_settings:
                return true;
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
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(this, result.getErrorCode(),request_code).show();
            return;
        }
        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }
        changeUI(false);
    }

    /*
      Will receive the activity result and check which request we are responding to

     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        // Check which request we're responding to
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (responseCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                progress_dialog.dismiss();

            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        // Get user's information and set it into the layout
        if (!is_signOutBtn_clicked) {
            getProfileInfo();
            prepareUI();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();
        changeUI(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show();
                gPlusSignIn();
                break;
            case R.id.sign_out_button:
                Toast.makeText(this, "Signed Out from G+", Toast.LENGTH_LONG).show();
                gPlusSignOut();
                break;
            case R.id.show_leaderboard:
                if (Leaderboard_Achievements.isFragmentActive) {
                    getAndRemoveActiveFragment();
                }
                //noinspection ConstantConditions
                getSupportActionBar().hide();
                getSupportFragmentManager().beginTransaction().add(R.id.la_container,
                        Leaderboard_Achievements.newInstance("leaderboard"), "leaderboard").commit();
                break;
            case R.id.show_achievements:
                if (Leaderboard_Achievements.isFragmentActive) {
                    getAndRemoveActiveFragment();
                }
                //noinspection ConstantConditions
                getSupportActionBar().hide();
                getSupportFragmentManager().beginTransaction().add(R.id.la_container,
                        Leaderboard_Achievements.newInstance("achievements"), "achievements").commit();
                break;
        }
    }

    public void getAndRemoveActiveFragment(){
        Leaderboard_Achievements.isFragmentActive = false;
        if (getSupportFragmentManager().findFragmentByTag("leaderboard") != null
                && getSupportFragmentManager().findFragmentByTag("achievements") == null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("leaderboard")).commit();
        }
        else getSupportFragmentManager().beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("achievements")).commit();
    }

    /*
      Sign-in into the Google + account
     */

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected","connected");
            is_signInBtn_clicked = true;
            progress_dialog.show();
            onConnectionFailed(connection_result);
            is_signOutBtn_clicked = false;
        }
        else
            Toast.makeText(LoginActivity.this, "Make sure the device is connected to internet", Toast.LENGTH_SHORT).show();
    }

    /*
      Method to resolve any signin errors
     */

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    /*
      Sign-out from Google+ account
     */

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            //noinspection deprecation
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
//            google_api_client.clearDefaultAccountAndReconnect();
//            google_api_client.connect();
            is_signOutBtn_clicked = true;
            dp = null;
            prepareUI();
        }
        else
            Toast.makeText(LoginActivity.this, "Make sure the device is connected to internet", Toast.LENGTH_SHORT).show();
    }

    /*
     Revoking access from Google+ account
     */
//    private void gPlusRevokeAccess() {
//        if (google_api_client.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(google_api_client);
//            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
//                    .setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(@NonNull Status arg0) {
//                            Log.d("MainActivity", "User access revoked!");
//                            buildNewGoogleApiClient();
//                            google_api_client.connect();
//                            changeUI(false);
//                        }
//
//                    });
//            dp = null;
//        }
//        else
//            Toast.makeText(LoginActivity.this, "Make sure the device is connected to internet", Toast.LENGTH_SHORT).show();
//    }

    /*
     get user's information name, email, profile pic,Date of birth,tag line and about me
     */


    @SuppressWarnings("deprecation")
    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                // Update the UI after signin
                changeUI(true);
                setPersonalInfo(currentPerson);
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     set the User information into the views defined in the layout
     */

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("ConstantConditions")
    private void setPersonalInfo(Person currentPerson){

        String personName = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        @SuppressWarnings("deprecation")
        String email = Plus.AccountApi.getAccountName(google_api_client);
        TextView   user_name = (TextView) findViewById(R.id.username);
        user_name.setText(personName);
        TextView gmail_id = (TextView)findViewById(R.id.emailId);
        gmail_id.setText("(" + email + ")");
        setProfilePic(personPhotoUrl);
        progress_dialog.dismiss();
        Toast.makeText(this, "Person information is shown!", Toast.LENGTH_LONG).show();
    }

    /*
     By default the profile pic url gives 50x50 px image.
     If you need a bigger image we have to change the query parameter value from 50 to the size you want
    */

    public void setProfilePic(String profile_pic){
        profile_pic = profile_pic.substring(0,
                profile_pic.length() - 2)
                + PROFILE_PIC_SIZE;
        ImageView user_picture = (ImageView)findViewById(R.id.profile_pic);
        new LoadProfilePic(user_picture).execute(profile_pic);
    }

   /*
    Perform background operation asynchronously, to load user profile picture with new dimensions from the modified url
    */

    private class LoadProfilePic extends AsyncTask<String, Void, Bitmap> {
        public ImageView bitmap_img;

        public LoadProfilePic(ImageView bitmap_img) {
            this.bitmap_img = bitmap_img;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap new_icon = null;
            try {
                InputStream in_stream = new java.net.URL(url).openStream();
                new_icon = BitmapFactory.decodeStream(in_stream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return new_icon;
        }

        protected void onPostExecute(Bitmap result_img) {

            bitmap_img.setImageBitmap(result_img);
            dp = result_img;
        }
    }

    @Override
    public void onBackPressed() {
        if (Leaderboard_Achievements.isFragmentActive) {
            Leaderboard_Achievements.rootView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fragment_anim_out));
            //noinspection ConstantConditions
            getSupportActionBar().show();
            getAndRemoveActiveFragment();
        }
        else super.onBackPressed();
    }
}