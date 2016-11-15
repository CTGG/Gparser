package ConstructTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import Glex.IOHelper;

public class ContructTable {
	ArrayList<Production> grammars = new ArrayList<>();
	LRitem startPoint = new LRitem();
	Set<String> terminals = new HashSet<>();
	Set<String> nonTerminals = new HashSet<>();
	
	public ArrayList<LRitem> closure(ArrayList<LRitem> iterms) {
		ArrayList<LRitem> expanded = iterms;
		boolean loop = true;
		while(loop){
			loop = false;
			for (LRitem lRitem : iterms) {
				String right = lRitem.getRight();
				int dot = getDotPosition(right);
				//dot at end, no expand
				if (dot == (right.length()-1)) {
					continue;
				}else {
					String next = right.substring(dot+1, dot+2);
					//if nonterminal, expand it
					if (isNonTerminal(next)) {
						loop = true;
						Set<String> matchedpros = getMatchPros(next);
						for (String production : matchedpros) {
							//search in grammars with char(0)=next
							String two[] = production.split(":");
							LRitem newitem = new LRitem();
							newitem.setLeft(two[0]);
							newitem.setRight("."+two[1]);
							String beta = "";
							if (right.length()>(dot+2)) {
								beta = right.substring(dot+2);
							}
							newitem.setPredictor(first(beta+lRitem.getPredictor()));
							expanded.add(newitem);
						}					
					}else {
						//if terminal, no expand
						continue;
					}
				}
			}
		}
		
		return expanded;
	}
	
	private Set<String> getMatchPros(String next) {
		Set<String> matchs = new HashSet<>();
		for (Production string : grammars) {
			String pro = string.getProduction();
			if (pro.startsWith(next)) {
				matchs.add(pro);
			}
		}
		return matchs;
	}

	private String first(String string) {
		String notNull = string.replaceAll("^", "");
		return notNull.charAt(0)+"";
	}

	private boolean isNonTerminal(String next) {
		return nonTerminals.contains(next);
	}

	private int getDotPosition(String right) {
		return right.indexOf(".");
	}
	
	private void init(){
		ArrayList<String> graStr = IOHelper.readFile("src/grammar.txt");
		//init startpoint & nonterminals
		startPoint.setLeft("S`");
		startPoint.setRight(".S");
		startPoint.setPredictor("$");
		nonTerminals.add("S`");
		nonTerminals.add("S");
		//init grammars
		Production firstpro = new Production();
		firstpro.setProduction("S`:S");
		firstpro.setIndex(0);
		int index = 1;
		for (String string : graStr) {
			Production production = new Production();
			production.setProduction(string);
			production.setIndex(index);
			grammars.add(production);
			index++;
			//init terminals and nonTer
			String two[] = string.split(":");
			//left must be nonterminal
			String left = two[0];
			nonTerminals.add(left);
			//right contain non & terminals
			String right = two[1];
			for (char each : right.toCharArray()) {
				if ((each>='A')&&(each<='Z')) {
					nonTerminals.add(each+"");
				}else {
					terminals.add(each+"");
				}
			}
		}
		
		
	}
	public static void main(String[] args) {
		ContructTable c = new ContructTable();
		c.init();
		
	}
}
