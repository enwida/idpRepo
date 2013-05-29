package de.enwida.web.utils;

import java.util.Calendar;

public class CalendarRange {
    
    private Calendar from;
    private Calendar to;
    
    public CalendarRange(Calendar from, Calendar to) {
        this.from = from;
        this.to = to;
    }

    public Calendar getFrom() {
        return from;
    }
    
    public void setFrom(Calendar from) {
        this.from = from;
    }
    
    public Calendar getTo() {
        return to;
    }
    
    public void setTo(Calendar to) {
        this.to = to;
    }

}
