import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;


public class Unit {
	boolean appear = true;
	//出现的位置
	private int x,y;
	//TestrisClient 的引用
	private TetrisClient tc;
	//大小
	public static final int SIZE = 30;
	//下落长度
	public static final int SPEED = 30;
	//停止状态
	public boolean stoped = false;
	//shape的颜色
	private Color color = Color.BLACK;
	
	//构造函数
	public Unit(Color color,TetrisClient tc) {
		this.color = color;
		this.tc = tc;
		tc.us.add(this);
	}
	
	public Unit(Color color) {
		this.color = color;
	}
	
	public Unit(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public Unit(int x,int y,Color color) {
		this(x,y);
		this.color = color;
	}
	
	public Unit(int x, int y, boolean stoped,boolean appear,TetrisClient tc) {
		this(x,y);
		this.stoped = stoped;
		this.tc = tc;
		this.appear = appear;
		tc.us.add(this);
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(3.0f));
		g.drawRect(x, y, SIZE-7, SIZE-7);
		g.fillRect(x + 4, y + 4, SIZE - 14, SIZE - 14);
		g.setColor(c);
	}
	//Unit对象下落
	public void drop() {
		y += SPEED;
	}
	//检测当前状态
	public void changeStatus() {
		//停止条件1.碰触到底部边缘
//		if(y + SIZE >= TetrisClient.CORRECT_Y + TetrisClient.GAME_HEIGHT) {
//			stoped = true;
//		}
		//停止条件2.碰触到其他的停止的unit
		if(isHit()) {
			stoped = true;
		}
	}
	
	public boolean isHit() {
		// TODO Auto-generated method stub
		for(int i=0;i<tc.us.size();i++) {
			Unit u = tc.us.get(i);
			if(x == u.getX() && y + Unit.SIZE == u.getY() && u.stoped) {
				return true;
			}
		}
		return false;
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
	public void moveLeft() {
		// TODO Auto-generated method stub
		x -= SIZE;
	}
	public void moveRight() {
		// TODO Auto-generated method stub
		x += SIZE;
	}
	public boolean hitLeft() {
		// TODO Auto-generated method stub
//		if(x <= TetrisClient.CORRECT_X) return true;
//		return false;
		for(int i=0; i<tc.us.size();i++) {
			Unit u = tc.us.get(i);
			if(u.getX() == x - Unit.SIZE && u.getY() == y && u.stoped){
				return true;
			}
		}
		return false;
	}
	public boolean hitRight() {
		// TODO Auto-generated method stub
//		if(x >= TetrisClient.CORRECT_X + TetrisClient.GAME_WIDTH - Unit.SIZE) 
//			return true;
//		return false;
		for(int i=0; i<tc.us.size();i++) {
			Unit u = tc.us.get(i);
			if(u.getX() == x + Unit.SIZE && u.getY() == y && u.stoped){
				return true;
			}
		}
		return false;
	}
}
