package com.plagiarismserver.springrest;

import com.plagiarismserver.springrest.antlr.antlrcpp.CPP14Lexer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SourceCodePlagiarismDetection {
//	public static void main(String[] args) throws IOException {
//
//		// set the target directory
//		// File[] files = getFiles("dataset/CPP_2016/Z1/Z1");
//		File[] files = getFiles("SourceCodeExamples");
//
//		// generate output file
//		FileWriter jaccardFileWriter = new FileWriter("output/jaccard_results.txt", false);
//		FileWriter cosineFileWriter = new FileWriter("output/cosine_results.txt", false);
//
//		for (int i = 0; i < files.length; i++) {
//			File targetFile = files[i];
//			CharStream charStream = CharStreams.fromPath(Paths.get(targetFile.getPath()));
//			CPP14Lexer cpp14Lexer = new CPP14Lexer(charStream);
//			CommonTokenStream commonTokenStream = new CommonTokenStream(cpp14Lexer);
//			CPP14Parser cpp14Parser = new CPP14Parser(commonTokenStream);
//			ParseTree parseTree = cpp14Parser.translationUnit();
//			System.out.println("Target file: " + targetFile);
//
//			for (int j = i + 1; j < files.length; j++) {
//				File file = files[j];
//				System.out.println("Comparing to : " + file);
//				CharStream temp = CharStreams.fromString(null);
//				CharStream fileCharStream = CharStreams.fromPath(Paths.get(file.getPath()));
//				CPP14Lexer fileCpp14Lexer = new CPP14Lexer(fileCharStream);
//				CommonTokenStream fileCommonTokenStream = new CommonTokenStream(fileCpp14Lexer);
//				CPP14Parser fileCpp14Parser = new CPP14Parser(fileCommonTokenStream);
//				ParseTree fileParseTree = fileCpp14Parser.translationUnit();
//
//				DecimalFormat df = new DecimalFormat("#.##");
//
//				// Jaccard Similarity
//				// JaccardSimilarity treeSimilarity = new JaccardSimilarity();
//				// double similarityValue = treeSimilarity.similarity(parseTree, fileParseTree)
//				// * 100;
//				// System.out.println("The jaccard similarity of two source code is: " +
//				// df.format(similarityValue) + "%");
//
//				// Cosine Similarity
//				CosineSimilarity similarityCalculator = new CosineSimilarity();
//				double cosineDistance = similarityCalculator.similarity(parseTree, fileParseTree) * 100;
//				System.out.println("The cosine similarity of two source code is: " + cosineDistance + "%");
//
//				// TF-IDF Cosine Similarity
//				Map<String, Integer> idf;
//				idf = calculateIDF(parseTree);
//				idf = calculateIDF(fileParseTree);
//				// Calculate the similarity
//				CosineSimilarityTFIDF cosineSimilarityTFIDF = new CosineSimilarityTFIDF();
//				double similarity = cosineSimilarityTFIDF.similarity(parseTree, fileParseTree, idf) * 100;
//				System.out.println("The TF-IDF cosine similarity of two source code is: : " + similarity + "%");
//
//				// jaccardFileWriter.write(targetFile.getName() + " compared to " +
//				// file.getName() + ": " + df.format(similarityValue) + "%" + "\n");
//				cosineFileWriter.write(targetFile.getName() + " compared to " + file.getName() + ": "
//						+ df.format(cosineDistance) + "%" + "\n");
//
//			}
//			System.out.println("----------------------------------------------------------------------------");
//		}
//		jaccardFileWriter.close();
//		cosineFileWriter.close();
//
//		// print tree node
//		TreePrinter treePrinter = new TreePrinter();
//		// treePrinter.printTree(parseTree2);
//
//	}

	public static File[] getFiles(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		return files;
	}

	private static Map<String, Integer> calculateIDF(ParseTree tree) {
		Map<String, Integer> idf = new HashMap<>();
		Map<String, Boolean> alreadyCounted = new HashMap<>();

		// Count the number of documents containing each token
		for (int i = 0; i < tree.getChildCount(); i++) {
			ParseTree child = tree.getChild(i);
			String key = child.getClass().getSimpleName() + "_" + child.getText();

			if (child instanceof TerminalNode
					&& ((TerminalNode) child).getSymbol().getType() == CPP14Lexer.Identifier) {
				// Ignore duplicate identifiers in the same document
				if (alreadyCounted.containsKey(key)) {
					continue;
				}
				alreadyCounted.put(key, true);
				idf.put(key, idf.getOrDefault(key, 0) + 1);
			} else {
				calculateIDF(child);
			}
		}

		return idf;
	}

	public static void CPPFile(File[] files) throws IOException {

	}

	public static void CFile(File[] files) throws IOException {

	}

}
