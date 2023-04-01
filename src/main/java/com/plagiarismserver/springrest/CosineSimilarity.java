package com.plagiarismserver.springrest;

import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class CosineSimilarity {
    private Map<String, Integer> getFrequency(ParseTree tree) {
        Map<String, Integer> frequency = new HashMap<>();

        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            String key = child.getClass().getSimpleName() + "_" + child.getText();
            frequency.put(key, frequency.getOrDefault(key, 0) + 1);
            frequency.putAll(getFrequency(child));
        }

        return frequency;
    }

    private double cosineSimilarity(ParseTree a, ParseTree b) {
        Map<String, Integer> aFrequency = getFrequency(a);
        Map<String, Integer> bFrequency = getFrequency(b);

        //calculate the dot product of two vectors
        int dotProduct = 0;
        for (String key : aFrequency.keySet()) {
            dotProduct += aFrequency.get(key) * bFrequency.getOrDefault(key, 0);
        }

        double magnitudeA = 0;
        for (int value : aFrequency.values()) {
            magnitudeA += value * value;
        }
        magnitudeA = Math.sqrt(magnitudeA);

        double magnitudeB = 0;
        for (int value : bFrequency.values()) {
            magnitudeB += value * value;
        }
        magnitudeB = Math.sqrt(magnitudeB);

        double similarity = dotProduct / (magnitudeA * magnitudeB);

        return similarity;
    }

    public double similarity(ParseTree tree1, ParseTree tree2) {

        return cosineSimilarity(tree1, tree2);
    }
}
