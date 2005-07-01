package gui;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JMenuBar;

import datastruct.IOHandler;
import datastruct.PersonResult;
import datastruct.ResultList;


/** klassen som beskriver indatafönstret och resultatinmatningsfönstret */
class ResultInputWindow {
	private HashMap startNbrMap, licenseMap;// startnummer och licensnummer mappar för att undvika dubletter
	private HashMap licenseIDMap;			// håller reda på vilket id licensnumret tillhör
	private HashMap personTracker;			// har koll på personernas identifikationsnummer
	private HashMap personNameTracker;		// används för att få fram personernas namn utifrån id
	public static ResultList RESULTLIST;	// resultatlistan
	public static ScoreBoardWindow BOARD;	// den visuella resultatlistan
	private IOHandler io;					// tar hand om skrivning och läsning till fil
	private int nbrRounds,surface;			// tävlingens varvantal och underlag
	private String[] classes;				// strängvektorn med de olika klassnamnen
	private static JComboBox klassChoice;			// valrutan för vilken klass
	private JFrame frame;							// huvudfönstret
	private JDialog inputDialog, popup;				// indatafönster och resultatinmatningsfönster
	private JPanel inputPanel, searchPanel;			// behållare för indata, sökning och redigering (av delsummor och placering)
	private JTextField prio, startNbrField, licenseNbrField; 	// inmatning av sorteringsordning, startnummer och licensnummer
	private JPanel statusPanel;									// statuspanelen
	private JTextField[] varvResult;							// inmatning av varvresultat
	private JButton inputOk,inputCancel,inputErase,inputRemove; // OK, avbryt, radera och ta bort är vad dessa knappar sköter om
	private JLabel person;										// visar namnet på personen som valts vid resultatinmatningsfönstret
	private JMenuBar bar;										// meny för att ändra namn och klubb
	private boolean firstTime;									// anger om det är första gången som setupResultInputPanel() körs
	private boolean[] boxData;									// talar om ifall startnummer och licensnummer har valts
	
	/** skapar indatafönstret och får in huvudfönstret mainFrame, sökpanelen searchPanel och fönstret för
			delsummorna för att kunna ställa in dessa senare när all indata som behövs har fåtts fram */
	public ResultInputWindow(JFrame mainFrame, JPanel searchPanel, JPanel statusPanel, JMenuBar resBar, HashMap personTracker, HashMap personNameTracker) {
		frame = mainFrame;
		this.searchPanel = searchPanel;
		this.statusPanel = statusPanel;
		bar = resBar;
		this.personTracker = personTracker;
		this.personNameTracker = personNameTracker;
		startNbrMap = new HashMap();
		io = new IOHandler();
		try {
			licenseMap = (HashMap) io.load("licensemap");
			licenseIDMap = (HashMap) io.load("licensenamemap");
		} catch (Exception e) {
			licenseMap = new HashMap();
			licenseIDMap = new HashMap();
		}
		firstTime = true;
	}
	
	/** returnerar etiketten som visar spelarens namn vid inmatning av resultat */
	public JLabel getNameLabel() {
		return person;
	}
	
	/** ställer in resultatinmatningsfönstret efter en redan skapad resultatlista result */
	public void init(ResultList result) {
		ResultInputWindow.RESULTLIST = result;
		setupResultInputPanel(true, result.getStartData(), result.getNbrRounds(), result.getSurface());
	}
	
	/** skapar resultatinmatningsfönstret, init är true om ingen ny resultatlista behöver skapas */
	protected void setupResultInputPanel(boolean init, boolean[] boxData, int nbrRounds, int surface) {
		this.boxData = boxData;
		this.nbrRounds = nbrRounds;
		this.surface = surface;
		startNbrMap.clear();
		inputPanel = new JPanel();
		GridLayout gridLayout;
		if(boxData[0] && boxData[1]) {
		    gridLayout = new GridLayout(4+nbrRounds+2,2);
		} else if(boxData[0] || boxData[1]) {
		    gridLayout = new GridLayout(4+nbrRounds+1,2);
		} else {
		    gridLayout = new GridLayout(4+nbrRounds,2);
		}
		gridLayout.setHgap(2);
		gridLayout.setVgap(2);
		inputPanel.setLayout(gridLayout);
		person = new JLabel();
		JLabel[] varvLabel = new JLabel[nbrRounds];
		varvResult = new JTextField[nbrRounds];
		
		try {
			classes = (String[])io.load("klasstring");
		} catch (Exception e) {
			classes = new String[3];
			classes[0] = "Klass 1";
			classes[1] = "Klass 2";
			classes[2] = "Klass 3";
		}
		klassChoice = new JComboBox(classes);
		inputPanel.add(person);
		inputPanel.add(klassChoice);
		EnterKeyHandler enterHandler = new EnterKeyHandler();
		JLabel startNrLabel = new JLabel("Startnummer:");
		JLabel licenseNrLabel = new JLabel("Licensnummer:");
		startNbrField = new JTextField();
		licenseNbrField = new JTextField();
		startNbrField.addKeyListener(enterHandler);
		licenseNbrField.addKeyListener(enterHandler);
		if(boxData[1]) {
			inputPanel.add(licenseNrLabel);
			inputPanel.add(licenseNbrField);
		}
		if(boxData[0]) {
			inputPanel.add(startNrLabel);
			inputPanel.add(startNbrField);
		}
		for(int i = 0; i < varvLabel.length; i++) {
			varvLabel[i] = new JLabel("Varv " + (i+1) + ":");
			varvResult[i] = new JTextField();
			varvResult[i].addKeyListener(enterHandler);
			inputPanel.add(varvLabel[i]);
			inputPanel.add(varvResult[i]);
		}
		popup = new JDialog(frame, "Notera resultat", true);
		popup.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		popup.setJMenuBar(bar);
		
		JLabel prioLabel = new JLabel("Ange prio vid behov:");
		prio = new JTextField();
		prio.addKeyListener(enterHandler);
		inputOk = new JButton("Ok");
		inputOk.setMnemonic(KeyEvent.VK_O);
		inputErase = new JButton("Radera");
		inputErase.setMnemonic(KeyEvent.VK_R);
		inputRemove = new JButton("Ta bort");
		inputRemove.setMnemonic(KeyEvent.VK_T);
		inputCancel = new JButton("Avbryt");
		inputCancel.setMnemonic(KeyEvent.VK_A);
		ResultHandler resHand = new ResultHandler();
		inputOk.addActionListener(resHand);
		inputOk.addKeyListener(enterHandler);
		inputErase.addActionListener(resHand);
		inputErase.addKeyListener(enterHandler);
		inputRemove.addActionListener(resHand);
		inputRemove.addKeyListener(enterHandler);
		inputCancel.addActionListener(resHand);
		inputCancel.addKeyListener(enterHandler);
		inputPanel.add(prioLabel);
		inputPanel.add(prio);
		inputPanel.add(inputOk);
		inputPanel.add(inputCancel);
		inputPanel.add(inputErase);
		inputPanel.add(inputRemove);
		if(firstTime) {
			RESULTLIST = new ResultList(nbrRounds, surface, boxData);
			BOARD = new ScoreBoardWindow(RESULTLIST);
			frame.getContentPane().add(searchPanel, BorderLayout.WEST);
			frame.getContentPane().add(BOARD.getScrollPane(), BorderLayout.CENTER);
			frame.getContentPane().add(statusPanel, BorderLayout.SOUTH);
			firstTime = false;
		} else if(init) {
			BOARD.setup(RESULTLIST);
		} else {
			RESULTLIST = new ResultList(nbrRounds, surface, boxData);
			BOARD.setup(RESULTLIST);
		}
	}
	
	/** ställer in resultatinmatningsfönstret efter personen som har valts genom att trycka på knappen button */
	public void setPopup(JButton button) {
		popup.getContentPane().add(inputPanel);
		person.setText(button.getText().substring(3, button.getText().length()));
		StringTokenizer str = new StringTokenizer(person.getText(), ",");
		String name = str.nextToken();
		String club = str.nextToken().trim();
		int[] results;
		String identity = name + ", " + club;
		Integer personID = ((Integer) personTracker.get(identity));
		if(licenseMap.containsKey(personID)) {
			licenseNbrField.setText((String)licenseMap.get(personID));
		} else {
			licenseNbrField.setText("");
		}
		PersonResult pr = RESULTLIST.getPerson(personID.intValue());
		if(pr != null) {
			klassChoice.setSelectedItem(pr.getKlass());
			results = pr.getResultList();
			startNbrField.setText(String.valueOf(pr.getStartNr()));
			int prioNbr = pr.getPrio();
			if(prioNbr != Integer.MAX_VALUE) {
				prio.setText(String.valueOf(prioNbr));
			} else {
				prio.setText("");
			}
		} else {
			klassChoice.setSelectedIndex(klassChoice.getSelectedIndex());
			results = new int[nbrRounds];
			startNbrField.setText("");
			prio.setText("");
		}
		for(int i = 0; i < nbrRounds; i++) {
			if(results[i] == ResultList.NO_RESULT_THIS_ROUND) {
				varvResult[i].setText("-");
			} else if(results[i] != 0) {
				varvResult[i].setText(String.valueOf(results[i]));
			} else {
				varvResult[i].setText("");
			}
		}
		popup.pack();
		popup.setLocationRelativeTo(frame);
		popup.setVisible(true);
	}
	
	/** raderar alla inmatningar i resultatinmatningsfönstret */
	private void clearInputs() {
		for(int i = 0; i < varvResult.length; i++) {
			varvResult[i].setText("");
		}
		startNbrField.setText("");
		licenseNbrField.setText("");
		prio.setText("");
	}
	
	/** tar bort en person ifrån resultatlistan */
	private void removeResult() {
		StringTokenizer str = new StringTokenizer(person.getText(), ",");
		String name = str.nextToken();
		String club = str.nextToken().trim();
		String identity = name + ", " + club;
		int personID = ((Integer) personTracker.get(identity)).intValue();
		PersonResult pr = RESULTLIST.getPerson(personID);
		if(RESULTLIST.removeResult(new Integer(personID))) {
			if(boxData[0] && pr!=null) {
				startNbrMap.remove(new Integer(pr.getStartNr()));
			}
			SearchWindow.CHANGE = true;
			SearchWindow.STATUSFIELD.setText("*");
			SearchWindow.MESSAGEFIELD.setText(identity + " är nu borttagen från tävlingen.");
			popup.setVisible(false);
			BOARD.update(false);
		} else {
			JOptionPane.showMessageDialog(popup, "Det gick inte att ta bort personen från tävlingen");
		}
	}
	
	/** klassen som tar hand om knapptryckningarna vid inmatning av resultat */
	class ResultHandler implements ActionListener {
		/** kollar vilken knapp som tryckts ned och utför lämplig handling */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == inputOk) {
				handleResultInput();
			} else if(e.getSource() == inputRemove) {
				removeResult();
			} else if(e.getSource() == inputErase) {
				clearInputs();
			} else if(e.getSource() == inputCancel) {
				popup.setVisible(false);
			}
		}
	}
	
	/** klassen som tar hand om tangentbordsinmatningen i sökfältet */
	class EnterKeyHandler extends KeyAdapter {
		/** utför lämplig handling */
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == inputCancel) {
				popup.setVisible(false);
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == inputErase) {
				clearInputs();
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == inputRemove) {
				removeResult();
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == inputOk) {
				handleResultInput();
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				handleResultInput();
			}
		}
	}
	
	/** tar hand om indatan från resultatinmatningsfönstret */
	private void handleResultInput() {
		boolean good = true;
		boolean goodValues = true;
		boolean startNbrAdded = false;
		int nbrRoundsToRead = 0;
		int nbrReadableInputs = 0;
		int prioNr = Integer.MAX_VALUE;
		int startNr = -1;
		String licenseNr = "null";
		int[] results = new int[nbrRounds];
		StringTokenizer str = new StringTokenizer(person.getText(), ",");
		String name = str.nextToken();
		String club = str.nextToken().trim();
		Integer personID = ((Integer) personTracker.get(name + ", " + club));
		if(boxData[1]) {
			if(licenseNbrField.getText() != null) {
				licenseNr = licenseNbrField.getText().trim();
				licenseNr = licenseNr.toUpperCase();
			}
			if(!licenseNr.equals("null") && licenseNr.length()==12) {
				if(licenseMap.containsKey(personID) && ((String)licenseMap.get(personID)).equals(licenseNr)) {}
				else if(licenseMap.containsValue(licenseNr)) {
					good = false;
					JOptionPane.showMessageDialog(popup, "Angivet licensnummer är upptaget av " + 
							(String)personNameTracker.get((Integer)licenseIDMap.get(licenseNr)));
				}
			} else {
				good = false;
				JOptionPane.showMessageDialog(popup, "Licensnumret är inte korrekt!");
			}
		}
		int tempStartNr = -1;
		try {
			if(boxData[0] && good) {
				PersonResult pr = RESULTLIST.getPerson(personID.intValue());
				if(pr != null) {
					tempStartNr = pr.getStartNr();
					startNbrMap.remove(new Integer(tempStartNr));
				}
				startNr = Integer.parseInt(startNbrField.getText());
				if(startNbrMap.containsKey(new Integer(startNr))) {
					good = false;
					JOptionPane.showMessageDialog(popup, "En person med detta startnummer är redan inlagd i tävlingen!");
				}
			}
		} catch (NumberFormatException f) {
			good = false;
			JOptionPane.showMessageDialog(popup, "Startnumret är inget giltigt siffervärde!");
		}
		if(good) {
			for (int i = 0; i < nbrRounds; i++) {
				if(!varvResult[i].getText().equals("")) {
					try {
						Integer.parseInt(varvResult[i].getText());
						nbrReadableInputs++;
					} catch (NumberFormatException g) {
						if(varvResult[i].getText().equals("-")) {
							nbrReadableInputs++;
						} else {
							nbrRoundsToRead = -1;
							JOptionPane.showMessageDialog(popup, "Varvresultat " + (i + 1) + " är inget giltigt värde! Det enda som kan skrivas istället för tal är tecknet \"-\".");
							break;
						}
					}
				}
			}
			if(nbrRoundsToRead != -1) {
				for (int i = 0; i < nbrRounds; i++) {
					try {
						results[i] = Integer.parseInt(varvResult[i].getText());
						nbrRoundsToRead++;
					} catch (NumberFormatException h) {
						if(varvResult[i].getText().equals("-")) {
							results[i] = ResultList.NO_RESULT_THIS_ROUND;
							nbrRoundsToRead++;
						} else {
							break;
						}
					}
				}
			}
			if(nbrRoundsToRead != -1 && nbrRoundsToRead < nbrReadableInputs) {
				JOptionPane.showMessageDialog(popup, "Kan ej mata in resultat för varv där föregående varv saknas!");
			} else if (nbrRoundsToRead != -1) {
				for(int i = 0; i < nbrRoundsToRead; i++) {
					if((results[i] < 18 || results[i] > 126) && results[i] != ResultList.NO_RESULT_THIS_ROUND) {
						goodValues = false;
					}
				}
				if(goodValues) {
					try {
						prioNr = Integer.parseInt(prio.getText());
					} catch (NumberFormatException f) {
						if(!prio.getText().equals("")) {
							goodValues = false;
							JOptionPane.showMessageDialog(popup, "Prioritetsnumret är inget giltigt siffervärde!");
						}
					}
				} else {
					JOptionPane.showMessageDialog(popup, "Resultatet för ett varv måste ligga mellan 18 och 126!");
				}
				if(goodValues) {
					String klass = (String)klassChoice.getSelectedItem();
					String identity = name + ", " + club;
					RESULTLIST.addResult(startNr,name,club,licenseNr,results,nbrRounds,klass,prioNr,nbrRoundsToRead,personID.intValue());
					SearchWindow.CHANGE = true;
					SearchWindow.STATUSFIELD.setText("*");
					SearchWindow.MESSAGEFIELD.setText("");
					startNbrMap.put(new Integer(startNr), null);
					startNbrAdded = true;
					if(boxData[1]) {
						licenseMap.put(personID, licenseNr);
						licenseIDMap.put(licenseNr, personID);
					}
					popup.setVisible(false);
					BOARD.update(true);
				}
			}
		}
		if(boxData[0] && !startNbrAdded) {
			startNbrMap.put(new Integer(tempStartNr), null);
		}
	}
	
	/** ställer in vilka klasser som skall kunna väljas efter strängvektorn s */
	public static void setKlassChoice(String[] s) {
		klassChoice.removeAllItems();
		for(int i = 0; i < s.length; i++) {
			klassChoice.addItem(s[i]);
		}
	}
	
	/** ange startnummerkontrollens HashMap till map */
	public void setStartNbrMap(HashMap map) {
		startNbrMap = map;
	}
	
	/** returnerar HashMapen som håller reda på licensnumren */
	public HashMap getLicenseMap() {
		return licenseMap;
	}
	
	/** returnerar HashMapen som håller reda på id för ett visst licensnummer */
	public HashMap getLicenseIDMap() {
		return licenseIDMap;
	}
	
	/** anpassar licensnumren och resultatlistan till nytt namn eller ny klubb */
	public void updateNameAndClub(String oldName, String oldClub, String newName, String newClub, Integer personID) {
		String oldIdentity = oldName + ", " + oldClub;
		String newIdentity = newName + ", " + newClub;
		RESULTLIST.changeNameAndClub(personID, newName, newClub);
		BOARD.update(true);
		if(licenseMap.containsKey(personID)) {
			String licenseNbr = (String) licenseMap.get(personID);
			licenseIDMap.put(licenseNbr, newIdentity);
		}
	}
	
	/** returnerar resultatlistan */
	public ResultList getResultList() {
		return RESULTLIST;
	}
}