package se.manet.bangolfresultat.snitt;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.border.EmptyBorder;

import se.manet.bangolfresultat.datastruct.IOHandler;
import se.manet.bangolfresultat.gui.ListPanel;

import java.util.HashMap;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

/** klassen som beskriver f�nstret d�r man best�mmer sorteringsordningen */
public class SortWindow extends JDialog {
	private HashMap map;			// h�ller reda p� vilket sorteringsalternativ som tillh�r en viss siffra
	private Vector[] sortVector;	// har koll p� hur listorna skall sorteras
	private int tabIndex;			// anger vilken flik som �r vald i snitthanterarf�nstret
	private int nbrTabs;			// antal olika flikar med snittlistor
	private JDialog compareDialog;	// sorteringsf�nstret
	private ListPanel compare;		// panelen d�r man kan v�lja hur snittlistan skall sorteras
	private IOHandler io;			// sk�ter skrivning till filer och inl�sning fr�n filer
	private JMenuItem quit;			// menyalternativ f�r att avsluta
	
	/** skapar f�nstret f�r att sk�ta sorteringen av snittlistan */
	public SortWindow(JFrame owner, int tabIndex, int nbrTabs) {
		super(owner, "Sorteraren", true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		compareDialog = this;
		this.tabIndex = tabIndex;
		this.nbrTabs = nbrTabs;
		
		io = new IOHandler();
		map = new HashMap();
		map.put("Klass", new Integer(Snitt.KLASS));
		map.put("Namn", new Integer(Snitt.NAME));
		map.put("Klubb", new Integer(Snitt.CLUB));
		map.put("T�vlingar", new Integer(Snitt.COMPS));
		map.put("Varv", new Integer(Snitt.ROUNDS));
		map.put("Slag", new Integer(Snitt.HITSUM));
		map.put("Snitt", new Integer(Snitt.MEAN));
		map.put("Snitt ifjol", new Integer(Snitt.EX_MEAN));
		map.put("+/-", new Integer(Snitt.CHANGE));
		try {
			sortVector = io.readFileList("compare", nbrTabs*2);
			boolean differentLengths = false;
			int expectedSizeOfVectors = getSelectionStartVector().size() + getSelectedStartVector().size();
			for(int i = 0; i < nbrTabs; i++) {
				int sizeOfVectors = sortVector[i*2].size() + sortVector[i*2 + 1].size();
			    if(sizeOfVectors == 0 || sizeOfVectors != expectedSizeOfVectors) {
			        differentLengths = true;
			        break;
			    }
			}
			if(differentLengths) {
			    Vector[] tempVector = new Vector[nbrTabs*2];
			    Vector selectionVector = getSelectionStartVector();
				Vector selectedVector = getSelectedStartVector();
			    for(int i = 0; i < nbrTabs; i++) {
			        tempVector[i*2] = selectionVector;
			        tempVector[i*2+1] = selectedVector;
			    }
			    if(sortVector[0].size() + sortVector[1].size() == tempVector[0].size() + tempVector[1].size()) {
			        for(int i = 0; i < sortVector.length && i < nbrTabs; i++) {
			            tempVector[i] = sortVector[i];
			        }
			    }
			    sortVector = tempVector;
			}
			compare = new ListPanel(sortVector[tabIndex*2], sortVector[tabIndex*2 + 1]);
		} catch (Exception e) {
			if (!(e instanceof FileNotFoundException)) {
				JOptionPane.showMessageDialog(owner, "Inl�sningen av sorteringslistan misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
			}
			sortVector = new Vector[nbrTabs*2];
			
			Vector selectionVector = getSelectionStartVector();
			Vector selectedVector = getSelectedStartVector();
			for(int i = 0; i < nbrTabs; i++) {
			    sortVector[i*2] = selectionVector;
			    sortVector[i*2+1] = selectedVector;
			}
			compare = new ListPanel(sortVector[tabIndex*2], sortVector[tabIndex*2 + 1]);
		}
		
		compare.setBorder(new EmptyBorder(5,5,5,5));
		compare.setSelectionText("Ej inkluderade i sorteringen:");
		compare.setSelectedText("Sortera efter:");
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Arkiv");
		menu.setMnemonic(KeyEvent.VK_A);
		bar.add(menu);
		MenuHandler menuHand = new MenuHandler();
		
		quit = new JMenuItem("St�ng sorterarf�nstret", KeyEvent.VK_G);
		quit.addActionListener(menuHand);
		menu.add(quit);
		compareDialog.setJMenuBar(bar);
		
		WindowHandler winHand = new WindowHandler();
		compareDialog.addWindowListener(winHand);
		compareDialog.getContentPane().add(compare);
		compareDialog.pack();
		compareDialog.setLocationRelativeTo(owner);
		compareDialog.setVisible(true);
	}
	
	/** returnerar standardinst�llningens vektor f�r det valbara f�ltet */
	private Vector getSelectionStartVector() {
	    Vector vector = new Vector();
	    vector.add("Klass");
		vector.add("Namn");
		vector.add("Klubb");
		vector.add("T�vlingar");
		vector.add("Slag");
		vector.add("Snitt ifjol");
		vector.add("+/-");
		return vector;
	}
	
	/** returnerar standardinst�llningens vektor f�r det valda f�ltet */
	private Vector getSelectedStartVector() {
	    Vector vector = new Vector();
		vector.add("Snitt");
		vector.add("Varv");
		return vector;
	}
	
	/** klassen som tar hand om knapptryckningarna i menyn */
	class MenuHandler implements ActionListener {
		/** kollar vilket menyalternativ som valts */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == quit) {
				exit();
			}
		}
	}
	
	/** klassen som sk�ter f�nsterhanteringen */
	class WindowHandler extends WindowAdapter {
		/** st�nger ned f�nstret */
		public void windowClosing(WindowEvent e) {
			exit();
		}
	}
	
	/** st�nger ned f�nstret efter att data sparats undan */
	public void exit() {
		sortVector[tabIndex*2] = compare.getSelection();
		sortVector[tabIndex*2 + 1] = compare.getSelected();
		String[] s = new String[sortVector[tabIndex*2 + 1].size()];
		int[][] compareBy;
		try {
		    compareBy = (int[][]) io.load("compareby");
		    if (compareBy.length < nbrTabs) {
		    	int[][] tempCompareBy = new int[nbrTabs][2];
		    	for (int i = 0; i < tempCompareBy.length; ++i) {
		    		if (compareBy.length > i) {
		    			tempCompareBy[i] = new int[compareBy[i].length];
		    			for (int j = 0; j < tempCompareBy[i].length; ++j) {
			    			tempCompareBy[i][j] = compareBy[i][j];
			    		}
		    		} else {
		    			tempCompareBy[i] = new int[2];
		    			tempCompareBy[i][0] = Snitt.MEAN;
				        tempCompareBy[i][1] = Snitt.ROUNDS;
		    		}
		    	}
		    	compareBy = tempCompareBy;
		    }
		} catch (Exception e) {
		    try {
		        int[] oldCompareBy = (int[]) io.load("compareby");
		        oldCompareBy.hashCode(); // can be removed without any sideeffects
		    } catch (Exception ex) {
		        if (!(ex instanceof FileNotFoundException)) {
		            JOptionPane.showMessageDialog(compareDialog, "Kunde inte l�sa in filen compareby", "Varning", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		    compareBy = new int[nbrTabs][2];
		    for(int i = 0; i < compareBy[tabIndex].length; i++) {
		        compareBy[i][0] = Snitt.MEAN;
		        compareBy[i][1] = Snitt.ROUNDS;
		    }
		}
		compareBy[tabIndex] = new int[s.length];
	    for(int i = 0; i < s.length; i++) {
			s[i] = (String)sortVector[tabIndex*2 + 1].get(i);
			compareBy[tabIndex][i] = ((Integer)map.get(s[i])).intValue();
		}
		try {
		    io.save("compareby", compareBy);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(compareDialog, "Sparandet av sorteringsordning misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		try {
			io.writeFileList("compare", sortVector);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(compareDialog, "Sparandet av sorteringslistorna misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		
		compareDialog.dispose();
	}
}