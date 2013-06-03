package de.enwida.web.model;

import java.util.ArrayList;
import java.util.List;

import de.enwida.transport.DataResolution;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.ProductLeaf;
import de.enwida.web.utils.ProductNode;

public class ProductTree {
    
    public static class ProductAttributes {
        public int product;
        public List<DataResolution> resolutions;
        public CalendarRange timeRange;

        public ProductAttributes(int product, List<DataResolution> resolutions, CalendarRange timeRange) {
            this.product = product;
            this.resolutions = resolutions;
            this.timeRange = timeRange;
        }
    }
    
    private List<ProductNode> nodes;

    public ProductTree(List<ProductNode> nodes) {
        this.nodes = nodes;
    }

    public ProductTree() {
        this(new ArrayList<ProductNode>());
    }
    
    /**
     * Flattens the tree to a list
     * @return a {@link List} of {@link ProductAttributes} containing the product id and
     *         attributes from the corresponding leaf
     */
    public List<ProductAttributes> flatten() {
        final List<ProductAttributes> result = new ArrayList<ProductAttributes>();
        for (final ProductNode node : nodes) {
            flatten(node, "", result);
        }
        return result;
    }
    
    private static void flatten(ProductNode node, String productId, List<ProductAttributes> accumulator) {
        if (node instanceof ProductLeaf) {
            final ProductLeaf leaf = (ProductLeaf) node;
            productId += leaf.getId();
            accumulator.add(new ProductAttributes(Integer.parseInt(productId), leaf.getResolution(), leaf.getTimeRange()));
        } else if (node.getChildren() != null) {
            productId += node.getId();
            for (final ProductNode child : node.getChildren()) {
                flatten(child, productId, accumulator);
            }
        }
    }
    
    /**
     * Traverses the tree based on a product id
     * @param product The product id (e.g. 211)
     * @return The corresponding {@link ProductLeaf}
     */
    public ProductLeaf getLeaf(int product) {
        return getLeaf(String.valueOf(product), getRoot());
    }
    
    private static ProductLeaf getLeaf(String product, ProductNode node) {
        if (product.isEmpty()) {
            if (node instanceof ProductLeaf ){
                return (ProductLeaf) node;
            } else  {
                // We should have arrived at a leaf but failed
                return null;
            }
        }
        // Split the product id
        final int nextId = Integer.parseInt(product.substring(0, 1));
        final String remainingId = product.substring(1);
        
        // Find the child node with id
        ProductNode nextNode = null;
        for (ProductNode child : node.getChildren()) {
            if (child.getId() == nextId) {
                nextNode = child;
                break;
            }
        }
        
        // Check if we found the next node
        if (nextNode == null) {
            return null;
        }
        return getLeaf(remainingId, nextNode);
    }
    
    public ProductNode getRoot() {
        return new ProductNode(0, "root", nodes);
    }
    
    public List<ProductNode> getNodes() {
        return nodes;
    }
    
    public void addNode(ProductNode node) {
        nodes.add(node);
    }

}
