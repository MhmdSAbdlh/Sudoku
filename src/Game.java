import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class Game extends JFrame implements ActionListener, KeyListener{

	//Style
	Font numberFont = new Font("Tahoma", Font.BOLD, 20);
	Font legendFont = new Font("Tahoma", Font.BOLD, 30);
	Font buttonFont = new Font("Tahoma", Font.BOLD, 15);
	Font smallFont = new Font("Tahoma", Font.BOLD, 12);
	Color greyC = new Color(0xDBD0C0);
	Color whiteC = new Color(0xEDEDED);
	Color pencilC = new Color(0x444444);
	Color blackC = new Color(0x171717);
//	Color shadowC = new Color(0xc0b7aa);
	Border panelBorder = new MatteBorder(2, 2, 2, 2, blackC);
	Border smallBorder = new MatteBorder(1, 1, 1, 1, blackC);
	Border btnBorder = BorderFactory.createLineBorder(pencilC, 2, true);
	
	JPanel bigP = new JPanel(new GridLayout(3, 3, 0, 0));
	JPanel numP = new JPanel(new GridLayout(1, 9));
	JPanel[] panel = new JPanel[9];
	JButton square[][] = new JButton[9][9];
	JButton numB[] = new JButton[9];
	JLabel numIndex[] = new JLabel[9];
	ButtonGroup levelBtn = new ButtonGroup();
	JRadioButton easyL = new JRadioButton("Easy");
	JRadioButton medL = new JRadioButton("Medium");
	JRadioButton hardL = new JRadioButton("Hard");
	Random randomNum = new Random();
	JButton undoBtn = new JButton("UNDO");
	JButton newGame = new JButton("NEW GAME");
	JButton deleteBtn = new JButton("DELETE");
	JButton checkBtn = new JButton("CHECK");
	JButton restartBtn = new JButton("RESTART");
	JLabel mistakeCountL = new JLabel("");
	JLabel levelLabel = new JLabel("LEVEL");
	String tempString = "";
	int numbers[][] = new int[9][9];
	int temp[] = new int[9];
	int lastI =-1, lastJ =-1;
	int mistake = 0;
	int level = 40;
	boolean isSelected = false;
	URL url = getClass().getResource("sudoku.png");
	ImageIcon icon = new ImageIcon(url);
	
	Game(){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 620);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(whiteC);
		this.addKeyListener(this);
		this.setTitle("SUDOKU");
		this.setIconImage(icon.getImage());

		fill(numbers);
		//Panel
		bigP.setBounds(5, 5, 500, 500);
		numP.setBounds(5, 525, 500, 50);
		for(int i=0; i<9;i++) {
			numB[i] = new JButton(i+1+"");
			numB[i].setFocusable(false);
			numB[i].addActionListener(this);
			numB[i].setFont(legendFont);
			numB[i].setBackground(greyC);
			numB[i].setForeground(blackC);
			numB[i].setBorder(panelBorder);
			numP.add(numB[i]);
			numIndex[i] = new JLabel();
			numIndex[i].setFont(smallFont);
			numIndex[i].setForeground(pencilC);
			numIndex[i].setBackground(greyC);
			numIndex[i].setOpaque(true);
			numIndex[i].setBorder(smallBorder);
			numIndex[i].setHorizontalAlignment(0);
			numIndex[i].setBounds(47+i*55, 510, 15, 15);
			this.add(numIndex[i]);
		}
		for(int i=0; i<9; i++) {
			panel[i] = new JPanel(new GridLayout(3,3,0,0));
			panel[i].setBorder(panelBorder);
			bigP.add(panel[i]);
		}
		//Initialize the buttons
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				square[i][j] = new JButton("");
				square[i][j].setFocusable(false);
				square[i][j].setBorder(new LineBorder(pencilC, 1));
				square[i][j].addActionListener(this);
				square[i][j].setBackground(whiteC);
				square[i][j].setFont(numberFont);
				if(i<3 && j<3)
					panel[0].add(square[i][j]);
				else
					if(i<3 && j<6)
						panel[1].add(square[i][j]);
					else
						if(i<3 && j<9)
							panel[2].add(square[i][j]);
						else
							if(i<6 && j<3)
								panel[3].add(square[i][j]);
							else
								if(i<6 && j<6)
									panel[4].add(square[i][j]);
								else
									if(i<6 && j<9)
										panel[5].add(square[i][j]);
									else
										if(i<9 && j<3)
											panel[6].add(square[i][j]);
										else
											if(i<9 && j<6)
												panel[7].add(square[i][j]);
											else
												panel[8].add(square[i][j]);
			}
		}
		
		//LEVEL Buttons
		easyL.addActionListener(this);
		easyL.setBounds(510, 60, 90, 50);
		easyL.setSelected(true);
		easyL.setFont(buttonFont);
		easyL.setForeground(pencilC);
		easyL.setFocusable(false);
		medL.addActionListener(this);
		medL.setBounds(600, 60, 100, 50);
		medL.setFont(buttonFont);
		medL.setForeground(pencilC);
		medL.setFocusable(false);
		hardL.addActionListener(this);
		hardL.setBounds(710, 60, 100, 50);
		hardL.setFont(buttonFont);
		hardL.setForeground(pencilC);
		hardL.setFocusable(false);
		levelBtn.add(easyL);
		levelBtn.add(medL);
		levelBtn.add(hardL);
		levelS(level);
		for(int i=0;i<9;i++)
			numIndex[i].setText(counterNum(i+1)+"");
		
		//Right Panel Buttons
		newGame.setFocusable(false);
		newGame.setBounds(520, 180, 100, 50);
		newGame.addActionListener(this);
		newGame.setBackground(greyC);
		newGame.setFont(buttonFont);
		newGame.setBorder(btnBorder);
		restartBtn.setBounds(670,180,100,50);
		restartBtn.addActionListener(this);
		restartBtn.setFocusable(false);
		restartBtn.setBackground(greyC);
		restartBtn.setFont(buttonFont);
		restartBtn.setBorder(btnBorder);
		deleteBtn.setBounds(570,525,150,50);
		deleteBtn.setFocusable(false);
		deleteBtn.setFont(legendFont);
		deleteBtn.addActionListener(this);
		deleteBtn.setBackground(greyC);
		deleteBtn.setBorder(btnBorder);
		undoBtn.setBounds(600, 450, 90, 50);
		undoBtn.addActionListener(this);
		undoBtn.setFocusable(false);
		undoBtn.setBackground(greyC);
		undoBtn.setFont(buttonFont);
		undoBtn.setBorder(btnBorder);
		checkBtn.setBounds(597,280,100,50);
		checkBtn.setFocusable(false);
		checkBtn.setFont(buttonFont);
		checkBtn.addActionListener(this);
		checkBtn.setBackground(greyC);
		checkBtn.setBorder(btnBorder);
		
		//Labels
		mistakeCountL.setBounds(550, 350, 200, 50);
		mistakeCountL.setHorizontalAlignment(0);
		mistakeCountL.setFont(numberFont);
		mistakeCountL.setForeground(pencilC);
		levelLabel.setBounds(505, 10, 295, 50);
		levelLabel.setHorizontalAlignment(0);
		levelLabel.setFont(legendFont);
		levelLabel.setForeground(blackC);
		
		//Add to frame
		this.add(levelLabel);
		this.add(easyL);
		this.add(medL);
		this.add(hardL);
		this.add(restartBtn);
		this.add(checkBtn);
		this.add(deleteBtn);
		this.add(mistakeCountL);
		this.add(newGame);
		this.add(undoBtn);
		this.add(numP);
		this.add(bigP);
		this.setVisible(true);
	}

	private void fill(int[][] array) {
		//Fill the first line
		int num = 0;
		for(int j=0;j<9;j++) {
			num = randomNum.nextInt(9)+1;
			while(!checkH(numbers, 0, num)) {
				num = randomNum.nextInt(9)+1;
			}
			numbers[0][j]=num;
		}
		//rows 2 & 3
		for(int i=1; i<=2; i++)
			for(int j=0;j<9;j++)
				if(j<6)
					numbers[i][j] = numbers[i-1][j+3];
				else
					numbers[i][j] = numbers[i-1][j-6];
		//rows 4-6
		for(int i=3; i<=5; i++)
			for(int j=0;j<9;j++)
				if(i==3)
					if(j!=8)
						numbers[i][j] = numbers[i-1][j+1];
					else
						numbers[i][j] = numbers[i-1][j-8];
				else
					if(j<6)
						numbers[i][j] = numbers[i-1][j+3];
					else
						numbers[i][j] = numbers[i-1][j-6];
		//rows 7-9
		for(int i=6; i<=8; i++)
			for(int j=0;j<9;j++)
				if(i==6)
					if(j!=8)
						numbers[i][j] = numbers[i-1][j+1];
					else
						numbers[i][j] = numbers[i-1][j-8];
				else
					if(j<6)
						numbers[i][j] = numbers[i-1][j+3];
					else
						numbers[i][j] = numbers[i-1][j-6];
		//Shuffle the rows
		for(int j=0; j<9; j+=3) {
			int index = randomNum.nextInt(3)+j;
			int index2 = randomNum.nextInt(3)+j;
			for(int i=0; i<9; i++) {
				temp[i] = numbers[index][i];
				numbers[index][i] = numbers[index2][i];
				numbers[index2][i] = temp[i];
			}
		}
		//Shuffle the columns
		for(int j=0; j<9; j+=3) {
			int index = randomNum.nextInt(3)+j;
			int index2 = randomNum.nextInt(3)+j;
			for(int i=0; i<9; i++) {
				temp[i] = numbers[i][index];
				numbers[i][index] = numbers[i][index2];
				numbers[i][index2] = temp[i];
			}
		}
	}
	
	private boolean checkH(int[][] square, int x, int num) {
		for(int i=0;i<9;i++)
			if(num == square[x][i])
				return false;
		return true;
	}	
	
	private void levelS(int level) {
		int locationX=0, locationY=0;
		for(int i=0; i<level; i++) {
			locationX = randomNum.nextInt(8)+1;
			locationY = randomNum.nextInt(8)+1;
			while(square[locationX][locationY].getText() != "") {
				locationX = randomNum.nextInt(8)+1;
				locationY = randomNum.nextInt(8)+1;
			}
			square[locationX][locationY].setText(numbers[locationX][locationY]+"");
			square[locationX][locationY].setEnabled(false);
			square[locationX][locationY].setBackground(greyC);
			UIManager.getDefaults().put("Button.disabledText",Color.black);
		}
	}

	private void clear() {
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
				if(!square[i][j].getBackground().equals(greyC))
					square[i][j].setText("");
	}
	
	private void endGame() {
		String[] optionS = {"RESTART","CHECK IT AGAIN","EXIT"};
		boolean endG = false;
		int result;
		aa:
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++)
				if(square[i][j].getText().isBlank()){
					endG = false;
					 break aa;
					}
				else
					endG = true;
		if(endG) {
			checkGame();
			if(mistakeCount() == 0)
				result = JOptionPane.showOptionDialog(null, "YOU WIN!", "GAME OVER", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, optionS , 0);
			else
				result = JOptionPane.showOptionDialog(null, "YOU LOSS!", "GAME OVER", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, optionS , 0);
		if(result == 2)
			System.exit(0);
		else
			if(result ==1)
				checkGame();
			else
				newGame();
		}
	}
	
	private void checkGame() {
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++)
				if(square[i][j].getText().equals(String.valueOf(numbers[i][j])))
					square[i][j].setForeground(Color.blue);
				else
					square[i][j].setForeground(Color.red);
		mistakeCount();
		mistakeCountL.setText("Mistakes: "+mistakeCount());
	}
	
	private void unCheck() {
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++) {
				square[i][j].setForeground(blackC);
				/* Mark Selected Number
				if(square[i][j].getBackground() != greyC)
					square[i][j].setBackground(whiteC);
					 Mark Selected Number */
			}
		mistakeCountL.setText("");
		
	}
	
	private void greyOutBtn() {
		deleteBtn.setBackground(greyC);
		deleteBtn.setForeground(blackC);
		int j=0;
		while(j<9) {
			numB[j].setBackground(greyC);
			numB[j].setForeground(blackC);
			j++;
			}
	}
	
	private int mistakeCount() {
		mistake =0;
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++)
				if(square[i][j].getForeground() == Color.red && square[i][j].getText()!="") {
					mistake++;
				}
		return (mistake);
	}

	private void newGame() {
		for(int i=0; i<9;i++)
			for(int j=0;j<9;j++) {
				square[i][j].setText("");
				square[i][j].setEnabled(true);
				square[i][j].setBackground(whiteC);
				numbers[i][j] = 0;
				tempString = "";
				greyOutBtn();
			}
		fill(numbers);
		levelS(level);
	}

	private int counterNum(int num) {
		int counter =0;
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
				if(square[i][j].getText().equals(num+""))
					counter++;
		return 9-counter;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		unCheck();
		//Button Select
		for(int i=0; i<9; i++) {
			//Select number
			if(e.getSource() == numB[i]) {
				deleteBtn.setBackground(greyC);
				deleteBtn.setForeground(blackC);
				if(!isSelected || tempString != numB[i].getText()) {
					tempString = numB[i].getText();
					isSelected = true;
					int j=0;
					while(j<9) {
						if(j!=i) {
							numB[j].setBackground(greyC);
							numB[j].setForeground(blackC);
						}
						j++;
						}
					numB[i].setBackground(blackC);
					numB[i].setForeground(whiteC);
				}
				else {
					tempString = "";
					isSelected = false;
					numB[i].setBackground(greyC);
					numB[i].setForeground(blackC);
				}
			}
			//Fill the button with the selected number
			for(int j=0; j<9; j++)
				if(e.getSource() == square[i][j]) {
						square[i][j].setText(tempString);
						lastI = i;
						lastJ = j;
					}
		}
		
		//UNDO BUTTON
		if(e.getSource() == undoBtn) {
			if(lastI != -1 && lastJ != -1)
				square[lastI][lastJ].setText("");
		}
		//NEW GAME BTN
		if(e.getSource() == newGame) {
			newGame();
		}
		//Delete Button
		if(e.getSource() == deleteBtn) {
			tempString = "";
			greyOutBtn();
			deleteBtn.setBackground(blackC);
			deleteBtn.setForeground(whiteC);
		}
		//Check Button
		if(e.getSource() == checkBtn)
			checkGame();
		//Clear Button
		if(e.getSource() == restartBtn) {
			clear();
		}
		//Level Selected
		if(e.getSource() == easyL) //EASY LEVEL
			if(level != 40) {
				level = 40;
				newGame();
			}
		if(e.getSource() == medL) //MEDUIM LEVEL
			if(level != 30) {
				level = 30;
				newGame();
			}
		if(e.getSource() == hardL) //HARD LEVEL
			if(level != 25) {
				level = 25;
				newGame();
				}
		//Change number remaining
		for(int i=0;i<9;i++)
			numIndex[i].setText(counterNum(i+1)+"");		
		//EndGame Check
		endGame();
	}

	/* Mark Selected Number
	private void markNum(String number) {
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
				if(number.equals(square[i][j].getText())) {
					for(int x=0;x<9;x++) {
						if(square[i][x].getBackground() != greyC)
							square[i][x].setBackground(shadowC); //Horizontal
						if(square[x][j].getBackground() != greyC)
							square[x][j].setBackground(shadowC); //Vertical
						}
						for(int x=0;x<3;x++)
							for(int y=0;y<3;y++)
								if(i<3 && j<3)
									square[x][y].setBackground(square[x][y].getBackground() != greyC ? shadowC :greyC);
									else
										if(i<3 && j<6)
											square[x][y+3].setBackground(square[x][y+3].getBackground() != greyC ? shadowC :greyC);
											else
												if(i<3 && j<9)
													square[x][y+6].setBackground(square[x][y+6].getBackground() != greyC ? shadowC :greyC);
												else
													if(i<6 && j<3)
														square[x+3][y].setBackground(square[x+3][y].getBackground() != greyC ? shadowC :greyC);
													else
														if(i<6 && j<6)
															square[x+3][y+3].setBackground(square[x+3][y+3].getBackground() != greyC ? shadowC :greyC);
														else
															if(i<6 && j<9)
																square[x+3][y+6].setBackground(square[x+3][y+6].getBackground() != greyC ? shadowC :greyC);
															else
																if(j<3)
																	square[x+6][y].setBackground(square[x+6][y].getBackground() != greyC ? shadowC :greyC);
																else
																	if(j<6)
																		square[x+6][y+3].setBackground(square[x+6][y+3].getBackground() != greyC ? shadowC :greyC);
																	else
																		square[x+6][y+6].setBackground(square[x+6][y+6].getBackground() != greyC ? shadowC :greyC);
														
					}
	}
	Mark Selected Number */

	@Override
	public void keyPressed(KeyEvent e) {
		//Select Number
		char[] keys = {'1','2','3','4','5','6','7','8','9'};
		for(int i=0;i<9;i++)
			if(e.getKeyChar() == keys[i]){
				deleteBtn.setBackground(greyC);
				deleteBtn.setForeground(blackC);
				if(!isSelected || !tempString.equals(keys[i]+"")) {
					tempString = keys[i]+"";
					isSelected = true;
					int j=0;
					while(j<9) {
						if(j!=i) {
							numB[j].setBackground(greyC);
							numB[j].setForeground(blackC);
						}
						j++;
						}
					numB[i].setBackground(blackC);
					numB[i].setForeground(whiteC);
				}
				else {
					tempString = "";
					isSelected = false;
					numB[i].setBackground(greyC);
					numB[i].setForeground(blackC);
				}
			}
		//Level Selection
		if(e.getKeyCode() == 69) //Easy
			if(level != 40) {
				level = 40;
				newGame();
				easyL.setSelected(true);
			}
		if(e.getKeyCode() == 72) //Hard
			if(level != 25) {
				level = 25;
				newGame();
				hardL.setSelected(true);
			}
		if(e.getKeyCode() == 77) //Med
			if(level != 30) {
				level = 30;
				newGame();
				medL.setSelected(true);
			}
		if(e.getKeyCode() == 78) //NEW GAME
			newGame();
		if(e.getKeyCode() == 67) //Check Game
			checkGame();
		if(e.getKeyCode() == 8){ //UNDO
			if(lastI != -1 && lastJ != -1)
				square[lastI][lastJ].setText("");
		}
		if(e.getKeyCode() == 127){ //Delete
			tempString = "";
			greyOutBtn();
			deleteBtn.setBackground(blackC);
			deleteBtn.setForeground(whiteC);
		}
		if(e.getKeyCode() == 82) //Restart
			clear();
		//Change number remaining
				for(int i=0;i<9;i++)
					numIndex[i].setText(counterNum(i+1)+"");
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}	
}
