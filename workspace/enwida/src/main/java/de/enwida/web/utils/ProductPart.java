package de.enwida.web.utils;

import java.util.ArrayList;
import java.util.List;

public class ProductPart {
    
    private int id;
    private String name;
    private List<ProductPart> children;
    
    public ProductPart(int id, String name, List<ProductPart> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }
    
    public ProductPart(int id, String name) {
        this(id, name, new ArrayList<ProductPart>());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductPart> getChildren() {
        return children;
    }
    
    public void addChild(ProductPart child) {
        children.add(child);
    }
    
}
