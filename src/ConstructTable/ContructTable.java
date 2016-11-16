package ConstructTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import Glex.IOHelper;

public class ContructTable {
	ArrayList<Production> grammars = new ArrayList<>();
	LRitem startPoint = new LRitem();
	Set<String> terminals = new HashSet<>();
	Set<String> nonTerminals = new HashSet<>();
	ArrayList<State> states = new ArrayList<>();
	ArrayList<TableItem> list = new ArrayList<>();
	
	public ArrayList<LRitem> closure(ArrayList<LRitem> iterms) {
		ArrayList<LRitem> expanded = new ArrayList<>(iterms);
		boolean loop = true;
		while (loop) {
			int beforesize = expanded.size();
            System.out.println();
			loop = false;
            ArrayList<LRitem> tempexpan = new ArrayList<>(expanded);
			for (LRitem lRitem : tempexpan) {
				String right = lRitem.getRight();
				int dot = getDotPosition(right);
				//dot at end, no expand
				if (dot == (right.length() - 1)) {
					continue;
				} else {
					String next;
					if ((dot + 2) <= (right.length() - 1)) {
						next = right.substring(dot + 1, dot + 2);
					} else {
						next = right.substring(dot + 1);
					}
					//if nonterminal, expand it
					if (isNonTerminal(next)) {
						loop = true;
						Set<String> matchedpros = new HashSet<>(getMatchPros(next));

						for (String production : matchedpros) {
							//search in grammars with char(0)=next
							String two[] = production.split(":");
							LRitem newitem = new LRitem();
							newitem.setLeft(two[0]);
							newitem.setRight("." + two[1]);
							String beta = "";
							if (right.length() > (dot + 2)) {
								beta = right.substring(dot + 2);
							}
							newitem.setPredictor(first(beta + lRitem.getPredictor()));
                            boolean isnewitem = true;
                            //check whether it is new
                            for(LRitem old:expanded){
                                if (old.getProductionWithDot().equals(newitem.getProductionWithDot())&&
                                        old.getPredictor().equals(newitem.getPredictor())){
                                    isnewitem = false;
                                }

                            }
                            if (isnewitem) {
                                expanded.add(newitem);
                            }
						}

					} else {
						//if terminal, no expand
						continue;
					}
				}
			}
            int aftersize = expanded.size();
            if (beforesize == aftersize) {
                loop = false;
            }
		}

		return expanded;
	}

	private int Shift(State state, String nonOrTer) {
		ArrayList<LRitem> list = new ArrayList<>(state.getIterms());
		ArrayList<LRitem> newlist = new ArrayList<>();
		for (LRitem lRitem : list) {
			String right = lRitem.getRight();
			int dot = getDotPosition(right);
			if (dot<(right.length()-1)) {
				String next;
				if ((dot+2)<=(right.length()-1)) {
					next = right.substring(dot+1, dot+2);
				}else {
					next = right.substring(dot+1);
				}
				if (next.equals(nonOrTer)) {
					String newright = right.substring(0,dot)+next+".";
					if ((dot+2)<=(right.length()-1)) {
						newright += right.substring(dot+2);
					}
					LRitem newLRitem = new LRitem();
					newLRitem.setLeft(lRitem.getLeft());
					newLRitem.setRight(newright);
					newLRitem.setPredictor(lRitem.getPredictor());
					newlist.add(newLRitem);
				}
			}
		}
		newlist = closure(newlist);
		//check whether this state is new state
		int staten = getStateN(newlist);
		if (staten >= 0) {
			return staten;
		}else {
			int newstaten = states.size();
			State newstate = new State();
			newstate.setIterms(newlist);
			newstate.setN(newstaten);
			states.add(newstate);
			return newstaten;
		}
		
	}
	
	private int getStateN(ArrayList<LRitem> list) {
		for (int i = 0; i < states.size(); i++) {
			boolean same = list.equals(states.get(i).getIterms());
			if (same) {
				return i;
			} 
		}
		return -1;
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
		startPoint.setLeft("G");
		startPoint.setRight(".E");
		startPoint.setPredictor("$");
		nonTerminals.add("G");
		nonTerminals.add("E");
		//init grammars
		Production firstpro = new Production();
		firstpro.setProduction("G:E");
		firstpro.setIndex(0);
		grammars.add(firstpro);
		int index = 1;
		for (String string : graStr) {
			System.out.println(string);
			Production production = new Production();
			production.setProduction(string);
			production.setIndex(index);
			grammars.add(production);
			index++;
			//init terminals and nonTer
			String[] two = string.split(":");

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
		terminals.add("$");
		//init state s0
		State s0 = new State();
		s0.setN(0);
		ArrayList<LRitem> core = new ArrayList<>();
		core.add(startPoint);
        System.out.println("core "+core.get(0).getProductionWithDot()+" "+core.get(0).getPredictor());
        core = closure(core);
        s0.setIterms(core);
		states.add(s0);
		
	}
	
//	
//	private void initTable() {
//		//init table items
//		for (int i = 0; i < states.size(); i++) {
//			for (String ter : terminals) {
//				TableItem item = new TableItem();
//				item.setStaterow(i);
//				item.setColumn(ter);
//				item.setValue("^");
//				list.add(item);
//			}
//		}
//		for (int i = 0; i < states.size(); i++) {
//			for (String nonter : nonTerminals) {
//				TableItem item = new TableItem();
//				item.setStaterow(i);
//				item.setColumn(nonter);
//				item.setValue("^");
//				list.add(item);
//			}
//		}
//	}
	private void construct() {
		//TODO need a loop of all states
//		State s0 = states.get(0);
//		ArrayList<String> sterms = getNexts(s0);
//		//set action shift & goto
//		setShiftAndGoto(s0, sterms);
//		//set r
//		setReduce(s0);
		int n = 0;
		while(n<states.size()){
			State sn = states.get(n);
			ArrayList<String> sterms = getNexts(sn);
            ArrayList<LRitem> sta = sn.getIterms();
            for (LRitem lRitem:sta){
                System.out.println(lRitem.getProductionWithDot()+" "+lRitem.getPredictor());
            }
			//set action shift & goto
			setShiftAndGoto(sn, sterms);
			//set r
			setReduce(sn);
			n++;
		}
		
	}
	
	private void setReduce(State state) {
		for (LRitem lRitem : state.getIterms()) {
			String right = lRitem.getRight();
			if (right.endsWith(".")) {
				//find the n of this reduce
				String column = lRitem.getPredictor();
				String reducep = lRitem.getProductionWithDot();
				reducep = reducep.substring(0, reducep.length()-1);
				int n = -1;
				for(int i = 0;i< grammars.size();i++){
					if (grammars.get(i).getProduction().equals(reducep)) {
						n = i;
					}
				}
				//set [state][column] = rn
				setIterm(state.getN(), column, "r"+n);
			}
			
		}
	}

	private void setShiftAndGoto(State state, ArrayList<String> nexts) {
		for (String next : nexts) {
			int nextstate = Shift(state, next);
			int row = state.getN();
			String column = next;
			if (isNonTerminal(next)) {
				setIterm(row, column, nextstate+"");
                System.out.println();
			}else {
				setIterm(row, column, "S"+nextstate);
			}
		}
	}
	
	private void setIterm(int staten, String column, String value){
//		int position = -1;
//		for(int i = 0; i < list.size();i++){
//			TableItem item = list.get(i);
//			if (item.getStaterow() == staten && item.getColumn().equals(column)) {
//				position = i;
//			}
//		}
//		//TODO to be tested whether it is a copy
//		list.get(position).setValue(value);
		TableItem item = new TableItem();
		item.setStaterow(staten);
		item.setColumn(column);
		item.setValue(value);
		list.add(item);
	}
	
	private ArrayList<String> getNexts(State state){
		ArrayList<String> terms = new ArrayList<>();
		for (LRitem lRitem : state.getIterms()) {
			String right = lRitem.getRight();
			int dot = getDotPosition(right);
			if (dot<(right.length()-1)) {
				String next;
				if ((dot+2)<=(right.length()-1)) {
					next = right.substring(dot+1, dot+2);
				}else {
					next = right.substring(dot+1);
				}
				if (terminals.contains(next)) {
					terms.add(next);
				}
			}
			
		}
		return terms;
	}
	
//	private HashMap<String,Integer> getShift(int staten){
//		//TODO
//		ArrayList<String> ters = new ArrayList<>();
//		State state = states.get(staten);
//		for (LRitem lRitem : state.getIterms()) {
//			String right = lRitem.getRight();
//			
//		}
//		return null;
//	}
	public static void main(String[] args) {
		ContructTable c = new ContructTable();
		c.init();
		System.out.println("init completed");
        State s0 = c.states.get(0);
        for(LRitem i : s0.getIterms()){
            System.out.println(i.getProductionWithDot()+" "+i.getPredictor());
        }
		c.construct();
        System.out.println(c.states.size());
        for (TableItem item:c.list){
            System.out.println(item.getStaterow()+ " "+item.getColumn()+" "+item.getValue());
        }
//		System.out.println("table");
	}
}
