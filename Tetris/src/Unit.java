import java.awt.Color;
import java.awt.Graphics;


public class Unit {
	//出现的位置
	private int x,y;
	
	//大小
	public static final int SIZE = 30;
	//下落长度
	public static final int SPEED = 30;
	//停止状态
	public boolean stoped = false;
	
	//构造函数
	public Unit() {
		
	}
	public Unit(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.drawRect(x, y, SIZE, SIZE);
		g.setColor(c);
	}
	//Unit对象下落
	public void drop() {
		y += SPEED;
	}
	//检测当前位置在哪里
	public void changeStatus() {
		if(y + SIZE >= TetrisClient.CORRECT_Y + TetrisClient.GAME_HEIGHT) {
			stoped = true;
		}
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
