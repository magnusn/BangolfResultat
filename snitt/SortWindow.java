package snitt;

import gui.ListPanel;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import datastruct.IOHandler;


import java.util.HashMap;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/** klassen som beskriver fönstret där man bestämmer sorteringsordningen */
public class SortWindow extends JDialog {
	private HashMap map;			// håller reda på vilket sorteringsalternativ som tillhör en viss siffra
	private Vector[] v;				// har koll på hur listorna skall sorteras
	private int tabIndex;			// anger vilken flik som är vald i snitthanterarfönstret
	private int nbrTabs;			// antal olika flikar med snittlistor
	private JDialog compareDialog;	// sorteringsfönstret
	private ListPanel compare;		// panelen där man kan välja hur snittlistan skall sorteras
	private IOHandler io;			// sköter skrivning till filer och inläsning från filer
	private JMenuItem quit;			// menyalternativ för att avsluta
	
	/** skapar fönstret för klasshantering */
	public SortWindow(JFrame owner, int tabIndex, int nbrTabs) {
		super(owner, "Sorteraren", true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		compareDialog = this;
		this.tabIndex = tabIndex;
		this.nbrTabs = nbrTabs;
		
		io = new IOHandler();
		map = new HashMap();
		map.put("Namn", new Integer(Snitt.NAME));
		map.put("Klubb", new Integer(Snitt.CLUB));
		map.put("Tävlingar", new Integer(Snitt.COMPS));
		map.put("Varv", new Integer(Snitt.ROUNDS));
		map.put("Slag", new Integer(Snitt.HITSUM));
		map.put("Snitt", new Integer(Snitt.MEAN));
		map.put("Snitt ifjol", new Integer(Snitt.EX_MEAN));
		map.put("+/-", new Integer(Snitt.CHANGE));
		try {
			v = io.readFileList("compare", nbrTabs*2);
			compare = new ListPanel(v[tabIndex*2], v[tabIndex*2 + 1]);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(owner, "Inläsningen av sorteringslistan misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
			v = new Vector[nbrTabs*2];
			Vector v1 = new Vector();
			Vector v2 = new Vector();
			v1.add("Namn");
			v1.add("Klubb");
			v1.add("Tävlingar");
			v2.add("Snitt");
			v2.add("Varv");
			v1.add("Slag");
			v1.add("Snitt ifjol");
			v1.add("+/-");
			for(int i = 0; i < nbrTabs; i++) {
			    v[i*2] = v1;
			    v[i*2+1] = v2;
			}
			compare = new ListPanel(v1, v2);
		}
		
		compare.setSelectionText("Ej inkluderade i sorteringen:");
		compare.setSelectedText("Sortera efter:");
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Arkiv");
		menu.setMnemonic(KeyEvent.VK_A);
		bar.add(menu);
		MenuHandler menuHand = new MenuHandler();
		
		quit = new JMenuItem("Stäng sorterarfönstret", KeyEvent.VK_G);
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
	
	/** klassen som tar hand om knapptryckningarna i menyn */
	class MenuHandler implements ActionListener {
		/** kollar vilket menyalternativ som valts */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == quit) {
				exit();
			}
		}
	}
	
	/** klassen som sköter fönsterhanteringen */
	class WindowHandler extends WindowAdapter {
		/** stänger ned fönstret */
		public void windowClosing(WindowEvent e) {
			exit();
		}
	}
	
	/** stänger ned fönstret efter att data sparats undan */
	public void exit() {
		v[tabIndex*2] = compare.getSelection();
		v[tabIndex*2 + 1] = compare.getSelected();
		String[] s = new String[v[tabIndex*2 + 1].size()];
		int[][] compareBy;
		try {
		    compareBy = (int[][]) io.load("compareby");
		    compareBy[tabIndex] = new int[s.length];
		    for(int i = 0; i < s.length; i++) {
				s[i] = (String)v[tabIndex*2 + 1].get(i);
				compareBy[tabIndex][i] = ((Integer)map.get(s[i])).intValue();
			}
		} catch (Exception e) {
		    compareBy = new int[nbrTabs][2];
		    for(int i = 0; i < compareBy[tabIndex].length; i++) {
		        compareBy[i][0] = Snitt.MEAN;
		        compareBy[i][1] = Snitt.ROUNDS;
		    }
		    JOptionPane.showMessageDialog(compareDialog, "Kunde inte läsa in filen compareby", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		try {
		    io.save("compareby", compareBy);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(compareDialog, "Sparandet av sorteringsordning misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		try {
			io.writeFileList("compare", v);
		} catch (Exception e) {
		    e.printStackTrace();
			JOptionPane.showMessageDialog(compareDialog, "Sparandet av sorteringslistorna misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		
		compareDialog.dispose();
	}
}