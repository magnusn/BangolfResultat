import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import java.util.HashMap;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/** klassen som beskriver fönstret där man bestämmer sorteringsordningen */
class CompareWindow extends JDialog {
	private HashMap map;			// håller reda på vilket sorteringsalternativ som tillhör en viss siffra
	private JDialog compareDialog;	// sorteringsfönstret
	private ListPanel compare;		// panelen där man kan välja hur snittlistan skall sorteras
	private IOHandler io;			// sköter skrivning till filer och inläsning från filer
	private JMenuItem quit;			// menyalternativ för att avsluta
	
	/** skapar fönstret för klasshantering */
	public CompareWindow(JFrame owner) {
		super(owner, "Sorteraren", true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		compareDialog = this;
		
		io = new IOHandler();
		map = new HashMap();
		map.put("Namn", new Integer(Snitt.NAME));
		map.put("Klubb", new Integer(Snitt.CLUB));
		map.put("Tävlingar", new Integer(Snitt.COMPS));
		map.put("Varv", new Integer(Snitt.ROUNDS));
		map.put("Slag", new Integer(Snitt.HITSUM));
		map.put("Snitt", new Integer(Snitt.MEAN));
		try {
			Vector[] v = io.readFileList("compare", 2);
			compare = new ListPanel(v[0], v[1]);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(compareDialog, "Inläsningen av fillistan misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
			Vector v1 = new Vector();
			Vector v2 = new Vector();
			v1.add("Namn");
			v1.add("Klubb");
			v1.add("Tävlingar");
			v2.add("Snitt");
			v2.add("Varv");
			v1.add("Slag");
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
		Vector[] v = new Vector[2];
		v[0] = compare.getSelection();
		v[1] = compare.getSelected();
		String[] s = new String[v[1].size()];
		int[] compareBy = new int[v[1].size()];
		for(int i = 0; i < s.length; i++) {
			s[i] = (String)v[1].get(i);
			compareBy[i] = ((Integer)map.get(s[i])).intValue();
		}
		try {
			io.writeFileList("compare", v);
			io.save("compareby", compareBy);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(compareDialog, "Sparandet av fillistan misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		compareDialog.dispose();
	}
}