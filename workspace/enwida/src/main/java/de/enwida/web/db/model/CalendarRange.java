package de.enwida.web.db.model;

import java.beans.Transient;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class CalendarRange {

	public static final String FROM = "START_DATE";
	public static final String TO = "END_DATE";

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = FROM)
	private Calendar from;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = TO)
	private Calendar to;
    
	/**
	 * 
	 */
	public CalendarRange() {
	}

	public CalendarRange(Calendar from, Calendar to) {
        this.from = from;
        this.to = to;
    }
    
    public CalendarRange(Date from, Date to) {
        this.from = Calendar.getInstance();
        this.to = Calendar.getInstance();
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
	    	if (range == null) {
	    		continue;
	    	}
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
	    	if (range == null) {
	    		continue;
	    	}
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

	@Transient
    public Calendar getFrom() {
        return from;
    }
    
    public void setFrom(Calendar from) {
        this.from = from;
    }
    
	@Transient
    public Calendar getTo() {
        return to;
    }
    
    public void setTo(Calendar to) {
        this.to = to;
    }
}
