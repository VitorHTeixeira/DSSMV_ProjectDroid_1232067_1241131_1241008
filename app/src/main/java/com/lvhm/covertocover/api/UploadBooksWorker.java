package com.lvhm.covertocover.api;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;

public class UploadBooksWorker extends Worker {
    public UploadBooksWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        BookContainer book_container = BookContainer.getInstance();
        ReviewContainer review_container = ReviewContainer.getInstance();

        boolean books_success = DatabaseAPIClient.addBookToDB(book_container);
        boolean reviews_success = DatabaseAPIClient.addReviewToDB(review_container);

        if (books_success && reviews_success) {
            return Result.success();
        } else {
            return Result.retry();
        }
    }
}
