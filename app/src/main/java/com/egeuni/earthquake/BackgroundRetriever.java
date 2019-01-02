package com.egeuni.earthquake;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BackgroundRetriever {
    private byte[] dataGIF;
    private static BackgroundRetriever instance = null;

    private BackgroundRetriever() {
        new RetrieveByteArray().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public class RetrieveByteArray extends AsyncTask<Void, Void, byte[]> {

        @Override
        protected byte[] doInBackground(Void... strings) {
            byte[] d = null;
            try {
                Log.d("FindMe" , "Run!");
                URL url = new URL("https://media.giphy.com/media/loIcistWoilEc/giphy.gif");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode() == 200) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    byte[] data = new byte[10240];
                    while ((nRead = in.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    d = buffer.toByteArray();
                    Log.d("FindMe", "End...");
                    return buffer.toByteArray();
                }


            }catch (Exception e) {
                e.printStackTrace();
            }
            return d;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            dataGIF = bytes;
        }
    }

    public byte[] getDataGIF() {
        return dataGIF;
    }

    public static BackgroundRetriever getInstance() {
        if(instance == null)
            instance = new BackgroundRetriever();
        return instance;
    }
}
