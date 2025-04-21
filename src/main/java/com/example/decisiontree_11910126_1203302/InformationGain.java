package com.example.decisiontree_11910126_1203302;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InformationGain implements SplitMethod {

    @Override
    public double calculateGain(Data data, Feature feature, Feature target) {
        double targetEntropy = entropyTarget(data, target);
        double featureEntropy = entropyFeature(data, feature, target);
        return targetEntropy - featureEntropy;

    }

    public double entropyTarget(Data data, Feature target) {
        double ent = 0;
        Map<String, Integer> occurrences = new HashMap<>();
        int index = data.getColumnIndex(target.getName());

        for (String[] x : data.getRows()) {
            String colValue = x[index];
            if (!occurrences.containsKey(colValue)) {
                occurrences.put(colValue, 1);
            } else {
                occurrences.put(colValue, occurrences.get(colValue) + 1);
            }
        }

        for (int i = 0; i < occurrences.size(); i++) {
            int occ = (int) occurrences.values().toArray()[i];
            double p = (double) occ / data.getRows().size();
            if (p > 0) {
                ent = p * (Math.log(p) / Math.log(2)) + ent;
            }
        }
        return -ent;
    }

    public double entropyFeature(Data data, Feature feature, Feature target) {
        double ent = 0;
        Map<String, ArrayList<String[]>> sets = feature.extractSubsets(data, feature);
        for (ArrayList<String[]> set : sets.values()) {
            Data subData = new Data(data.getColumns(), set);
            double p = (double) set.size() / data.getRows().size();
            ent = p * entropyTarget(subData, target) + ent;
        }
        return ent;
    }


}
