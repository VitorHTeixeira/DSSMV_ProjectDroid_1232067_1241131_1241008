package com.lvhm.covertocover.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;
import com.lvhm.covertocover.repo.UserTokenContainer;

public class DownloadDBWorker extends Worker {
    public DownloadDBWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        boolean books_success = DatabaseAPIClient.getBooksFromDB();
        boolean reviews_success = DatabaseAPIClient.getReviewsFromDB();

        BookContainer book_container = BookContainer.getInstance();
        ReviewContainer review_container = ReviewContainer.getInstance();

        if (books_success && reviews_success) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(getApplicationContext(),
                        "\uD83E\uDDFE Downloaded " + book_container.getBooks().size() +
                                " books and " + review_container.getReviews().size() + " reviews",
                        Toast.LENGTH_LONG).show();
            });
            return Result.success();
        } else {
            return Result.retry();
        }
    }
}
