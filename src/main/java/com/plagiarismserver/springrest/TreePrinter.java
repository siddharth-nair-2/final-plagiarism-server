package com.plagiarismserver.springrest;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;

public class TreePrinter implements ParseTreeListener {
	@Override
	public void visitTerminal(TerminalNode terminalNode) {

	}

	@Override
	public void visitErrorNode(ErrorNode errorNode) {

	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		System.out.println("Node: " + ctx.getRuleContext().getClass().getSimpleName());
	}

	@Override
	public void exitEveryRule(ParserRuleContext parserRuleContext) {

	}

	public void printTree(ParseTree tree) {
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(this, tree);
	}

}
