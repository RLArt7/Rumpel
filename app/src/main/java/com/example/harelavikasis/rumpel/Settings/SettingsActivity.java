package com.example.harelavikasis.rumpel.Settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.harelavikasis.rumpel.Login.MainLoginActivity;
import com.example.harelavikasis.rumpel.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.prefs.Prefs;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Settings");



    }

    @OnClick(R.id.sign_out_btn)
    public void onSignOutClick() {
        Prefs.with(this).writeBoolean(MainLoginActivity.FIRST_TIME, false);
        Prefs.with(this).remove(MainLoginActivity.KEY_USER);

        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, MainLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
