package de.enwida.web.utils;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY)

public class ProductNode implements Cloneable {
    
    private int id;
    private String name;
    private List<ProductNode> children;
    
    public ProductNode() { }
    
    public ProductNode(int id, String name, List<ProductNode> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    public ProductNode(int id, String name) {
        this(id, name, new ArrayList<ProductNode>());
    }
    
    public ProductNode clone() {
        final ProductNode result = new ProductNode(id, name);
        for (final ProductNode child : children) {
            result.addChild(child.clone());
        }
        return result;
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

	public void setChildren(List<ProductNode> children) {
		this.children = children;
	}
    
}
