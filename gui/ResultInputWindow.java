package gui;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.border.EmptyBorder;




import datastruct.CompareFile;
import datastruct.IOHandler;
import datastruct.PersonMean;
import datastruct.PersonResult;
import datastruct.ResultList;

/** klassen som beskriver indataf�nstret och resultatinmatningsf�nstret */
public class ResultInputWindow {
	private HashMap startNbrMap, licenseMap;// startnummer- och licensnummermappar f�r att undvika dubletter
	private HashMap licenseIDMap;			// h�ller reda p� vilket id licensnumret tillh�r
	private HashMap personTracker;			// har koll p� personernas identifikationsnummer
	private HashMap personNameTracker;		// anv�nds f�r att f� fram personernas namn utifr�n id
	public static ResultList RESULTLIST;	// resultatlistan
	public static ScoreBoardWindow BOARD;	// den visuella resultatlistan
	private IOHandler io;					// tar hand om skrivning och l�sning till fil
	private int nbrRounds;					// t�vlingens varvantal
	private String[] classes;				// str�ngvektorn med de olika klassnamnen
	private static JComboBox klassChoice;	// valrutan f�r vilken klass
	private JFrame frame;					// huvudf�nstret
	private JDialog popup;					// resultatinmatningsf�nster
	private JPanel inputPanel;				// beh�llare f�r indata
	private JTextField prio, startNbrField, licenseNbrField; 	// inmatning av sorteringsordning, startnummer och licensnummer
	private JTextField meanField;								// inmatning av snitt
	private JTextField[] varvResult;							// inmatning av varvresultat
	private JButton inputOk,inputCancel,inputErase,inputRemove; // OK, avbryt, radera och ta bort �r vad dessa knappar sk�ter om
	private JLabel person;							// visar namnet p� personen som valts vid resultatinmatningsf�nstret
	private JMenuBar bar;							// meny f�r att �ndra namn och klubb
	private boolean[] boxData;						// talar om ifall startnummer och licensnummer har valts
	private int mode;								// anger vilket l�ge som g�ller
	private CompareFile compareFile;				// datastruktur f�r j�mf�relsesnittlistan
	
	/** skapar indataf�nstret och f�r in huvudf�nstret mainFrame, s�kpanelen searchPanel och f�nstret f�r
			delsummorna f�r att kunna st�lla in dessa senare n�r all indata som beh�vs har f�tts fram */
	public ResultInputWindow(JFrame mainFrame, JMenuBar resBar, HashMap personTracker, HashMap personNameTracker) {
		frame = mainFrame;
		bar = resBar;
		this.personTracker = personTracker;
		this.personNameTracker = personNameTracker;
		startNbrMap = new HashMap();
		io = new IOHandler();
		try {
			licenseMap = (HashMap) io.load("licensemap");
			licenseIDMap = (HashMap) io.load("licensenamemap");
			if(!licenseMap.isEmpty()) {
			    LinkedList list = new LinkedList(licenseMap.values());
			    if(list.getFirst() instanceof Integer) {
			        throw new Exception("Gammalt licensnummerformat, nytt register skapas");
			    }
			}
		} catch (Exception e) {
			licenseMap = new HashMap();
			licenseIDMap = new HashMap();
		}
	}
	
	/** returnerar etiketten som visar spelarens namn vid inmatning av resultat */
	public JLabel getNameLabel() {
		return person;
	}
	
	/** st�ller in resultatinmatningsf�nstret efter en redan skapad resultatlista result */
	public void init(ResultList result) {
		ResultInputWindow.RESULTLIST = result;
		setupResultInputPanel(true, result.getStartData(), result.getNbrRounds(), result.getSurface());
	}
	
	/** skapar resultatinmatningsf�nstret */
	protected void setupResultInputPanel(int mode) {
	    this.mode = mode;
	    if(mode == SearchWindow.MODE_DUMMY) {
	        inputPanel = new JPanel();
	        inputPanel.setBorder(new EmptyBorder(5,5,5,5));
	        person = new JLabel();
	        inputPanel.add(person);
	        klassChoice = new JComboBox();
	        
	        popup = new JDialog(frame, "Redigera", true);
	        popup.setResizable(false);
	        popup.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	        popup.setJMenuBar(bar);
	    } else if(mode == SearchWindow.MODE_SNITT) {
	        inputPanel = new JPanel(new GridLayout(4,2,6,3));
	        inputPanel.setBorder(new EmptyBorder(5,5,5,5));
	        person = new JLabel();
	        inputPanel.add(person);
	        inputPanel.add(new JLabel());
	        inputPanel.add(new JLabel("Snitt:"));
	        EnterKeyHandler enterHandler = new EnterKeyHandler();
	        meanField = new JTextField();
	        meanField.addKeyListener(enterHandler);
	        inputPanel.add(meanField);
	        klassChoice = new JComboBox();
	        
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
		    inputErase.addActionListener(resHand);
		    inputRemove.addActionListener(resHand);
		    inputCancel.addActionListener(resHand);
		    inputPanel.add(inputOk);
		    inputPanel.add(inputCancel);
		    inputPanel.add(inputErase);
		    inputPanel.add(inputRemove);
	        
	        popup = new JDialog(frame, "Notera snitt", true);
	        popup.setResizable(false);
	        popup.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	        popup.setJMenuBar(bar);
	        
	        BOARD.setup(compareFile);
	    }
	}
	
	/** skapar resultatinmatningsf�nstret, init �r true om ingen ny resultatlista beh�ver skapas */
	protected void setupResultInputPanel(boolean init, boolean[] boxData, int nbrRounds, int surface) {
	    this.mode = SearchWindow.MODE_COMP;
	    this.boxData = boxData;
	    this.nbrRounds = nbrRounds;
	    startNbrMap.clear();
	    inputPanel = new JPanel();
	    inputPanel.setBorder(new EmptyBorder(5,5,5,5));
	    GridLayout gridLayout;
	    if(boxData[0] && boxData[1]) {
	        gridLayout = new GridLayout(4+nbrRounds+2,2,6,3);
	    } else if(boxData[0] || boxData[1]) {
	        gridLayout = new GridLayout(4+nbrRounds+1,2,6,3);
	    } else {
	        gridLayout = new GridLayout(4+nbrRounds,2,6,3);
	    }
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
	    popup.setResizable(false);
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
	    if(init) {
	        BOARD.setup(RESULTLIST);
	    } else {
	        RESULTLIST = new ResultList(nbrRounds, surface, boxData);
	        BOARD.setup(RESULTLIST);
	    }
	}
	
	/** st�ller in resultatinmatningsf�nstret efter personen som har valts genom att trycka p� knappen button */
	public void setPopup(JButton button, int mode) {
		popup.getContentPane().add(inputPanel);
		person.setText(button.getText().substring(3, button.getText().length()));
		if(mode == SearchWindow.MODE_COMP) {
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
		    	String klass = pr.getKlass();
		    	if (klass.trim().equals(""))
		    		klassChoice.setSelectedItem(KlassWindow.NO_KLASS);
		    	else
		    		klassChoice.setSelectedItem(klass);
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
		} else if(mode == SearchWindow.MODE_SNITT) {
		    StringTokenizer str = new StringTokenizer(person.getText(), ",");
		    String name = str.nextToken();
		    String club = str.nextToken().trim();
		    String identity = name + ", " + club;
		    Integer personID = ((Integer) personTracker.get(identity));
		    PersonMean personMean = compareFile.getPerson(personID);
		    if(personMean != null) {
		        meanField.setText(personMean.getMeanWithComma());
		    } else {
		        meanField.setText("");
		    }
		}
		popup.pack();
		popup.setLocationRelativeTo(frame);
		popup.setVisible(true);
	}
	
	/** raderar alla inmatningar i resultatinmatningsf�nstret */
	private void clearInputs() {
		for(int i = 0; i < varvResult.length; i++) {
			varvResult[i].setText("");
		}
		startNbrField.setText("");
		licenseNbrField.setText("");
		prio.setText("");
	}
	
	/** tar bort en person ifr�n resultatlistan */
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
			SearchWindow.MESSAGEFIELD.setText(identity + " �r nu borttagen fr�n t�vlingen.");
			popup.setVisible(false);
			BOARD.update(false);
		} else {
			JOptionPane.showMessageDialog(popup, "Det gick inte att ta bort personen fr�n t�vlingen");
		}
	}
	
	/** st�ller in j�mf�relselistans data */
	protected void setCompareFile(CompareFile compareFile) {
	    this.compareFile = compareFile;
	}
	
	/** returnerar j�mf�relselistans data */
	protected CompareFile getCompareFile() {
	    return compareFile;
	}
	
	/** klassen som tar hand om knapptryckningarna vid inmatning av resultat */
	class ResultHandler implements ActionListener {
		/** kollar vilken knapp som tryckts ned och utf�r l�mplig handling */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == inputOk && mode == SearchWindow.MODE_COMP) {
				handleResultInput();
			} else if(e.getSource() == inputRemove && mode == SearchWindow.MODE_COMP) {
				removeResult();
			} else if(e.getSource() == inputErase && mode == SearchWindow.MODE_COMP) {
				clearInputs();
			} else if(e.getSource() == inputCancel) {
				popup.setVisible(false);
			} else if(e.getSource() == inputOk && mode == SearchWindow.MODE_SNITT) {
			    handleMeanInput(true);
			} else if(e.getSource() == inputRemove && mode == SearchWindow.MODE_SNITT) {
			    handleMeanInput(false);
			} else if(e.getSource() == inputErase && mode == SearchWindow.MODE_SNITT) {
				meanField.setText("");
			}
		}
	}
	
	/** l�gger till eller tar bort ett snitt */
	private void handleMeanInput(boolean add) {
	    boolean done = false;
	    StringTokenizer str = new StringTokenizer(person.getText(), ",");
	    String name = str.nextToken();
	    String club = str.nextToken().trim();
	    String nameAndClub = name + ", " + club;
	    Integer personID = ((Integer) personTracker.get(nameAndClub));
	    
	    if(add) {
	        String meanFieldValue = meanField.getText();
	        meanFieldValue = meanFieldValue.replace(',', '.');
	        double mean;
	        try {
	            mean = Double.parseDouble(meanFieldValue);
	            if(mean < ResultList.MIN_SCORE || mean > ResultList.MAX_SCORE) {
	                throw new Exception("Ej giltigt snitt");
	            }
	            compareFile.addMean(personID, name, club, mean);
	            done = true;
	            SearchWindow.CHANGE = true;
				SearchWindow.STATUSFIELD.setText("*");
				SearchWindow.MESSAGEFIELD.setText("");
	            BOARD.updateMean(true);
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(popup, "Inget giltigt v�rde!");
	        }
	    } else {
	        done = compareFile.removeMean(personID);
	        if(!done) {
	            JOptionPane.showMessageDialog(popup, "Personen gick ej att ta bort!");;
	        } else {
	            SearchWindow.CHANGE = true;
				SearchWindow.STATUSFIELD.setText("*");
				SearchWindow.MESSAGEFIELD.setText(nameAndClub + ", �r nu borttagen fr�n snittlistan.");
	            BOARD.updateMean(false);
	        }
	    }
	    if(done) {
	        popup.setVisible(false);
	    }
	}
	
	/** klassen som tar hand om tangentbordsinmatningen i s�kf�ltet */
	class EnterKeyHandler extends KeyAdapter {
		/** utf�r l�mplig handling */
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == inputCancel) {
				popup.setVisible(false);
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == inputErase) {
				clearInputs();
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == inputRemove) {
				removeResult();
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == inputOk) {
				handleResultInput();
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER && mode == SearchWindow.MODE_COMP) {
				handleResultInput();
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER && mode == SearchWindow.MODE_SNITT) {
			    handleMeanInput(true);
			}
		}
	}
	
	/** tar hand om indatan fr�n resultatinmatningsf�nstret */
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
					JOptionPane.showMessageDialog(popup, "Angivet licensnummer �r upptaget av " + 
							(String)personNameTracker.get((Integer)licenseIDMap.get(licenseNr)));
				}
			} else {
				good = false;
				JOptionPane.showMessageDialog(popup, "Licensnumret �r inte korrekt!");
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
					JOptionPane.showMessageDialog(popup, "En person med detta startnummer �r redan inlagd i t�vlingen!");
				}
			}
		} catch (NumberFormatException f) {
			good = false;
			JOptionPane.showMessageDialog(popup, "Startnumret �r inget giltigt sifferv�rde!");
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
							JOptionPane.showMessageDialog(popup, "Varvresultat " + (i + 1) + " �r inget giltigt v�rde! Det enda som kan skrivas ist�llet f�r tal �r tecknet \"-\".");
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
				JOptionPane.showMessageDialog(popup, "Kan ej mata in resultat f�r varv d�r f�reg�ende varv saknas!");
			} else if (nbrRoundsToRead != -1) {
			    LinkedList lapList = new LinkedList();
				for(int i = 0; i < nbrRoundsToRead; i++) {
					if((results[i] < ResultList.MIN_SCORE || results[i] > ResultList.MAX_SCORE) 
					        && results[i] != ResultList.NO_RESULT_THIS_ROUND) {
						goodValues = false;
						lapList.add(new Integer(i+1));
					}
				}
				if(goodValues) {
					try {
						prioNr = Integer.parseInt(prio.getText());
					} catch (NumberFormatException f) {
						if(!prio.getText().equals("")) {
							goodValues = false;
							JOptionPane.showMessageDialog(popup, "Prioritetsnumret �r inget giltigt sifferv�rde!");
						}
					}
				} else {
				    String faultyLaps = "";
				    for(int i = 0; i < lapList.size(); i++) {
				        faultyLaps += lapList.get(i).toString();
				        if(i+1 != lapList.size()) {
				            faultyLaps += ", ";
				        }
				    }
					JOptionPane.showMessageDialog(popup, "Resultatet f�r ett varv m�ste ligga mellan 18 och 126!\n" +
							"F�ljande varvs resultat g�r inte det: " + faultyLaps + ".");
				}
				if(goodValues) {
					String klass = (String) klassChoice.getSelectedItem();
					if(klass == null || klass.equals(KlassWindow.NO_KLASS)) {
					    klass = "";
					}
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
	
	/** st�ller in vilka klasser som skall kunna v�ljas efter str�ngvektorn s */
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
	
	/** returnerar HashMapen som h�ller reda p� licensnumren */
	public HashMap getLicenseMap() {
		return licenseMap;
	}
	
	/** returnerar HashMapen som h�ller reda p� id f�r ett visst licensnummer */
	public HashMap getLicenseIDMap() {
		return licenseIDMap;
	}
	
	/** tar bort personen med ID:t personID fr�n licensmapparna */
	public void removeFromLicenseMap(Integer personID) {
	    if(licenseMap.containsKey(personID)) {
	        String licenseNbr = (String) licenseMap.remove(personID);
	        licenseIDMap.remove(licenseNbr);
	    }
	}
	
	/** anpassar licensnumren och resultatlistan till nytt namn eller ny klubb */
	public void updateNameAndClub(String oldName, String oldClub, String newName, String newClub, Integer personID) {
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