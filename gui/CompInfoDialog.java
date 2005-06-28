package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import datastruct.ResultList;


/** klassen som beskriver fönstret där tävlingens inställningar ställs in */
public class CompInfoDialog extends JDialog {
	private ResultInputWindow resultInput;  // fönstret som sköter resultatinmatningen
	private LapSumDialog lapSumDialog;		// fönstret som används för att välja delsummor och placering
	private JFrame owner;					// huvudfönstret
	private boolean[] boxData;				// talar om ifall startnummer och licensnummer har valts
	private JCheckBox[] startBox;			// för att välja startnummer samt licensnummer
	private JButton varvButton;				// OK-knapp för indatafönstret
	private VarvHandler varvHand;			// sköter om knapparna i fönstret inmatningsfönstret
	private int nbrRounds,surface;			// tävlingens varvantal och underlag
	public static final int MAXROUNDS = 10;	// max antal varv för en tävling
	private JComboBox surfaceChoice,numberRoundsChoice; // de grafiska komponenterna där man väljer underlag och antal varv
	
	/** skapar fönstret för tävlingsinställningar */
	public CompInfoDialog(JFrame owner, LapSumDialog lapSumDialog, ResultInputWindow resultInput) {
		super(owner, "Indatafönster", true);
		this.owner = owner;
		this.lapSumDialog = lapSumDialog;
		this.resultInput = resultInput;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JPanel varvPanel = new JPanel();
		JLabel varvLabel2 = new JLabel("Ange antal varv som tävlingen är på:");
		JLabel varvLabel1 = new JLabel("Ange tävlingens underlag:");
		surfaceChoice = new JComboBox();
		surfaceChoice.addItem("Filt");
		surfaceChoice.addItem("EB");
		surfaceChoice.addItem("Betong");
		numberRoundsChoice = new JComboBox();
		for(int i = 2; i <= MAXROUNDS; i++) {
			numberRoundsChoice.addItem(String.valueOf(i));
		}
		varvHand = new VarvHandler();
		varvButton = new JButton("Ok");
		varvButton.setMnemonic(KeyEvent.VK_O);
		varvButton.addActionListener(varvHand);
		EnterKeyHandler enterHandler = new EnterKeyHandler();
		varvButton.addKeyListener(enterHandler);
		varvPanel.setLayout(new GridLayout(7,1));
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
		varvPanel.add(varvButton);
		this.getContentPane().add(varvPanel);
		this.pack();
		this.setLocationRelativeTo(owner);
		this.setVisible(true);
	}
	
	/** tar hand om indatan från tävlingsinställningarna */
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
		resultInput.setupResultInputPanel(false, boxData, nbrRounds, surface);
		lapSumDialog.setNbrRounds(nbrRounds);
		lapSumDialog.setEditData(new boolean[nbrRounds-1]);
		this.setVisible(false);
		owner.pack();
		owner.setVisible(true);
	}
	
	/** klassen som tar hand om tangentbordsinmatningen i sökfältet */
	class EnterKeyHandler extends KeyAdapter {
		/** utför lämplig handling */
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				handleIndata();
			}
		}
	}
	
	/** klassen som tar hand om knapptryckningarna vid val av antal varv, underlag, startnummer och licensnummer */
	class VarvHandler implements ActionListener {
		/** kollar vilken knapp som tryckts ned och utför lämplig handling */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == varvButton) {
				handleIndata();
			}
		}
	}
}