package com.example.decisiontree_11910126_1203302;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DecisionTree {

    Data data;
    Feature target;
    SplitMethod splitMethod;
    int maxDepth = 4;
    double tp, tn,fp,fn;
    String positiveClass;
    Map<String,Double> confusion = new HashMap<>();

    public DecisionTree(Data data, Feature target, SplitMethod splitMethod) {
        this.data = data;
        this.target = target;
        this.splitMethod = splitMethod;
    }

    boolean purity(Data data) { //the rows have the same target class value
        if (data.getRows().isEmpty()) {
            return true;
        }
        String targetType = data.getRows().get(0)[data.getColumnIndex(target.getName())];
        for (String[] x : data.getRows()) {
            if (!x[data.getColumnIndex(target.getName())].equals(targetType)) {
                return false;
            }
        }
        return true;
    }

    //no features left to split
    boolean noFeatureRemaining(Data data) {
        return data.getColumns().length <= 1;
    }


    Feature findMostGainFeature(Data data) {
        double max = Double.NEGATIVE_INFINITY;
        Feature feature = null;

        for (Feature f : data.getColumns()) {
            if (f.getName().equals(target.getName())) { //skip target class
                continue;
            }
            double infoGain = splitMethod.calculateGain(data, f, target);
            if (infoGain > max) {
                max = infoGain;
                feature = f;
                f.infoGain = infoGain;
            }
        }
        return feature;
    }

    String commonLabel(Data data) {
        Map<String, Integer> occurrences = new HashMap<>();
        int index = data.getColumnIndex(target.getName());
        for (String[] row : data.getRows()) {
            String l = row[index]; //label for current row
            if (!occurrences.containsKey(l)) {
                occurrences.put(l, 1);
            } else {
                occurrences.put(l, occurrences.get(l) + 1);
            }
        }

        String common = null;
        int max = 0;
        for (Map.Entry<String, Integer> e : occurrences.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                common = e.getKey();
            }
        }
        return common;
    }

    Node createLeaf(Data data, Node node, int depth) {
        String label = commonLabel(data);
        return new Node(label, node, depth, data.getRows().size());
    }

    Data removeFeatureFromData(Data data, ArrayList<String[]> rowsSubsets, String name) {
        if (rowsSubsets != null) {
            int index = data.getColumnIndex(name);
            ArrayList<Feature> newCols = new ArrayList<>();
            for (int i = 0; i < data.getColumns().length; i++) {
                if (i != index) {
                    newCols.add(data.getColumns()[i]);
                }
            }
            ArrayList<String[]> newRows = new ArrayList<>();
            for (String[] row : rowsSubsets) {
                String[] newR = new String[row.length - 1];
                int x = 0;
                for (int i = 0; i < row.length; i++) {
                    if (i != index) {
                        newR[x++] = row[i];
                    }
                }
                newRows.add(newR);

            }

            return new Data(newCols.toArray(new Feature[0]), newRows);
        } else {
            return null;
        }
    }

    void splitDataset(Data data, Node decision, Feature mostGain, int depth) {
        Map<String, ArrayList<String[]>> sets = mostGain.extractSubsets(data, mostGain);
        for (Map.Entry<String, ArrayList<String[]>> entry : sets.entrySet()) {
            String value = entry.getKey();
            ArrayList<String[]> rowsSubsets = entry.getValue();
            if (rowsSubsets.isEmpty()) {
                Node leaf = createLeaf(data, decision, depth + 1);
                decision.addChild(value, leaf);
                continue;
            }
            Data reducedData = removeFeatureFromData(data, rowsSubsets, mostGain.getName());
            Node child = build(reducedData, decision, depth + 1);
            decision.addChild(value, child);
        }


    }

    Node build(Data data, Node root, int depth) {
        if (depth >= maxDepth) {
            return createLeaf(data, root, depth);
        }
        if (purity(data) || noFeatureRemaining(data)) {
            return createLeaf(data, root, depth);
        }
        Feature mostGain = findMostGainFeature(data);
        if (mostGain == null) {
            return createLeaf(data, root, depth);
        }
        Node decision = new Node(mostGain, root, depth, data.getRows().size());
        splitDataset(data, decision, mostGain, depth);
        return decision;
    }

    String predict(String[] testRows, Node node, Data test) {
        while (!node.isLeaf()) {
            Feature f = node.getSplitFeature();
            int index = test.getColumnIndex(f.getName());
            String value = testRows[index];

            if (!node.getBranches().containsKey(value)) {
                return node.commonClass();
            }
            node = node.getBranches().get(value);
        }
        return node.getClassLabel();
    }

    Map<String, Double> evaluate(Data test, Node node, Feature target) {
        tp = 0; tn = 0; fp = 0; fn = 0;
        int total = test.getRows().size();

        int index = test.getColumnIndex(target.getName());

        for (String[] x : test.getRows()) {
            String actual = x[index];
            String predicted = predict(x, node, test);

            if (predicted.equals(positiveClass)) {
                if (actual.equals(positiveClass)) {
                    tp++;
                } else {
                    fp++;
                }
            } else {
                if (actual.equals(positiveClass)) {
                    fn++;
                } else {
                    tn++;
                }
            }
        }
        double accuracy = (double) (tp + tn ) / total;
        double precision = 0;
        if ((tp + fp) != 0) {
            precision = (double) tp / (tp + fp);
        }
        double recall = 0;
        if ((tp + fn) != 0) {
            recall = (double) tp / (tp + fn);
        }
        double f1score = 0;
        if (precision + recall != 0) {
            f1score = 2 * (precision * recall) / (precision + recall);
        }

        Map<String, Double> accuracyMeasures = new HashMap<>();
        accuracyMeasures.put("Accuracy", accuracy);
        accuracyMeasures.put("Precision", precision);
        accuracyMeasures.put("Recall", recall);
        accuracyMeasures.put("F1-Score", f1score);
        confusion.put("True Positive: "+ positiveClass, tp);
        confusion.put("False Positive: "+ positiveClass, fp);
        confusion.put("True Negative: " + ((positiveClass.equals("EDIBLE")) ? "POISONOUS" : "EDIBLE"), tn);
        confusion.put("False Negative: " + ((positiveClass.equals("EDIBLE")) ? "POISONOUS" : "EDIBLE"), fn);

        return accuracyMeasures;
    }

}
