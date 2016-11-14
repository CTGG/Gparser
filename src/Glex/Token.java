package Glex;


public class Token {
	private TokenType type;
	private int codenum;
	private String name;
	
	public Token(TokenType type, int codenum, String name) {
		this.type = type;
		this.codenum = codenum;
		this.name = name;
	}
	
	

	public TokenType getType() {
		return type;
	}



	public int getCodenum() {
		return codenum;
	}



	public String getName() {
		return name;
	}



	public static String toString(Token token){
		return "<"+token.getType()+" , "+token.getCodenum()+" , "+token.getName()+">";
	}
}
