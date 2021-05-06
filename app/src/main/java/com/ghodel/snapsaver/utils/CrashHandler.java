package com.ghodel.snapsaver.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Handler;
import android.widget.Toast;
import android.net.Uri;

public final class CrashHandler {

    public static final UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();

    public static void init(Application app) {
        init(app, null);
    }

    public static void init(final Application app, final String crashDir) {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                try {
                    tryUncaughtException(thread, throwable);
                } catch (Throwable e) {
                    e.printStackTrace();
                    if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null)
                        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(thread, throwable);
                }
            }

            private void tryUncaughtException(Thread thread, Throwable throwable) {
                final String time = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(new Date());
                File crashFile = new File(TextUtils.isEmpty(crashDir) ? new File(app.getExternalFilesDir(null), "crash")
                        : new File(crashDir), "crash_" + time + ".txt");

                String versionName = "unknown";
                long versionCode = 0;
                try {
                    PackageInfo packageInfo = app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
                    versionName = packageInfo.versionName;
                    versionCode = Build.VERSION.SDK_INT >= 28 ? packageInfo.getLongVersionCode()
                            : packageInfo.versionCode;
                } catch (PackageManager.NameNotFoundException ignored) {}

                String fullStackTrace; {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    throwable.printStackTrace(pw);
                    fullStackTrace = sw.toString();
                    pw.close();
                }

                StringBuilder sb = new StringBuilder();
                sb.append("*** Device Information ***").append("\n\n");
                sb.append("Time Of Crash             : ").append(time).append("\n");
                sb.append("Device Manufacturer: ").append(Build.MANUFACTURER).append("\n");
                sb.append("Device Model              : ").append(Build.MODEL).append("\n");
                sb.append("Android Version          : ").append(Build.VERSION.RELEASE).append("\n");
                sb.append("Android SDK                 : ").append(Build.VERSION.SDK_INT).append("\n");
                sb.append("App VersionName     : ").append(versionName).append("\n");
                sb.append("App VersionCode       : ").append(versionCode).append("\n\n");
                sb.append("*** Crash Head ***").append("\n");
                sb.append("\n").append(fullStackTrace);
                sb.append("\n\n").append("*** End of current Report ***");

                String errorLog = sb.toString();

                try {
                    writeFile(crashFile, errorLog);
                } catch (IOException ignored) {

                }

                gotoCrashActiviy: {
                    Intent intent = new Intent(app, CrashActivity.class);
                    intent.addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );
                    intent.putExtra(CrashActivity.EXTRA_CRASH_INFO, errorLog);
                    try {
                        app.startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null)
                            DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(thread, throwable);
                    }
                }

            }

            private void writeFile(File file, String content) throws IOException {
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.exists()) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(content.getBytes());
                try {
                    fos.close();
                } catch (IOException e) {

                }
            }

        });


    }

    public static final class CrashActivity extends Activity implements MenuItem.OnMenuItemClickListener {

        private static final String EXTRA_CRASH_INFO = "crashInfo";
        private Boolean pressAgainToExit = false;
        private String mLog;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTheme(android.R.style.Theme_DeviceDefault);

            mLog = getIntent().getStringExtra(EXTRA_CRASH_INFO);
            setContentView: {
                ScrollView contentView = new ScrollView(this);
                contentView.setFillViewport(true);
                contentView.setVerticalScrollBarEnabled(false);
                TextView message = new TextView(this); {
                    int padding = dp2px(16);
                    message.setPadding(padding, padding, padding, padding);
                    message.setText(mLog);
                    message.setTextIsSelectable(true);
                }

                contentView.addView(message, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                setContentView(contentView);
            }
        }

        @Override
        public void onBackPressed() {
            if(pressAgainToExit){
                super.onBackPressed();
            }
            this.pressAgainToExit = true;
            showToast("Press back again to exit");
            new Handler().postDelayed(new Runnable(){

                @Override
                public void run() {
                    pressAgainToExit = false;
                }
            }, 2000);
        }

        private void showToast(String msg){
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        private void restart() {
            PackageManager pm = getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(getPackageName());
            if (intent != null) {
                intent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                );
                startActivity(intent);
            }
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }

        private int dp2px(final float dpValue) {
            final float scale = Resources.getSystem().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        private void sendEmail(String email, String msg){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL  , new String[] {email});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Error report");
            intent.putExtra(Intent.EXTRA_TEXT, msg);

            startActivity(Intent.createChooser(intent, "Send email"));
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case 1:
                    //send log to email
                    sendEmail("muhammadmayudin12@gmail.com", mLog);
                    break;

                case 2:
                    //Restart
                    restart();
                    break;
            }
            return true;
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            menu.add(0, 1, 0, "Report Log ").setOnMenuItemClickListener(this)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add(0, 2, 0, "Restart").setOnMenuItemClickListener(this)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            return true;
        }

    }

}