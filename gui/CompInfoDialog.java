package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import datastruct.ResultList;


/** klassen som beskriver f�nstret d�r t�vlingens inst�llningar st�lls in */
public class CompInfoDialog extends JDialog {
    private CompInfoDialog thisDialog;		// referens till detta f�nster
	private SearchWindow mainWindow;		// huvudf�nstret som visar allting
	private JFrame owner;					// huvudf�nstret
	private boolean[] boxData;				// talar om ifall startnummer och licensnummer har valts
	private JCheckBox[] startBox;			// f�r att v�lja startnummer samt licensnummer
	private JButton acceptButton;			// OK-knapp f�r indataf�nstret
	private JButton cancelButton;			// knapp f�r att avbryta
	private VarvHandler varvHand;			// sk�ter om knapparna i f�nstret inmatningsf�nstret
	private int nbrRounds,surface;			// t�vlingens varvantal och underlag
	public static final int MAXROUNDS = 10;	// max antal varv f�r en t�vling
	private JComboBox surfaceChoice,numberRoundsChoice; // de grafiska komponenterna d�r man v�ljer underlag och antal varv
	
	/** skapar f�nstret f�r t�vlingsinst�llningar */
	public CompInfoDialog(JFrame owner, SearchWindow mainWindow) {
		super(owner, "Indataf�nster", true);
		setResizable(false);
		thisDialog = this;
		this.owner = owner;
		this.mainWindow = mainWindow;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JPanel varvPanel = new JPanel();
		varvPanel.setBorder(new EmptyBorder(5,5,5,5));
		JLabel varvLabel2 = new JLabel("Ange antal varv som t�vlingen �r p�:");
		JLabel varvLabel1 = new JLabel("Ange t�vlingens underlag:");
		surfaceChoice = new JComboBox();
		surfaceChoice.addItem("Filt");
		surfaceChoice.addItem("EB");
		surfaceChoice.addItem("Betong");
		numberRoundsChoice = new JComboBox();
		for(int i = 2; i <= MAXROUNDS; i++) {
			numberRoundsChoice.addItem(String.valueOf(i));
		}
		varvHand = new VarvHandler();
		acceptButton = new JButton("Ok");
		acceptButton.setMnemonic(KeyEvent.VK_O);
		acceptButton.addActionListener(varvHand);
		cancelButton = new JButton("Avbryt");
		cancelButton.setMnemonic(KeyEvent.VK_A);
		cancelButton.addActionListener(varvHand);
		EnterKeyHandler enterHandler = new EnterKeyHandler();
		acceptButton.addKeyListener(enterHandler);
		varvPanel.setLayout(new GridLayout(6,1));
		varvPanel.add(varvLabel1);
		varvPanel.add(surfaceChoice);
		varvPanel.add(varvLabel2);
		varvPanel.add(numberRoundsChoice);
		startBox = new JCheckBox[2];
		startBox[0] = new JCheckBox("Startnummer", false);
		startBox[0].setMnemonic(KeyEvent.VK_S);
		startBox[1] = new JCheckBox("Licensnummer", false);
		startBox[1].setMnemonic(KeyEvent.VK_L);
		varvPanel.add(startBox[0]);
		varvPanel.add(startBox[1]);
		JPanel buttonPanel = new JPanel(new GridLayout(1,2,6,0));
		buttonPanel.setBorder(new EmptyBorder(5,5,5,5));
		buttonPanel.add(acceptButton);
		buttonPanel.add(cancelButton);
		this.getContentPane().add(varvPanel, BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.pack();
	}
	
	/** tar hand om indatan fr�n t�vlingsinst�llningarna */
	private void handleIndata() {
		nbrRounds = Integer.parseInt((String)numberRoundsChoice.getSelectedItem());
		if(((String)surfaceChoice.getSelectedItem()).equals("Filt")) {
			surface = ResultList.FILT;
		} else if(((String)surfaceChoice.getSelectedItem()).equals("EB")) {
			surface = ResultList.EB;
		} else {
			surface = ResultList.BETONG;
		}
		boxData = new boolean[2];
		for(int i = 0; i < boxData.length; i++) {
			boxData[i] = startBox[i].isSelected();
		}
		
		mainWindow.setupResultInputPanel(false, boxData, nbrRounds, surface);
		this.setVisible(false);
		owner.pack();
	}
	
	/** klassen som tar hand om tangentbordsinmatningen i s�kf�ltet */
	class EnterKeyHandler extends KeyAdapter {
		/** utf�r l�mplig handling */
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				handleIndata();
			}
		}
	}
	
	/** klassen som tar hand om knapptryckningarna vid val av antal varv, underlag, startnummer och licensnummer */
	class VarvHandler implements ActionListener {
		/** kollar vilken knapp som tryckts ned och utf�r l�mplig handling */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == acceptButton) {
				handleIndata();
			} else if(e.getSource() == cancelButton) {
			    getWindowListeners()[0].windowClosing(new WindowEvent(thisDialog, WindowEvent.WINDOW_CLOSING));
			}
		}
	}
}