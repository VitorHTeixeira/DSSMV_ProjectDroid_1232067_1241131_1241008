package com.lvhm.covertocover.api;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.repo.UserTokenContainer;

public class DownloadDBWorker extends Worker {
    public DownloadDBWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        UserTokenContainer tokenContainer = UserTokenContainer.getInstance(context);
        String userToken = tokenContainer.getToken();

        if (userToken == null || userToken.isEmpty()) {
            return Result.failure();
        }

        boolean books_success = DatabaseAPIClient.getBooksFromDBByToken(userToken);
        boolean reviews_success = DatabaseAPIClient.getReviewsFromDBByToken(userToken);

        if (books_success && reviews_success) {
            return Result.success();
        } else {
            return Result.retry();
        }
    }
}
