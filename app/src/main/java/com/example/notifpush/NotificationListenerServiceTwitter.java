package com.example.notifpush;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

public class NotificationListenerServiceTwitter extends NotificationListenerService {
    private static final String TAG = "WhatsappAccessibilityService";
    private static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
    private static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    private static final String MESSENGER_PACKAGE_NAME = "com.facebook.orca";
    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";

    private MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = MediaPlayer.create(this, R.raw.bell);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        boolean switchState = getSwitchState();
        if(switchState){
         /*   if (packageName.equals(TWITTER_PACKAGE_NAME) ||
                    packageName.equals(FACEBOOK_PACKAGE_NAME) ||
                    packageName.equals(MESSENGER_PACKAGE_NAME) ||
                    packageName.equals(WHATSAPP_PACKAGE_NAME)) {
                mPlayer.start();
            }*/
            if (packageName.equals(TWITTER_PACKAGE_NAME) ) {
                mPlayer.start();
            }
        }else {
            Toast.makeText(getApplicationContext(), "The listener is OFF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    private boolean getSwitchState() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);

        boolean state = false;

        if (cursor.moveToLast()) {
            int switchStateColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_STATE);
            int switchState = cursor.getInt(switchStateColumnIndex);
            state = switchState == 1;
        }

        cursor.close();
        db.close();

        return state;
    }

}
