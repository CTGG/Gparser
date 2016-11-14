package Glex;


public enum TokenType {
	RESERVED(1),ID(2),OPERATOR(3),DELIMITER(4),NUM(5);
	private int typevalue;
	private TokenType(int typevalue) {
		this.typevalue = typevalue;
	}
	public int getTypeValue() {
		return typevalue;
	}
	
}
