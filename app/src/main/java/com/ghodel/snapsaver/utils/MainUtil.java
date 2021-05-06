package com.ghodel.snapsaver.utils;

import android.content.Context;
import android.widget.Toast;

public class MainUtil {

    public static void showMessage(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
