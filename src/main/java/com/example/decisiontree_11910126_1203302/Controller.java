package com.example.decisiontree_11910126_1203302;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class Controller {

    public static Data fileReader(String path) throws IOException {
        Feature[] features = null;
        ArrayList<String[]> rows = new ArrayList<>();
        Data data = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String[] columns = reader.readLine().split(",");
            features = new Feature[columns.length];
            for (int i = 0; i < features.length; i++) {
                features[i] = new Feature(columns[i]);
            }
            data = new Data(features, rows);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length != columns.length) {
                    continue;
                }
                data.addRow(tokens);
            }
        }
        return data;
    }

    public static String printTree(Node node, int depth) {
        String space = "";
        StringBuilder s = new StringBuilder();
        String pre = (depth > 0) ? "↳" : "->";
        for (int i = 0; i < depth; i++) {
            space += "     ";
        }
        if (node.getSplitFeature() != null) {
            s.append(space).append(pre).append(node.getSplitFeature().getName())
                    .append(" InfoGain: ").append(node.getSplitFeature().infoGain)
                    .append(" Sample Size: ").append(node.getSampleSize())
                    .append("\n");
        } else {
            s.append(space).append(pre).append(node.getClassLabel())
                    .append(" Sample Size: ").append(node.getSampleSize())
                    .append("\n");
        }

        if (node.getBranches() != null) {
            for (String key : node.getBranches().keySet()) {
                s.append(space).append("  If ").append(key).append(":\n");
                s.append(printTree(node.getBranches().get(key), depth + 1));
            }
        }
        return s.toString();
    }

    public static void main(String[] args) throws IOException {
//        Data data = fileReader("C:/Users/Lenovo/Downloads/mushroom.csv");
//        System.out.println(data.rows.size());
//        int totalSize = data.getRows().size();
//        int trainSize = (int) (totalSize * 0.7);
//        //int testSize = totalSize - trainSize;
//
//        Random r = new Random(1234);
//        Collections.shuffle(data.getRows(), r);
//
//        ArrayList<String[]> trainRows = new ArrayList<>(data.getRows().subList(0, trainSize));
//        ArrayList<String[]> testRows = new ArrayList<>(data.getRows().subList(trainSize, totalSize));
//
//        Data trainData = new Data(data.getColumns(), trainRows);
//        Data testData = new Data(data.getColumns(), testRows);
//        Feature target = new Feature("EDIBLE");
//
//        SplitMethod splitMethod = new InformationGain();
//
//        DecisionTree decisionTree = new DecisionTree(trainData, target, splitMethod);
//        Node root = decisionTree.build(trainData, null, 0);
//        printTree(root, 0);
//
//        Map<String, Double> ev = decisionTree.evaluate(testData, root, target);
//        for (Map.Entry<String, Double> entry : ev.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
    }


}

