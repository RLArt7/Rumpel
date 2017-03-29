package com.example.harelavikasis.rumpel.Login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.harelavikasis.rumpel.ContactList.ContactListActivity;
import com.example.harelavikasis.rumpel.Managers.UserManger;
import com.example.harelavikasis.rumpel.Models.User;
import com.example.harelavikasis.rumpel.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.prefs.Prefs;

public class MainLoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult>{
    public static final String TAG = "RUMPEL";
    public static final String KEY_USER = "USER";
    public static final String FIRST_TIME = "FIRST_TIME";
    public static final String NOTIFICATION_STATUS = "NOTIFICATIONS_STATUS";

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private MainLoginActivity self = this;

//    public static Facebook facebook = null;
//    public static AsyncFacebookRunner mAsyncRunner = null;

    private List<String> friends_list = new ArrayList<String>();

    @Bind(R.id.button_facebook_login)
    LoginButton loginButton;

    @Bind(R.id.sign_in_text)
    TextView editText;

    private CallbackManager mCallbackManager;
//    LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        ButterKnife.bind(this);
        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    setAuthWithOAuth(user);
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (!Prefs.with(self).readBoolean(FIRST_TIME)) {
                        Prefs.with(self).writeBoolean(FIRST_TIME, true);
                    } else {
                        loginButton.setVisibility(View.GONE);
                        editText.setText("Welcome Back");
                        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(AccessToken.getCurrentAccessToken(),
                                "me/friends",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {

                                    public void onCompleted(GraphResponse response) {
                                        Intent intent = new Intent(self, ContactListActivity.class);
                                        try {
                                            if (response.getJSONObject() != null) {
                                                JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                                intent.putExtra("jsondata", rawName.toString());
                                                startActivity(intent);
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).executeAsync();
                    }
                    if (UserManger.getInstance().isSet()) {

                    }
                }
            }
        };

    }

    private void setAuthWithOAuth(FirebaseUser u) {
        if (AccessToken.getCurrentAccessToken() != null) {
            // build writer object from firebase user.

            UserManger.getInstance().setUserId(u.getUid());
            UserManger.getInstance().setUserName(u.getDisplayName());
            UserManger.getInstance().setFacebookId(AccessToken.getCurrentAccessToken().getUserId());

            // get writers database reference
            usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(AccessToken.getCurrentAccessToken().getUserId());
//            usersRef.setValue(new User("null"));
            // set the data locally and remotely and update the login state.
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        // remote user in firebase
                        User remoteUser = dataSnapshot.getValue(User.class);

                        // check for user changes if user exist remotely.
                        if (remoteUser != null) {
                            checkForUserChanges(remoteUser);
                        }
                        // update the local writer in the firebase
                        usersRef.setValue(new User("null"));

                        // save the user to shared preferences.
                        Prefs.with(self).write(KEY_USER, new Gson()
                                .toJson(new User("null")));

                        if (UserManger.getInstance().isSet()) {

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                }
            });
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(token,
                "me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {

                    public void onCompleted(GraphResponse response) {
                        Intent intent = new Intent(self, ContactListActivity.class);
                        try {
                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                            intent.putExtra("jsondata", rawName.toString());
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void checkForUserChanges(User remoteUser) {

        // check user name
        if (!remoteUser.getUserName().equals(UserManger.getInstance().getUserName())) {
            UserManger.getInstance().setUserName(remoteUser.getUserName());
        }
        UserManger.getInstance().setChatIdMap(remoteUser.getChatIdMap());

//        // check user image
//        if (!remoteUser.getImage().equals(localWriter.getImage())) {
//            localWriter.setImage(remoteUser.getImage());
//        }
    }

    @OnClick(R.id.button_facebook_login)
    public void onLoginClick() {
        if (isGooglePlayServicesAvailable(this)) {
            mCallbackManager = CallbackManager.Factory.create();
            loginButton.setReadPermissions("email", "public_profile", "user_friends");
            loginButton.registerCallback(mCallbackManager, this);
            loginButton.performClick();
            loginButton.setEnabled(false);
        }
    }

    private boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Log.d(TAG, "facebook:onSuccess:" + loginResult);

        Prefs.with(self).writeBoolean(NOTIFICATION_STATUS, true);
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "facebook:onCancel:");

    }

    @Override
    public void onError(FacebookException error) {
        Log.d(TAG, "facebook:onError:");

    }
}
