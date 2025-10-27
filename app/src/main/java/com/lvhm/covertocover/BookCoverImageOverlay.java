package com.lvhm.covertocover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookCoverImageOverlay extends Fragment {
    private ImageView book_cover;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overlay_book_cover_image, container, false);
        book_cover = view.findViewById(R.id.overlay_book_cover_view);

        Bundle book_cover_info = getArguments();
        if (book_cover_info != null) {
            Bitmap thumbnailBitmap = book_cover_info.getParcelable("thumbnail_bitmap");
            if (thumbnailBitmap != null) {
                book_cover.setImageBitmap(thumbnailBitmap);
            } else {
                String image_url = book_cover_info.getString("thumbnail");
                if (image_url != null) {
                    getBookCoverImage(image_url);
                }
            }
        }

        return view;
    }

    public void getBookCoverImage(String image_url) {
        image_url = image_url.replace("http://", "https://");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        final Bitmap[] image_cover = {null};

        String finalImage_url = image_url;
        executor.execute(() -> {
            try {
                InputStream in = new URL(finalImage_url).openStream();
                image_cover[0] = BitmapFactory.decodeStream(in);
                handler.post(() -> book_cover.setImageBitmap(image_cover[0]));
            } catch (Exception e) {
                String message = "Error getting book cover image. Error: " + e.getMessage();
                System.out.println(message);
                handler.post(() -> NotificationCentral.showNotification(requireContext(), message));
            }
        });
    }
}
