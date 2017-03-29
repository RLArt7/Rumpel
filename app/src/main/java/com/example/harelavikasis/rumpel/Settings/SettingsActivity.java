package com.example.harelavikasis.rumpel.Settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.harelavikasis.rumpel.Login.MainLoginActivity;
import com.example.harelavikasis.rumpel.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.prefs.Prefs;
import io.ghyeok.stickyswitch.widget.StickySwitch;



public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.sticky_switch)
    StickySwitch stickySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        Boolean notificationStatus = Prefs.with(this).readBoolean(MainLoginActivity.NOTIFICATION_STATUS);
        stickySwitch.setDirection(notificationStatus ? StickySwitch.Direction.LEFT : StickySwitch.Direction.RIGHT);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Settings");
        setSwitch();
    }

    private void setSwitch() {
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {

                registerNotifications(direction);
                Log.d(MainLoginActivity.TAG, "Now Selected : " + direction.name() + ", Current Text : " + text);
            }
        });
    }

    private void registerNotifications(StickySwitch.Direction direction) {
        if (direction == StickySwitch.Direction.LEFT){
            FirebaseInstanceId.getInstance().getToken();
            Prefs.with(this).writeBoolean(MainLoginActivity.NOTIFICATION_STATUS, true);
        }else{

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                        Prefs.with(SettingsActivity.this).writeBoolean(MainLoginActivity.NOTIFICATION_STATUS, false);
                    } catch (Exception e) {
                        Log.d(MainLoginActivity.TAG, "disconnect: ");
                    }
                }
            });
            thread.start();
        }
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
