import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import Glex.Main;
import Glex.Token;
import Glex.TokenType;

public class Monitor {
	public Queue<String> intokens = new LinkedList<String>();
	public Stack<String> parsingStack = new Stack<>();
	ParsingTable table = new ParsingTable();
	//TODO
	String nonTerminals[] = {"E","E`","T","T`","F"};
	String reduce[]={"E->TE`","E`->+TE`","E`->^","T->FT`","","T`->^",""};
	
	public void init() {
		Main main = new Main();
		//init reader
		ArrayList<Token> tokens = main.getTokens();
		for(Token t:tokens){
			if (t.getType() == TokenType.ID) {
				intokens.add("ID");
			}else if (t.getType() == TokenType.NUM) {
				intokens.add("Num");
			}else {
				intokens.add(t.getName());
			}			
		}
		intokens.add("$");
		parsingStack.push("$");
		parsingStack.push("E");
	}
	
	public int lookUpTable(String stacktop, String reader){
		int row = table.getRow(stacktop);
		int column = table.getColumn(reader);
		return table.getReduceN(row, column);
	}
	
	private boolean isNonTerminal(String stacktop) {
		boolean res = false;
		for (String nter : nonTerminals) {
			if (nter.equals(stacktop)) {
				res = true;
				break;
			}
		}
		return res;
	}
	
	public void analyze() {	
		while (!intokens.isEmpty()) {
			String stacktop = parsingStack.peek();
			String reader = intokens.peek();
			boolean nonterminal =isNonTerminal(stacktop);
			if (nonterminal) {
				int production = lookUpTable(stacktop,reader);
				if (production>=0) {
					pushReduce(production);
				}else {
					System.out.println("error");
				}
			}else {
				if (stacktop.equals(reader)) {
					System.out.println("matched: "+reader);
					parsingStack.pop();
					intokens.remove();
				}else {
					System.out.println("error");
				}
			}
		}
		
		
	}

	private void pushReduce(int production) {
		//TODO
		switch (production) {
		case 0:
			//push reverse production
			parsingStack.add("");
			break;

		default:
			break;
		}
	}
	public static void main(String[] args) {
		Monitor monitor = new Monitor();
		monitor.init();
		monitor.analyze();
		System.out.println("Parsing completed");
	}
	

}
