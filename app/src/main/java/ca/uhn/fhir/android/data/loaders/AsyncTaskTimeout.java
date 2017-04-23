package ca.uhn.fhir.android.data.loaders;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 2017-04-21.
 */

public class AsyncTaskTimeout implements Runnable {

    protected final long TIMEOUT = TimeUnit.MINUTES.toMillis(1) / 3;

    protected AsyncTask mTask;
    Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if ((mTask != null) && (mTask.getStatus() == AsyncTask.Status.RUNNING || mTask.getStatus() == AsyncTask.Status.PENDING)) {
                mTask.cancel(true); //Cancel Async task or do the operation you want after 1 minute
            }
        }
    };

    public AsyncTaskTimeout(AsyncTask at) {
        mTask = at;
    }

    @Override
    public void run() {
        mHandler.postDelayed(runnable, TIMEOUT);
        // After 60sec the task in run() of runnable will be done
    }
}