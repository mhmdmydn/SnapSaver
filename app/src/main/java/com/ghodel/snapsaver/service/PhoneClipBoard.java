package com.ghodel.snapsaver.service;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ghodel.snapsaver.activity.DirectMessageActivity;

import java.util.regex.Pattern;

public class PhoneClipBoard extends Service {
    private ClipboardManager clipboardManager;
    private static final String TAG = "ClipboardManager";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        assert clipboardManager != null;
        clipboardManager.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    final private ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener =
            new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    Log.d(TAG, "onPrimaryClipChanged");
                    ClipData clip = clipboardManager.getPrimaryClip();
                    String paste = clip.getItemAt(0).getText().toString();

                    if (isValidMobile(paste)) {
                        Intent launchIntent = new Intent(getApplicationContext(), DirectMessageActivity.class);
                        if (launchIntent != null) {
                            try {
                                startActivity(launchIntent);
                            } catch (ActivityNotFoundException ex) {
                                ex.printStackTrace();
                            }

                        }
                    }
                }
            };

}
