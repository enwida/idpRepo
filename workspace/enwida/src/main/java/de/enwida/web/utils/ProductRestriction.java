package de.enwida.web.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.enwida.transport.DataResolution;
import de.enwida.web.db.model.CalendarRange;

public class ProductRestriction {
    
    private List<DataResolution> resolutions;
    private CalendarRange timeRange;

    public ProductRestriction() {
    	this.resolutions = new ArrayList<DataResolution>();
    	this.timeRange = null;
    } 
    
    public ProductRestriction(List<DataResolution> resolutions, CalendarRange timeRange) {
        this.resolutions = resolutions;
        this.timeRange = timeRange;
    }
    
    public static ProductRestriction combineMaximum(List<ProductRestriction> restrictions) {
        final List<CalendarRange> ranges = new ArrayList<CalendarRange>();
        final Set<DataResolution> resolutions = new HashSet<DataResolution>();
        
        for (final ProductRestriction restriction : restrictions) {
            if (restriction != null) {
                ranges.add(restriction.getTimeRange());
                resolutions.addAll(restriction.getResolutions());
            }
        }
        
        // Return null if all list elements (i.e. restrictions) are null or the list is empty
        if (ranges.size() == 0 || resolutions.size() == 0) {
            return null;
        }
        return new ProductRestriction(new ArrayList<DataResolution>(resolutions), CalendarRange.getMaximum(ranges));
    }

    public List<DataResolution> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<DataResolution> resolutions) {
        this.resolutions = resolutions;
    }

    public CalendarRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(CalendarRange timeRange) {
        this.timeRange = timeRange;
    }
    
}
