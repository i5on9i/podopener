package com.namh.podopener.downloader;


import com.namh.podopener.recyclerview.Pod;

import java.util.HashMap;


/**
 * Created by namh on 2016-08-21.
 */
public class DownloadIdStore {

    private static DownloadIdStore _instance = null;
    private HashMap<Long, Pod> _datastore = null;

    private DownloadIdStore() {
        _datastore = new HashMap<>();

    }

    public static DownloadIdStore getInstance(){
        if(_instance == null){
            synchronized (DownloadIdStore.class){
                if(_instance == null){
                    _instance = new DownloadIdStore();
                }
            }
        }
        return _instance;
    }

    public boolean exist(Long downloadId){
        Pod ret = _datastore.get(downloadId);
        if(ret == null){
            return false;
        }
        return true;
    }

    public Pod get(Long downloadId){
        return _datastore.get(downloadId);
    }

    public void put(Long downloadId, Pod pod){
        _datastore.put(downloadId, pod);
    }

    public void remove(Long downloadId){
        _datastore.remove(downloadId);
    }

}