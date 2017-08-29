package se.manet.bangolfresultat.gui;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/** klassen som beskriver fönstret där man anger vilka delsummor samt om placering skall visas */
public class LapSumDialog extends JDialog {
	private boolean firstTime = true;		// talar om ifall det är första gången antal varv sätts
	private JButton acceptButton;			// OK-knappen vid ändring av visning av delsummor och placering
	private JButton cancelButton;			// knapp för att avbryta
	private LapSumHandler lapSumHand;		// sköter om knapparna i fönstret för delsummor och placering
	private JPanel panel;					// övergripande behållare
	private JPanel lapSumPanel;				// behållare för redigering (av delsummor och placering)
	private JPanel buttonPanel;				// behållare för knapparna Ok och Avbryt
	private JCheckBox[] lapBox;				// för att välja delsummor
	private JCheckBox place;				// för att välja om placering skall visas
	private int nbrRounds;					// antal varv tävlingen är på
	private boolean[] editData;				// anger vilka delsummor och om placering har valts
	
	/** skapar fönster för inställning av delsummor och placering */
	public LapSumDialog(JFrame owner) {
		super(owner, "Delsummor och placering", true);
		setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		lapSumHand = new LapSumHandler();
	}
	
	/** anger att tävlingen består av nbrRounds varv */
	public void setNbrRounds(int nbrRounds) {
		this.nbrRounds = nbrRounds;
		
		if(firstTime) {
			setupLapSumPanel();
			firstTime = false;
		}
	}
	
	/** skapar panelen för delsummor och placering */
	private void setupLapSumPanel() {
		acceptButton = new JButton("Ok");
		acceptButton.setMnemonic(KeyEvent.VK_O);
		acceptButton.addActionListener(lapSumHand);
		cancelButton = new JButton("Avbryt");
		cancelButton.setMnemonic(KeyEvent.VK_A);
		cancelButton.addActionListener(lapSumHand);
		buttonPanel = new JPanel(new GridLayout(1,2,6,0));
		buttonPanel.setBorder(new EmptyBorder(5,5,5,5));
		buttonPanel.add(acceptButton);
		buttonPanel.add(cancelButton);
		panel = new JPanel(new BorderLayout());
		lapSumPanel = new JPanel();
		lapSumPanel.setBorder(new EmptyBorder(5,5,5,5));
		panel.add(lapSumPanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		lapBox = new JCheckBox[CompInfoDialog.MAXROUNDS-2];
		int mnemonic = KeyEvent.VK_2;
		for(int i = 0; i < lapBox.length; i++) {
			lapBox[i] = new JCheckBox("Delsumma efter varv " + (i + 2), false);
			lapBox[i].setMnemonic(mnemonic);
			mnemonic++;
		}
		place = new JCheckBox("Placering");
		place.setMnemonic(KeyEvent.VK_P);
	}
	
	/** returnerar panelen för delsummor och placering efter att ha ställt in den efter antal varv */
	public JPanel lapSumPanel() {
		lapSumPanel.setLayout(new GridLayout(nbrRounds, 1));
		lapSumPanel.removeAll();
		lapSumPanel.add(new JLabel("Ange om följande skall visas:"));
		for(int i = 0; i < nbrRounds-2; i++) {
			lapSumPanel.add(lapBox[i]);
		}
		lapSumPanel.add(place);
		return panel;
	}
	
	/** återställer så att det är markerat efter det som har valts */
	public void setEditWindow() {
		place.setSelected(editData[0]);
		for(int i = 1; i < editData.length; i++) {
			lapBox[i-1].setSelected(editData[i]);
		}
	}
	
	/** ställer in om placering och vilka delsummor som skall visas */
	public void setEditData(boolean[] editData) {
		this.editData = editData;
		place.setSelected(editData[0]);
		boolean[] lapSums = new boolean[nbrRounds-2];
		for(int i = 1; i < editData.length; i++) {
			lapBox[i-1].setSelected(editData[i]);
			lapSums[i-1] = lapBox[i-1].isSelected();
		}
		ResultInputWindow.RESULTLIST.setLapSum(lapSums);
		ResultInputWindow.RESULTLIST.setPlace(editData[0]);
		ResultInputWindow.BOARD.setup(ResultInputWindow.RESULTLIST);
	}
	
	/** returnerar info om ifall placering och vilka delsummor som har valts */
	public boolean[] getEditData() {
		return editData;
	}
	
	/** klassen som tar hand om knapptryckningarna vid val av delsummor och placering */
	class LapSumHandler implements ActionListener {
		/** kollar vilken knapp som tryckts ned och utför lämplig handling */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == acceptButton) {
				editData = new boolean[1+nbrRounds-2];
				editData[0] = place.isSelected();
				boolean[] lapSums = new boolean[nbrRounds-2];
				for(int i = 1; i < editData.length; i++) {
					editData[i] = lapBox[i-1].isSelected();
					lapSums[i-1] = lapBox[i-1].isSelected();
				}
				ResultInputWindow.RESULTLIST.setLapSum(lapSums);
				ResultInputWindow.RESULTLIST.setPlace(editData[0]);
				ResultInputWindow.BOARD.setup(ResultInputWindow.RESULTLIST);
				setVisible(false);
				SearchWindow.CHANGE = true;
				SearchWindow.STATUSFIELD.setText("*");
				SearchWindow.MESSAGEFIELD.setText("");
			} else if(e.getSource() == cancelButton) {
			    setVisible(false);
			}
		}
	}
}