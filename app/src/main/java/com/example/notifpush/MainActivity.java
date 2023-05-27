package com.example.notifpush;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.card.MaterialCardView;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    MediaPlayer mPlayer;
    SwitchCompat switchCompat;
    GifImageView gifImageView;
    MaterialCardView materialCardView;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    //for database to store the state
    private boolean switchState;
    private static final String PREFS_NAME = "SwitchPrefs";
    private static final String SWITCH_STATE_KEY = "SwitchState";
    private static final String TAPTARGET_KEY = "tutorial_shown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mPlayer = MediaPlayer.create(this, R.raw.sound);
        switchCompat = findViewById(R.id.switchcompat);
        gifImageView = findViewById(R.id.gif);
        radioButton = (RadioButton) findViewById(R.id.twitter);
        materialCardView = findViewById(R.id.card);
        loadSwitchState();

        if (firstTapTarget()) {
            showTapTarget();
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchState = isChecked;
                updateSwitchImage();
                setSwitchState(switchState);
            }
        });

        materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotificationAccessSettings();
            }
        });

    }

    /***************************/

    @Override
    protected void onResume() {
        super.onResume();
        if (isNotificationServiceEnabled()) {
            radioButton.setChecked(true);
        } else {
            Toast.makeText(this, "Notification listener service is not enabled", Toast.LENGTH_SHORT).show();
            radioButton.setChecked(false);
        }
    }

    private void openNotificationAccessSettings() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }

    private boolean isNotificationServiceEnabled() {
        String packageName = getPackageName();
        String flat = Settings.Secure.getString(getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
        if (flat != null) {
            return flat.contains(packageName);
        }
        return false;
    }

    private void loadSwitchState() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        switchState = preferences.getBoolean(SWITCH_STATE_KEY, false);
        switchCompat.setChecked(switchState);
        if ((switchState)) {
            gifImageView.setImageResource(R.drawable.notifications);
        } else {
            gifImageView.setImageResource(R.drawable.nbimages);
        }
    }

    private void updateSwitchImage() {
        if ((switchState)) {
            gifImageView.setImageResource(R.drawable.notifications);
        } else {
            gifImageView.setImageResource(R.drawable.nbimages);
        }
    }

    private void saveSwitchState(boolean switchState) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SWITCH_STATE_KEY, switchState);
        editor.apply();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STATE, switchState ? 1 : 0);
        db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }

    public void setSwitchState(boolean state) {
        switchState = state;
        saveSwitchState(state);
    }

    public void showTapTarget() {
        new TapTargetSequence(this).targets(
                TapTarget.forView(materialCardView, "STEP 1", "Turn on the notification access of Notif Push")
                        .outerCircleColor(R.color.teal_200)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.black)
                        .textColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(60),

                TapTarget.forView(radioButton, "STEP 2", "The Twitter card appear in blue if the\n notification access is ON")
                        .outerCircleColor(R.color.teal_200)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.black)
                        .textColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(60),
                TapTarget.forView(switchCompat, "LAST STEP", "Turn ON the Listener if you want to active it.")
                        .outerCircleColor(R.color.teal_700)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.black)
                        .textColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(60)).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                Toast.makeText(getApplicationContext(), "Congrats", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(TAPTARGET_KEY, false);
                editor.apply();
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
            }
        }).start();
    }

    private boolean firstTapTarget() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(TAPTARGET_KEY, true);

    }

}

