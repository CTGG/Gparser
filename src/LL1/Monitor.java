package LL1;
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
	String nonTerminals[] = {"E","E`","T","T`","F"};
	String reduce[]={"E->TE`","E`->+TE`","E`->^","T->FT`","T`->*FT`","T`->^","F->I","F->(E)"};
	
	public void init() {
		Main main = new Main();
		String filePath = "src/input_LL1.txt";
		//init reader
		ArrayList<Token> tokens = main.getTokens(filePath);
		for(Token t:tokens){
//			if (t.getType() == TokenType.ID) {
//				intokens.add("ID");
//			}else if (t.getType() == TokenType.NUM) {
//				intokens.add("Num");
//			}else {
//				intokens.add(t.getName());
//			}
			if (t.getType() == TokenType.ID) {
				String tString = t.getName();
				int len = tString.length();
				intokens.add(tString.substring(1, len-1));
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
					break;
				}
			}else {
				if (stacktop.equals(reader)) {
					System.out.println("matched: "+reader);
					parsingStack.pop();
					intokens.remove();
				}else {
					System.out.println("error");
					break;
				}
			}
		}
		
		
	}

	private void pushReduce(int production) {
		switch (production) {
		case 0:
			//push reverse production
			parsingStack.pop();
			parsingStack.add("E`");
			parsingStack.add("T");
			System.out.println(reduce[0]);
			break;
		case 1:
			parsingStack.pop();
			parsingStack.add("E`");
			parsingStack.add("T");
			parsingStack.add("+");
			System.out.println(reduce[1]);
			break;
		case 2:
			parsingStack.pop();
			break;
		case 3:
			parsingStack.pop();
			parsingStack.add("T`");
			parsingStack.add("F");
			System.out.println(reduce[3]);
			break;
		case 4:
			parsingStack.pop();
			parsingStack.add("T`");
			parsingStack.add("F");
			parsingStack.add("*");
			System.out.println(reduce[4]);
			break;
		case 5:
			parsingStack.pop();
			break;
		case 6:
			parsingStack.pop();
			parsingStack.add("i");
			System.out.println(reduce[6]);
			break;
		case 7:
			parsingStack.pop();
			parsingStack.add(")");
			parsingStack.add("E");
			parsingStack.add("(");
			System.out.println(reduce[7]);
			break;
		default:
			System.out.println("REDUCE NUMBER ERROR");
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
