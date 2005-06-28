package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import datastruct.ResultList;


/** beskriver f�nstret som visar de inmatade resultaten */
class ScoreBoardWindow {
	private JPanel board;				// panelen f�r rutn�tet av etiketter
	private JScrollPane scrollpane;		// g�r panelen skrollbar
	private GridBagLayout gridbag;		// resultatlistans layout
	private GridBagConstraints c;		// inst�llning f�r layouten
	private int cols,nbrRounds;			// antal kolumner och antal varv
	private ResultList result;			// resultatlistan
	private JLabel[][] label;			// matrisen av etiketter som visar resultaten
	private static JLabel headerLabel;	// rubriketikett
	
	/** skapar ett resultatf�nster f�r resultatlistan result */
	public ScoreBoardWindow(ResultList result) {
		this.cols = result.getNbrCols();
		this.nbrRounds = result.getNbrRounds();
		board = new JPanel();
		label = new JLabel[65][cols];
		headerLabel = new JLabel();
		this.result = result;
		
		gridbag = new GridBagLayout();
		c = new GridBagConstraints();
		//board.setFont(new Font("Helvetica", Font.PLAIN, 14));
		board.setLayout(gridbag);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(headerLabel, c);
		board.add(headerLabel);
		setLabelLayout(label);
		
		scrollpane = new JScrollPane(board);
		scrollpane.setPreferredSize(new Dimension(760,406));
	}
	
	/** returnerar JScrollpanen som allting ligger i */
	public JScrollPane getScrollPane() {
		return scrollpane;
	}
	
	/** st�ller in resultatf�nstret f�r resultatlistan result */
	public void setup(ResultList result) {
		this.cols = result.getNbrCols();
		this.nbrRounds = result.getNbrRounds();
		board.removeAll();
		label = new JLabel[65][cols];
		this.result = result;
		
		board.setLayout(gridbag);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(headerLabel, c);
		board.add(headerLabel);
		setLabelLayout(label);
		board.repaint();
		if(result.size() != 0) {
			update(true);
		}
	}
	
	/** ritar om inneh�llet i f�nstret, add talar om ifall det har lagts till resultat eller ej */
	public void update(boolean add) {
		if(!add) {
			clearAll();
		}
		String[][] output = result.getOutput();
		String[][] style = result.getOutputStyle();
		Color color;
		for(int i = 0; i < output.length; i++) {
			if(i+2 == label.length) {
				enlarge();
			}
			for(int j = 0; j < output[i].length; j++) {
				if(style[i][j] != null) {
					if(style[i][j].equals("red")) {
						color = new Color(255,0,0);
					} else if(style[i][j].equals("green")) {
						color = new Color(0,170,0);
					} else if(style[i][j].equals("blue")) {
						color = new Color(0,0,255);
					} else {
						color = Color.black;
					}
				} else {
					color = Color.black;
				}
				label[i][j].setText(output[i][j]);
				label[i][j].setForeground(color);
			}
		}
	}
	
	/** raderar alla etiketters inneh�ll */
	private void clearAll() {
		for(int i = 0; i < label.length; i++) {
			for(int j = 0; j < label[i].length; j++) {
				label[i][j].setText("");
			}
		}
	}
	
	/** f�rstorar rutn�tet av etiketter vid behov */
	private void enlarge() {
		JLabel[][] temp = label;
		label = new JLabel[temp.length+50][temp[0].length];
		
		for(int i = 0; i < label.length; i++) {
			c.gridwidth = 1;
			for(int j = 0; j < label[i].length; j++) {
				if(i < temp.length) {
					label[i][j] = temp[i][j];
				} else {
					label[i][j] = new JLabel();
					if(j == 0) {
						c.anchor = GridBagConstraints.WEST;
					} else if(j == 2) {
						c.anchor = GridBagConstraints.CENTER;
					}
					gridbag.setConstraints(label[i][j], c);
					board.add(label[i][j]);
					if(j+2 == label[i].length) {
						c.gridwidth = GridBagConstraints.REMAINDER;
					}
				}
			}
		}
	}
	
	/** st�ller in layouten f�r etikettrutn�tet label */
	private void setLabelLayout(JLabel[][] label) {
		for(int i = 0; i < label.length; i++) {
			c.gridwidth = 1;
			for(int j = 0; j < cols; j++) {
				label[i][j] = new JLabel();
				if(j == 0) {
					c.anchor = GridBagConstraints.WEST;
				} else if(j == 2) {
					c.anchor = GridBagConstraints.CENTER;
				}
				gridbag.setConstraints(label[i][j], c);
				board.add(label[i][j]);
				if(j+2 == cols) {
					c.gridwidth = GridBagConstraints.REMAINDER;
				}
			}
		}
	}
	
	/** s�tter rubriken till header */
	public static void setHeader(String header) {
		headerLabel.setText(header);
	}
	
	/** returnerar rubriken */
	public static String getHeader() {
		return headerLabel.getText();
	}
}