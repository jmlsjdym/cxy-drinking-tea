package Games5;
import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PinBall
{
	// 桌面的宽度
	private final int TABLE_WIDTH = 1000;
	// 桌面的高度
	private final int TABLE_HEIGHT = 800;
	// 球拍的垂直位置
	private final int RACKET_Y = TABLE_HEIGHT-50;
	// 下面定义球拍的高度和宽度
	private final int RACKET_HEIGHT = 30;
	private final int RACKET_WIDTH = 200;
	// 小球的大小
	private final int BALL_SIZE = 24;
	private Frame f = new Frame("弹球游戏");
	Random rand = new Random();
	// 小球纵向的运行速度
	private int ySpeed = 30;
	// 返回一个-0.5~0.5的比率，用于控制小球的运行方向。
	private double xyRate = rand.nextDouble() - 0.5;
	// 小球横向的运行速度
	private int xSpeed = (int)(ySpeed * xyRate * 2);
	// ballX和ballY代表小球的坐标
	private int ballX = 100;
	private int ballY = RACKET_Y-BALL_SIZE*2;
	// racketX代表球拍的水平位置
	private int racketX = ballX+BALL_SIZE/2-RACKET_WIDTH/2;
	private MyCanvas tableArea = new MyCanvas();
	Timer timer;
	// 游戏是否结束的旗标
	private boolean isLose = false;
	// 游戏是否开始的旗标
	private boolean isStart = false;
	public void init()
	{
		//按下关闭按钮，退出程序
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// 设置桌面区域的最佳大小
		tableArea.setPreferredSize(
			new Dimension(TABLE_WIDTH , TABLE_HEIGHT));
		f.add(tableArea);
		// 定义键盘监听器
		KeyAdapter keyProcessor = new KeyAdapter()
		{
			public void keyPressed(KeyEvent ke)
			{
				// 按下向左、向右键时，球拍水平坐标分别减少、增加
				if (ke.getKeyCode() == KeyEvent.VK_LEFT)
				{
					if (racketX > 0)
					racketX -= 3*Math.abs(xSpeed);
				}
				if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					if (racketX < TABLE_WIDTH - RACKET_WIDTH)
					racketX += 3*Math.abs(xSpeed);
				}
			}
		};
		// 为窗口和tableArea对象分别添加键盘监听器
		f.addKeyListener(keyProcessor);
		tableArea.addKeyListener(keyProcessor);
		// 定义每0.1秒执行一次的事件监听器。
		ActionListener taskPerformer = evt ->
		{
			// 如果小球碰到左边边框
			if (ballX  <= 0 || ballX >= TABLE_WIDTH - BALL_SIZE)
			{
				xSpeed = -xSpeed;
			}
			// 如果小球高度超出了球拍位置，且横向不在球拍范围之内，游戏结束。
			if (ballY >= RACKET_Y - BALL_SIZE &&
				(ballX < racketX-BALL_SIZE || ballX > racketX + RACKET_WIDTH+BALL_SIZE))
			{
				timer.stop();
				// 设置游戏是否结束的旗标为true。
				isLose = true;
				tableArea.repaint();
			}
			// 如果小球位于球拍之内，且到达球拍位置，小球反弹
			else if (ballY  <= 0 ||
				(ballY >= RACKET_Y - BALL_SIZE
					&& ballX > racketX && ballX <= racketX + RACKET_WIDTH))
			{
				ySpeed = -ySpeed;
			}
			// 小球坐标增加
			if(isStart)
			{
				ballY += ySpeed;
				ballX += xSpeed;
			}
			tableArea.repaint();
		};
		timer = new Timer(50, taskPerformer);
		timer.start();
		
		// 添加鼠标事件监听器
		tableArea.addMouseListener(new MouseAdapter (){
			public void mousePressed(MouseEvent e) {
				if(!isStart) {
					isStart = true;
				}
			}
		});
		// 添加鼠标移动事件监听器
		tableArea.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(!isStart) {
					ballX=e.getX()-BALL_SIZE/2;
					racketX=e.getX()-RACKET_WIDTH/2;
				}
			}
		});
		f.pack();
		f.setVisible(true);
	}
	public static void main(String[] args)
	{
		new PinBall().init();
	}
	class MyCanvas extends Canvas
	{
		// 重写Canvas的paint方法，实现绘画
		public void paint(Graphics g)
		{
			// 如果游戏已经结束
			if (isLose)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("Times" , Font.BOLD, 30));
				g.drawString("游戏已结束！" , TABLE_WIDTH/2-80 ,TABLE_HEIGHT/2);
			}
			// 如果游戏还未结束
			else
			{
				// 设置颜色，并绘制小球
				g.setColor(new Color(230, 230, 80));
				g.fillOval(ballX , ballY , BALL_SIZE, BALL_SIZE);
				// 设置颜色，并绘制球拍
				g.setColor(new Color(80, 80, 200));
				g.fillRect(racketX , RACKET_Y
					, RACKET_WIDTH , RACKET_HEIGHT);
			}
		}
	}
}