package ConstructTable;

public class LRitem {
	private String left;
	private String right;
	private String predictor;
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	public String getPredictor() {
		return predictor;
	}
	public void setPredictor(String predictor) {
		this.predictor = predictor;
	}
	public String getProductionWithDot() {
		return left+":"+right;
	}
	
	
}
