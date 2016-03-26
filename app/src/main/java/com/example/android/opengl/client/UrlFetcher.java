package com.example.android.opengl.client;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.opengl.producerconsumer.SingleShotProducer;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlFetcher implements SingleShotProducer {

    private Context Context;
    private URL URL;

    public UrlFetcher(Context context, final String url) {
        this.Context = context;
        try {
            this.URL = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL: " + url);
        }
    }

    @Override
    public void Produce() {
        ConnectivityManager connMgr = (ConnectivityManager)
                Context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null)
            throw new RuntimeException("Network info could not be read");
        if (networkInfo.isConnected() == false)
            throw new RuntimeException("Network is not connected");
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) URL.openConnection();
            String fetchedString = CharStreams.toString(
                    new InputStreamReader(conn.getInputStream(), Charsets.UTF_8));
            // Log.i("Url fetched string", fetchedString);
        } catch (MalformedURLException e) {
            throw new RuntimeException("malformed url");
        } catch (IOException e) {
            throw new RuntimeException("io  exception");
        }
        finally {
            conn.disconnect();
        }
    }
}
