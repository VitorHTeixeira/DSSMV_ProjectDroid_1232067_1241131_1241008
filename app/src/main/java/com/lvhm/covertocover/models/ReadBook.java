package com.lvhm.covertocover.models;

import java.util.Date;

public class ReadBook extends OnGoingBook {
    private Date end_date;
    private int finished_days;

    public Date getEndDate() {
        return end_date;
    }
    public void setEndDate(Date end_date) {
        this.end_date = end_date;
    }
    public int getFinishedDays() {
        return finished_days;
    }
    public void setFinishedDays(int finished_days) {
        this.finished_days = finished_days;
    }
}
