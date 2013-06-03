package de.enwida.web.utils;

import java.util.ArrayList;
import java.util.List;

public class ProductNode {
    
    private int id;
    private String name;
    private List<ProductNode> children;
    
    public ProductNode(int id, String name, List<ProductNode> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }
    
    public ProductNode(int id, String name) {
        this(id, name, new ArrayList<ProductNode>());
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

    public List<ProductNode> getChildren() {
        return children;
    }
    
    public void addChild(ProductNode child) {
        children.add(child);
    }
    
}