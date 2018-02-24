package dittlau.ControlCenter;

import android.content.Context;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class MyVolley {

    private static MyVolley mAppSingletonInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    private MyVolley(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MyVolley getInstance(Context context) {
        if (mAppSingletonInstance == null) {
            mAppSingletonInstance = new MyVolley(context);
        }
        return mAppSingletonInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}