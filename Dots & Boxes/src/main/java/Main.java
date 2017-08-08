import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

import javax.swing.JRadioButton;

import org.jpl7.Query;
import org.jpl7.Term;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;

import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JLabel;

public class Main {

	private JFrame frmDotsBoxes;
	private final Action easyAction = new EasyAction();
	JLabel lblScore;
	JLabel lblComputer;
	private JRadioButton grid[][];
	JButton btnLeave ;
	private boolean line[][];
	private BufferedImage backDraw;
	private int gWidth;
	private int gHeight;
	private int userWins;
	private int computerWins;
	private final int VALID = 3;
	private final int NOT_VALID = 4;
	private final int WIN = 5;
	private final int WIN2 = 6;
	private final int FIRST = 7;
	private int maxLevel;
	private boolean waive;
	private final Action mediumAction = new MediumAction();
	private final Action hardAction = new HardAction();
	private final Action exitAction = new ExitAction();
	private final PointAction pointAction = new PointAction();
	private final Action sacrificeAction = new SacrificeAction();
	private final Action expertAction = new ExpertAction();

	/**
	 * Launch the application.
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmDotsBoxes.setLocationRelativeTo(null);
					window.frmDotsBoxes.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {

		frmDotsBoxes = new JFrame();
		frmDotsBoxes.setTitle("Dots & Boxes");
		frmDotsBoxes.setResizable(false);
		backDraw = new BufferedImage(352, 390, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = backDraw.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 352, 392);
		frmDotsBoxes.setContentPane(new DrawPane(backDraw));
		frmDotsBoxes.getContentPane().setBackground(Color.WHITE);
		frmDotsBoxes.setForeground(Color.BLACK);
		frmDotsBoxes.setBackground(Color.WHITE);
		frmDotsBoxes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDotsBoxes.getContentPane().setLayout(null);
		grid = new JRadioButton[6][6];
		line = new boolean[37][37];
		userWins=0;
		computerWins=0;
		waive=false;
		grid[0][0] = new JRadioButton("");
		grid[0][0].setBounds(40, 60, 20, 20);
		grid[0][0].setBackground(Color.WHITE);
		grid[0][0].setAction(pointAction);
		grid[0][0].setOpaque(false);
		frmDotsBoxes.getContentPane().add(grid[0][0]);
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 410, 21);
		frmDotsBoxes.getContentPane().add(menuBar);
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnNew = new JMenu("     New       ");
		mnFile.add(mnNew);

		JMenuItem mntmEasy = new JMenuItem("     Easy         ");
		mntmEasy.setAction(easyAction);
		mnNew.add(mntmEasy);

		JMenuItem mntmMedium = new JMenuItem("     Medium         ");
		mntmMedium.setAction(mediumAction);
		mnNew.add(mntmMedium);

		JMenuItem mntmHard = new JMenuItem("      Hard         ");
		mntmHard.setAction(hardAction);
		mnNew.add(mntmHard);
		
		JMenuItem mntmExpert = new JMenuItem("      Expert");
		mntmExpert.setAction(expertAction);
		mnNew.add(mntmExpert);

		JMenuItem mntmExit = new JMenuItem("     Exit         ");
		mntmExit.setAction(exitAction);
		mnFile.add(mntmExit);
		
		btnLeave = new JButton("Leave Turn");
		btnLeave.setAction(sacrificeAction);
		btnLeave.setBounds(61, 227, 103, 23);
		frmDotsBoxes.getContentPane().add(btnLeave);
		
		lblScore = new JLabel("You : 0 ");
		lblScore.setBounds(5, 22, 65, 14);
		frmDotsBoxes.getContentPane().add(lblScore);
		
		lblComputer = new JLabel("Computer : 0");
		lblComputer.setBounds(139, 22, 85, 14);
		frmDotsBoxes.getContentPane().add(lblComputer);
		for (int i = 0; i < 6; ++i) {
			line[i][0] = false;
			if (i != 0) {
				grid[i][0] = new JRadioButton("");
				grid[i][0].setBounds(grid[i - 1][0].getBounds().x, grid[i - 1][0].getBounds().y + 60, 20, 20);
				grid[i][0].setBackground(Color.WHITE);
				grid[i][0].setAction(pointAction);
				;
				grid[i][0].setOpaque(false);
				frmDotsBoxes.getContentPane().add(grid[i][0]);
			}
			for (int j = 1; j < 6; ++j) {
				grid[i][j] = new JRadioButton("");
				grid[i][j].setBounds(grid[i][j - 1].getBounds().x + 60, grid[i][j - 1].getBounds().y, 20, 20);
				grid[i][j].setBackground(Color.WHITE);
				grid[i][j].setAction(pointAction);
				grid[i][j].setOpaque(false);
				frmDotsBoxes.getContentPane().add(grid[i][j]);
				line[i][j] = false;
			}
			
			
		}
		maxLevel=1;
		frmDotsBoxes.setBounds(100, 100, 230, 290);
		frmDotsBoxes.setLocationRelativeTo(null);
		gWidth = 3;
		gHeight = 3;
		for (int i = 0; i < 5; ++i) {
			int j = 0;
			if (i < 3)
				j = 3;
			for (; j < 5; ++j) {
				grid[i][j].setVisible(false);
			}

		}
	}

	private class PointAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			JRadioButton jr = (JRadioButton) e.getSource();
			if (!jr.isSelected())
				return;
			int result = play(jr, Color.BLUE);
			if (result == NOT_VALID){
				jr.setSelected(false);
			}
			else if(result==WIN2){
				userWins+=2;
				lblScore.setText("You : "+userWins);
			}
			else if(result==WIN){
				userWins++;
				lblScore.setText("You : "+userWins);
			}

			if(userWins+computerWins>=(gWidth-1)*(gHeight-1)){
				if(userWins>computerWins)
					JOptionPane.showMessageDialog(null, "You Win");
				else if(userWins<computerWins)
					JOptionPane.showMessageDialog(null, "You Lose");
				else
					JOptionPane.showMessageDialog(null, "Draw !");
				btnLeave.setEnabled(false);

				return;
			}
			
			else if (result == VALID)
				computerTurn();
		}

		public void computerTurn() {
			Query.hasSolution("consult('dots&boxes.pl')");
			
			int result=WIN;
			while(result==WIN){
				String State="([],[";
				for(int i=0;i<gHeight-1;++i){
					for(int j=0;j<gWidth-1;++j){
						if(line[i*gWidth+j][(i+1)*gWidth+j])
							State+="[1,";
						else 
							State+="[0,";
						
						if(line[i*gWidth+j+1][(i+1)*gWidth+j+1])
								State+="1,";
							else 
								State+="0,";
						
						if(line[i*gWidth+j][i*gWidth+j+1])
							State+="1,";
						else 
							State+="0,";
						
						if(line[(i+1)*gWidth+j][(i+1)*gWidth+j+1])
								State+="1]";
						else 
							State+="0]";
						
						if(i<gHeight-2||j<gWidth-2)
							State+=",";
						
					}
				}
				State+="])";
				String query=State+","+maxLevel+",NState,Desired";
				int iX=0,iY=0,jX = 0,jY=0;
				Map<String, Term> p = Query.oneSolution("run("+query+")");
				
				Term NState[]=p.get("NState").toTermArray();
				for(int i=0,c=0;i<NState.length;++i){

					if(!line[i+c][i+c+gWidth]&&NState[i].toTermArray()[0].intValue()==1)
					{
						iX=(i+c)/gWidth;
						jX=(i+c)%gWidth;
						iY=(i+c+gWidth)/gWidth;
						jY=(i+c+gWidth)%gWidth;
						break;

						
					}
					else if(!line[i+c+1][i+1+c+gWidth]&&NState[i].toTermArray()[1].intValue()==1)
					{
						iX=(i+c+1)/gWidth;
						jX=(i+c+1)%gWidth;
						iY=(i+c+1+gWidth)/gWidth;
						jY=(i+c+1+gWidth)%gWidth;
						break;
						
					}
					
					else if(!line[i+c][i+c+1]&&NState[i].toTermArray()[2].intValue()==1)
					{
						iX=(i+c)/gWidth;
						jX=(i+c)%gWidth;
						iY=(i+c+1)/gWidth;
						jY=(i+c+1)%gWidth;
						break;
						
					}
					
					else if(!line[i+c+gWidth][i+c+1+gWidth]&&NState[i].toTermArray()[3].intValue()==1)
					{
						iX=(i+c+gWidth)/gWidth;
						jX=(i+c+gWidth)%gWidth;
						iY=(i+c+1+gWidth)/gWidth;
						jY=(i+c+1+gWidth)%gWidth;
						break;
						
					}
					if(i>0&&(i+1)%(gWidth-1)==0)
						c++;
					
				}
				if(waive){
					if(p.get("Desired").intValue()==0){
						JOptionPane.showMessageDialog(frmDotsBoxes, "Computer Refused !");
						return;
					}
					waive=false;
				}
				else{
					if(p.get("Desired").intValue()==0){
						if(JOptionPane.showConfirmDialog(frmDotsBoxes,"Computer Wants to Leave his Turn , Do you Agree ?")==JOptionPane.YES_OPTION){
							return;
						}
					}
				}
				
				JRadioButton jr = grid[iX][jX];
				jr.setSelected(true);
				play(jr, Color.RED);
				jr = grid[iY][jY];
				jr.setSelected(true);
				
				result=play(jr, Color.RED);
				if(result==WIN){
					computerWins++;
					lblComputer.setText("Computer : "+computerWins);

				}
				else if(result==WIN2){
					computerWins+=2;
					lblComputer.setText("Computer : "+computerWins);

				}
				if(userWins+computerWins>=(gWidth-1)*(gHeight-1)){
					if(userWins>computerWins)
						JOptionPane.showMessageDialog(null, "You Win");
					else if(userWins<computerWins)
						JOptionPane.showMessageDialog(null, "You Lose");
					else
						JOptionPane.showMessageDialog(null, "Draw !");
					btnLeave.setEnabled(false);
					break;
				}
			}
		}

		private int play(JRadioButton jr,  Color color) {
			int dx[] = { 1, -1, 0, 0 }, dy[] = { 0, 0, 1, -1 };
			int cnt = 0;
			for (int i = 0; i < gHeight; ++i) {
				for (int j = 0; j < gWidth; ++j) {
					if (grid[i][j].isSelected())
						cnt++;
					if (jr.equals(grid[i][j])) {
						for (int k = 0; k < 4; ++k) {

							if (i + dx[k] >= 0 && i + dx[k] < gHeight && j + dy[k] >= 0 && j + dy[k] < gWidth) {
								JRadioButton tmp = grid[i + dx[k]][j + dy[k]];
								if (tmp.isSelected()) {
									if (line[(i + dx[k]) * gWidth + j + dy[k]][i * gWidth + j])
										return NOT_VALID;
									tmp.setSelected(false);
									jr.setSelected(false);
									line[(i + dx[k]) * gWidth + j + dy[k]][i * gWidth + j] = true;
									line[i * gWidth + j][(i + dx[k]) * gWidth + j + dy[k]] = true;
									int x1 = tmp.getBounds().x, y1 = tmp.getBounds().y, x2 = jr.getBounds().x,
											y2 = jr.getBounds().y;
									draw(new Point(x1 + 8, y1 + 8), new Point(x2 + 8, y2 + 8), color);
									frmDotsBoxes.getContentPane().repaint();
									int ret = VALID;
									if (y1 == y2) {
										if (i - 1 >= 0) {
											if (line[(i - 1) * gWidth + j][(i - 1) * gWidth + j + dy[k]] 
													&& line[(i - 1) * gWidth + j + dy[k]][(i) * gWidth + j
															+ dy[k]] 
													&& line[(i) * gWidth + j][(i) * gWidth + j + dy[k]] 
													&& line[(i) * gWidth + j][(i - 1) * gWidth + j] ) {
												if (x1 < x2) {
													draw(new Point(grid[i - 1][j].getBounds().x,
															grid[i][j + dy[k]].getBounds().y),
															new Point(grid[i][j + dy[k]].getBounds().x + 16,
																	grid[i - 1][j].getBounds().y + 16),
															color);

												} else {
													draw(new Point(grid[i - 1][j].getBounds().x + 16,
															grid[i - 1][j].getBounds().y + 16),
															new Point(grid[i][j + dy[k]].getBounds().x,
																	grid[i][j + dy[k]].getBounds().y),
															color);

												}
												if(ret==WIN)
													ret = WIN2;
												else 
													ret=WIN;

											}
										}
										if (i + 1 < gHeight) {
											if (line[(i + 1) * gWidth + j][(i + 1) * gWidth + j + dy[k]] 
													&& line[(i + 1) * gWidth + j + dy[k]][(i) * gWidth + j
															+ dy[k]] 
													&& line[(i) * gWidth + j][(i) * gWidth + j + dy[k]] 
													&& line[(i) * gWidth + j][(i + 1) * gWidth + j] ) {
												if (x1 < x2) {
													draw(new Point(grid[i][j].getBounds().x,
															grid[i + 1][j + dy[k]].getBounds().y),
															new Point(grid[i + 1][j + dy[k]].getBounds().x + 16,
																	grid[i][j].getBounds().y + 16),
															color);

												} else {
													draw(new Point(grid[i][j].getBounds().x + 16,
															grid[i][j].getBounds().y + 16),
															new Point(grid[i + 1][j + dy[k]].getBounds().x,
																	grid[i + 1][j + dy[k]].getBounds().y),
															color);

												}
												if(ret==WIN)
													ret = WIN2;
												else 
													ret=WIN;

											}
										}

									}

									else {
										if (j - 1 >= 0) {

											if (line[(i + dx[k]) * gWidth + j][(i + dx[k]) * gWidth + j - 1] 
													&& line[(i + dx[k]) * gWidth + j - 1][(i) * gWidth + j - 1] 
													&& line[(i) * gWidth + j][(i) * gWidth + j - 1] 
													&& line[(i) * gWidth + j][(i + dx[k]) * gWidth + j] ) {
												if (y1 < y2) {
													draw(new Point(grid[i][j - 1].getBounds().x + 16,
															grid[i + dx[k]][j - 1].getBounds().y + 16),
															new Point(grid[i + dx[k]][j].getBounds().x,
																	grid[i][j].getBounds().y),
															color);

												} else {
													draw(new Point(grid[i][j - 1].getBounds().x + 16,
															grid[i][j].getBounds().y + 16),
															new Point(grid[i + dx[k]][j].getBounds().x,
																	grid[i + dx[k]][j - 1].getBounds().y),
															color);

												}
												if(ret==WIN)
													ret = WIN2;
												else 
													ret=WIN;

											}
										}
										if (j + 1 < gWidth) {
											if (line[(i + dx[k]) * gWidth + j][(i + dx[k]) * gWidth + j + 1] 
													&& line[(i + dx[k]) * gWidth + j + 1][(i) * gWidth + j + 1] 
													&& line[(i) * gWidth + j][(i) * gWidth + j + 1] 
													&& line[(i) * gWidth + j][(i + dx[k]) * gWidth + j] ) {
												if (y1 < y2) {
													draw(new Point(grid[i][j].getBounds().x + 16,
															grid[i + dx[k]][j].getBounds().y + 16),
															new Point(grid[i + dx[k]][j + 1].getBounds().x,
																	grid[i][j + 1].getBounds().y),
															color);

												} else {
													draw(new Point(grid[i][j].getBounds().x + 16,
															grid[i][j + 1].getBounds().y + 16),
															new Point(grid[i + dx[k]][j + 1].getBounds().x,
																	grid[i + dx[k]][j].getBounds().y),
															color);

												}
												if(ret==WIN)
													ret = WIN2;
												else 
													ret=WIN;
											}
										}

									}

									frmDotsBoxes.getContentPane().repaint();
									return ret;
								}
							}
						}
					}
				}
			}
			if (cnt == 1)
				return FIRST;
			else
				return NOT_VALID;
		}

		private void draw(Point p1, Point p2, Color color) {
			int width = p2.x - p1.x;
			int height = p2.y - p1.y;
			Graphics2D g = backDraw.createGraphics();
			g.setColor(color);
			if (width < 0 || height < 0)
				g.fillRect(p2.x, p2.y, Math.abs(width) + 5, Math.abs(height) + 5);
			else
				g.fillRect(p1.x, p1.y, Math.abs(width) + 5, Math.abs(height) + 5);
		}
	}

	//@wbp.parser.entryPoint
	private class EasyAction extends AbstractAction {
		public EasyAction() {
			putValue(NAME, "     Easy         ");
			putValue(SHORT_DESCRIPTION, "Easy Game");

		}

		public void actionPerformed(ActionEvent e) {
			frmDotsBoxes.dispose();
			initialize();
			frmDotsBoxes.setVisible(true);
			
		}
	}

	private class MediumAction extends AbstractAction {
		public MediumAction() {
			putValue(NAME, "     Medium         ");
			putValue(SHORT_DESCRIPTION, "Medium Game");
		}

		public void actionPerformed(ActionEvent e) {
			frmDotsBoxes.dispose();
			initialize();
			frmDotsBoxes.setVisible(true);
			frmDotsBoxes.setBounds(100, 100, 293, 350);
			frmDotsBoxes.setLocationRelativeTo(null);
			gWidth = 4;
			gHeight = 4;
			maxLevel=3;
			lblComputer.setBounds(290-lblComputer.getBounds().width,lblComputer.getBounds().y,lblComputer.getBounds().width,lblComputer.getBounds().height);
			btnLeave.setBounds(89, 285, 103, 23);

			for (int i = 0; i < 6; ++i) {

				grid[i][3].setVisible(true);
				grid[i][4].setVisible(false);
			}
			for (int i = 0; i < 6; ++i) {
				if (i == 4)
					continue;
				grid[3][i].setVisible(true);
				grid[4][i].setVisible(false);
			}
			
		}
	}

	private class HardAction extends AbstractAction {
		public HardAction() {
			putValue(NAME, "      Hard         ");
			putValue(SHORT_DESCRIPTION, "Hard Game");
		}

		public void actionPerformed(ActionEvent e) {
			frmDotsBoxes.dispose();
			initialize();
			frmDotsBoxes.setVisible(true);
			frmDotsBoxes.setBounds(100, 100, 352, 410);
			frmDotsBoxes.setLocationRelativeTo(null);
			gWidth = 5;
			gHeight = 5;
			maxLevel=5;
			lblComputer.setBounds(349-lblComputer.getBounds().width,lblComputer.getBounds().y,lblComputer.getBounds().width,lblComputer.getBounds().height);

			btnLeave.setBounds(120, 345, 103, 23);
		
			for (int i = 0; i < 6; ++i) {
				grid[i][3].setVisible(true);
				grid[i][4].setVisible(true);
				grid[i][5].setVisible(false);
			}
			for (int i = 0; i < 6; ++i) {
				if (i == 5)
					continue;
				grid[3][i].setVisible(true);
				grid[4][i].setVisible(true);
				grid[5][i].setVisible(false);
			}
		}
	}
	
	private class ExpertAction extends AbstractAction {
		public ExpertAction() {
			putValue(NAME, "      Expert");
			putValue(SHORT_DESCRIPTION, "Expert");
		}
		public void actionPerformed(ActionEvent e) {
			frmDotsBoxes.dispose();
			initialize();
			frmDotsBoxes.setVisible(true);
			frmDotsBoxes.setBounds(100, 100, 410, 470);
			frmDotsBoxes.setLocationRelativeTo(null);
			gWidth = 6;
			gHeight = 6;
			maxLevel=7;
			lblComputer.setBounds(407-lblComputer.getBounds().width,lblComputer.getBounds().y,lblComputer.getBounds().width,lblComputer.getBounds().height);
			btnLeave.setBounds(148, 405, 103, 23);
			for (int i = 0; i < 6; ++i) {
				for (int j = 0; j < 6; ++j) {
					grid[i][j].setVisible(true);
				}
			}
		}
	}

	private class ExitAction extends AbstractAction {
		public ExitAction() {
			putValue(NAME, "     Exit");
			putValue(SHORT_DESCRIPTION, "Exit");
		}

		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	class DrawPane extends JPanel {
		BufferedImage image;

		public DrawPane(BufferedImage i) {
			image = i;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this);
		}
	}
	private class SacrificeAction extends AbstractAction {
		public SacrificeAction() {
			putValue(NAME, "Leave Turn");
			putValue(SHORT_DESCRIPTION, "Leave Turn");
		}
		public void actionPerformed(ActionEvent e) {
			waive=true;
			pointAction.computerTurn();
			waive=false;

		}
	}
}
