package de.enwida.web.model;

import java.util.ArrayList;
import java.util.List;

import de.enwida.web.utils.ProductNode;

public class ProductTree {
    
    private List<ProductNode> nodes;

    public ProductTree(List<ProductNode> nodes) {
        this.nodes = nodes;
    }
    
    public ProductTree() {
        this(new ArrayList<ProductNode>());
    }
    
    public List<ProductNode> getNodes() {
        return nodes;
    }
    
    public void addNode(ProductNode node) {
        nodes.add(node);
    }

}
