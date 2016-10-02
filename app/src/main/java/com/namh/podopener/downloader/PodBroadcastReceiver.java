package com.namh.podopener.downloader;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.namh.podopener.recyclerview.Pod;

/**
 * Created by namh on 2016-08-21.
 */
public class PodBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // check whether the download-id is mine
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
        Pod pod = DownloadIdStore.getInstance().get(id);
        if (pod == null){
            // not our download id, ignore
            return;
        }


        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        // make a query
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);

        // check the status
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            // when download completed

            int statusColumn = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusColumn)) {
                Log.w("FourthFragment", "Download Failed");
                return;
            }

            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            String downloadedPackageUriString = cursor.getString(uriIndex);
            pod.setStatus(DownloadManager.STATUS_SUCCESSFUL);

        }else{
            // when canceled
            pod.setStatus(DownloadManager.STATUS_FAILED);

        }

        return;


    }

}
