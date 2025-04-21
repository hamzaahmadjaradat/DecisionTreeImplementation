package com.example.decisiontree_11910126_1203302;

import java.util.HashMap;
import java.util.Map;

public class Node {

    boolean isLeaf;
    Feature splitFeature;
    Map<String, Node> branches;
    String classLabel;
    Node parent;
    int depth;
    int sampleSize;

    //Decision Nodes
    public Node(Feature splitFeature, Node parent, int depth, int sampleSize) {
        this.splitFeature = splitFeature;
        this.parent = parent;
        this.depth = depth;
        this.sampleSize = sampleSize;
        this.branches = new HashMap<>();
        this.classLabel = null;
        this.isLeaf = false;

    }

    //Leaf
    public Node (String classLabel, Node parent, int depth, int sampleSize){
        this.classLabel = classLabel;
        this.parent = parent;
        this.depth = depth;
        this.sampleSize = sampleSize;
        this.splitFeature = null;
        this.branches = null;
        this.isLeaf = true;
    }

    public void addChild(String value, Node child) {
        if( branches != null) {
            branches.put(value,child);
        }
    }

    public String commonClass(){
        if (isLeaf) {
            return classLabel;
        }

        Map<String, Integer> count = new HashMap<>();
        for (Node child: branches.values()){
            String label = child.getClassLabel();
            if(!count.containsKey(label)) {
                count.put(label,1);
            }
            else {
                count.put(label, count.get(label) + 1);
            }
        }

        String common = null;
        int max = 0;
        for (Map.Entry<String,Integer> x : count.entrySet()) {
            if (x.getValue() > max) {
                common = x.getKey();
                max = x.getValue();
            }
        }

        return common;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public Feature getSplitFeature() {
        return splitFeature;
    }

    public Map<String, Node> getBranches() {
        return branches;
    }

    public String getClassLabel() {
        return classLabel;
    }

    public Node getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

    public int getSampleSize() {
        return sampleSize;
    }
}
