package com.plagiarismserver.springrest.controller;

import org.springframework.web.bind.annotation.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.plagiarismserver.springrest.antlr.antlrcpp.CPP14Lexer;
import com.plagiarismserver.springrest.antlr.antlrcpp.CPP14Parser;
import com.plagiarismserver.springrest.antlr.antlrc.CLexer;
import com.plagiarismserver.springrest.antlr.antlrc.CParser;
import com.plagiarismserver.springrest.antlr.antlrjava.JavaLexer;
import com.plagiarismserver.springrest.antlr.antlrjava.JavaParser;
import com.plagiarismserver.springrest.antlr.antlrpython.PythonLexer;
import com.plagiarismserver.springrest.antlr.antlrpython.PythonParser;

import com.plagiarismserver.springrest.CosineSimilarityTFIDF;
import com.plagiarismserver.springrest.model.Plagiarism;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

@RestController
@RequestMapping("/plagiarism")
public class PlagiarismController {
	private List<Plagiarism> submissions = new ArrayList<Plagiarism>();

	@CrossOrigin
	@RequestMapping()
	public List<Plagiarism> getStudentDetails() {

		return submissions;
	}

	@CrossOrigin
	@PostMapping
	public ArrayList<String> addComparisons(@RequestBody Plagiarism content) {

		// Generate parse tree base on the type of the source code
		ParseTree parseTree = getParseTree(content.getAnswer(), content.getLanguageName());

		Plagiarism[] otherContent = content.getOtherSubmissions();
		ArrayList<String> resultStringList = new ArrayList<String>();
		System.out.println("Target student: " + content.getStudentName());

		for (Plagiarism singleItem : otherContent) {

			System.out.println("Comparing to : " + singleItem.getStudentName());
			// Generate others' parse tree
			ParseTree otherParseTree = getParseTree(singleItem.getAnswer(), singleItem.getLanguageName());

			Map<String, Integer> idf;
			idf = calculateIDF(parseTree);
			idf = calculateIDF(otherParseTree);
			// Calculate the similarity
			CosineSimilarityTFIDF cosineSimilarityTFIDF = new CosineSimilarityTFIDF();
			double similarity = cosineSimilarityTFIDF.similarity(parseTree, otherParseTree, idf) * 100;
			System.out.println("The TF-IDF cosine similarity of two source code is: : " + similarity + "%");

			String jsonInputStringMain = "{\"courseID\": \"" + content.getCourseID() + "\"" + ",\"assignmentID\": \""
					+ content.getAssignmentID() + "\"" + ",\"questionID\": \"" + content.getQuestionID()
					+ "\",\"questionNum\": " + content.getQuestionNum() + ",\"languageName\": \""
					+ content.getLanguageName() + "\",\"student1Name\": \"" + content.getStudentName()
					+ "\",\"student_1_ID\": \"" + content.getStudentID() + "\",\"student2Name\": \""
					+ singleItem.getStudentName() + "\",\"student_2_ID\": \"" + singleItem.getStudentID()
					+ "\",\"similarity\": " + ((int) (similarity * 100) / 100.0) + "}";

			try {
				URL url = new URL("https://auto-grader.cyclic.app/api/tracker/createComparison");
				HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
				//HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);

				try (OutputStream os = con.getOutputStream()) {
					byte[] input = jsonInputStringMain.getBytes("utf-8");
					os.write(input, 0, input.length);
				}

				try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
					StringBuilder response = new StringBuilder();
					String responseLine = null;
					while ((responseLine = br.readLine()) != null) {
						response.append(responseLine.trim());
					}
					System.out.println(response.toString());
					resultStringList.add(response.toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resultStringList;
	}

	private ParseTree getParseTree(String sourceCode, String language) {
		CharStream charStream = CharStreams.fromString(sourceCode);

		switch (language) {
		case "54":
			CPP14Lexer cpp14Lexer = new CPP14Lexer(charStream);
			CPP14Parser cpp14Parser = new CPP14Parser(new CommonTokenStream(cpp14Lexer));
			return cpp14Parser.translationUnit();
		case "50":
			CLexer cLexer = new CLexer(charStream);
			CParser cParser = new CParser(new CommonTokenStream(cLexer));
			return cParser.translationUnit();
		case "62":
			JavaLexer javaLexer = new JavaLexer(charStream);
			JavaParser javaParser = new JavaParser(new CommonTokenStream(javaLexer));
			return javaParser.compilationUnit();
		case "71":
			PythonLexer pythonLexer = new PythonLexer(charStream);
			PythonParser pythonParser = new PythonParser(new CommonTokenStream(pythonLexer));
			return pythonParser.file_input();
		default:
			return null;
		}

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
}
