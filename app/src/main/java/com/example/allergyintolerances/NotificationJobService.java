package com.example.allergyintolerances;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        new NotificationHandler(getApplicationContext()).send("Haligali");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
