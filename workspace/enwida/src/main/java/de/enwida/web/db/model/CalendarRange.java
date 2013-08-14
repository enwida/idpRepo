package de.enwida.web.db.model;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class CalendarRange implements Cloneable, Comparable<CalendarRange> {

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
	
	public static List<CalendarRange> getConnectedRanges(List<CalendarRange> ranges) {
		final List<CalendarRange> result = new ArrayList<>(ranges.size());
		if (ranges.isEmpty()) {
			return result;
		}
		Collections.sort(ranges);
		result.add((CalendarRange) ranges.get(0).clone());
		
		for (int i = 1; i < ranges.size(); i++) {
			final CalendarRange current = ranges.get(i);
			final CalendarRange last = result.get(result.size() - 1);
			
			if (current.getFrom().compareTo(last.getTo()) <= 0) {
				if (current.getTo().compareTo(last.getTo()) >= 0) {
					last.setTo((Calendar) current.getTo().clone());
				}
			} else {
				result.add(current.clone());
			}
		}
		return result;
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

	@Override
	public int compareTo(CalendarRange o) {
		return from.compareTo(o.from);
	}
	
	@Override
	protected CalendarRange clone() {
		final Calendar fromClone = (Calendar) from.clone();
		final Calendar toClone = (Calendar) to.clone();
		return new CalendarRange(fromClone, toClone);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CalendarRange other = (CalendarRange) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
	
}
