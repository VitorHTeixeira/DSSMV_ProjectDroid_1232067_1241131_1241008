package com.lvhm.covertocover.models;

import java.util.Date;

public class OnGoingBook extends Book {
    protected Date start_date;
    protected int current_page;
    protected double book_progress;

    public Date getStartDate() {
        return start_date;
    }
    public void setStartDate(Date start_date) {
        this.start_date = start_date;
    }
    public int getCurrentPage() {
        return current_page;
    }
    public void setCurrentPage(int current_page) {
        this.current_page = current_page;
    }
    public double getBookProgress() {
        return book_progress;
    }
    public void setBookProgress(double book_progress) {
        this.book_progress = book_progress;
    }
}
