import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class TetrisClient extends Frame{
	
	//level
	int level = 1;
	//GameOver
	boolean over = false;
	//暂停状态的标志
	boolean pause = false;
	int score = 0;
	//下一个图形对象
	Shape nextShape = null;
	
	public static Random r = new Random();
	//下一个shape对象
	int nextType;
	//下一个shape对象类型的变形次数
	int nextTypeChangeStep;
	
	//声明变量，窗口出现的位置
	int x = 300;
	int y = 100;
	//游戏窗体宽高
	public static final int WIDTH = 500;
	public static final int HEIGHT = 680;
	public static final int CORRECT_X = 20;
	public static final int CORRECT_Y = 50;
	//游戏区域大小
	public static final int GAME_WIDTH = 300;
	public static final int GAME_HEIGHT = 600;
	
	Image offScreenImage = null;
	
	//容器盛放unit对象
	List<Unit> us = new ArrayList<Unit>();
	
	//实例化一个Unit对象
	//Shape s = new Shape(CORRECT_X + 90,CORRECT_Y + 90, 0,this);
	
	//声明一个shape对象
	Shape s = null;
	
	public void lancher() {
		//窗口出现
		this.setLocation(x, y);
		//大小
		this.setSize(WIDTH,HEIGHT);
		//设置标题
		this.setTitle("Tetris Game");
		//不可以调节大小
		this.setResizable(false);
		//设置背景颜色
		this.setBackground(new Color(240,255,240));
		//添加窗口关闭
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				System.exit(0);
			}
			
		});
		//实例化一个Shape对象
		s = new Shape(CORRECT_X + 90,CORRECT_Y - 120, 2,this);
		nextType = r.nextInt(7);
		nextTypeChangeStep =r.nextInt(4);
		//实例化下一个图形对象
		nextShape = new Shape(355, CORRECT_Y +120 ,nextType,nextTypeChangeStep,this);
		//启动线程 
		new Thread(new paintThread()).start();
		//修改边缘机制--左右
		for(int i=0;i<21;i++) {
			new Unit(CORRECT_X - Unit.SIZE , CORRECT_Y + i * Unit.SIZE,true,false,this);
			new Unit(CORRECT_X + GAME_WIDTH , CORRECT_Y + i * Unit.SIZE,true,false,this);
		}
		//修改边缘机制--底部
		for(int i=0; i<10;i++) {
			new Unit(CORRECT_X + i *  Unit.SIZE , CORRECT_Y + GAME_HEIGHT,true,false,this);
		}
		//可见性 
		this.setVisible(true);
		//添加按键监控
		this.addKeyListener(new keyMonitor());
		
	}
	
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.black);
		g.drawRect(CORRECT_X-4, CORRECT_Y-4
				, GAME_WIDTH, GAME_HEIGHT);
		g.setColor(c);
		drawBackground(g);
		//check shape
		//s.draw(g);
		s.changeStatus();
		
		//画出所有存放到us里面的unit对象
		for(int i=0;i<us.size();i++) {
			Unit u = us.get(i);
			if(u.appear && u.getY() >= CORRECT_Y) {
				u.draw(g);
			}	
		}
		
		//得分面板
		Font f = g.getFont();
		g.setColor(Color.BLACK);
		g.setFont(new Font("宋体",Font.BOLD,20));
		g.drawString("SCORE:",330,80); 
		g.drawString(score + "",330,110);
		
		//下一个图形提示。
		g.drawString("NEXT:", 330, 140);
		//画出下一个图形。
		if(nextShape != null) {
			nextShape.draw(g);
		}
		
		//暂停状态显示
		if(pause) {
			g.setColor(Color.BLACK);
			g.drawString("PAUSE", 330, 360);
		}
		
		//游戏速度显示
		g.setColor(Color.BLACK);
		g.drawString("LEVEL:", 330, 310);
		g.drawString(level+"", 330, 340);
		
		//如果gameover打印字符串
		if(over) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("黑体",Font.BOLD,40));
			g.drawString("GAME OVER!", 100, 340);
		}
		g.setFont(f);
		
		
	}
	
    //刷新
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(WIDTH,HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(new Color(240,255,240));
		gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage,0,0,null);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TetrisClient().lancher();
	}

	private class paintThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) {
				repaint();
				//判断是否gameover
				if(!over) {

					if(s.stoped) {
						s = null;
						s = new Shape(CORRECT_X + 90,CORRECT_Y - 120,nextType,TetrisClient.this);
						nextType = r.nextInt(7);
						for(int i=0;i<nextTypeChangeStep;i++) {
							s.rotate();
						}
						nextTypeChangeStep = r.nextInt(4);
						
						if(nextShape == null) {
							nextShape = new Shape(380,CORRECT_Y + 120, nextType , nextTypeChangeStep, TetrisClient.this);
						}else {
							nextShape = null;
							nextShape = new Shape(380,CORRECT_Y + 120, nextType , nextTypeChangeStep, TetrisClient.this);
						}
					}
					//如果不是停止状态，或者是暂停状态。
					if(!s.stoped && !s.pause) s.drop();
				}
				
				
				try {
					//“下”键按下加速
					switch(score) {
					case 100:
						level = 2;
						break;
					case 200:
						level = 3;
						break;
					case 300:
						level = 4;
						break;
					case 400:
						level = 5;
						break;
					default:
						break;
					}
					if(!s.speedUp) {
						int paintSleepTime = 400;
						switch(level) {
						case 1:
							paintSleepTime = 400;
							break;
						case 2:
							paintSleepTime = 350;
							break;
						case 3:
							paintSleepTime = 300;
							break;
						case 4:
							paintSleepTime = 250;
							break;
						case 5:
							paintSleepTime = 200;
							break;
						default:
							break;
						}
						Thread.sleep(paintSleepTime);
					}
					Thread.sleep(20);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//按键事件的内部类
	private class keyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			s.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			s.KeyReleased(e);
		}
	}
	//画背景暗格。
	public void drawBackground(Graphics g) {
		Color c = g.getColor();
		g.setColor(new Color(220,220,220,220));
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(3.0f));
		
		for(int i=0; i<20; i++) {
			for(int j=0; j<10; j++) {
				g2.drawRect(CORRECT_X + j*Unit.SIZE, CORRECT_Y + i*Unit.SIZE, Unit.SIZE-7, Unit.SIZE-7);
				g.fillRect(CORRECT_X + j*Unit.SIZE + 4, CORRECT_Y + i*Unit.SIZE + 4, Unit.SIZE - 14, Unit.SIZE - 14);
			}
		}
		g.setColor(c);
	}
	
	//判断是否满行
	public boolean isFull(int height) {
		int count = 0;
		for(int i=0;i<us.size();i++) {
			Unit u = us.get(i);
			if(u.getY()==height && u.getY() != CORRECT_Y + GAME_HEIGHT) {
				count++;
			}
		}
		if(count==12) {
			return true;
		}else {
			return false;
		}
	}
	//满行时
	public void disappear(int height) {
		//拿到迭代器
		Iterator it = us.iterator();
		//循环对象
		while(it.hasNext()) {
			Unit u = (Unit)it.next();
			if(u.getY() == height && u.getX() > CORRECT_X && u.getX() < CORRECT_X + GAME_WIDTH) {
				it.remove();
			}
		}
		//得分
		score += 100;
	}
	//满行消除后下落
	public void reloadUnit(int height) {
		Iterator it = us.iterator();
		while(it.hasNext()) {
			Unit u = (Unit)it.next();
			if(u.getY() < height && u.getX() > CORRECT_X && u.getX() < CORRECT_X + GAME_WIDTH) {
				u.setY(u.getY() + Unit.SIZE);
			}
		}
	}
}

