package com.plagiarismserver.springrest;

import java.util.HashSet;
import java.util.Set;
import org.antlr.v4.runtime.tree.ParseTree;

public class JaccardSimilarity {
	public double jaccardSimilarity(Set<String> tree1, Set<String> tree2) {
		Set<String> union = new HashSet<>(tree1);
		union.addAll(tree2);

		Set<String> intersection = new HashSet<>(tree1);
		intersection.retainAll(tree2);
		// J(tree1, tree2) = intersection(tree1,tree2) / union(tree1,tree2)
		return (double) intersection.size() / union.size();
	}

	public Set<String> getFingerprint(ParseTree tree) {
		Set<String> fingerprint = new HashSet<>();

		for (int i = 0; i < tree.getChildCount(); i++) {
			ParseTree child = tree.getChild(i);
			fingerprint.add(child.getClass().getSimpleName() + "_" + child.getText());
			fingerprint.addAll(getFingerprint(child));
		}

		return fingerprint;
	}

	public double similarity(ParseTree tree1, ParseTree tree2) {
		Set<String> tree1fingerprint = getFingerprint(tree1);
		Set<String> tree2fingerprint = getFingerprint(tree2);

		return jaccardSimilarity(tree1fingerprint, tree2fingerprint);
	}
}