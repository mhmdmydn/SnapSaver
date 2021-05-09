package com.ghodel.snapsaver.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ghodel.snapsaver.utils.Constants;
import com.ghodel.snapsaver.utils.MainUtil;

import java.io.File;
import java.util.ArrayList;

public class SnapSaverData extends AsyncTask<Void, Void, ArrayList<File>> {

    private ArrayList<File> list;
    private SnapSaverTaskListener listener;
    private SwipeRefreshLayout srl;
    private Context context;
    private boolean isPhoto;

    public SnapSaverData(Context context, SwipeRefreshLayout srl, boolean isPhoto, SnapSaverTaskListener listener){
        this.context = context;
        this.isPhoto = isPhoto;
        this.listener = listener;
        this.srl = srl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        srl.setRefreshing(true);
    }

    @Override
    protected ArrayList<File> doInBackground(Void... voids) {
        File dirPath = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + Constants.FolderWhatsApp);
        list = new ArrayList<>();
        list.clear();
        if (isPhoto){
            list = MainUtil.getListImage(dirPath);
        } else {
            list = MainUtil.getListVideo(dirPath);
        }


        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<File> snapSaverModels) {
        super.onPostExecute(snapSaverModels);
        srl.setRefreshing(false);

        if (snapSaverModels != null){
            listener.onResult(snapSaverModels);
        } else {
            listener.onError("Tidak ada Photo atau  Video yang tersimpan");
        }

    }

    public interface SnapSaverTaskListener {
        void onResult(ArrayList<File> list);
        void onError(String error);
    }
}
