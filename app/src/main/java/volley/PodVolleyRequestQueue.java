package volley;

/**
 * Created by namh on 2016-08-20.
 */


import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Custom implementation of Volley Request Queue
 */
public class PodVolleyRequestQueue {

    private static PodVolleyRequestQueue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private PodVolleyRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static PodVolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PodVolleyRequestQueue.class) {
                if (mInstance == null) {
                    mInstance = new PodVolleyRequestQueue(context);
                }
            }
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
}