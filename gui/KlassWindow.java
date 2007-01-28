package gui;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import java.util.Vector;
import java.util.HashMap;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

import datastruct.IOHandler;


/** klassen som beskriver fönstret som där man sköter val av vilka klasser som skall kunna väljas */
class KlassWindow extends JFrame {
	private HashMap map;							// används för att undvika klassdubletter
	private JFrame frame;							// klasshanterarfönstret
	private ListPanel klass;						// panelen där man kan välja och välja bort klasser
	private IOHandler io;							// sköter skrivning till filer och inläsning från filer
	private JMenuItem addKlass, removeKlass, quit;	// menyalternativ för att lägga till eller ta bort klasser samt avsluta
	public static String NO_KLASS = "[Ingen klass]";// representerar en tom klassträng
	
	/** skapar fönstret för klasshantering */
	public KlassWindow(JFrame owner) {
		super("Klasshanteraren");
		frame = this;
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setIconImage(SearchWindow.ICON);
		
		io = new IOHandler();
		try {
			Vector[] v = io.readFileList("klass", 2);
			klass = new ListPanel(v[0], v[1]);
			map = (HashMap) io.load("klassmap");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Inläsningen av fillistan misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
			klass = new ListPanel(new Vector(), new Vector());
			map = new HashMap();
		}
		
		klass.setSelectionText("Ej valda klasser:");
		klass.setSelectedText("Valda klasser:");
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Arkiv");
		menu.setMnemonic(KeyEvent.VK_A);
		bar.add(menu);
		MenuHandler menuHand = new MenuHandler();
		addKlass = new JMenuItem("Lägg till en klass...", KeyEvent.VK_L);
		addKlass.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		removeKlass = new JMenuItem("Ta bort markerade klasser", KeyEvent.VK_T);
		removeKlass.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		quit = new JMenuItem("Stäng klasshanterarfönstret", KeyEvent.VK_G);
		addKlass.addActionListener(menuHand);
		removeKlass.addActionListener(menuHand);
		quit.addActionListener(menuHand);
		menu.add(addKlass);
		menu.add(removeKlass);
		menu.add(quit);
		frame.setJMenuBar(bar);
		
		WindowHandler winHand = new WindowHandler();
		frame.addWindowListener(winHand);
		frame.getContentPane().add(klass);
		frame.pack();
		frame.setLocationRelativeTo(owner);
		frame.setVisible(true);
	}
	
	/** klassen som tar hand om knapptryckningarna i menyn */
	class MenuHandler implements ActionListener {
		/** kollar vilket menyalternativ som valts */
		public void actionPerformed(ActionEvent e) {
			String inputValue;
			if(e.getSource() == addKlass) {
				inputValue = JOptionPane.showInputDialog(frame, "Skriv in klassnamnet:");
				if(inputValue != null) {
					inputValue = inputValue.trim();
					if (inputValue.equals("")) {
						inputValue = NO_KLASS;
					}
					if(!map.containsKey(inputValue)) {
						map.put(inputValue, inputValue);
						klass.add(inputValue);
					} else {
						JOptionPane.showMessageDialog(frame, "En klass med namnet " + inputValue + " finns redan listad", "Varning", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else if(e.getSource() == removeKlass) {
				if((klass.getPossibleSelected().length != 0 && ListPanel.POSSIBLEINFOCUS) ||
				   (klass.getSelectedSelected().length != 0 && !ListPanel.POSSIBLEINFOCUS)) {
					int val = JOptionPane.showConfirmDialog(frame, "Vill du ta bort markerade klasser ifrån listan?", "Ta bort ifrån listan"
					, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(val == JOptionPane.YES_OPTION) {
						Object[] object;
						if(ListPanel.POSSIBLEINFOCUS) {
							object = klass.getPossibleSelected();
							klass.remove(object);
							for(int i = 0; i < object.length; i++) {
								map.remove((String)object[i]);
							}
						} else {
							object = klass.getSelectedSelected();
							klass.remove(object);
							for(int i = 0; i < object.length; i++) {
								map.remove((String)object[i]);
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Inga klasser har markerats", "Klasser ej borttagna", JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(e.getSource() == quit) {
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
		v[0] = klass.getSelection();
		v[1] = klass.getSelected();
		String[] s = new String[v[1].size()];
		for(int i = 0; i < s.length; i++) {
			s[i] = (String)v[1].get(i);
		}
		ResultInputWindow.setKlassChoice(s);
		try {
			io.save("klasstring", s);
			io.writeFileList("klass", v);
			io.save("klassmap", map);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Sparandet av fillistan misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
		SearchWindow.KLASSOPEN = false;
		frame.dispose();
	}
}