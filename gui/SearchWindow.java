package gui;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import snitt.SnittWindow;

import datastruct.CompareFile;
import datastruct.DataManager;
import datastruct.Filter;
import datastruct.IOHandler;
import datastruct.NameList;
import datastruct.ResultList;

import java.util.StringTokenizer;

/** klassen som beskriver sj�lva huvudf�nstret och delen f�r s�kning */
public class SearchWindow {
    private SearchWindow searchWindow;		// detta f�nster
	private HashMap personTracker;			// h�ller reda p� vem personen �r �ven om n�gon persondata �ndras
	private HashMap personNameTracker;		// lagrar namnet som idnumret tillh�r
	private NameList name;					// namnlista som anv�nds f�r s�kning
	private ResultInputWindow resultInput;	// f�nstret f�r indata
	private JFrame frame;					// huvudf�nster
	private JPanel searchPanel;				// panelen f�r s�kf�ltet
	private LapSumDialog lapSumDialog; 		// redigeringsf�nstret
	private JDialog compInfoDialog;			// indataf�nster
	private JTextField searchField,nameField,clubField;	// s�kf�lt, namn- och klubbf�lt f�r namnlistan
	public static JTextField STATUSFIELD,MESSAGEFIELD;	// meddelande- och statusf�lt
	private JButton[] button;							// knapparna med funna personer som matchar s�kningen
	private JButton addPlayerButton,removePlayerButton;	// l�gg till och ta bort spelare
	private JMenuItem newComp, openFromSKV, save;		// meny f�r ny t�vling och �ppna samt spara
	private	JMenuItem saveToHTML, quit, editItem; 		// spara till HTML, avsluta, redigera
	private JMenuItem klassStart, snittStart, about;	// hantera klasser och snittlista, om programmet
	private JMenuItem makeCompareFile, openCompareFile;	// skapar eller �ppnar en j�mf�rande snittlista
	private JMenuItem headerItem, saveAs, saveAsHTML;	// s�tter t�vlingens namn, spara som f�r SKV- och HTML-filer
	private JMenuItem changeName, changeClub;			// �ndra namn och klubb p� en spelare
	private JMenuItem numberOrientation;				// alternativ f�r sifferorienteringen
	private JLabel inputNameLabel;				// inneh�ller namnet p� den person som resultat skrivs in f�r
	private JFileChooser fileChooser;			// filv�ljare
	private Filter skvFilter, htmFilter;		// filter f�r skv- och htmfiler
	private Filter snittFilter;					// filter f�r snittfiler
	private IOHandler io;						// sk�ter skrivning till och l�sning fr�n filer
	private DataManager dataManager;			// h�ller reda p� inst�llningar
	public static boolean SNITTOPEN, KLASSOPEN;	// talar om ifall motsvarande f�nster �r �ppet
	private boolean warningHTM;					// varnar f�r att inte skriva till ej �nskv�rd fil
	private boolean compInfoDialogClosed;		// talar om ifall indataf�nstret har st�ngts ner utan anv�ndande av knappen Ok
	public static boolean CHANGE;				// h�ller reda p� om �ndringar har skett som b�r sparas
	public static Image ICON;					// programmets ikon
	private KlassWindow klassWindow;			// klasshanterarf�nstret
	private SnittWindow snittWindow;			// snittlistshanterarf�nstret
	private String compHeader, fileNameSKV, fileNameHTM;// t�vlingsrubrik, filnamn f�r SKV- och HTML-filerna
	private String fileNameSNITT;				// filnamn f�r SNITT-fil
	public static File DIRSKV, DIRHTM, DIRSNITT, DIRJMF;// mappar f�r olika filtyper
	public static final int DUMMY_STARTUP = 0;	// antal varv som anges vid "dum"-starten
	private int mode;							// talar om vilket l�ger som g�ller
	public static final int MODE_DUMMY = 0;		// heltal f�r "dum"-l�get
	public static final int MODE_COMP = 1;		// heltal f�r l�get d�r vanlig t�vling matas in
	public static final int MODE_SNITT = 2;		// heltal f�r l�get d� man skapar en j�mf�rande snittlista
	
	/** skapar huvudf�nstret */
	public SearchWindow() {
		name = new NameList();
		try {
			name.readNames();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Inl�sningen av namnlistan misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
			name.sortedNames();
		}
		try {
			frame = new JFrame("BangolfResultat");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Programmet gick ej att starta", "Programfel", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		try {
			ImageIcon frameIcon = new ImageIcon("data/brikon.gif");
			ICON = frameIcon.getImage();
			frame.setIconImage(ICON);
		} catch (Exception e) {
		}
		compHeader = "";
		fileNameSKV = null;
		fileNameHTM = null;
		warningHTM = false;
		CHANGE = false;
		
		dataManager = new DataManager();
		if(!dataManager.loadOrientation()) {
		    JOptionPane.showMessageDialog(null, "Tidigare sifferorienteringsinst�llningar gick ej att l�sa in",
                    "Varning", JOptionPane.ERROR_MESSAGE);
		}
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowHandler window = new WindowHandler();
		frame.addWindowListener(window);
		
		JPanel buttonpanel = new JPanel();
		searchPanel = new JPanel();
		button = new JButton[10];
		
		JLabel[] searchLabel = new JLabel[6];
		searchLabel[0] = new JLabel("Registrera, �ndra eller ta bort resultat");
		searchLabel[1] = new JLabel("genom att s�ka upp spelaren i detta f�lt");
		searchLabel[2] = new JLabel("och sedan klicka p� knappen med namnet:");
		searchLabel[3] = new JLabel("�ndra i det s�kbara namnregistret:");
		searchLabel[4] = new JLabel("Namn:");
		searchLabel[5] = new JLabel("Klubb:");
		for(int i = 0; i < 4; i++) {
			searchLabel[i].setPreferredSize(new Dimension(250,15));
		}
		searchLabel[4].setPreferredSize(new Dimension(120,15));
		searchLabel[5].setPreferredSize(new Dimension(120,15));
		
		KeyHandler keyhand = new KeyHandler();
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(240,20));
		searchField.addKeyListener(keyhand);
		
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(120,20));
		clubField = new JTextField();
		clubField.setPreferredSize(new Dimension(120,20));
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		searchPanel.setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0,1,0,1);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(searchLabel[0], c);
		gridbag.setConstraints(searchLabel[1], c);
		gridbag.setConstraints(searchLabel[2], c);
		gridbag.setConstraints(searchField, c);
		gridbag.setConstraints(searchLabel[3], c);
		searchPanel.add(searchLabel[0]);
		searchPanel.add(searchLabel[1]);
		searchPanel.add(searchLabel[2]);
		searchPanel.add(searchField);
		
		SearchHandler searchHand = new SearchHandler();
		ButtonKeyHandler buttonHand = new ButtonKeyHandler();
		int key = KeyEvent.VK_0;
		for(int i = 0; i < 10; i++) {
			button[i] = new JButton();
			button[i].setMnemonic(key);
			key++;
			button[i].setPreferredSize(new Dimension(240,25));
			button[i].setHorizontalAlignment(SwingConstants.LEFT);
			button[i].setFocusable(false);
			button[i].addActionListener(searchHand);
			button[i].addKeyListener(buttonHand);
			gridbag.setConstraints(button[i], c);
			searchPanel.add(button[i]);
		}
		JLabel emptyLabel = new JLabel(" ");
		gridbag.setConstraints(emptyLabel, c);
		searchPanel.add(emptyLabel);
		c.gridwidth = 1;
		gridbag.setConstraints(searchLabel[4], c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(searchLabel[5], c);
		c.gridwidth = 1;
		gridbag.setConstraints(nameField, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(clubField, c);
		c.gridwidth = 1;
		addPlayerButton = new JButton("L�gg till");
		addPlayerButton.setMnemonic(KeyEvent.VK_L);
		addPlayerButton.setPreferredSize(new Dimension(120,25));
		removePlayerButton = new JButton("Ta bort");
		removePlayerButton.setMnemonic(KeyEvent.VK_T);
		removePlayerButton.setPreferredSize(new Dimension(120,25));
		PlayerHandler playHand = new PlayerHandler();
		addPlayerButton.addActionListener(playHand);
		addPlayerButton.addKeyListener(buttonHand);
		removePlayerButton.addActionListener(playHand);
		removePlayerButton.addKeyListener(buttonHand);
		gridbag.setConstraints(addPlayerButton, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(removePlayerButton, c);
		searchPanel.add(searchLabel[3]);
		searchPanel.add(searchLabel[4]);
		searchPanel.add(searchLabel[5]);
		searchPanel.add(nameField);
		searchPanel.add(clubField);
		searchPanel.add(addPlayerButton);
		searchPanel.add(removePlayerButton);
		
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Arkiv");
		menu.setMnemonic(KeyEvent.VK_A);
		JMenu edit = new JMenu("Redigera");
		edit.setMnemonic(KeyEvent.VK_R);
		JMenu klassMenu = new JMenu("Klasser");
		klassMenu.setMnemonic(KeyEvent.VK_K);
		JMenu snittMenu = new JMenu("Snittlista");
		snittMenu.setMnemonic(KeyEvent.VK_S);
		JMenu help = new JMenu("Info");
		help.setMnemonic(KeyEvent.VK_I);
		bar.add(menu);
		bar.add(edit);
		bar.add(klassMenu);
		bar.add(snittMenu);
		bar.add(help);
		MenuHandler menuHand = new MenuHandler();
		newComp = new JMenuItem("Ny t�vling...", KeyEvent.VK_N);
		newComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		openFromSKV = new JMenuItem("�ppna t�vling...", KeyEvent.VK_P);
		openFromSKV.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		save = new JMenuItem("Spara", KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.setEnabled(false);
		saveAs = new JMenuItem("Spara som...", KeyEvent.VK_M);
		saveAs.setEnabled(false);
		saveToHTML = new JMenuItem("Skapa webbsida", KeyEvent.VK_W);
		saveToHTML.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		saveToHTML.setEnabled(false);
		saveAsHTML = new JMenuItem("Skapa webbsida som...", KeyEvent.VK_B);
		saveAsHTML.setEnabled(false);
		quit = new JMenuItem("Avsluta", KeyEvent.VK_A);
		headerItem = new JMenuItem("Ange t�vlingens rubrik...", KeyEvent.VK_R);
		headerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		headerItem.setEnabled(false);
		editItem = new JMenuItem("Delsummor och placering...", KeyEvent.VK_D);
		editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		editItem.setEnabled(false);
		numberOrientation = new JMenuItem("Sifferorientering...", KeyEvent.VK_O);
		numberOrientation.setEnabled(false);
		klassStart = new JMenuItem("Hantera klasser...", KeyEvent.VK_H);
		klassStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
		snittStart = new JMenuItem("Hantera snittlistan...", KeyEvent.VK_H);
		snittStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		makeCompareFile = new JMenuItem("Ny j�mf�rande snittlista...", KeyEvent.VK_N);
		openCompareFile = new JMenuItem("�ppna j�mf�rande snittlista...", KeyEvent.VK_P);
		about = new JMenuItem("Om BangolfResultat...", KeyEvent.VK_O);
		newComp.addActionListener(menuHand);
		saveToHTML.addActionListener(menuHand);
		saveAsHTML.addActionListener(menuHand);
		saveAs.addActionListener(menuHand);
		save.addActionListener(menuHand);
		openFromSKV.addActionListener(menuHand);
		quit.addActionListener(menuHand);
		headerItem.addActionListener(menuHand);
		editItem.addActionListener(menuHand);
		numberOrientation.addActionListener(menuHand);
		klassStart.addActionListener(menuHand);
		snittStart.addActionListener(menuHand);
		makeCompareFile.addActionListener(menuHand);
		openCompareFile.addActionListener(menuHand);
		about.addActionListener(menuHand);
		menu.add(newComp);
		menu.add(openFromSKV);
		menu.add(save);
		menu.add(saveAs);
		menu.add(saveToHTML);
		menu.add(saveAsHTML);
		menu.add(quit);
		edit.add(headerItem);
		edit.add(editItem);
		edit.add(numberOrientation);
		klassMenu.add(klassStart);
		snittMenu.add(snittStart);
		snittMenu.add(makeCompareFile);
		snittMenu.add(openCompareFile);
		help.add(about);
		frame.setJMenuBar(bar);
		
		JMenuBar resBar = new JMenuBar();
		JMenu resMenu = new JMenu("Redigera");
		resMenu.setMnemonic(KeyEvent.VK_R);
		resBar.add(resMenu);
		changeName = new JMenuItem("�ndra namn...", KeyEvent.VK_N);
		changeName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		changeClub = new JMenuItem("�ndra klubb...", KeyEvent.VK_K);
		changeClub.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
		changeName.addActionListener(menuHand);
		changeClub.addActionListener(menuHand);
		resMenu.add(changeName);
		resMenu.add(changeClub);
		
		io = new IOHandler();
		try {
			personTracker = (HashMap) io.load("ptrack");
		} catch (Exception e) {
			personTracker = new HashMap();
			personTracker.put(" ", new Integer(0));
		}
		try {
			personNameTracker = (HashMap) io.load("pnametrack");
		} catch (Exception e) {
			personNameTracker = new HashMap();
		}
		
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());
		MESSAGEFIELD = new JTextField("Starta ny t�vling genom att klicka p� Arkiv -> Ny t�vling...");
		MESSAGEFIELD.setEditable(false);
		MESSAGEFIELD.setFocusable(false);
		STATUSFIELD = new JTextField();
		STATUSFIELD.setEditable(false);
		STATUSFIELD.setHorizontalAlignment(JTextField.CENTER);
		STATUSFIELD.setPreferredSize(new Dimension(20,10));
		STATUSFIELD.setFocusable(false);
		statusPanel.add(MESSAGEFIELD, BorderLayout.CENTER);
		statusPanel.add(STATUSFIELD, BorderLayout.WEST);
		resultInput = new ResultInputWindow(frame, searchPanel, statusPanel, resBar, personTracker, personNameTracker);
		lapSumDialog = new LapSumDialog(frame);
		compInfoDialog = new CompInfoDialog(frame, this);
		compInfoDialog.addWindowListener(new WinHandForCompInfoDialog());
		
		fileChooser = new JFileChooser();
		skvFilter = new Filter(new String[]{"skv"}, "Semikolonseparerad fil");
		htmFilter = new Filter(new String[]{"htm", "html"}, "Webbsida");
		snittFilter = new Filter(new String[]{"snitt"}, "J�mf�relsefil f�r snittlista");
		try {
			DIRSKV = (File) io.load("dirskv");
			DIRHTM = (File) io.load("dirhtm");
			DIRSNITT = (File) io.load("dirsnitt");
			DIRJMF = (File) io.load("dirjmf");
		} catch (Exception e) {
			DIRSKV = null;
			DIRHTM = null;
			DIRSNITT = null;
			DIRJMF = null;
		}
		
		frame.getContentPane().add(searchPanel, BorderLayout.WEST);
		frame.getContentPane().add(statusPanel, BorderLayout.SOUTH);
		dummyStartUp();
		frame.pack();
		frame.setVisible(true);
		searchWindow = this;
	}
	
	/** fixar till menyalternativen f�r skapandet av en j�mf�relsesnittlista */
	private void enableCompareFileMenus() {
	    headerItem.setEnabled(false);
		editItem.setEnabled(false);
		numberOrientation.setEnabled(false);
	    save.setEnabled(true);
		saveAs.setEnabled(true);
		saveToHTML.setEnabled(false);
		saveAsHTML.setEnabled(false);
	}
	
	/** g�r menyalternativen under Redigera och de olika alternativen f�r att spara tillg�ngliga */
	private void enableDisabledMenus() {
	    headerItem.setEnabled(true);
		editItem.setEnabled(true);
		numberOrientation.setEnabled(true);
	    save.setEnabled(true);
		saveAs.setEnabled(true);
		saveToHTML.setEnabled(true);
		saveAsHTML.setEnabled(true);
		setMode(SearchWindow.MODE_COMP, null);
	}
	
	/** st�ller l�get till mode, alternativen �r MODE_COMP, MODE_DUMMY och MODE_SNITT 
	 * 	skickar med ett objekt o som anv�nds vid vissa tillf�llen */
	private void setMode(int mode, Object o) {
	    int oldMode = this.mode;
	    this.mode = mode;
	    if(mode == SearchWindow.MODE_SNITT) {
	        CompareFile compareFile = (CompareFile) o;
	        if(compareFile == null) {
	            compareFile = new CompareFile(newCompareFile());
	        }
	        if(compareFile.getSurface() != -1) {
	            resultInput.setCompareFile(compareFile);
	            resultInput.setupResultInputPanel(mode);
	        } else {
	            this.mode = oldMode;
	        }
	    }
	}
	
	/** startar upp saker f�r att skapa en j�mf�relsesnittlista */
	private int newCompareFile() {
	    int surface = -1;
	    int val = JOptionPane.YES_OPTION;
		if(CHANGE) {
			val = JOptionPane.showConfirmDialog(frame, "De senaste �ndringarna �r ej sparade. Vill du spara nu?"
			, "Spara?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(val == JOptionPane.YES_OPTION) {
				autoSave();
			}
		}
		if(!CHANGE || val == JOptionPane.NO_OPTION) {
		    String[] possibleValues = { "Filt", "EB", "Betong", "Blandad" };
		    String selectedValue = (String) JOptionPane.showInputDialog(frame, "V�lj underlag", "Indata", 
		            JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
			if(selectedValue != null) {
	            if(selectedValue.equals(possibleValues[0])) {
	                surface = ResultList.FILT;
	            } else if(selectedValue.equals(possibleValues[1])) {
	                surface = ResultList.EB;
	            } else if(selectedValue.equals(possibleValues[2])) {
	                surface = ResultList.BETONG;
	            } else if(selectedValue.equals(possibleValues[3])) {
	                surface = SnittWindow.BLANDAD;
	            }
				compHeader = "";
				fileNameSKV = null;
				fileNameHTM = null;
				fileNameSNITT = null;
				warningHTM = false;
				CHANGE = false;
				STATUSFIELD.setText("");
				MESSAGEFIELD.setText("Ny j�mf�relsesnittlista har �ppnats.");
				ScoreBoardWindow.setHeader(compHeader);
				inputNameLabel = resultInput.getNameLabel();
				enableCompareFileMenus();
			}
		}
		return surface;
	}
	
	/** �ppnar en ny t�vling bara f�r att f� ig�ng programmet korrekt */
	private void dummyStartUp() {
	    setMode(SearchWindow.MODE_DUMMY, null);
	    ResultInputWindow.RESULTLIST = new ResultList(2, 0, new boolean[]{false, false});
		ResultInputWindow.BOARD = new ScoreBoardWindow(ResultInputWindow.RESULTLIST);
		frame.getContentPane().add(ResultInputWindow.BOARD.getScrollPane(), BorderLayout.CENTER);
		resultInput.setupResultInputPanel(MODE_DUMMY);
		inputNameLabel = resultInput.getNameLabel();
	}
	
	/** st�ller in resultatinmatningsf�nstret och f�nstret f�r delsummor, init �r true om 
	 *  ingen ny resultatlista beh�ver skapas, boxData[0] talar om ifall startnummer skall vara med,
	 *  boxData[1] s�ger om licensnummer skall vara med, nbrRounds �r antal varv och surface anger
	 *  t�vlingens underlag */
	protected void setupResultInputPanel(boolean init, boolean[] boxData, int nbrRounds, int surface) {
	    resultInput.setupResultInputPanel(init, boxData, nbrRounds, surface);
	    lapSumDialog.setNbrRounds(nbrRounds);
	    lapSumDialog.setEditData(new boolean[nbrRounds-1]);
	}
	
	/** uppdaterar s�kresultaten */
	private void updatePersonMatch() {
		String search = searchField.getText();
		String[] names = name.getNames(search);
		for(int i = 0; i < 10; i++) {
			if(names[i] != null) {
				button[i].setFocusable(true);
				button[i].setText(i + ". " + names[i]);
			}
			else {
				button[i].setFocusable(false);
				button[i].setText("");
			}
		}
	}
	
	/** sk�ter om avslutandet av programmet */
	private void exit() {
		int val = JOptionPane.YES_OPTION;
		if(CHANGE) {
			val = JOptionPane.showConfirmDialog(frame, "De senaste �ndringarna �r ej sparade. Vill du spara nu?"
			, "Spara?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(val == JOptionPane.YES_OPTION) {
				autoSave();
			}
		}
		if(!CHANGE || val == JOptionPane.NO_OPTION) {
			if(SNITTOPEN) {
				snittWindow.exit();
			}
			if(KLASSOPEN) {
				klassWindow.exit();
			}
			save();
			System.exit(0);
		}
	}
	
	/** sparar filer som programmet anv�nder sig av */
	private void save() {
	    if(!dataManager.saveOrientation()) {
	        JOptionPane.showMessageDialog(frame, "Orienteringsinst�llningarna gick ej att spara",
                    "Varning", JOptionPane.ERROR_MESSAGE);
	    }
		try {
			io.save("licensemap", resultInput.getLicenseMap());
			io.save("licensenamemap", resultInput.getLicenseIDMap());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Listan �ver licensnummer gick ej att spara", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		try {
			name.writeNames();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Namnlistan gick ej att spara", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		try {
			io.save("dirskv", DIRSKV);
			io.save("dirhtm", DIRHTM);
			io.save("dirsnitt", DIRSNITT);
			io.save("dirjmf", DIRJMF);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Senast anv�nda mappar gick ej att spara", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		try {
			io.save("ptrack", personTracker);
			io.save("pnametrack", personNameTracker);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Intern ID-lista gick ej att spara", "Varning", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** sparar efter uppmaning */
	private void autoSave() {
		MenuHandler menu = new MenuHandler();
		if(mode == MODE_COMP) {
		    menu.saveToSKV();
		} else if(mode == MODE_SNITT) {
		    menu.saveToSNITT();
		}
	}
	
	/** talar om f�r resultatinmatningsf�nstret vilken person som valts */
	private void setPopup(JButton b) {
	    searchField.selectAll();
		searchField.requestFocus();
		resultInput.setPopup(b, mode);
	}

    /** l�gger till och tar bort personen fr�n namnlistan, l�gger till om add �r true */
	private void handlePlayer(boolean add) {
		boolean done = false;
		boolean visit = false;
		int confirmValue =  JOptionPane.NO_OPTION;
		String nameString = nameField.getText().trim();
		String clubString = clubField.getText().trim();
		if(!nameString.equals("") && !clubString.equals("")) {
			if(add) {
				done = name.add(nameString, clubString);
				if(done) {
					addPerson(nameString + ", " + clubString);
				}
				name.sortedNames();
				visit = true;
			} else {
			    if(name.containsPerson(nameString, clubString)) {
			        confirmValue = JOptionPane.showConfirmDialog(frame, "�r du s�ker p� att du vill ta bort "
			                + nameString + ", " + clubString + ", permanent?");
			        if(confirmValue == JOptionPane.YES_OPTION) {
			            done = name.remove(nameString, clubString);
			            if(done) {
			                resultInput.removeFromLicenseMap((Integer) personTracker.get(nameString + 
			                        ", " + clubString));
			            }
			            name.sortedNames();
			            visit = true;
			        } else if (confirmValue == JOptionPane.NO_OPTION) {
			            nameField.setText("");
			            clubField.setText("");
			        }
			    } else {
			        visit = true;
			    }
			}
		} else {
			JOptionPane.showMessageDialog(frame, "F�lten m�ste inneh�lla mer �n blanksteg!");
		}
		if(done && visit) {
			if(!add) {
				updatePersonMatch();
				save();
				MESSAGEFIELD.setText(nameString + ", " + clubString + ", �r nu borttagen fr�n s�kningsf�ltet.");
			} else {
				updatePersonMatch();
				save();
				MESSAGEFIELD.setText(nameString + ", " + clubString + ", har nu lagts till i s�kningsf�ltet.");
			}
			nameField.setText("");
			clubField.setText("");
		} else if(visit) {
		    if(!add) {
		        JOptionPane.showMessageDialog(frame, nameString + ", " + clubString + 
		        ", gick inte att ta bort fr�n s�kningsf�ltet. Kolla s� att r�tt namn och klubb �r angiven.");		        
		    } else {
		        JOptionPane.showMessageDialog(frame, nameString + ", " + clubString + 
		        ", gick inte att l�gga till i s�kningsf�ltet. Kolla s� att personen inte redan finns d�r.");
		    }
		}
	}
	
	/** l�gger till personen identity i listan �ver idnummer */
	private void addPerson(String identity) {
		Integer i = (Integer) personTracker.get(" ");
		personTracker.put(identity, i);
		personNameTracker.put(i, identity);
		personTracker.put(" ", new Integer(i.intValue() + 1));
	}
	
	/** loggar informationen om Throwable throwable */
	private static void logError(Throwable throwable) {
	    JOptionPane.showMessageDialog(null, "Ett fel i programmet har uppst�tt. " +
        		"Informationen om detta sparas i filen error.log.\n" +
        		"Denna fil finns i katalogen d�r programmet installerades, " +
        		"vanligtvis C:\\Program\\BangolfResultat.\n\n" +
        		"Var v�nlig spara aktivt arbete och starta om programmet.\n" +
        		"Om problem uppst�r igen tag kontakt med programmets tillverkare.",
        		"Varningsmeddelande", JOptionPane.WARNING_MESSAGE);
	    if(IOHandler.logError(throwable)) {
	        JOptionPane.showMessageDialog(null, "Loggningen �r klar.");
	    } else {
	        JOptionPane.showMessageDialog(null, "Loggningen misslyckades.");
	    }
	}
	
	/** mainmetoden som startar hela programmet */
	public static void main(String[] args) {
	    System.setProperty("sun.awt.exception.handler", "gui.SearchWindow$ErrorHandler");
	    SearchWindow window = new SearchWindow();
	}

    /** klassen som tar hand om knapptryckningarna i menyn */
    class MenuHandler implements ActionListener {
    	/** kollar vilket menyalternativ som valts och utf�r l�mplig handling */
    	public void actionPerformed(ActionEvent e) {
    		if(e.getSource() == saveToHTML) {
    			if(fileNameHTM == null) {
    				saveAsHTML();
    			} else {
    				try {
    					boolean doit = true;
    					if(warningHTM) {
    						int val = JOptionPane.showConfirmDialog(frame, "Vill du att webbsidan sparas till filen " + fileNameHTM + "?"
    						, "Spara webbsida", JOptionPane.YES_NO_OPTION);
    						if(val != JOptionPane.YES_OPTION) {
    							doit = false;
    						}
    					}
    					if(doit) {
    					    int align = DataManager.getOrientation(AlignmentWindow.COMP_OWNER);
    						io.outputToHTML(fileNameHTM, resultInput.getResultList(), compHeader, align);
    						warningHTM = false;
    						MESSAGEFIELD.setText("T�vlingen �r sparad som webbsida.");
    					} else {
    						saveAsHTML();
    					}
    				} catch (Exception f) {
    					JOptionPane.showMessageDialog(frame, "Skrivning till HTML-fil misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
    				}
    			}
    		}
    		else if(e.getSource() == saveAsHTML) {
    			saveAsHTML();
    		}
    		else if(e.getSource() == save) {
    		    if(mode == MODE_COMP) {
    		        saveToSKV();
    		    } else if(mode == MODE_SNITT) {
    		        saveToSNITT();
    		    }
    		}
    		else if(e.getSource() == saveAs) {
    		    if(mode == MODE_COMP) {
    		        saveAsSKV();
    		    } else if(mode == MODE_SNITT) {
    		        saveAsSNITT();
    		    }
    		}
    		else if(e.getSource() == openFromSKV) {
    			int val = JOptionPane.YES_OPTION;
    			if(CHANGE) {
    				val = JOptionPane.showConfirmDialog(frame, "De senaste �ndringarna �r ej sparade. Vill du spara nu?"
    				, "Spara?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    				if(val == JOptionPane.YES_OPTION) {
    					autoSave();
    				}
    			}
    			if(!CHANGE || val == JOptionPane.NO_OPTION) {
    				fileChooser.setCurrentDirectory(DIRSKV);
    				fileChooser.setSelectedFile(new File(""));
    				fileChooser.setFileFilter(skvFilter);
    				int retval = fileChooser.showOpenDialog(frame);
    				if(retval == JFileChooser.APPROVE_OPTION) {
    					File file = fileChooser.getSelectedFile();
    					DIRSKV = fileChooser.getCurrentDirectory();
    					fileNameSKV = file.getPath();
    					if(!fileNameSKV.endsWith(".skv")) {
    						fileNameSKV = fileNameSKV + ".skv";
    					}
    					if(new File(fileNameSKV).exists()) {
    					    try {
    					        Object[] o = io.inputFromSKV(fileNameSKV);
    					        compHeader = (String)o[0];
    					        fileNameHTM = (String)o[1];
    					        warningHTM = true;
    					        ResultList result = (ResultList) o[2];
    					        resultInput.init(result);
    					        resultInput.setStartNbrMap((HashMap)o[3]);
    					        lapSumDialog.setNbrRounds(result.getNbrRounds());
    					        lapSumDialog.setEditData((boolean[])o[4]);
    					        ScoreBoardWindow.setHeader(compHeader);
    					        inputNameLabel = resultInput.getNameLabel();
    					        CHANGE = false;
    					        STATUSFIELD.setText("");
    					        MESSAGEFIELD.setText("�ppnat filen: " + fileNameSKV + ".");
    					        enableDisabledMenus();
    					    } catch (Exception f) {
    					        f.printStackTrace();
    					        JOptionPane.showMessageDialog(frame, "Inl�sningen fr�n SKV-fil misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
    					    }
    					} else {
    					    JOptionPane.showMessageDialog(frame, "Filen, " + fileNameSKV + ", existerar inte", "Varning", JOptionPane.ERROR_MESSAGE);
    					}
    				}
    			}
    		}
    		else if(e.getSource() == newComp) {
    			int val = JOptionPane.YES_OPTION;
    			if(CHANGE) {
    				val = JOptionPane.showConfirmDialog(frame, "De senaste �ndringarna �r ej sparade. Vill du spara nu?"
    				, "Spara?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    				if(val == JOptionPane.YES_OPTION) {
    					autoSave();
    				}
    			}
    			if(!CHANGE || val == JOptionPane.NO_OPTION) {
    				compInfoDialog.setLocationRelativeTo(frame);
    				compInfoDialog.setVisible(true);
    				if(!compInfoDialogClosed) {
    					compHeader = "";
    					fileNameSKV = null;
    					fileNameHTM = null;
    					warningHTM = false;
    					CHANGE = false;
    					STATUSFIELD.setText("");
    					MESSAGEFIELD.setText("Ny t�vling har �ppnats.");
    					ScoreBoardWindow.setHeader(compHeader);
    					inputNameLabel = resultInput.getNameLabel();
    					enableDisabledMenus();
    				} else {
    					compInfoDialogClosed = false;
    				}
    			}
    		}
    		else if(e.getSource() == editItem) {
    			lapSumDialog.setEditWindow();
    			lapSumDialog.getContentPane().add(lapSumDialog.lapSumPanel(), BorderLayout.NORTH);
    			lapSumDialog.pack();
    			lapSumDialog.setLocationRelativeTo(frame);
    			lapSumDialog.setVisible(true);
    		}
    		else if(e.getSource() == headerItem) {
    			String tempHeader = JOptionPane.showInputDialog(frame, "Skriv in rubriken f�r t�vlingen", ScoreBoardWindow.getHeader());
    			if(tempHeader != null) {
    				compHeader = tempHeader;
    				ScoreBoardWindow.setHeader(compHeader);
    				CHANGE = true;
    				STATUSFIELD.setText("*");
    				MESSAGEFIELD.setText("");
    			}
    		}
    		else if(e.getSource() == numberOrientation) {
    		    new AlignmentWindow(frame, AlignmentWindow.COMP_OWNER, dataManager);
    		}
    		else if(e.getSource() == klassStart) {
    			if(!KLASSOPEN) {
    				KLASSOPEN = true;
    				klassWindow = new KlassWindow(frame);
    			} else {
    				klassWindow.setVisible(true);
    			}
    		}
    		else if(e.getSource() == snittStart) {
    			if(!SNITTOPEN) {
    				SNITTOPEN = true;
    				snittWindow = new SnittWindow(frame, personNameTracker, dataManager, searchWindow);
    			} else {
    				snittWindow.setVisible(true);
    			}
    		}
    		/** �ppnar f�nster f�r att mata in en j�mf�relsesnittlista */
			else if(e.getSource() == makeCompareFile) {
			    setMode(SearchWindow.MODE_SNITT, null);
			}
			else if(e.getSource() == openCompareFile) {
    			int val = JOptionPane.YES_OPTION;
    			if(CHANGE) {
    				val = JOptionPane.showConfirmDialog(frame, "De senaste �ndringarna �r ej sparade. Vill du spara nu?"
    				, "Spara?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    				if(val == JOptionPane.YES_OPTION) {
    					autoSave();
    				}
    			}
    			if(!CHANGE || val == JOptionPane.NO_OPTION) {
    				fileChooser.setCurrentDirectory(DIRJMF);
    				fileChooser.setSelectedFile(new File(""));
    				fileChooser.setFileFilter(snittFilter);
    				int retval = fileChooser.showOpenDialog(frame);
    				if(retval == JFileChooser.APPROVE_OPTION) {
    					File file = fileChooser.getSelectedFile();
    					DIRJMF = fileChooser.getCurrentDirectory();
    					fileNameSNITT = file.getPath();
    					if(!fileNameSNITT.endsWith(".snitt")) {
    						fileNameSNITT = fileNameSNITT + ".snitt";
    					}
    					if(new File(fileNameSNITT).exists()) {
    					    try {
    					        CompareFile compareFile = io.inputFromSNITT(fileNameSNITT);
    					        setMode(MODE_SNITT, compareFile);
    					        CHANGE = false;
    					        STATUSFIELD.setText("");
    					        MESSAGEFIELD.setText("�ppnat filen: " + fileNameSNITT + ".");
    					        enableCompareFileMenus();
    					    } catch (IOException ioe) {
    					        JOptionPane.showMessageDialog(frame, "Inl�sningen fr�n SNITT-fil misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
    					    }
    					} else {
    					    JOptionPane.showMessageDialog(frame, "Filen, " + fileNameSNITT + ", existerar inte", "Varning", JOptionPane.ERROR_MESSAGE);
    					}
    				}
    			}
    		}
    		else if(e.getSource() == about) {
    			AboutWindow aboutWindow = new AboutWindow(frame);
    		}
    		else if(e.getSource() == quit) {
    			exit();
    		}
    		else if(e.getSource() == changeName) {
    			StringTokenizer inString = new StringTokenizer(inputNameLabel.getText(), ",");
    			String labelName = inString.nextToken();
    			String labelClub = inString.nextToken().trim();
    			String newName = JOptionPane.showInputDialog(frame, "Skriv in nytt namn", labelName);
    			if(newName != null && newName.trim() != "") {
    				if(name.add(newName, labelClub)) {
    					name.remove(labelName, labelClub);
    					name.sortedNames();
    					updatePersonMatch();
    					Integer personID = (Integer) personTracker.get(labelName + ", " + labelClub);
    					personTracker.remove(labelName + ", " + labelClub);
    					personTracker.put(newName + ", " + labelClub, personID);
    					personNameTracker.put(personID, newName + ", " + labelClub);
    					inputNameLabel.setText(newName + ", " + labelClub);
    					resultInput.updateNameAndClub(labelName, labelClub, newName, labelClub, personID);
    					save();
    				} else {
    					JOptionPane.showMessageDialog(frame, "Personen finns redan inlagd!");
    				}
    			}
    		}
    		else if(e.getSource() == changeClub) {
    			StringTokenizer inString = new StringTokenizer(inputNameLabel.getText(), ",");
    			String labelName = inString.nextToken();
    			String labelClub = inString.nextToken().trim();
    			String newClub = JOptionPane.showInputDialog(frame, "Skriv in ny klubb", labelClub);
    			if(newClub != null && newClub.trim() != "") {
    				if(name.add(labelName, newClub)) {
    					name.remove(labelName, labelClub);
    					name.sortedNames();
    					updatePersonMatch();
    					Integer personID = (Integer) personTracker.get(labelName + ", " + labelClub);
    					personTracker.remove(labelName + ", " + labelClub);
    					personTracker.put(labelName + ", " + newClub, personID);
    					personNameTracker.put(personID, labelName + ", " + newClub);
    					inputNameLabel.setText(labelName + ", " + newClub);
    					resultInput.updateNameAndClub(labelName, labelClub, labelName, newClub, personID);
    					save();
    				} else {
    					JOptionPane.showMessageDialog(frame, "Personen finns redan inlagd!");
    				}
    			}
    		}
    	}
    	
    	/** sparar t�vlingen som HTML-fil */
    	private void saveAsHTML() {
    		fileChooser.setCurrentDirectory(DIRHTM);
    		fileChooser.setSelectedFile(new File(""));
    		fileChooser.setFileFilter(htmFilter);
    		int retval = fileChooser.showSaveDialog(frame);
    		if(retval == JFileChooser.APPROVE_OPTION) {
    			File file = fileChooser.getSelectedFile();
    			String filePath = file.getPath();
    			while(filePath.endsWith(".htm") || filePath.endsWith(".skv") || filePath.endsWith(".html")) {
    				if(filePath.endsWith(".html")) {
    					filePath = filePath.substring(0, filePath.length()-5);
    				} else {
    					filePath = filePath.substring(0, filePath.length()-4);
    				}
    			}
    			file = new File(filePath + ".htm");
    			DIRHTM = fileChooser.getCurrentDirectory();
    			int val;
    			if(file.exists()) {
    				val = JOptionPane.showConfirmDialog(frame, "Filen finns redan. Vill du ers�tta den?",
    				"Skriva �ver?", JOptionPane.YES_NO_OPTION);
    			} else {
    				val = JOptionPane.YES_OPTION;
    			}
    			if(val == JOptionPane.YES_OPTION) {
    				fileNameHTM = file.getPath();
    				if(compHeader.equals("")) {
    					val = JOptionPane.showConfirmDialog(frame, "T�vlingen saknar rubrik. Vill du skriva in den nu?",
    					"S�tta rubrik?", JOptionPane.YES_NO_OPTION);
    					if(val == JOptionPane.YES_OPTION) {
    						String tempHeader = JOptionPane.showInputDialog(frame, "Skriv in rubriken f�r t�vlingen", ScoreBoardWindow.getHeader());
    						if(tempHeader != null) {
    							compHeader = tempHeader;
    							ScoreBoardWindow.setHeader(compHeader);
    						}
    					}
    				}
    				try {
    				    int align = DataManager.getOrientation(AlignmentWindow.COMP_OWNER);
    					io.outputToHTML(fileNameHTM, resultInput.getResultList(), compHeader, align);
    					warningHTM = false;
    					CHANGE = true;
    					MESSAGEFIELD.setText("T�vlingen �r sparad som webbsida.");
    					STATUSFIELD.setText("*");
    				} catch (Exception f) {
    					JOptionPane.showMessageDialog(frame, "Skrivning till HTML-fil misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
    				}
    			}
    		}
    	}
    	
    	/** sparar t�vlingen */
    	private void saveToSKV() {
    		if(fileNameSKV == null) {
    			saveAsSKV();
    		} else {
    			try {
    				io.outputToSKV(fileNameSKV, fileNameHTM, resultInput.getResultList(), compHeader, lapSumDialog.getEditData());
    				CHANGE = false;
    				MESSAGEFIELD.setText("T�vlingen �r sparad.");
    				STATUSFIELD.setText("");
    			} catch (Exception f) {
    				JOptionPane.showMessageDialog(frame, "Skrivning till SKV-fil misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
    			}
    		}
    	}
    	
    	/** sparar t�vlingen under angivet filnamn */
    	private void saveAsSKV() {
    		fileChooser.setCurrentDirectory(DIRSKV);
    		fileChooser.setSelectedFile(new File(""));
    		fileChooser.setFileFilter(skvFilter);
    		int retval = fileChooser.showSaveDialog(frame);
    		if(retval == JFileChooser.APPROVE_OPTION) {
    			File file = fileChooser.getSelectedFile();
    			String filePath = file.getPath();
    			while(filePath.endsWith(".htm") || filePath.endsWith(".skv") || filePath.endsWith(".html")) {
    				if(filePath.endsWith(".html")) {
    					filePath = filePath.substring(0, filePath.length()-5);
    				} else {
    					filePath = filePath.substring(0, filePath.length()-4);
    				}
    			}
    			file = new File(filePath + ".skv");
    			DIRSKV = fileChooser.getCurrentDirectory();
    			int val;
    			if(file.exists()) {
    				val = JOptionPane.showConfirmDialog(frame, "Filen finns redan. Vill du ers�tta den?",
    				"Skriva �ver?", JOptionPane.YES_NO_OPTION);
    			} else {
    				val = JOptionPane.YES_OPTION;
    			}
    			if(val == JOptionPane.YES_OPTION) {
    				fileNameSKV = file.getPath();
    				try {
    					io.outputToSKV(fileNameSKV, fileNameHTM, resultInput.getResultList(), compHeader, lapSumDialog.getEditData());
    					CHANGE = false;
    					MESSAGEFIELD.setText("T�vlingen �r sparad.");
    					STATUSFIELD.setText("");
    				} catch (Exception f) {
    					JOptionPane.showMessageDialog(frame, "Skrivning till SKV-fil misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
    				}
    			}
    		}
    	}
    	
    	/** sparar den inmatade snittlistan */
    	private void saveToSNITT() {
    	    if(fileNameSNITT == null) {
    			saveAsSNITT();
    		} else {
    			try {
    				io.outputToSNITT(fileNameSNITT, resultInput.getCompareFile());
    				CHANGE = false;
    				MESSAGEFIELD.setText("J�mf�relsesnittlistan �r sparad.");
    				STATUSFIELD.setText("");
    			} catch (Exception f) {
    				JOptionPane.showMessageDialog(frame, "Skrivning till SNITT-fil misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
    			}
    		}
    	}
    	
    	/** sparar den inmatade snittlistan under angivet filnamn */
    	private void saveAsSNITT() {
    	    fileChooser.setCurrentDirectory(DIRJMF);
    		fileChooser.setSelectedFile(new File(""));
    		fileChooser.setFileFilter(snittFilter);
    		int retval = fileChooser.showSaveDialog(frame);
    		if(retval == JFileChooser.APPROVE_OPTION) {
    			File file = fileChooser.getSelectedFile();
    			String filePath = file.getPath();
    			while(filePath.endsWith(".htm") || filePath.endsWith(".skv") || filePath.endsWith(".html")
    			        || filePath.endsWith(".snitt")) {
    			    if(filePath.endsWith(".snitt")) {
    					filePath = filePath.substring(0, filePath.length()-6);
    				} else if(filePath.endsWith(".html")) {
    					filePath = filePath.substring(0, filePath.length()-5);
    				} else {
    					filePath = filePath.substring(0, filePath.length()-4);
    				}
    			}
    			file = new File(filePath + ".snitt");
    			DIRJMF = fileChooser.getCurrentDirectory();
    			int val;
    			if(file.exists()) {
    				val = JOptionPane.showConfirmDialog(frame, "Filen finns redan. Vill du ers�tta den?",
    				"Skriva �ver?", JOptionPane.YES_NO_OPTION);
    			} else {
    				val = JOptionPane.YES_OPTION;
    			}
    			if(val == JOptionPane.YES_OPTION) {
    				fileNameSNITT = file.getPath();
    				try {
    					io.outputToSNITT(fileNameSNITT, resultInput.getCompareFile());
    					CHANGE = false;
    					MESSAGEFIELD.setText("J�mf�relsesnittlistan �r sparad.");
    					STATUSFIELD.setText("");
    				} catch (Exception f) {
    					JOptionPane.showMessageDialog(frame, "Skrivning till SNITT-fil misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
    				}
    			}
    		}
    	}
    }

    /** klassen som tar hand om knapptryckningarna vid s�kning */
    class SearchHandler implements ActionListener {
    	/** kollar vilken knapp som tryckts ned */
    	public void actionPerformed(ActionEvent e) {
    		for(int i = 0; i < 10; i++) {
    			if(e.getSource()==button[i] && !button[i].getText().equals("")){
    				setPopup(button[i]);
    			}
    		}
    	}
    }

    /** klassen som tar hand om knapptryckningarna vid �ndring av namnregistret */
    class PlayerHandler implements ActionListener {
    	/** kollar vilken knapp som tryckts ned */
    	public void actionPerformed(ActionEvent e) {
    		if(e.getSource() == addPlayerButton) {
    			handlePlayer(true);
    		} else if (e.getSource() == removePlayerButton) {
    			handlePlayer(false);
    		}
    	}
    }

    /** klassen som tar hand om tangentbordsinmatningen i s�kf�ltet */
    class KeyHandler extends KeyAdapter {
    	private int searchFieldLength = 0;	// h�ller reda p� l�ngden p� s�kf�ltet
    	
    	/** utf�r l�mplig handling */
    	public void keyReleased(KeyEvent e) {
    		/** om tecknet som matas in inte ligger mellan 0 och 9 s� 
    		 *  uppdateras tr�ffarna efter v�rdet i s�kf�ltet searchField
    		 */
    		if(e.getKeyChar() < '0' || e.getKeyChar() > '9') {
    			searchFieldLength = searchField.getText().length();
    			updatePersonMatch();
    		/** annars st�lls s�kf�ltets v�rde tillbaka till det ursprungliga */
    		} else {
    			String search = searchField.getText();
    			if(search.length() >= searchFieldLength) {
    			    searchField.setText(search.substring(0, searchFieldLength));
    			}
    		}
    	}
    	
    	/** ser till s� att resultatinmatningsf�nstret poppar upp n�r 
    	 * 	man trycker p� n�gon av knapparna 0 till 9 i s�kf�ltet
    	 */
    	public void keyPressed(KeyEvent e) {
    		if((e.getKeyChar() >= '0' && e.getKeyChar() <= '9')) {
    			searchField.setEditable(false);
    			int keyNbr = Integer.parseInt(String.valueOf(e.getKeyChar()));
    			if(!button[keyNbr].getText().equals("")) {
    				setPopup(button[keyNbr]);
    			}
    			searchField.setEditable(true);
    		}
    	}
    }

    /** klassen som tar hand om tangentbordsinmatningen p� s�kknapparna */
    class ButtonKeyHandler extends KeyAdapter {
    	/** utf�r l�mplig handling */
    	public void keyPressed(KeyEvent e) {
    		JButton pressedButton = (JButton) e.getSource();
    		if((e.getKeyChar() >= '0' && e.getKeyChar() <= '9')) {
    			int keyNbr = Integer.parseInt(String.valueOf(e.getKeyChar()));
    			if(!button[keyNbr].getText().equals("")) {
    				setPopup(button[keyNbr]);
    			}
    		} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
    			if(pressedButton == addPlayerButton) {
    				handlePlayer(true);
    			} else if(pressedButton == removePlayerButton) {
    				handlePlayer(false);
    			} else if(!pressedButton.getText().equals("")) {
    				setPopup(pressedButton);
    			}
    		} 
    	}
    }

    /** klassen som sk�ter f�nsterhanteringen i huvudf�nstret */
    class WindowHandler extends WindowAdapter {
    	/** st�nger ned f�nstret */
    	public void windowClosing(WindowEvent e) {
    		exit();
    	}
    }

    /** klassen som sk�ter f�nsterhanteringen f�r f�nstret d�r man matar in t�vlingsinst�llningarna */
    class WinHandForCompInfoDialog extends WindowAdapter {
    	/** st�nger ned f�nstret */
    	public void windowClosing(WindowEvent e) {
    		compInfoDialogClosed = true;
    		((CompInfoDialog)e.getSource()).setVisible(false);
    	}
    }
    
    /** klassen som tar hand om fel som uppst�r */
    public static class ErrorHandler {
        /** tar hand om felet som uppst�r */
        public void handle(Throwable t) {
            logError(t);
        }
    }
}