package com.lvhm.covertocover.api;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lvhm.covertocover.NotificationCentral;

public class DownloadDBWorker extends Worker {
    public DownloadDBWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        boolean books_success = DatabaseAPIClient.getBooksFromDB();
        boolean reviews_success = DatabaseAPIClient.getReviewsFromDB();

        if (books_success && reviews_success) {
            return Result.success();
        } else {
            return Result.retry();
        }
    }
}
