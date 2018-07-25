import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shape {
	//游戏GameOver的标记。
	boolean over = false;
	//TetrisClient引用
	TetrisClient tc;
	int x,y;
	
	//pause状态
	boolean pause = false;
	
	//类型
	int type;
	
	//是否停止
	public boolean stoped = false;
	//图形颜色
	private Color color = null;
	//颜色数组
	public static Color[] colorArr = {
			Color.BLACK,
			Color.RED,
			new Color(255,20,147),
			Color.YELLOW,
			Color.BLUE,
			Color.GREEN,
			Color.ORANGE,
			
			};
	//随机类的对象
	int colorIndex = TetrisClient.r.nextInt(colorArr.length);
	
	int[][][] data = {
			{   //0.I
				{0,0,0,0},
				{0,0,0,0},
				{2,2,2,2},
				{0,0,0,0}
			},
			{   //1.田字
				{2,2},
				{2,2},
			},
			{   //2.L
				
				{0,2,0},
				{0,2,0},
				{0,2,2}
			},
			{   //3.反L
				{0,2,0},
				{0,2,0},
				{2,2,0}
			},
			{   //4.Z
				{2,2,0},
				{0,2,2},
				{0,0,0}
			},
			{   //5.反Z
				{0,2,2},
				{2,2,0},
				{0,0,0}
			},
			{   //6.品
				{0,2,0},
				{2,2,0},
				{0,2,0}
			},
	};
	List<Unit> units = new ArrayList<Unit>();
	//是否按“下”键了？
	boolean speedUp = false;
	//构造方法
	public Shape(int x,int y,int type,TetrisClient tc) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.tc = tc;
		//使用随机颜色
		this.color = colorArr[colorIndex];
		//实例化4个unit对象
		for(int i=0;i<4;i++) {
			units.add(new Unit(color,tc));
		}
		createByType();
	}
	
	public Shape(int x,int y,int type,int nextTypeChangeStep,TetrisClient tc) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.tc = tc;
		//使用随机颜色
		this.color = colorArr[colorIndex];
		//实例化4个unit对象
		for(int i=0;i<4;i++) {
			units.add(new Unit(Color.BLACK));
		}
		createByType();
	}
	
	private void createByType() {
		int count = 0;
		// TODO Auto-generated method stub
		for(int i=0;i<data[type].length;i++) {
			for(int j=0;j<data[type][i].length;j++) {
				if(data[type][i][j] == 2) {
					units.get(count).setX(x + j * Unit.SIZE);
					units.get(count).setY(y + i * Unit.SIZE);
					count ++;
				}
			}
		}
	}
	
	//画出形状
	public void draw(Graphics g) {
		g.setColor(color);
		for(int i=0; i<units.size();i++) {
			units.get(i).draw(g);
		}
	}
	
	//下落方法
	public void drop() {
		y += Unit.SPEED;
		for(int i=0; i<units.size();i++) {
			units.get(i).drop();
		}
	}
	//检测是否停止
	public void changeStatus() {
		//如果判断每一个unit有任意一个停止，就修改为停止状态。
		for(int i=0;i<units.size();i++) {
			Unit u = units.get(i);
			u.changeStatus();
			if(u.stoped) {
				stoped = true;
				//判断该Shape对象所影响的行，是否满
				for(int t=0;t<data[type].length;t++) {
					int temp_height = y + t * Unit.SIZE;
					if(tc.isFull(temp_height)) {
						tc.disappear(temp_height);
						tc.reloadUnit(temp_height);
					}
				}
				break;
			}
		}
		if(stoped) {
			for(int i=0;i<units.size();i++) {
				units.get(i).stoped = true;
			}
		}
		
		//判断游戏是否停止
		if(stoped && y <= tc.CORRECT_Y) {
			over = true;
			tc.over = true;
		}
	}
	//按键事件
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		//判断是否按下暂停键
		if(key == KeyEvent.VK_P) {
			pause = !pause; 
			tc.pause = !tc.pause;
		}
		if(pause) {
			return;
		}
		switch(key) {
		case KeyEvent.VK_LEFT:
			if(!hitLeft()) moveLeft();
			tc.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			if(!hitRight()) moveRight();
			tc.repaint();
			break;
		case KeyEvent.VK_DOWN:
			speedUp = true;
			tc.repaint();
			break;
		case KeyEvent.VK_UP:
			rotatePlus();
			tc.repaint();
			break;
		}
	}
	
	public void rotate() {
		// TODO Auto-generated method stub
		for(int i=0;i<units.size();i++) {
			Unit u = units.get(i);
			int old_x = u.getX();
			int old_y = u.getY();
			int times_x = (old_x - x) / Unit.SIZE;
			int times_y = (old_y - y) / Unit.SIZE;
			u.setX((data[type].length - 1 - times_y) * Unit.SIZE + x);
			u.setY(times_x * Unit.SIZE + y);
		}
		tc.repaint();
	}
	private void moveRight() {
		// TODO Auto-generated method stub
		//如果stoped直接返回不操作
		if(stoped) return;
		x += Unit.SIZE;
		for(int i=0;i<units.size();i++) {
			units.get(i).moveRight();
		}
	}
	private void moveLeft() {
		// TODO Auto-generated method stub
		//如果stoped直接返回不操作
		if(stoped) return;
		x -= Unit.SIZE;
		for(int i=0;i<units.size();i++) {
			units.get(i).moveLeft();
		}
	}
	//判断是否触碰了左右侧
	public boolean hitLeft() {
		boolean b = false;
		for(int i = 0;i<units.size();i++) {
			if(units.get(i).hitLeft()) {
				b = true;
				break;
			}
		}
		return b;
	}
	public boolean hitRight() {
		boolean b = false;
		for(int i = 0;i<units.size();i++) {
			if(units.get(i).hitRight()) {
				b = true;
				break;
			}
		}
		return b;
	}
	public void KeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			speedUp = false;
		}
	}
	
	//旋转方法
	public void rotatePlus() {
		//如果当前shape对象所在的数据没有和tc.us的Unit对象相交。
		if(!hasHit()) {
			rotate();
		}
		//如果相交了
		else {
			if(hitLeft() && hitRight()) {			//左右相邻
				return;
			}else if(hitLeft() && !hitRight()) {	//左边相邻
				moveRight();
				//移动后是否与其他相交。
				if(hitOtherStopedUnits()) {
					moveLeft();
				}else {
					rotate();
				}
			}else if(!hitLeft() && hitRight()) {	//右边相邻
				moveLeft();
				//移动后是否与其他相交。
				if(hitOtherStopedUnits()) {
					moveRight();
				}else {
					rotate();
				}
			}
		}
	}
	
	public boolean hitOtherStopedUnits() {
		// TODO Auto-generated method stub
		for(int i=0;i<units.size();i++) {
			Unit u = units.get(i);
			for(int j=0;j<tc.us.size();j++) {
				Unit t_u = tc.us.get(j);
				if(u.getX() == t_u.getX() && u.getY() == t_u.getY() && t_u.stoped) {
					return true;
				}
			}
		}
		return false;
	}
	
	//判断Shape对象是否与tc.us里面的对象相交
	public boolean hasHit() {
		// TODO Auto-generated method stub
		for(int i=0;i<tc.us.size();i++) {
			Unit u = tc.us.get(i);
			//拿到shape对象所在data数组中的位置进行对比
			for(int m=0;m<data[type].length;m++) {
				for(int n=0;n<data[type][m].length;n++) {
					if(u.getX() == x + n*Unit.SIZE && u.getY() == y + m * Unit.SIZE && u.stoped) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
