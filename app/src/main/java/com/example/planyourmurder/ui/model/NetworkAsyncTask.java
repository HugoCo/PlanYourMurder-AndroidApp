package com.example.planyourmurder.ui.model;

import android.util.Log;

import java.lang.ref.WeakReference;

public class NetworkAsyncTask extends android.os.AsyncTask<String, Void, String> {
    public interface Listeners {
        void onPreExecute();
        void doInBackground();
        void onPostExecute(String success);
    }

    private final WeakReference<Listeners> callback;

    public NetworkAsyncTask(Listeners callback){
        this.callback = new WeakReference<>(callback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.callback.get().onPreExecute();
        Log.e("TAG", "AsyncTask is started");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.callback.get().onPostExecute(s);
        Log.e("TAG", "AsyncTask is finished");
    }

    @Override
    protected String doInBackground(String... url) {
        this.callback.get().doInBackground();
        Log.e("TAG", "Async task is requesting http (big work)");
        return MyHttpURLConnection.startHttpRequest(url[0]);
    }
}
