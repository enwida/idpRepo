package de.enwida.web.model;

import java.util.Locale;

import de.enwida.transport.DataResolution;
import de.enwida.web.utils.CalendarRange;

public class ChartLinesRequest {
    
    private int chartId;
    private int product;
    private int tso;
    private CalendarRange timeRange;
    private DataResolution resolution;
    private Locale locale;
    
    public ChartLinesRequest(int chartId, int product, int tso, CalendarRange timeRange,
                             DataResolution resolution, Locale locale) {

        this.chartId = chartId;
        this.product = product;
        this.tso = tso;
        this.timeRange = timeRange;
        this.resolution = resolution;
        this.locale = locale;
    }
    
    public int getChartId() {
        return chartId;
    }

    public void setChartId(int chartId) {
        this.chartId = chartId;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getTso() {
        return tso;
    }

    public void setTso(int tso) {
        this.tso = tso;
    }

    public CalendarRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(CalendarRange timeRange) {
        this.timeRange = timeRange;
    }

    public DataResolution getResolution() {
        return resolution;
    }

    public void setResolution(DataResolution resolution) {
        this.resolution = resolution;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
}
