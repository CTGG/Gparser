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
	ArrayList<State> states = new ArrayList<>();
	ArrayList<TableItem> list = new ArrayList<>();
	
	public ArrayList<LRitem> closure(ArrayList<LRitem> iterms) {
		ArrayList<LRitem> expanded = new ArrayList<>(iterms);
		boolean loop = true;
		while (loop) {
			int beforesize = expanded.size();
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
							newitem.setPredictor(first(beta,lRitem.getPredictor()));
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
			System.out.println("state "+newstaten);
			for (int i = 0; i < newlist.size(); i++) {
				System.out.println(newlist.get(i).getProductionWithDot()+" "+newlist.get(i).getPredictor());
			}
			System.out.println();
			return newstaten;
		}
		
	}
	
	private int getStateN(ArrayList<LRitem> list) {
		for (int i = 0; i < states.size(); i++) {
			boolean same = true;
            ArrayList<LRitem> existed = new ArrayList<>(states.get(i).getIterms());
            if (list.size() != existed.size()){
                same=false;
            }else {
                for(int j = 0;j<list.size();j++){
                    if (list.get(j).getProductionWithDot().equals(existed.get(j).getProductionWithDot())
                            && list.get(j).getPredictor().equals(existed.get(j).getPredictor())){
                    }else {
                        same = false;
                    }
                }
            }
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

	private String first(String beta,String a) {
        if (beta.length() == 0) {
            return a;
        }else {
            boolean nt = isNonTerminal(beta.charAt(0)+"");
            if (nt){
                String ret = "";
                Set<String> pros = getMatchPros(beta.charAt(0)+"");
                for (String s:pros){
                    String two[] = s.split(":");
                    ret+=first(two[1],a);
                }
                return ret;
            }else {
                return beta.charAt(0)+"";
            }
        }


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
        core = closure(core);
        s0.setIterms(core);
		states.add(s0);
		System.out.println("state 0");
		for (int i = 0; i < core.size(); i++) {
			System.out.println(core.get(i).getProductionWithDot()+" "+core.get(i).getPredictor());
		}
		System.out.println();
	}

	private void construct() {
		int n = 0;
		while(n<states.size()){
			State sn = states.get(n);
			ArrayList<String> sterms = getNexts(sn);
            ArrayList<LRitem> sta = sn.getIterms();
			//set action shift & goto
			setShiftAndGoto(sn, sterms);
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
				setIterm(state.getN(), column, "r"+(n-1));
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
			}else {
				setIterm(row, column, "S"+nextstate);
			}
		}
	}
	
	private void setIterm(int staten, String column, String value){
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
				if (terminals.contains(next)||nonTerminals.contains(next)) {
					terms.add(next);
				}

			}
			
		}
		return terms;
	}

	public static void main(String[] args) {
		ContructTable c = new ContructTable();
		c.init();
        State s0 = c.states.get(0);
		c.construct();
        for (TableItem item:c.list){
            System.out.println(item.getStaterow()+ " "+item.getColumn()+" "+item.getValue());
        }
	}
}
