package de.enwida.web.utils;

import java.util.ArrayList;
import java.util.List;

import de.enwida.transport.DataResolution;

public class NavigationDefaults implements Cloneable {
    
    private int tsoId;
    private DataResolution resolution;
    private int product;
    private CalendarRange timeRange;
    private List<Integer> disabledLines;
    
    public NavigationDefaults() { }

    public NavigationDefaults(int tsoId, DataResolution resolution, int product, CalendarRange timeRange) {
        this.tsoId = tsoId;
        this.resolution = resolution;
        this.product = product;
        this.timeRange = timeRange;
        this.disabledLines = new ArrayList<>();
    }
    
    public NavigationDefaults clone() {
        final NavigationDefaults result = new NavigationDefaults(tsoId, resolution, product, new CalendarRange(timeRange.getFrom(), timeRange.getTo()));
        result.getDisabledLines().addAll(disabledLines);
        return result;
    }

    public int getTsoId() {
        return tsoId;
    }

    public void setTsoId(int tsoId) {
        this.tsoId = tsoId;
    }

    public DataResolution getResolution() {
        return resolution;
    }

    public void setResolution(DataResolution resoulution) {
        this.resolution = resoulution;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public CalendarRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(CalendarRange timeRange) {
        this.timeRange = timeRange;
    }
    
    public List<Integer> getDisabledLines() {
        return disabledLines;
    }

	public void setDisabledLines(List<Integer> disabledLines) {
		this.disabledLines = disabledLines;
	}
    
}
