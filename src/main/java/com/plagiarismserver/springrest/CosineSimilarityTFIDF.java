package com.plagiarismserver.springrest;

import java.util.HashMap;
import java.util.Map;

import com.plagiarismserver.springrest.antlr.antlrcpp.CPP14Lexer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;

public class CosineSimilarityTFIDF {
    private Map<String, Integer> getFrequency(ParseTree tree, Map<String, Integer> idf) {
        Map<String, Integer> frequency = new HashMap<>();
        double identifierWeight = 0.2; // lower weight for identifiers

        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            String key = child.getClass().getSimpleName() + "_" + child.getText();

            if (child instanceof TerminalNode) {
                if (((TerminalNode) child).getSymbol().getType() == CPP14Lexer.Identifier) {
                    // Use IDF to reduce the weight of Identifier nodes
                    double weight = 1 / Math.sqrt(idf.getOrDefault(child.getText(), 1)) * identifierWeight;
                    key = child.getClass().getSimpleName() + "_idf" + StringUtils.capitalize(child.getText());
                    frequency.put(key, frequency.getOrDefault(key, 0) + (int)weight);
                } else {
                    // Assign higher weight to other terminal nodes
                    frequency.put(key, frequency.getOrDefault(key, 0) + 3);
                }
            } else {
                // Recursively process non-terminal nodes
                frequency.put(key, frequency.getOrDefault(key, 0) + 1);
                frequency.putAll(getFrequency(child, idf));
            }
        }
        return frequency;
    }

    private double cosineSimilarity(ParseTree a, ParseTree b, Map<String, Integer> idf) {
        Map<String, Integer> aFrequency = getFrequency(a, idf);
        Map<String, Integer> bFrequency = getFrequency(b, idf);

        // calculate the dot product of two vectors
        double dotProduct = 0;
        for (String key : aFrequency.keySet()) {
            double aWeight = aFrequency.get(key) / (double) aFrequency.size();
            double bWeight = bFrequency.getOrDefault(key, 0) / (double) bFrequency.size();
            dotProduct += aWeight * bWeight;
        }

        double magnitudeA = 0;
        for (double value : aFrequency.values()) {
            magnitudeA += Math.pow(value / aFrequency.size(), 2);
        }
        magnitudeA = Math.sqrt(magnitudeA);

        double magnitudeB = 0;
        for (double value : bFrequency.values()) {
            magnitudeB += Math.pow(value / bFrequency.size(), 2);
        }
        magnitudeB = Math.sqrt(magnitudeB);

        double similarity = dotProduct / (magnitudeA * magnitudeB);

        return similarity;
    }

    public double similarity(ParseTree tree1, ParseTree tree2, Map<String, Integer> idf) {

        return cosineSimilarity(tree1, tree2, idf);
    }
}
