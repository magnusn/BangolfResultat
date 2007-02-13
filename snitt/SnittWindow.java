package snitt;

import gui.AlignmentWindow;
import gui.ListPanel;
import gui.SearchWindow;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import java.util.HashMap;
import java.util.LinkedList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

import datastruct.DataManager;
import datastruct.Filter;
import datastruct.IOHandler;
import datastruct.PersonResult;
import datastruct.ResultList;

/** klassen som beskriver fönstret för snittlistshanteringen */
public class SnittWindow extends JFrame {
    private DataManager dataManager;		// håller kolla på vissa inställningar
    private SnittData snittData;			// lagrar inställningarna för snittlistorna
	private HashMap fileMap;				// datastruktur för att lagra filernas namn och sökväg
	private HashMap personNameTracker;		// håller reda på vilket namn ID-numret tillhör
	private JFrame frame;					// snittlistsfönstret
	private JTabbedPane tab;				// tab för de olika snitthanterarpanelerna
	private JTextField messageField;		// informationsfält
	private ListPanel[] snittList;			// filt, eb, betong, blandad;
	private JFileChooser fileChooser;		// filväljare
	private IOHandler io;					// sköter in- och utdata
	private Filter skvFilter, htmFilter;	// filter för skv- och htmfiler
	private Filter snittFilter;				// filter för snitt-filer
	private JMenuItem addComp, removeComp;	// menyalternativ för att lägga till och ta bort tävling
	private JMenuItem saveToHTML, quit;		// spara snittlistan som webbsida och avsluta
	private JMenuItem saveCompareFile;		// sparar en snittlista som kan användas vid jämförelse
	private JMenuItem sort, classStarts; 	// välja sorteringsordning, ta reda på antal klasstarter
	private JMenuItem setTabTitle;			// sätter fliktitel
	private JMenuItem appearance;			// ställer in vad som skall visas på snittlistans webbsida
	private JMenuItem compareFileChooser;	// väljer fil att jämföra snittet med
	private JMenuItem numberAlignment;		// för att ställa in sifferorienteringen
	private JMenuItem setClubs;				// ställer in vilka klubbar som skall vara med i snittlistan
	private String[] headers;				// rubriker för snittlistorna
	private String[] tabTitles;				// fliktitlar
	private HashSet[] excludedClubs;		// klubbar som inte skall vara med i snittlistan
	public static final int BLANDAD = 10; 	// underlagets heltalsvärde vid snittlista för flera underlag
	protected static final int NBR_SNITT=6; // antal snittlistor som kan hanteras
	
	/** skapar snitthanterarfönstret */
	public SnittWindow(JFrame owner, HashMap personNameTracker, 
	        DataManager dataManager) {
		super("Snittlistshanteraren");
		frame = this;
		this.personNameTracker = personNameTracker;
		this.dataManager = dataManager;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(SearchWindow.ICON);
		
		fileChooser = new JFileChooser();
		skvFilter = new Filter(new String[]{"skv"}, "Semikolonseparerad fil");
		htmFilter = new Filter(new String[]{"htm", "html"}, "Webbsida");
		snittFilter = new Filter(new String[]{"snitt"}, "Jämförelsefil för snittlista");
		snittList = new ListPanel[NBR_SNITT];
		io = new IOHandler();
		try {
			headers = (String[]) io.load("snittstring");
			if (headers.length != NBR_SNITT) {
				String[] tempHeaders = new String[NBR_SNITT];
				for (int i = 0; i < tempHeaders.length; ++i) {
					if (headers.length > i)
						tempHeaders[i] = headers[i];
					else
						tempHeaders[i] = new String();
				}
				headers = tempHeaders;
			}
		} catch (Exception e) {
			headers = new String[NBR_SNITT];
			for(int i = 0; i < headers.length; i++) {
				headers[i] = new String();
			}
		}
		try {
			tabTitles = (String[]) io.load("snittitle");
			if (tabTitles.length != NBR_SNITT) {
				String[] tempTitles = new String[NBR_SNITT];
				for (int i = 0; i < tempTitles.length; ++i) {
					if (tabTitles.length > i)
						tempTitles[i] = tabTitles[i];
					else
						tempTitles[i] = new String("Snitt " + (i+1));
				}
				tabTitles = tempTitles;
			}
		} catch (Exception e) {
			tabTitles = new String[NBR_SNITT];
			for(int i = 0; i < tabTitles.length; i++) {
				tabTitles[i] = new String("Snitt " + (i+1));
			}
		}
		try {
			excludedClubs = (HashSet[]) io.load("snittclub");
			if (excludedClubs.length != NBR_SNITT) {
				HashSet[] tempClubs = new HashSet[NBR_SNITT];
				for (int i = 0; i < tempClubs.length; ++i) {
					if (excludedClubs.length > i)
						tempClubs[i] = excludedClubs[i];
					else
						tempClubs[i] = new HashSet();
				}
				excludedClubs = tempClubs;
			}
		} catch (Exception e) {
			excludedClubs = new HashSet[NBR_SNITT];
			for(int i = 0; i < excludedClubs.length; i++) {
				excludedClubs[i] = new HashSet();
			}
		}
		try {
			Vector[] v = io.readFileList("snitt", NBR_SNITT * 2);
			for(int i = 0; i < snittList.length; i++) {
				snittList[i] = new ListPanel(v[i*2], v[i*2+1]);
			}
			fileMap = (HashMap) io.load("snittmap");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Inläsningen av fillistan misslyckades",
					"Varning", JOptionPane.ERROR_MESSAGE);
			for(int i = 0; i < snittList.length; i++) {
				snittList[i] = new ListPanel(new Vector(), new Vector());
			}
			fileMap = new HashMap();
		}
		for(int i = 0; i < snittList.length; i++) {
			snittList[i].setSelectionText("Ej valda tävlingar:");
			snittList[i].setSelectedText("Tävlingar valda för snittlistan:");
		}
		
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Arkiv");
		menu.setMnemonic(KeyEvent.VK_A);
		JMenu edit = new JMenu("Redigera");
		edit.setMnemonic(KeyEvent.VK_R);
		JMenu compute = new JMenu("Beräkna");
		compute.setMnemonic(KeyEvent.VK_B);
		bar.add(menu);
		bar.add(edit);
		bar.add(compute);
		MenuHandler menuHand = new MenuHandler();
		addComp = new JMenuItem("Lägg till tävling...", KeyEvent.VK_L);
		addComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		removeComp = new JMenuItem("Ta bort markerade tävlingar", KeyEvent.VK_T);
		removeComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		saveToHTML = new JMenuItem("Spara snittlistan som webbsida...", KeyEvent.VK_S);
		saveToHTML.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveCompareFile = new JMenuItem("Spara snittlista att jämföra med...", KeyEvent.VK_J);
		saveCompareFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.CTRL_MASK));
		quit = new JMenuItem("Stäng snitthanterarfönstret", KeyEvent.VK_G);
		setTabTitle = new JMenuItem("Fliktitel...", KeyEvent.VK_F);
		appearance = new JMenuItem("Utseende...", KeyEvent.VK_U);
		sort = new JMenuItem("Sorteringsordning...", KeyEvent.VK_S);
		compareFileChooser = new JMenuItem("Välj fil att jämföra med...", KeyEvent.VK_V);
		numberAlignment = new JMenuItem("Sifferorientering...", KeyEvent.VK_O);
		setClubs = new JMenuItem("Klubbar...", KeyEvent.VK_K);
		classStarts = new JMenuItem("Klasstarter", KeyEvent.VK_K);
		addComp.addActionListener(menuHand);
		removeComp.addActionListener(menuHand);
		saveToHTML.addActionListener(menuHand);
		saveCompareFile.addActionListener(menuHand);
		quit.addActionListener(menuHand);
		setTabTitle.addActionListener(menuHand);
		appearance.addActionListener(menuHand);
		sort.addActionListener(menuHand);
		compareFileChooser.addActionListener(menuHand);
		numberAlignment.addActionListener(menuHand);
		setClubs.addActionListener(menuHand);
		classStarts.addActionListener(menuHand);
		menu.add(addComp);
		menu.add(removeComp);
		menu.addSeparator();
		menu.add(saveToHTML);
		menu.add(saveCompareFile);
		menu.addSeparator();
		menu.add(quit);
		edit.add(setTabTitle);
		edit.addSeparator();
		edit.add(appearance);
		edit.add(numberAlignment);
		edit.add(sort);
		edit.add(setClubs);
		edit.addSeparator();
		edit.add(compareFileChooser);
		compute.add(classStarts);
		setJMenuBar(bar);
		
		WindowHandler winHand = new WindowHandler();
		frame.addWindowListener(winHand);
		tab = new JTabbedPane();
		for (int i = 0; i < NBR_SNITT; ++i)
			tab.addTab(tabTitles[i], snittList[i]);
		
		messageField = new JTextField();
		messageField.setEditable(false);
		messageField.setFocusable(false);
		getContentPane().add(tab, BorderLayout.CENTER);
		getContentPane().add(messageField, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
		
		snittData = new SnittData(tab.getTabCount());
		if(!snittData.readCompareFiles()) {
		    JOptionPane.showMessageDialog(frame, "Jämförelsefilernas adresser kunde ej läsas in",
		            "Varning", JOptionPane.ERROR_MESSAGE);
		}
		// måste göras efter readCompareFiles() för att fungera korrekt
		if(!snittData.readAppearanceSettings()) {
		    JOptionPane.showMessageDialog(frame, "Föregående inställningar för snittlistans utseende gick ej" +
		    		" att läsa in", "Varning", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * visar meddelandet message i statusfältet
	 * @param message - meddelandet som visas längst ner i snittlistsfönstret
	 */
	private void setMessage(String message, boolean showTime) {
	    String time;
	    if(showTime) {
	        long currentTime = System.currentTimeMillis();
		    Date date = new Date(currentTime);
		    time = date.toString();
		    time = time.substring(time.indexOf(":") - 2, time.lastIndexOf(":") + 3);
	    } else {
	        time = "";
	    }
	    messageField.setText(time + " " + message);
	}
	
	/** klassen som tar hand om knapptryckningarna i menyn */
	class MenuHandler implements ActionListener {
		/** kollar vilket menyalternativ som valts och utför lämplig handling */
		public void actionPerformed(ActionEvent e) {
			ListPanel listPanel = (ListPanel)tab.getSelectedComponent();
			Vector selected = listPanel.getSelected();
			/** sparar snittlistan som webbsida */
			if(e.getSource() == saveToHTML) {
				if(selected.size() != 0) {
					fileChooser.setCurrentDirectory(SearchWindow.DIRSNITT);
					fileChooser.setSelectedFile(new File(""));
					fileChooser.setFileFilter(htmFilter);
					int retval = fileChooser.showSaveDialog(frame);
					if(retval == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						SearchWindow.DIRSNITT = fileChooser.getCurrentDirectory();
						int val;
						if(file.exists()) {
							val = JOptionPane.showConfirmDialog(frame, "Filen finns redan. Vill du ersätta den?",
									"Skriva över?", JOptionPane.YES_NO_OPTION);
						} else {
							val = JOptionPane.YES_OPTION;
						}
						if(val == JOptionPane.YES_OPTION) {
							String inputHeader = JOptionPane.showInputDialog(frame, "Skriv in snittlistans rubrik",
									headers[tab.getSelectedIndex()]);
							if(inputHeader == null) {
								inputHeader = "";
							}
							makeSnitt(file.getPath(), inputHeader, listPanel);
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Inga tävlingar har valts! Snittlista gick ej att skapa."
							, "Varning", JOptionPane.ERROR_MESSAGE);
				}
			}
			/** sparar snittlistan som jämförelsefil */
			if(e.getSource() == saveCompareFile) {
				if(selected.size() != 0) {
					fileChooser.setCurrentDirectory(SearchWindow.DIRJMF);
					fileChooser.setSelectedFile(new File(""));
					fileChooser.setFileFilter(snittFilter);
					int retval = fileChooser.showSaveDialog(frame);
					if(retval == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						SearchWindow.DIRJMF = fileChooser.getCurrentDirectory();
						int val;
						if(file.exists()) {
							val = JOptionPane.showConfirmDialog(frame, "Filen finns redan. Vill du ersätta den?",
									"Skriva över?", JOptionPane.YES_NO_OPTION);
						} else {
							val = JOptionPane.YES_OPTION;
						}
						if(val == JOptionPane.YES_OPTION) {
							makeCompareFile(file.getPath(), listPanel);
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Inga tävlingar har valts! Snittlista gick ej att skapa."
							, "Varning", JOptionPane.ERROR_MESSAGE);
				}
			}
			/** lägger till tävlingar */
			else if(e.getSource() == addComp) {
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(SearchWindow.DIRSKV);
				fileChooser.setFileFilter(skvFilter);
				int retval = fileChooser.showOpenDialog(frame);
				if(retval == JFileChooser.APPROVE_OPTION) {
					File[] file = fileChooser.getSelectedFiles();
					SearchWindow.DIRSKV = fileChooser.getCurrentDirectory();
					for(int i = 0; i < file.length; i++) {
						String name = file[i].getName();
						String path = file[i].getPath();
						
						boolean contains = false;
						if(listPanel.contains(name)) {
						    contains = true;
						}
						if(!contains) {
							if(!fileMap.containsKey(name) || ((String) fileMap.get(name)).equals(path)) {
								try {
									int tabIndex = tab.getSelectedIndex();
									if (tabIndex != -1) {
										snittList[tabIndex].add(name);
									} else {
										JOptionPane.showMessageDialog(frame, "Filen " + name + " går ej att lägga till. " +
												"Kontrollera att en flik är vald i Snittlistshanteraren.", 
												"Varning", JOptionPane.ERROR_MESSAGE);
									}
									fileMap.put(name, path);
								} catch (Exception f) {
									JOptionPane.showMessageDialog(frame, "Filen " + name + " gick inte att lägga till", 
											"Varning", JOptionPane.ERROR_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(frame, "En fil med namnet " + name + " finns redan men med en annan sökväg. " +
										"Filen gick ej att lägga till.", "Varning", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(frame, "En fil med namnet " + name + " finns redan listad.",
									"Varning", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				fileChooser.setMultiSelectionEnabled(false);
				setMessage("", false);
			}
			/** tar bort tävlingar */
			else if(e.getSource() == removeComp) {
				if((listPanel.getPossibleSelected().length != 0 && ListPanel.POSSIBLEINFOCUS) ||
					 (listPanel.getSelectedSelected().length != 0 && !ListPanel.POSSIBLEINFOCUS)) {
					int val = JOptionPane.showConfirmDialog(frame, "Vill du ta bort markerade tävlingar ifrån listan?", "Ta bort ifrån listan"
							, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(val == JOptionPane.YES_OPTION) {
						Object[] object;
						if(ListPanel.POSSIBLEINFOCUS) {
							object = listPanel.getPossibleSelected();
							listPanel.remove(object);
							for(int i = 0; i < object.length; i++) {
								boolean contains = false;
								for(int j = 0; j < snittList.length && !contains; j++) {
									if(snittList[j].contains(object[i])) {
										contains = true;
									}
								}
								if(!contains) {
									fileMap.remove((String)object[i]);
								}
							}
						} else {
							object = listPanel.getSelectedSelected();
							listPanel.remove(object);
							for(int i = 0; i < object.length; i++) {
								boolean contains = false;
								for(int j = 0; j < snittList.length && !contains; j++) {
									if(snittList[j].contains(object[i])) {
										contains = true;
									}
								}
								if(!contains) {
									fileMap.remove((String)object[i]);
								}
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Inga tävlingar har markerats",
							"Tävlingar ej borttagna", JOptionPane.ERROR_MESSAGE);
				}
				setMessage("", false);
			}
			/** stänger fönstret */
			else if(e.getSource() == quit) {
				exit();
			}
			/** ställer in fliktiteln */
			else if(e.getSource() == setTabTitle) {
				int tabIndex = tab.getSelectedIndex();
				if (tabIndex == -1)
					return;
				String tempTitle = JOptionPane.showInputDialog(frame,
						"Skriv in önskad titel på fliken", tabTitles[tabIndex]);
    			if(tempTitle != null) {
    				tabTitles[tabIndex] = tempTitle;
    				tab.setTitleAt(tabIndex, tabTitles[tabIndex]);
    				pack();
    			}
			    setMessage("", false);
			}
			/** visar fönster för att ställa in snittlistans utseende */
			else if(e.getSource() == appearance) {
			    new AppearanceWindow(frame, tab.getSelectedIndex(), snittData);
			    setMessage("", false);
			}
			/** visar fönster för att ställa in justeringen av fälten med siffror */
			else if(e.getSource() == numberAlignment) {
			    new AlignmentWindow(frame, AlignmentWindow.SNITT_OWNER, dataManager);
			    setMessage("", false);
			}
			/** öppnar fönstret som används för att bestämma sorteringsordningen */
			else if(e.getSource() == sort) {
				new SortWindow(frame, tab.getSelectedIndex(), tab.getTabCount());
				setMessage("", false);
			}
			/** visar fönster för att välja vilka klubbar som skall tas med */
			else if(e.getSource() == setClubs) {
				TreeMap clubMap = new TreeMap();
				Iterator it = selected.iterator();
				Vector fileError = new Vector();
				String filePath = "";
				while (it.hasNext()) {
					try {
						filePath = (String) fileMap.get(it.next());
						ResultList res = (ResultList) io.inputFromSKV(filePath)[2];
						Iterator prIt = res.iterator();
						while (prIt.hasNext()) {
							PersonResult pr = (PersonResult) prIt.next();
							String club = pr.getClub().trim();
							if (!clubMap.containsKey(club.toLowerCase())) {
								clubMap.put(club.toLowerCase(), club);
							}
						}
					} catch (IOException ioe) {
						fileError.add(filePath);
					}
				}
				if (fileError.size() > 0) {
					showReadFileError(fileError);
					return;
				}
				int size = clubMap.size();
				String[] clubs = new String[size];
				it = clubMap.values().iterator();
				for (int i  = 0; i < size; ++i)
					clubs[i] = (String) it.next();
				new ClubWindow(frame, tab.getSelectedIndex(), clubs, excludedClubs);
				setMessage("", false);
			}
			/** väljer fil att jämföra med */
			else if(e.getSource() == compareFileChooser) {
			    new CompareWindow(frame, tab.getSelectedIndex(), snittData);
			    setMessage("", false);
			}
			/** tar fram antal starter i de olika klasserna */
			else if(e.getSource() == classStarts) {
				Snitt snitt = new Snitt("notused.htm");
				boolean readOk = true;
				if(selected.size() != 0) {
					String[] fileNames = new String[selected.size()];
					for(int i = 0; i < fileNames.length; i++) {
						String file = (String) selected.get(i);
						try {
							fileNames[i] = (String) fileMap.get(file);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(frame, "Inläsningen av filen " + file + " misslyckades." +
									"\nBeräkningen gick ej att utföra.", "Varning", JOptionPane.ERROR_MESSAGE);
							readOk = false;
							break;
						}
					}
					if(readOk) {
						HashMap map = new HashMap();
						try {
							map = snitt.countClasses(fileNames);
						}
						catch (IOException ioe) {
							JOptionPane.showMessageDialog(frame, "Inläsningen av filerna misslyckades." +
									"\nBeräkningen gick ej att utföra.", "Varning", JOptionPane.ERROR_MESSAGE);
							readOk = false;
						}
						if(readOk) {
						    LinkedList list = new LinkedList();
							list.addAll(map.keySet());
							Collections.sort(list);
							Iterator iterator = list.iterator();
							String classCount = "";
							while(iterator.hasNext()) {
								String entry = (String)iterator.next();
								classCount = classCount + entry + ": " + map.get(entry) + "\n";
							}
							JOptionPane.showMessageDialog(frame, classCount,
									"Klasstarter", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Beräkningen gick ej att utföra " +
							"eftersom inga tävlingar har markerats.", "Varning", JOptionPane.ERROR_MESSAGE);
				}
				setMessage("", false);
			}
		}
	}
	
	/** producerar en snittlista med filnamnet fileName och rubriken header 
	 *  där filerna som inläses tas från ListPanel listPanel */
	private void makeSnitt(String fileName, String header, ListPanel listPanel) {
	    int tabIndex = tab.getSelectedIndex();
	    if (tabIndex == -1) {
	    	JOptionPane.showMessageDialog(frame, "En flik måste vara vald.",
	    			"Varning", JOptionPane.ERROR_MESSAGE);
	    	return;
	    }
	    
		Snitt snitt = new Snitt(fileName, header, tabIndex);
		int surface = -1;
		int compareSurface = -1;
		boolean readOk = true;
		
		headers[tabIndex] = header;
		Vector v = listPanel.getSelected();
		String[] fileNames = new String[v.size()];
		Vector fileError = new Vector();
		for(int i = 0; i < fileNames.length; i++) {
			String file = (String) v.get(i);
			try {
				fileNames[i] = (String) fileMap.get(file);
				int readSurface = io.readSurface(fileNames[i]);
				if(surface == -1) {
					surface = readSurface;
				} else if(surface != readSurface && surface != BLANDAD) {
					surface = BLANDAD;
				}
			} catch (IOException e) {
				fileError.add(fileNames[i]);
				readOk = false;
			}
		}
		if(readOk) {
			try {
				snitt.readResults(fileNames, personNameTracker);
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "Inläsningen av filerna misslyckades",
						"Varning", JOptionPane.ERROR_MESSAGE);
				readOk = false;
			}
			if(readOk) {
			    String compareFile = snittData.getCompareFile(tab.getSelectedIndex());
			    if(compareFile != null) {
			        try {
			            File file = new File(compareFile);
			            if(file.exists()) {
			                compareSurface = snitt.readCompareFile(compareFile);
			            } else {
			                JOptionPane.showMessageDialog(frame, "Vald jämförelsefil existerar " +
			                		"inte", "Varning", JOptionPane.ERROR_MESSAGE);
			                readOk = false;
			            }
			        } catch (IOException ioe) {
			            JOptionPane.showMessageDialog(frame, "Inläsningen av snittlista att " +
			            		"jämföra med misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
			            readOk = false;
			        }
			    }
			}
			if(readOk) {
				LinkedList list = snitt.sortMap(excludedClubs[tabIndex]);
				try {
				    JCheckBox[] headerCheckBox = snittData.getAppearanceHeaders(tabIndex);
				    boolean[] headerList = new boolean[headerCheckBox.length];
				    for(int i = 0; i < headerCheckBox.length; i++) {
				        headerList[i] = headerCheckBox[i].isSelected();
				    }
				    int align = DataManager.getOrientation(AlignmentWindow.SNITT_OWNER);
					snitt.outputToHTML(list, surface, compareSurface, headerList, align);
					setMessage("Snittlistan är sparad som webbsida.", true);
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(frame, "Skrivning till HTML-fil " +
							"misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			showReadFileError(fileError);
		}
	}
	
	/** Visar en lista med filer som ej gick att läsa */
	private void showReadFileError(Vector files) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(new JLabel("Operationen gick ej att utföra då en eller " +
				"flera filer inte kunde läsas av programmet."));
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		if (files.size() == 1) {
			panel.add(new JLabel("Kontrollera att nedanstående fil existerar."));
		} else {
			panel.add(new JLabel("Kontrollera att nedanstående filer existerar."));
		}
		/*Inläsningen av följande fil(er) misslyckades:"*/
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		JTextArea textArea = new JTextArea(10, 0);
		for (int i = 0; i < files.size(); ++i) {
			textArea.append("" + files.get(i));
			if (i < files.size() - 1)
				textArea.append("\n");
		}
		textArea.setMargin(new Insets(0, 0, 0, 10));
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setMaximumSize(new Dimension(800,
				(int) scrollPane.getMaximumSize().getHeight()));
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(scrollPane);
		
		JOptionPane.showMessageDialog(frame, panel, "Varning", JOptionPane.ERROR_MESSAGE);
	}
	
	/** producerar en snittlista att använda som jämförelse med filnamnet fileName 
	 * 	och där filerna som läses in tas från ListPanel listPanel */
	private void makeCompareFile(String fileName, ListPanel listPanel) {
		Snitt snitt = new Snitt(fileName);
		int surface = -1;
		boolean readOk = true;
		Vector v = listPanel.getSelected();
		String[] fileNames = new String[v.size()];
		Vector fileError = new Vector();
		for(int i = 0; i < fileNames.length; i++) {
			String file = (String) v.get(i);
			try {
				fileNames[i] = (String) fileMap.get(file);
				int readSurface = io.readSurface(fileNames[i]);
				if(surface == -1) {
					surface = readSurface;
				} else if(surface != readSurface && surface != BLANDAD) {
					surface = BLANDAD;
				}
			} catch (Exception e) {
				fileError.add(fileNames[i]);
				readOk = false;
			}
		}
		if(readOk) {
			try {
				snitt.readResults(fileNames, personNameTracker);
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Resultaten kunde inte läsas" +
						" in från filerna", "Varning", JOptionPane.ERROR_MESSAGE);
				readOk = false;
			}
			if(readOk) {
				LinkedList list = snitt.sortMap(null);
				try {
					snitt.outputToCompareFile(list, surface);
					setMessage("Jämförelselistan är sparad.", true);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "Jämförelsefilen kunde " +
							"ej skrivas till fil", "Varning", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			showReadFileError(fileError);
		}
	}
	
	/** klassen som sköter fönsterhanteringen */
	class WindowHandler extends WindowAdapter {
		/** stänger ned fönstret */
		public void windowClosing(WindowEvent e) {
			exit();
		}
	}
	
	/** sköter om nedstängandet av snitthanterarfönstret */
	public void exit() {
	    if(!snittData.saveCompareFiles()) {
	        JOptionPane.showMessageDialog(frame, "Inställningarna för jämförelsefilerna" +
	        		" gick ej att spara", "Varning", JOptionPane.ERROR_MESSAGE);
	    }
	    if(!snittData.saveAppearanceSettings()) {
	        JOptionPane.showMessageDialog(frame, "Utseendeinställningarna gick ej att" +
	        		" spara", "Varning", JOptionPane.ERROR_MESSAGE);
	    }
	    
		Vector[] v = new Vector[NBR_SNITT * 2];
		for(int i = 0; i < snittList.length; i++) {
			v[i*2] = snittList[i].getSelection();
			v[i*2+1] = snittList[i].getSelected();
		}
		try {
			io.save("snittstring", headers);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Sparandet av rubrikerna misslyckades",
					"Varning", JOptionPane.ERROR_MESSAGE);
		}
		try {
			io.save("snittitle", tabTitles);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Sparandet av fliktitlarna" +
					" misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		try {
			io.save("snittclub", excludedClubs);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Sparandet av klubbarna" +
					" misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		try {
			io.writeFileList("snitt", v);
			io.save("snittmap", fileMap);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Sparandet av fillistan" +
					" misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		SearchWindow.SNITTOPEN = false;
		frame.dispose();
	}
}