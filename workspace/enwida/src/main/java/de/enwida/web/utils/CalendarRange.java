package de.enwida.web.utils;

import java.util.Calendar;

public class CalendarRange {
    
    private Calendar from;
    private Calendar to;
    
    public CalendarRange(Calendar from, Calendar to) {
        this.from = from;
        this.to = to;
    }
    
    public static CalendarRange always() {
		final Calendar from = Calendar.getInstance();
		final Calendar to = Calendar.getInstance();
		from.setTimeInMillis(0);
		to.setTimeInMillis(Long.MAX_VALUE);
		return new CalendarRange(from, to);
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
