package Glex;

import java.util.ArrayList;

public class LexicalAnalyzer {
	
	private static int IDcode = 56;
	private static String[] reservedWords = { "class", "public", "protected",
			"private", "void", "static", "int", "char", "float", "double",
			"String", "if", "else", "do", "while", "try", "catch", "switch",
			"case" ,"for" };

	
	public char[] str2chars(String str){
		char[] chars = new char[100];
		str.getChars(0, str.length(), chars, 0);
		return chars;
	}
	
	 
	public ArrayList<Token> analyzeChars(char[] chars, ArrayList<Token> outlist){
		//the pointer of chars
		int pointer = 0;
		int charlen = chars.length;
		do{
			
			//1.get the first char
			char firstChar = chars[pointer];
			//2.1check whether it starts with character
//			System.out.println("firstchar: "+firstChar);
//			System.out.println("pointer: "+pointer);
			if((firstChar>='a'&&firstChar<='z')||(firstChar>='A'&&firstChar<='Z')) {
				//2.2check whether it is reserved words
				boolean isReserved = true;
				int tempPointer = 0;
				int reserveNumber = 0;
				String reserveWord = "";
				//2.2.1check each reserved word
				for (int i=0;i<reservedWords.length;i++) {
					String word = reservedWords[i];
					isReserved = true;
					tempPointer = pointer;
//					System.out.println("in this loop word is"+word+" and tempPointer is "+tempPointer);
					//2.2.2check each char in words
					if(tempPointer+word.length()<=(charlen-tempPointer)){
						//check each char
						for (char c : word.toCharArray()) {
							if (c != chars[tempPointer]) {								
//								System.out.println("c is "+c+" and i is "+tempPointer+"chars[i] is "+chars[tempPointer]);
								isReserved = false;
								break;
							}
							tempPointer++;
						}						
					}else {
						isReserved = false;
						continue;
					}
					if(isReserved){
						reserveNumber = i+1;
						reserveWord += word;
						break;
					}
				}
					if (isReserved) {
//						System.out.println("====this is reserved====");
						//add this reserved word into list							
						Token token = new Token(TokenType.RESERVED, reserveNumber, reserveWord);
						outlist.add(token);
						//to check next char
						pointer = tempPointer;
					}else{
						//this is ID, loop to check next char until it is neither char nor number
						ArrayList<Character> tempchars =  new ArrayList<Character>();
						char ch = firstChar;
						while (((ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z')||(ch>='0'&&ch<='9'))&&(pointer<charlen)) {
							tempchars.add(ch);
							pointer++;
							ch = chars[pointer];
						}
//						pointer--;
						//check if this ID already exists
						boolean isExist = false;
						for(Token t:outlist){
							if (String.valueOf(tempchars).equals(t.getName())) {
								isExist = true;
							}
						}
						//if this is a new ID, add IDcode
						if (!isExist) {
							IDcode++;
						}
						//add this ID into out list
						Token token = new Token(TokenType.ID, IDcode, String.valueOf(tempchars));
						outlist.add(token);
					}
					//TODO
//					System.out.println(isReserved);
				
				//2.3check number
			}else if(firstChar>='0'&&firstChar<='9'){
				ArrayList<Character> tempchars =  new ArrayList<Character>();
				//loop to check next char until it is not number
				char ch = firstChar;
				while ((ch>='0'&&ch<='9')&&(pointer<charlen)) {
					tempchars.add(ch);
					pointer++;
					ch = chars[pointer];
				}
//				pointer--;
				//add this ID into out list
				Token token = new Token(TokenType.NUM, 56, String.valueOf(tempchars));
				outlist.add(token);
				//2.4check operator and delimiter
			}else{
				int next = pointer+1;
				switch(firstChar){
				case '-':
					if(chars[next] == '='){
						Token token = new Token(TokenType.OPERATOR, 25, "-=");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 24, "-");
						outlist.add(token);
						pointer++;
					}				
					break;
				case '+':
					if(chars[next] == '='){
						Token token = new Token(TokenType.OPERATOR, 23, "+=");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 22, "+");
						outlist.add(token);
						pointer++;
					}
					break;
				case '*':
					if(chars[next] == '='){
						Token token = new Token(TokenType.OPERATOR, 27, "*=");
						outlist.add(token);
						pointer+=2;
					}else if(chars[next] == '/'){
						Token token = new Token(TokenType.DELIMITER, 44, "*/");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 26, "*");
						outlist.add(token);
						pointer++;
					}
					break;
				case '/':
					if(chars[next] == '='){
						Token token = new Token(TokenType.OPERATOR, 29, "/=");
						outlist.add(token);	
						pointer+=2;
					}else if(chars[next] == '*'){
						Token token = new Token(TokenType.DELIMITER, 43, "/*");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 28, "/");
						outlist.add(token);
						pointer++;
					}
					break;
				case '=':
//					System.out.println("===========");
					if(chars[next] == '='){
						Token token = new Token(TokenType.OPERATOR, 31, "==");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 30, "=");
						outlist.add(token);
						pointer++;
					}
					break;
				case '&':
					if(chars[next] == '&'){
						Token token = new Token(TokenType.OPERATOR, 33, "&&");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 32, "&");
						outlist.add(token);
						pointer++;
					}
					break;
				case '|':
					if(chars[next] == '|'){
						Token token = new Token(TokenType.OPERATOR, 35, "||");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 34, "|");
						outlist.add(token);
						pointer++;
					}
					break;
				case '!':
					if(chars[next] == '='){
						Token token = new Token(TokenType.OPERATOR, 37, "!=");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 36, "!");
						outlist.add(token);
						pointer++;
					}
					break;
				case '<':
					if(chars[next] == '='){
						Token token = new Token(TokenType.OPERATOR, 39, "<=");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 38, "<");
						outlist.add(token);
						pointer++;
					}
					break;
				case '>':
					if(chars[next] == '='){
						Token token = new Token(TokenType.OPERATOR, 41, ">=");
						outlist.add(token);	
						pointer+=2;
					}else{
						Token token = new Token(TokenType.OPERATOR, 40, ">");
						outlist.add(token);
						pointer++;
					}
					break;
				case '(':
					outlist.add(new Token(TokenType.DELIMITER, 45, "("));	
					pointer++;
					break;
				case ')':
					outlist.add(new Token(TokenType.DELIMITER, 46, ")"));	
					pointer++;
					break;
				case '[':
					outlist.add(new Token(TokenType.DELIMITER, 47, "["));	
					pointer++;
					break;
				case ']':
					outlist.add(new Token(TokenType.DELIMITER, 48, "]"));	
					pointer++;
					break;
				case '{':
					outlist.add(new Token(TokenType.DELIMITER, 49, "{"));	
					pointer++;
					break;
				case '}':
					outlist.add(new Token(TokenType.DELIMITER, 50, "}"));	
					pointer++;
					break;
				case ',':
					outlist.add(new Token(TokenType.DELIMITER, 51, ","));	
					pointer++;
					break;
				case ':':
					outlist.add(new Token(TokenType.DELIMITER, 52, ":"));	
					pointer++;
					break;
				case ';':
					outlist.add(new Token(TokenType.DELIMITER, 53, ";"));	
					pointer++;
					break;
				case '\'':
					outlist.add(new Token(TokenType.DELIMITER, 54, "'"));	
					pointer++;
					break;
				case '"':
					outlist.add(new Token(TokenType.DELIMITER, 55, "\""));	
					pointer++;
					break;
			    default:
					pointer++;
					break;
				}
			}
		}while(pointer<chars.length);
		return outlist;
	}
	
	
}
