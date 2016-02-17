package com.example.android.opengl.client;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.opengl.primitives.XYZf;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A thing capable of continuously polling the application server to fetch the
 * latest mandate for a user's virtual world position, and to post this fetched data
 * into the sink you provide in the constructor. It matches its request rate to the reponse rate
 * provided by the server using backpressure throttling.
 * It works asynchronously. You start it with
 * the resume() method (which does not block), and can pause it similarly. Whilst it uses
 * AsyncTasks for the comms, it writes to the position sink from the MAIN thread.
 *
 */
public class NetworkPositionFeeder {

    private final int BACKLOG_THRESHOLD = 3;
    private final String SERVER_URL = "nosuchserver";

    private XYZf positionSink;
    private boolean isPaused;

    public NetworkPositionFeeder(XYZf positionSink) {
        this.positionSink = positionSink;
        this.isPaused = true;
    }

    public void resume() {
        isPaused = false;
        loadUpMessageQueueToBacklogLimit();
        // That's it. The callbacks daisy-chain the adding of new requests as and when
        // the back pressure is relieved.
    }

    public void pause() {
        isPaused = true;
    }

    private void loadUpMessageQueueToBacklogLimit() {
        for (int i = 0; i < BACKLOG_THRESHOLD; i++) {
            launchOneRequest();
        }
    }

    private void launchOneRequest() {
        AsyncTask fetchTask = new FetchTask();
        fetchTask.execute();
    }

    /**
     * Helper class to wrap a URL fetch inside an AsyncTask.
     */
    private class FetchTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            Long myLong = 42l;
            HttpURLConnection conn = null;
            try {
                URL serverURL = new URL("http://cadboardsvr.appspot.com/");
                conn = (HttpURLConnection) serverURL.openConnection();
                String fetchedString = CharStreams.toString(
                        new InputStreamReader(conn.getInputStream(), Charsets.UTF_8));
                Log.i("pos feeder C fetched string", fetchedString);
            }
            catch (IOException e) {
                throw new RuntimeException("comms failed in doinbackgroun", e);
            }
            finally {
                conn.disconnect();
            }
            return myLong;
        }

        protected void onPostExecute(Long result) {
            Log.i("onpostexec A", result.toString());
            // Daisy chain another unless been paused
            if (isPaused == false)
                launchOneRequest();
        }
    }
}