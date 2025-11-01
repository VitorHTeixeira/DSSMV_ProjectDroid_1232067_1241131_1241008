package com.lvhm.covertocover.repo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExportToXLSX {
    public static void exportToXLSX(ArrayList<Book> books, ArrayList<Review> reviews, String file_name, Context context) {
        Workbook workbook = new XSSFWorkbook();
        Sheet book_sheet = workbook.createSheet("Books");
        int book_row_number = 0;
        for (Book book : books) {
            Row row = book_sheet.createRow(book_row_number++);
            row.createCell(0).setCellValue(book.getISBN());
            row.createCell(1).setCellValue(book.getName());
            row.createCell(2).setCellValue(book.getAuthor().toString());
            row.createCell(3).setCellValue(book.getYear());
            row.createCell(4).setCellValue(book.getGenre().toString());
            row.createCell(5).setCellValue(book.getRead());
            row.createCell(6).setCellValue(book.getPageCount());
            row.createCell(7).setCellValue(book.getIsWishlisted());
            row.createCell(8).setCellValue(book.getOnGoing());
        }
        Sheet review_sheet = workbook.createSheet("Reviews");
        int review_row_number = 0;
        for (Review review : reviews) {
            Row row = review_sheet.createRow(review_row_number++);
            row.createCell(0).setCellValue(review.getBook().getISBN());
            row.createCell(1).setCellValue(review.getRating());
            row.createCell(2).setCellValue(review.getReviewText());
            row.createCell(3).setCellValue(review.getDate().toString());
        }
        File files_dir = context.getCacheDir();
        File new_file = new File(files_dir, file_name);
        try (FileOutputStream outputStream = new FileOutputStream(new_file)) {
            workbook.write(outputStream);
            workbook.close();
            Uri file_uri = FileProvider.getUriForFile(
                    context,
                    context.getApplicationContext().getPackageName() + ".provider",
                    new_file
            );
            Intent share_intent = new Intent(Intent.ACTION_SEND);
            share_intent.setType("application/vnd.openxmlformatsofficedocument.spreadsheetml.sheet");
            share_intent.putExtra(Intent.EXTRA_STREAM, file_uri);
            share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share_intent, "Export XLSX data"));
            NotificationCentral.showNotification(context, "Successfully exported to XLSX");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
