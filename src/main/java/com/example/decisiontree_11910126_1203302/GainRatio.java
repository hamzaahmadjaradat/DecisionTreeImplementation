package com.example.decisiontree_11910126_1203302;


public class GainRatio implements SplitMethod {

    InformationGain infoGain = new InformationGain();

    @Override
    public double calculateGain(Data data, Feature feature, Feature target) {
        double informationGain = infoGain.calculateGain(data, feature, target);
        double splitInfo = feature.splitInfo(data, feature);
        if (splitInfo != 0) {
            return informationGain / splitInfo;
        }
        return 0;
    }
}

