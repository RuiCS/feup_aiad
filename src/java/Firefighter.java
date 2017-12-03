
public class Firefighter {

	public int x;
	public int y;
	public int tsX;
	public int tsY;
	
	public Firefighter() {
		this.x=0;
		this.y=0;
	}
	
	public Firefighter(int x, int y, int tableSizeX, int tableSizeY) {
		this.x=x;
		this.y=y;
		this.tsX=tableSizeX;
		this.tsY=tableSizeY;
	}
	
	public void move(int xInc, int yInc) {
		this.x += xInc;
		this.y += yInc;
		if (this.x < 0) this.x=0;
		if (this.y < 0) this.y=0;
		if (this.x >= this.tsX) this.x = this.tsX;
		if (this.y >= this.tsY) this.y = this.tsY;
	}
	
	public void moveUp() { this.move(0, -1); }
	public void moveDown() { this.move(0, 1); }
	public void moveLeft() { this.move(-1, 0); }
	public void moveRight() { this.move(1, 0); }
	
}
