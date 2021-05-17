package com.ghodel.snapsaver.utils;

import android.os.Environment;

import java.io.File;

public class Constants {

    public static final String FolderWhatsApp = "/WhatsApp/Media/.Statuses";
    public static final String SnapSaverPath = Environment.getExternalStorageDirectory() + File.separator + "SNAPSAVER" + File.separator;

    public static final String VIDEO_PARAMS_CONTROL_ACTION = "com.ghodel.snapsaver.utils";
    public static final String ACTION = "action";
    public static final int ACTION_VOICE_SILENCE = 0x101;
    public static final int ACTION_VOICE_NORMAL = 0x102;
    public static final String IS_SILENCE = "is_silence";
}
