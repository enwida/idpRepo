package de.enwida.web.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.enwida.transport.DataResolution;
import de.enwida.web.db.model.CalendarRange;

public class ProductLeaf extends ProductNode {
    
    private List<DataResolution> resolution;
    private CalendarRange timeRange;

    public ProductLeaf() { }
    
    public ProductLeaf(int id, String name, List<DataResolution> resolution, CalendarRange timeRange) {
        super(id, name, null);
        this.resolution = resolution;
        this.timeRange = timeRange;
    }

    public ProductLeaf(int id, String name) {
        this(id, name, new ArrayList<DataResolution>(), CalendarRange.always());
    }
    
    @Override
    public ProductNode clone() {
        final ProductLeaf result = new ProductLeaf(getId(), getName());
        result.getResolution().addAll(resolution);
        result.setTimeRange(new CalendarRange(timeRange.getFrom(), timeRange.getTo()));
        return result;
    }

    public List<DataResolution> getResolution() {
        return resolution;
    }

    public void setResolution(List<DataResolution> resolution) {
        this.resolution = resolution;
    }

    public CalendarRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(CalendarRange timeRange) {
        this.timeRange = timeRange;
    }   
    
    @Override
    public List<ProductNode> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public void addChild(ProductNode child) {
        throw new IllegalAccessError("Illegal call to addChild of a leaf");
    }
    
}
