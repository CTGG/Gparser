package LR1;

public class ParsingTable {
	String action[][] = {{"r3","r4","w"},
			{"w","w","r0"},
			{"s4","w","w"},
			{"w","s5","w"},
			{"w","r3","w"},
			{"r4","w","w"},
			{"w","s8","w"},
			{"s9","w","w"},
			{"w","w","r1"},
			{"w","w","r2"}};
	int GOTO[][] = {{1,2,3},
			{-1,-1,-1},
			{-1,-1,-1},
			{-1,-1,-1},
			{-1,6,-1},
			{-1,-1,7},
			{-1,-1,-1},
			{-1,-1,-1},
			{-1,-1,-1},
			{-1,-1,-1}};
	
	public int getColumn(String str) {
		switch (str) {
		case "a":
		case "S":
			return 0;
		case "b":
		case "A":
			return 1;
		case "$":
		case "B":
			return 2;
		default:
			return -1;
		}
	}

	public String getAction(int state,int column) {
		System.out.println(state+" "+column);
		return action[state][column];
	}
	public int getGOTO(int state,int column) {
		return GOTO[state][column];
	}

	

}

