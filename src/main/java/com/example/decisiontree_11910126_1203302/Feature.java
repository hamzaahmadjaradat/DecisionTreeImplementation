package com.example.decisiontree_11910126_1203302;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Feature {
    public double infoGain;
    String name;

    public Feature(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Map<String, ArrayList<String[]>> extractSubsets(Data data, Feature feature) {
        Map<String, ArrayList<String[]>> sets = new HashMap<>();
        int index = data.getColumnIndex(feature.getName());
        for (String[] row: data.getRows()){
            String v = row[index];
            if(!sets.containsKey(v)){
                sets.put(v, new ArrayList<>());
                sets.get(v).add(row);
            }
            else {
                sets.get(v).add(row);
            }
        }
        return sets;
    }

    public double splitInfo(Data data, Feature feature){
        Map<String, ArrayList<String[]>> sets = extractSubsets(data,feature);
        double splitInfo =0;
        for (ArrayList<String[]> set: sets.values()) {
            double p = (double) set.size() / data.getRows().size();
            if (p >0) {
                splitInfo = p*(Math.log(p)) / Math.log(2) + splitInfo;

            }

        }
        return -splitInfo;
    }
}
