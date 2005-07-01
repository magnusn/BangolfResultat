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

/** klassen som beskriver f�nstret d�r man best�mmer sorteringsordningen */
public class SortWindow extends JDialog {
	private HashMap map;			// h�ller reda p� vilket sorteringsalternativ som tillh�r en viss siffra
	private Vector[] v;				// har koll p� hur listorna skall sorteras
	private int tabIndex;			// anger vilken flik som �r vald i snitthanterarf�nstret
	private JDialog compareDialog;	// sorteringsf�nstret
	private ListPanel compare;		// panelen d�r man kan v�lja hur snittlistan skall sorteras
	private IOHandler io;			// sk�ter skrivning till filer och inl�sning fr�n filer
	private JMenuItem quit;			// menyalternativ f�r att avsluta
	
	/** skapar f�nstret f�r klasshantering */
	public SortWindow(JFrame owner, int tabIndex, int nbrTabs) {
		super(owner, "Sorteraren", true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		compareDialog = this;
		this.tabIndex = tabIndex;
		
		io = new IOHandler();
		map = new HashMap();
		map.put("Namn", new Integer(Snitt.NAME));
		map.put("Klubb", new Integer(Snitt.CLUB));
		map.put("T�vlingar", new Integer(Snitt.COMPS));
		map.put("Varv", new Integer(Snitt.ROUNDS));
		map.put("Slag", new Integer(Snitt.HITSUM));
		map.put("Snitt", new Integer(Snitt.MEAN));
		map.put("Snitt ifjol", new Integer(Snitt.EX_MEAN));
		map.put("F�r�ndring", new Integer(Snitt.CHANGE));
		try {
			v = io.readFileList("compare", nbrTabs*2);
			compare = new ListPanel(v[tabIndex*2], v[tabIndex*2 + 1]);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(compareDialog, "Inl�sningen av fillistan misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
			Vector v1 = new Vector();
			Vector v2 = new Vector();
			v1.add("Namn");
			v1.add("Klubb");
			v1.add("T�vlingar");
			v2.add("Snitt");
			v2.add("Varv");
			v1.add("Slag");
			v1.add("Snitt ifjol");
			v1.add("F�r�ndring");
			compare = new ListPanel(v1, v2);
		}
		
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
		v[tabIndex*2] = compare.getSelection();
		v[tabIndex*2 + 1] = compare.getSelected();
		String[] s = new String[v[tabIndex*2 + 1].size()];
		try {
		    int[][] compareBy = (int[][]) io.load("compareby");
		    compareBy[tabIndex] = new int[s.length];
		    for(int i = 0; i < s.length; i++) {
				s[i] = (String)v[tabIndex*2 + 1].get(i);
				compareBy[tabIndex][i] = ((Integer)map.get(s[i])).intValue();
			}
			try {
				io.writeFileList("compare", v);
				io.save("compareby", compareBy);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(compareDialog, "Sparandet av fillistan misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
		    JOptionPane.showMessageDialog(compareDialog, "Kunde inte l�sa in filen compareby", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		
		compareDialog.dispose();
	}
}