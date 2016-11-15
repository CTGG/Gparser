package LR1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import Glex.Main;
import Glex.Token;
import Glex.TokenType;

public class Monitor {
	public Queue<String> intokens = new LinkedList<String>();
	public Stack<Integer> stateStack = new Stack<>();
	public Stack<String> symbolStack = new Stack<>();
	ParsingTable table = new ParsingTable();
	
	public void init(){
		Main main = new Main();
		String filePath = "src/input_LR1.txt";
		//init reader
		ArrayList<Token> tokens = main.getTokens(filePath);
		for(Token t:tokens){
			if (t.getType() == TokenType.ID) {
				String tString = t.getName();
				int len = tString.length();
				intokens.add(tString.substring(1, len-1));
			}else {
				intokens.add(t.getName());
			}
			
		}
		intokens.add("$");
		stateStack.push(0);
		symbolStack.push("$");
	}
	
	public void analyze() {
		while (true) {
			int state = stateStack.peek();
			String reader = intokens.peek();
			System.out.println(stateStack);
			System.out.println(symbolStack);
			String action = lookActionTable(state, reader);
			char firstchar = action.charAt(0);
			if (action.equals("r0")) {
				break;
			}
			switch (firstchar) {
			case 's':
				symbolStack.push(reader);
				stateStack.push(Integer.parseInt(action.substring(1)));
				intokens.remove();
				System.out.println("match: "+reader);
				break;
			case 'r':
				reduce(action);
			default:
				break;
			}
		}
		
	}
	
	private void reduce(String action) {
		switch (action) {
		case "r1":
			popSymbolStack(4);//pop AaAb
			while (lookGOTOTable(stateStack.peek(), "S")==-1) {
				stateStack.pop();
				
			}
			symbolStack.push("S");
			int state = stateStack.peek();
			String nonTerminal = "S";
			stateStack.push(lookGOTOTable(state, nonTerminal));
			System.out.println("S->AaAb");
			break;
		case "r2":
			popSymbolStack(4);
			while (lookGOTOTable(stateStack.peek(), "S")==-1) {
				stateStack.pop();
				
			}
			symbolStack.push("S");
			int state2 = stateStack.peek();
			stateStack.push(lookGOTOTable(state2, "S"));
			System.out.println("S->BaBb");
			break;
		case "r3":
			symbolStack.push("A");
			int state3 = stateStack.peek();
			stateStack.push(lookGOTOTable(state3, "A"));
			System.out.println("A->^");
			break;
		case "r4":
			symbolStack.push("B");
			int state4 = stateStack.peek();
			stateStack.push(lookGOTOTable(state4, "B"));
			System.out.println("B->^");
		default:
			System.out.println("reduce error");
			break;
		}
		
	}
	
	private void popSymbolStack(int n) {
		for (int i = 0; i < n; i++) {
			symbolStack.pop();
		}
	}

	public String lookActionTable(int state, String reader) {
		System.out.println("reader "+reader);
		int column = table.getColumn(reader);
		return table.getAction(state, column);
	}
	public int lookGOTOTable(int state, String nonTerminal) {
		int column = table.getColumn(nonTerminal);
		return table.getGOTO(state, column);
	}
	
	public static void main(String[] args) {
		Monitor monitor = new Monitor();
		monitor.init();
		monitor.analyze();
		System.out.println("LR1 Parsing completed");
	}
}
