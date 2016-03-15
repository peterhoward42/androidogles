package com.example.android.opengl.client;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.opengl.primitives.XYZf;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A thing capable of continuously polling the application server to fetch the
 * latest mandate for a user's virtual world position, and to post this fetched data
 * into the sink you provide. It matches its request rate to the response rate
 * provided by the server using back-pressure throttling.
 * It works asynchronously. You start it with
 * the resume() method (which does not block), and can pause it similarly. Whilst it uses
 * AsyncTasks for the comms, it writes to the position sink from the GUI thread - courtesy of
 * AsyncTask standard behaviour.
 *
 */
public class NetworkPositionFeeder {

    private final int BACKLOG_THRESHOLD = 1;
    private final String SERVER_URL = "nosuchserver";

    private XYZf positionSink;
    private boolean isPaused;
    private Context context;

    public NetworkPositionFeeder(Context context) {
        this.positionSink = null;
        this.isPaused = true;
        this.context = context;
    }

    public void resume(XYZf positionSink) {
        this.positionSink = positionSink;
        isPaused = false;
        // Get <N> messages in-flight.
        loadUpMessageQueueToBacklogLimit();
        // That's it. The callbacks daisy-chain the adding of new requests as and when
        // the back pressure is relieved.
    }

    public void pause() {
        this.positionSink = null;
        isPaused = true;
    }

    private void loadUpMessageQueueToBacklogLimit() {
        for (int i = 0; i < BACKLOG_THRESHOLD; i++) {
            launchOneRequest();
        }
    }

    private void launchOneRequest() {
        FetchTask fetchTask = new FetchTask();
        fetchTask.execute("foo", "bar");
    }

    private class FetchTask extends AsyncTask<String, Integer, Long> {

        protected Long doInBackground(String... unusedParams) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null)
                throw new RuntimeException("Network info could not be read");
            if (networkInfo.isConnected() == false)
                throw new RuntimeException("Network is not connected");
            HttpURLConnection conn = null;
            try {
                URL serverURL = new URL("http://cadboardserver.appspot.com/mouseposnquery");
                conn = (HttpURLConnection) serverURL.openConnection();
                String fetchedString = CharStreams.toString(
                        new InputStreamReader(conn.getInputStream(), Charsets.UTF_8));
                Log.i("C fetched string", fetchedString);
            } catch (MalformedURLException e) {
                throw new RuntimeException("malformed url");
            } catch (IOException e) {
                throw new RuntimeException("io  exception");
            }
            finally {
                conn.disconnect();
            }
            return 42L;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            Log.i("onpostexec A", result.toString());
            // Daisy chain another request unless feeder has been paused by client.
            if (isPaused == false) {
                // launchOneRequest();
            }
        }
    }
}