package com.lvhm.covertocover.repo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ExportToJSON {
    private static Gson gson;
    public static void exportToJSON(ArrayList<Book> books, ArrayList<Review> reviews, String file_name, Context context) {
        try {
            gson = new Gson();
            File file = new File(context.getFilesDir(), file_name);
            FileWriter file_writer = new FileWriter(file);
            BufferedWriter buffered_writer = new BufferedWriter(file_writer);

            // Books
            for(Book book : books) {
                String line = gson.toJson(book);
                buffered_writer.write(line);
                buffered_writer.newLine();
            }
            buffered_writer.newLine();

            // Reviews
            for(Review review : reviews) {
                String line = gson.toJson(review);
                buffered_writer.write(line);
                buffered_writer.newLine();
            }
            buffered_writer.close();

            Uri file_uri = FileProvider.getUriForFile(
                    context,
                    context.getApplicationContext().getPackageName() + ".provider",
                    file
            );

            Intent share_intent = new Intent(Intent.ACTION_SEND);
            share_intent.setType("application/json");
            share_intent.putExtra(Intent.EXTRA_STREAM, file_uri);
            share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share_intent, "Export JSON data"));
            NotificationCentral.showNotification(context, "Successfully exported to JSON");
        } catch (IOException e) {
            NotificationCentral.showNotification(context, "Error (JSON): " + e.getMessage());
        } catch (Exception e) {
            NotificationCentral.showNotification(context, "Error (Share): " + e.getMessage());
            e.printStackTrace();
        }
    }
}
