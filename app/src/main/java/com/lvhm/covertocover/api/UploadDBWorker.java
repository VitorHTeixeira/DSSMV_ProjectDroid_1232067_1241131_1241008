package com.lvhm.covertocover.api;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.PrintToast;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;
import com.lvhm.covertocover.repo.UserTokenContainer;

public class UploadDBWorker extends Worker {
    private PrintToast toast_printer;

    public UploadDBWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        //DatabaseAPIClient.initialize(context);

        UserTokenContainer tokenContainer = UserTokenContainer.getInstance(context);
        String userToken = tokenContainer.getToken();

        if (userToken == null || userToken.isEmpty()) {
            return Result.failure();
        }

        BookContainer book_container = BookContainer.getInstance();
        ReviewContainer review_container = ReviewContainer.getInstance();

        book_container.updateAllBooksToken(context, userToken);
        review_container.updateAllReviewsToken(context, userToken);

        boolean books_success = DatabaseAPIClient.uploadBooksToDB(book_container);
        boolean reviews_success = DatabaseAPIClient.uploadReviewsToDB(review_container);

        if (books_success && reviews_success) {
            DatabaseAPIClient.updateLastSync(userToken);

            NotificationCentral.showNotification(context,
                    "☁️ Uploaded " + book_container.getBooks().size() +
                            " books and " + review_container.getReviews().size() + " reviews.");
            return Result.success();
        } else {
            return Result.retry();
        }
    }
}
