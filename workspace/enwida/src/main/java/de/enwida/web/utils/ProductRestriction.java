package de.enwida.web.utils;

import java.util.List;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;

public class ProductRestriction {
    
    private int productId;
    private Aspect aspect;
    private List<DataResolution> resolutions;
    private CalendarRange timeRange;

    public ProductRestriction(int productId, Aspect aspect,
           List<DataResolution> resolutions, CalendarRange timeRange) {

        this.productId = productId;
        this.aspect = aspect;
        this.resolutions = resolutions;
        this.timeRange = timeRange;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Aspect getAspect() {
        return aspect;
    }

    public void setAspect(Aspect aspect) {
        this.aspect = aspect;
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
