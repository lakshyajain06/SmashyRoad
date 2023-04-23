
public class vector {
	
	private double x, y;
	
	public vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double getMagnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	public void subtract(vector v2) {
		this.x -= v2.getX();
		this.y -= v2.getY();
	}
	public void add(vector v2) {
		this.x += v2.getX();
		this.y += v2.getY();
	}
	public void divide(double c) {
		this.x /= c;
		this.y/= c;
	}
	public void setVector(vector v2) {
		this.x = v2.getX();
		this.y = v2.getY();
	}
	public vector afterAdd(vector v2) {
		return new vector(this.x + v2.getX(), this.y + v2.getY());
	}
	public double getX() {
		return x;
	}
	public vector negativeVector() {
		return new vector(-1 * this.x, -1 * this.y);
	}
	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
}
