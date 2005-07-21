package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import datastruct.CompareFile;
import datastruct.DataManager;
import datastruct.ResultList;

/** beskriver fönstret som visar de inmatade resultaten */
class ScoreBoardWindow {
	private JPanel board;				// panelen för rutnätet av etiketter
	private JScrollPane scrollpane;		// gör panelen skrollbar
	private GridBagLayout gridbag;		// resultatlistans layout
	private GridBagConstraints c;		// inställning för layouten
	private int cols,nbrRounds;			// antal kolumner och antal varv
	private ResultList result;			// resultatlistan
	private CompareFile compareFile;	// listan över snitt
	private JLabel[][] label;			// matrisen av etiketter som visar resultaten
	private static JLabel headerLabel;	// rubriketikett
	private int align;					// talar om hur sifferorienteringen skall se ut
	
	/** skapar ett resultatfönster för resultatlistan result */
	public ScoreBoardWindow(ResultList result) {
		this.cols = result.getNbrCols();
		this.nbrRounds = result.getNbrRounds();
		board = new JPanel();
		label = new JLabel[65][cols];
		headerLabel = new JLabel();
		this.result = result;
		this.align = getAlignment();
		
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
	
	/** ställer in resultatfönstret för resultatlistan result */
	public void setup(ResultList result) {
		this.cols = result.getNbrCols();
		this.nbrRounds = result.getNbrRounds();
		board.removeAll();
		label = new JLabel[65][cols];
		this.result = result;
		this.compareFile = null;
		
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
	
	/** ställer in resultatfönstret för jämförelsesnittlistan compareFile */
	public void setup(CompareFile compareFile) {
	    this.compareFile = compareFile;
	    this.cols = 3;
	    board.removeAll();
	    label = new JLabel[65][cols];
	    setLabelLayout(label);
	    board.repaint();
	    if(compareFile.size() != 0) {
	        updateMean(true);
	    }
	}
	
	/** ritar om innehållet i fönstret, add talar om ifall det har lagts till resultat eller ej */
	public void updateMean(boolean add) {
	    if(!add) {
	        clearAll();
	    }
	    String[][][] data = compareFile.getOutput();
	    String[][] output = data[0];
	    String[][] outputColor = data[1];
	    if(output.length >= label.length) {
	        JLabel[][] tempLabel = new JLabel[label.length + 50][cols];
	        setLabelLayout(tempLabel);
	        for(int i = 0; i < label.length; i++) {
	            for(int j = 0; j < label[i].length; j++) {
	                tempLabel[i][j] = label[i][j];
	            }
	        }
	        label = tempLabel;
	    }
	    Color color;
	    for(int i = 0; i < output.length; i++) {
	        for(int j = 0; j < output[i].length; j++) {
	            color = getColor(outputColor[i][j]);
	            label[i][j].setText(output[i][j]);
	            label[i][j].setForeground(color);
	        }
	    }
	}
	
	/** ritar om innehållet i fönstret, add talar om ifall det har lagts till resultat eller ej */
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
			    color = getColor(style[i][j]);
				label[i][j].setText(output[i][j]);
				label[i][j].setForeground(color);
			}
		}
		
		if(align != getAlignment()) {
		    boolean licenseNbr = result.getExtras()[2];
		    align = getAlignment();
		    c.anchor = align;
		    for(int i = 0; i < label.length; i++) {
		        c.gridwidth = 1;
		        for(int j = 0; j < label[i].length; j++) {
		            if((j == 2 && !licenseNbr) || j > 2) {
						gridbag.setConstraints(label[i][j], c);
					}
		            if(j+2 == cols) {
						c.gridwidth = GridBagConstraints.REMAINDER;
					}
		        }
		    }
		}
	}
	
	/** returnerar färg beroende på strängen colorString */
	private Color getColor(String colorString) {
	    Color color;
	    if(colorString != null) {
			if(colorString.equals("red")) {
				color = new Color(255,0,0);
			} else if(colorString.equals("green")) {
				color = new Color(0,170,0);
			} else if(colorString.equals("blue")) {
				color = new Color(0,0,255);
			} else {
				color = Color.black;
			}
		} else {
			color = Color.black;
		}
	    return color;
	}
	
	/** raderar alla etiketters innehåll */
	private void clearAll() {
		for(int i = 0; i < label.length; i++) {
			for(int j = 0; j < label[i].length; j++) {
				label[i][j].setText("");
			}
		}
	}
	
	/** förstorar rutnätet av etiketter vid behov */
	private void enlarge() {
		JLabel[][] temp = label;
		label = new JLabel[temp.length+50][temp[0].length];
		
		boolean licenseNbr = result.getExtras()[2];
	    align = getAlignment();
		
		for(int i = 0; i < label.length; i++) {
			c.gridwidth = 1;
			for(int j = 0; j < label[i].length; j++) {
				if(i < temp.length) {
					label[i][j] = temp[i][j];
				} else {
					label[i][j] = new JLabel();
					if(j == 0) {
						c.anchor = GridBagConstraints.WEST;
					} else if((j == 2 && !licenseNbr) || j == 3) {
						c.anchor = align;
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
	
	/** ställer in layouten för etikettrutnätet label */
	private void setLabelLayout(JLabel[][] label) {
	    boolean licenseNbr = result.getExtras()[2];
	    align = getAlignment();
		for(int i = 0; i < label.length; i++) {
			c.gridwidth = 1;
			for(int j = 0; j < cols; j++) {
				label[i][j] = new JLabel();
				if(j == 0) {
					c.anchor = GridBagConstraints.WEST;
				} else if(((j == 2 && !licenseNbr) || j == 3) && compareFile == null) {
					c.anchor = align;
				}
				gridbag.setConstraints(label[i][j], c);
				board.add(label[i][j]);
				if(j+2 == cols) {
					c.gridwidth = GridBagConstraints.REMAINDER;
				}
			}
		}
	}
	
	/** tar reda på hur siffrorna skall vara orienterade */
	private int getAlignment() {
	    int align = DataManager.getOrientation(AlignmentWindow.COMP_OWNER);
	    if(align == AlignmentWindow.LEFT) {
	        align = GridBagConstraints.WEST;
	    } else if(align == AlignmentWindow.CENTER) {
	        align = GridBagConstraints.CENTER;
	    } else {
	        align = GridBagConstraints.EAST;
	    }
	    return align;
	}
	
	/** sätter rubriken till header */
	public static void setHeader(String header) {
		headerLabel.setText(header);
	}
	
	/** returnerar rubriken */
	public static String getHeader() {
		return headerLabel.getText();
	}
}