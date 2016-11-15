package LL1;
import java.util.Stack;

import javax.imageio.metadata.IIOInvalidTreeException;

public class ParsingTable {
	//get Tokens
	int table[][] = {{0,-1,-1,0,-1,-1},
					 {-1,1,-1,-1,2,2},
					 {3,-1,-1,3,-1,-1},
					 {-1,5,4,-1,5,5},
					 {6,-1,-1,7,-1,-1}};

	
	
	public int getRow(String nonTerminal){
		switch (nonTerminal) {
		case "E":
			return 0;
		case "E`":
			return 1;
		case "T":
			return 2;
		case "T`":
			return 3;
		case "F":
			return 4;
		default:
			return -1;
		}
	}
	
	public int getColumn(String tokenName){
		switch (tokenName) {
		case "i":
			return 0;
		case "+":
			return 1;
		case "*":
			return 2;
		case "(":
			return 3;
		case ")":
			return 4;
		case "$":
			return 5;
		default:
			return -1;
		}
	}
	
	public int getReduceN(int row, int column){
		return table[row][column];
	}
	
	

}
