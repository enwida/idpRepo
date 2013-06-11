package de.enwida.web.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarRange {
    
    private Calendar from;
    private Calendar to;
    
    public CalendarRange(Calendar from, Calendar to) {
        this.from = from;
        this.to = to;
    }
    
    public CalendarRange(Date from, Date to) {
        this.from.setTime(from);
        this.to.setTime(to);
    }

	public static CalendarRange always() {
		final Calendar from = Calendar.getInstance();
		final Calendar to = Calendar.getInstance();
		from.setTimeInMillis(0);
		to.setTimeInMillis(Long.MAX_VALUE);
		return new CalendarRange(from, to);
    }
    
	public static CalendarRange getMaximum(List<CalendarRange> ranges) {
	    Calendar from = null;
	    Calendar to = null;
	    
	    for (final CalendarRange range : ranges) {
	        if (from == null || range.getFrom().compareTo(from) < 0) {
	            from = range.getFrom();
	        }
	        if (to == null || range.getTo().compareTo(to) > 0) {
	            to = range.getTo();
	        }
	    }
	    if (from == null || to == null) {
	        return null;
	    }
	    return new CalendarRange(from, to);
	}

	public static CalendarRange getMinimum(List<CalendarRange> ranges) {
	    Calendar from = null;
	    Calendar to = null;
	    
	    for (final CalendarRange range : ranges) {
	        if (from == null || range.getFrom().compareTo(from) > 0) {
	            from = range.getFrom();
	        }
	        if (to == null || range.getTo().compareTo(to) < 0) {
	            to = range.getTo();
	        }
	    }
	    if (from == null || to == null) {
	        return null;
	    }
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
