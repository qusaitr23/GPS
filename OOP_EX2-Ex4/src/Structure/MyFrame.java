package Structure;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.plaf.synth.SynthSpinnerUI;

import Algorithms.ShortestPathAlgo;
import File_format.Path2kml;
import Players.Fruit;
import Players.Game;
import Players.Pacman;

public class MyFrame extends JPanel implements MouseListener, MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static MenuBar _MB;
	private int H = 642; 
	private int y, x = 0;
	private int W = 1299;

	private boolean isPacman = false;
	private boolean isFruit = false;
	private boolean isSaved = false;
	private boolean isDemo = false;

	private static int i = 0;

	private File Load;
	private File Save;

	private Map _Map = new Map();

	private ArrayList<Fruit> _Fruits;
	private ArrayList<Pacman> _Pacmans;
	private ArrayList<Game> _List;


	private static void setMB(MenuBar menu) {
		_MB = menu;
	}

	private void setSave(File save) {
		this.Save = save;
	}

	private File getSave() {
		return this.Save;
	}


	private void setList(ArrayList<Game> list) {
		this._List = list;
	}

	public ArrayList<Game> getList(){
		return this._List;
	}

	public void setPList(ArrayList<Pacman> pList) {
		this._Pacmans = pList;
	}

	public void setFList(ArrayList<Fruit> fList) {
		this._Fruits = fList;
	}

	public ArrayList<Pacman> getPList(){
		return this._Pacmans;
	}

	public ArrayList<Fruit> getFList(){
		return this._Fruits;
	}

	public static MenuBar getMB() {
		return _MB;
	}

	public MyFrame() {
		initGUI();
		this.addMouseListener(this); 
	}

	private void initGUI() {
		_Fruits = new ArrayList<Fruit>();
		_Pacmans = new ArrayList<Pacman>();
		_List = new ArrayList<Game>();

		MenuBar menuBar = new MenuBar();
		Menu menu1 = new Menu("File");
		MenuItem open = new MenuItem("   Open File...   ");
		MenuItem save = new MenuItem("   Save ");
		Menu menu2 = new Menu("Edit");
		MenuItem clear = new MenuItem("   Clear   ");
		MenuItem here = new MenuItem("   New   ");
		MenuItem pacman = new MenuItem("   Pacman   ");
		MenuItem fruit = new MenuItem("   Fruit   ");
		Menu menu3 = new Menu("Demo");
		MenuItem run = new MenuItem("   Run   ");
		MenuItem demo = new MenuItem("   Demo   ");
		MenuItem kml = new MenuItem("   Convert to KML   ");

		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);

		menu1.add(here);
		here.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isPacman = true;
				isFruit = true;
				paintElement();
				paintElement();
				isPacman = false;
				isFruit = false;
				open.setEnabled(true);
				here.setEnabled(false);
				pacman.setEnabled(true);
				fruit.setEnabled(true);
				clear.setEnabled(true);
				save.setEnabled(true);
			}
		});
		here.setEnabled(true);

		menu1.add(open);
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				run.setEnabled(true);
				demo.setEnabled(true);
				_Pacmans.clear();
				_Fruits.clear();
				_List.clear();
				Game Try = new Game(new File("/home/eli/eclipse-workspace/OOP_EX2-EX4-master/newdata/game_1543685769754.csv"));
				//Game Try = new Game(Load);
				ShortestPathAlgo Name = new ShortestPathAlgo(Try.read());
				Set(Name);
				repaint();
			}
		});
		open.setEnabled(false);

		menu1.add(save);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isPacman = false;
				isFruit = false;
				isSaved = true;
				setList(Save());
				//setSave(new File(""));
				run.setEnabled(true);
				demo.setEnabled(true);
				kml.setEnabled(true);
			}
		});
		save.setEnabled(false);

		menu2.add(pacman);
		pacman.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isPacman = true;
				isFruit = false;
			}
		});
		pacman.setEnabled(false);

		menu2.add(fruit);
		fruit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isFruit = true;
				isPacman = false;
			}
		});
		fruit.setEnabled(false);

		menu2.add(clear);
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_Pacmans.clear();
				_Fruits.clear();
				_List.clear();
				isSaved = false;
				paint(getGraphics());
				isPacman = false;
				isFruit = false;
			}
		});
		clear.setEnabled(false);

		menu3.add(run);
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isSaved = true;
				Paths(getGraphics());
				run.setEnabled(false);
				demo.setEnabled(false);
				repaint();
			}
		});
		run.setEnabled(false);

		menu3.add(demo);
		demo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)  {
				isDemo = true;
				isPacman = false;
				isFruit = false;
				isSaved = false;
				run.setEnabled(false);
				Calculate();
				Path p = new Path();
				ArrayList<Path> pList = new ArrayList<Path>();
				pList = p.Create(getList(), getPList());
				Change();
				while((pList = p.Print(pList)).size() > 0) {
					for(int i=0; i<_Pacmans.size(); i++) {
						for(int j=0; j<pList.size(); j++) {
							if(_Pacmans.get(i).getiD().equals(pList.get(j).getList().get(0).getiD())) {
								_Pacmans.get(i).setPoint(pList.get(j).getList().get(0).getPoint());
							}
						}
					}
					Change();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					paintComponent(getGraphics());
				}
				demo.setEnabled(false);
			}
		});
		demo.setEnabled(false);

		menu3.add(kml);
		kml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		kml.setEnabled(false);

		setMB(menuBar);
	}
	
	public void Change() {
		for(int i=0; i<_Pacmans.size(); i++) {
			for(int j=0; j<_Fruits.size(); j++) {
				String[] Data1 = _Pacmans.get(i).getPoint().split(",");
				String[] Data2 = _Fruits.get(j).getPoint().split(",");
				int pX = (int)(Double.parseDouble(Data1[0]));
				int pY = (int)(Double.parseDouble(Data1[1]));
				
				int fX = (int)(Double.parseDouble(Data2[0]));
				int fY = (int)(Double.parseDouble(Data2[1]));
				if(pX == fX && pY == fY) {
					_Fruits.get(j).setPicture("Done");
				}
			}
		}
	}

	public void Set(ShortestPathAlgo spa){
		setPList(spa.getPList());
		setFList(spa.getFList());

		setList(Save());
		setList(_Map.ConvertPoints2Pixel(getList()));

		ShortestPathAlgo test = new ShortestPathAlgo(getList());

		setPList(test.getPList());
		setFList(test.getFList());
	}

	public void Calculate() {
		ShortestPathAlgo Test = new ShortestPathAlgo(getList());
		ArrayList<Game> it = Test.Calculate2Pixel();
		setList(it);
	}
	public void Paths(Graphics g) {
		Calculate();
		ArrayList<Game> it = getList();
		g.setColor(Color.WHITE);
		for(int i=0; i<it.size(); i+=2) {
			String[] Data1 = it.get(i).getPoint().split(",");
			String[] Data2 = it.get(i+1).getPoint().split(",");

			int x1 = (int)((Double.parseDouble(Data1[0])) * this.getWidth() / W);
			int y1 = (int)((Double.parseDouble(Data1[1])) * this.getHeight() / H);

			int x2 = (int)((Double.parseDouble(Data2[0])) * this.getWidth() / W);
			int y2 = (int)((Double.parseDouble(Data2[1])) * this.getHeight() / H);

			Image img = Toolkit.getDefaultToolkit().getImage("/home/eli/eclipse-workspace/OOP_EX2-EX4-master/newdata/Done.png");

			g.drawImage(img, x2-16, y2-16, null);
			g.drawLine(x1, y1, x2, y2);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(_Map.getImage(), 0 , 0 , this.getWidth() , this.getHeight() , this);

		Image Apple = Toolkit.getDefaultToolkit().getImage("/home/eli/eclipse-workspace/OOP_EX2-EX4-master/newdata/Apple.png");
		Image Pacman = Toolkit.getDefaultToolkit().getImage("/home/eli/eclipse-workspace/OOP_EX2-EX4-master/newdata/Pacman.png");
		Image img = Toolkit.getDefaultToolkit().getImage("/home/eli/eclipse-workspace/OOP_EX2-EX4-master/newdata/Done.png");
		
		String Done = "Done";
		for(int i=0; i<_Fruits.size(); i++) {
			String[] Data = _Fruits.get(i).getPoint().split(",");
			int x2 = (int)((Double.parseDouble(Data[0])) * this.getWidth() / W);
			int y2 = (int)((Double.parseDouble(Data[1])) * this.getHeight() / H);
			g.drawImage(Apple, x2-16, y2-16, this);
			g.setFont(new Font("Monospaced", Font.PLAIN, 14));  
			g.setColor(Color.WHITE);
			g.drawString("("+String.valueOf(x2)+","+String.valueOf(y2)+")", x2, y2); 
		}
		for(int i=0; i<_Pacmans.size(); i++) {
			String[] Data = _Pacmans.get(i).getPoint().split(",");
			int x1 = (int)((Double.parseDouble(Data[0])) * this.getWidth() / W);
			int y1 = (int)((Double.parseDouble(Data[1])) * this.getHeight() / H);
			g.drawImage(Pacman, x1-16, y1-16, this);
			g.setFont(new Font("Monospaced", Font.PLAIN, 14));  
			g.setColor(Color.WHITE);
			g.drawString("("+String.valueOf(x1)+","+String.valueOf(y1)+")", x1, y1); 
		}

		if(isSaved == true) {
			for(int i=0; i<_List.size(); i+=2) {
				g.setColor(Color.WHITE);
				String[] Data1 = _List.get(i).getPoint().split(",");
				String[] Data2 = _List.get(i+1).getPoint().split(",");

				int x1 = (int)((Double.parseDouble(Data1[0])) * this.getWidth() / W);
				int y1 = (int)((Double.parseDouble(Data1[1])) * this.getHeight() / H);

				int x2 = (int)((Double.parseDouble(Data2[0])) * this.getWidth() / W);
				int y2 = (int)((Double.parseDouble(Data2[1])) * this.getHeight() / H);

				//Image img = Toolkit.getDefaultToolkit().getImage("/home/eli/eclipse-workspace/OOP_EX2-EX4-master/newdata/Done.png");

				g.drawImage(img, x2-16, y2-16, null);
				g.drawLine(x1, y1, x2, y2);
			}
			for(int i=0; i<_Pacmans.size(); i++) {
				String[] Data = _Pacmans.get(i).getPoint().split(",");
				int x1 = (int)((Double.parseDouble(Data[0])) * this.getWidth() / W);
				int y1 = (int)((Double.parseDouble(Data[1])) * this.getHeight() / H);
				g.drawImage(Pacman, x1-16, y1-16, this);
				g.setFont(new Font("Monospaced", Font.PLAIN, 14));  
				g.setColor(Color.WHITE);
				g.drawString("("+String.valueOf(x1)+","+String.valueOf(y1)+")", x1, y1); 
			}
		}
		
		if(isDemo == true) {
			for(int i=0; i<_Fruits.size(); i++) {
				if(_Fruits.get(i).getPicture().equals(Done)) {
					String[] Data = _Fruits.get(i).getPoint().split(",");
					int x2 = (int)((Double.parseDouble(Data[0])) * this.getWidth() / W);
					int y2 = (int)((Double.parseDouble(Data[1])) * this.getHeight() / H);
					g.drawImage(img, x2-16, y2-16, this);
				}
			}
		}
	}

	public static void main(String[] args){
		JFrame frame = new JFrame("Game GUI");
		frame.getContentPane().add(new MyFrame());
		frame.setMenuBar(_MB);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1299, 697);
		frame.setResizable(true);
		frame.setVisible(true);
	}

	public long TimeStamp() {
		return new Date().getTime() + 7200000;
	}

	protected void paintElement() {
		Graphics g = getGraphics();
		//	The method getGraphics is called to obtain a Graphics object
		Image Apple = Toolkit.getDefaultToolkit().getImage("/home/eli/eclipse-workspace/OOP_EX2-EX4-master/newdata/Apple.png");
		Image Pacman = Toolkit.getDefaultToolkit().getImage("/home/eli/eclipse-workspace/OOP_EX2-EX4-master/newdata/Pacman.png");

		x = (int)(x * W / this.getWidth());
		y = (int)(y * H / this.getHeight());

		if(isPacman == true && g.drawImage(Pacman, x-16, y-16, this) == true){
			g.setFont(new Font("Monospaced", Font.PLAIN, 14));  
			g.setColor(Color.WHITE);
			g.drawString("("+Integer.toString(x)+","+Integer.toString(y)+")",x, y);
			_Pacmans.add(new Pacman("Pacman", x+","+y+","+"0", "1", "1", "Pacman", String.valueOf(i)));
			i++;
		} 
		if(isFruit == true && g.drawImage(Apple, x-16, y-16, this) == true){
			g.setFont(new Font("Monospaced", Font.PLAIN, 14));  
			g.setColor(Color.WHITE);
			g.drawString("("+Integer.toString(x)+","+Integer.toString(y)+")",x, y);
			_Fruits.add(new Fruit("Fruit", x+","+y+","+"0", "Apple"));
		}
		repaint();
	}

	public ArrayList<Game> Save(){
		for(int i=0; i<_Pacmans.size(); i++) {
			Game temp = new Game(_Pacmans.get(i));
			_List.add(temp);
		}
		for(int i=0; i<_Fruits.size(); i++) {
			Game temp = new Game(_Fruits.get(i));
			_List.add(temp);
		}
		return _List;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
	@Override
	public void mouseClicked(MouseEvent e) {
		x = e.getX();
		y = e.getY();  
		paintElement();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		paintComponent(_Map.getImage().createGraphics().create(0, 0, e.getX(), e.getY()));
		H = this.getHeight();
		W = this.getWidth();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}