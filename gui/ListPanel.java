package gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/** klassen som beskriver en panel bestående av två listor med valda och ej valda element */
public class ListPanel extends JPanel {
	private JButton addButton, removeButton;		// knappar för att välja eller välja bort ifrån listan
	private JButton upButton, downButton;			// knappar för att flytta elementen uppåt eller neråt
	private JLabel selectionLabel, selectedLabel; 	// beskrivande rubriker till listorna
	private JList selectedList, possibleList;		// de synliga listorna
	private Vector selection, selected;				// vektorerna som sköter om listorna
	public static boolean POSSIBLEINFOCUS;			// håller reda på vilken lista som senast har varit aktiv
	
	/** skapar panelen utifrån vektorerna selection och selected */
	public ListPanel(Vector selection, Vector selected) {
		this.selection = selection;
		this.selected = selected;
		possibleList = new JList(selection);
		JScrollPane sp1 = new JScrollPane(possibleList);
		selectedList = new JList(selected);
		JScrollPane sp2 = new JScrollPane(selectedList);
		upButton = new JButton("\u2191");
		downButton = new JButton("\u2193");
		addButton = new JButton(">");//\u2192");
		removeButton = new JButton("<");//"\u2190");
		upButton.setToolTipText("Flytta element uppåt");
		downButton.setToolTipText("Flytta element nedåt");
		addButton.setToolTipText("Lägg till");
		removeButton.setToolTipText("Ta bort");
		ListHandler listHand = new ListHandler();
		addButton.addActionListener(listHand);
		removeButton.addActionListener(listHand);
		upButton.addActionListener(listHand);
		downButton.addActionListener(listHand);
		MouseHandler mouseHand = new MouseHandler();
		possibleList.addMouseListener(mouseHand);
		selectedList.addMouseListener(mouseHand);
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		JPanel buttonPanel = new JPanel();
		JPanel sp1Panel = new JPanel();
		JPanel sp2Panel = new JPanel();
		selectionLabel = new JLabel();
		selectedLabel = new JLabel();
		buttonPanel.setLayout(gridbag);
		sp1Panel.setLayout(gridbag);
		sp2Panel.setLayout(gridbag);
		sp1.setPreferredSize(new Dimension(250, 400));
		sp2.setPreferredSize(new Dimension(250, 400));
		c.insets = new Insets(5,1,5,1);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(removeButton, c);
		c.gridx = 1;
		c.gridy = 0;
		gridbag.setConstraints(upButton, c);
		//c.insets = new Insets(0,1,0,1);
		c.gridy = 2;
		gridbag.setConstraints(downButton, c);
		//c.insets = new Insets(0,1,0,1);
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(addButton, c);
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridy = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(selectionLabel, c);
		gridbag.setConstraints(selectedLabel, c);
		c.gridwidth = 3;
		gridbag.setConstraints(sp1Panel, c);
		gridbag.setConstraints(buttonPanel, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(sp2Panel, c);
		
		buttonPanel.add(upButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(addButton);
		buttonPanel.add(downButton);
		sp1Panel.add(selectionLabel);
		sp1Panel.add(sp1);
		sp2Panel.add(selectedLabel);
		sp2Panel.add(sp2);
		
		add(sp1Panel);
		add(buttonPanel);
		add(sp2Panel);
	}
	
	/** lägger till en valbar sträng name */
	public void add(String name) {
		selection.addElement(name);
		possibleList.setListData(selection);
	}
	
	/** tar bort objekten i vektorn o från listorna */
	public void remove(Object[] o) {
		for(int i = 0; i < o.length; i++) {
			selection.removeElement((String)o[i]);
			selected.removeElement((String)o[i]);
		}
		possibleList.setListData(selection);
		selectedList.setListData(selected);
	}
	
	/** returnerar de objekt som är markerade i listan med ej valda element */
	public Object[] getPossibleSelected() {
		Object[] o = possibleList.getSelectedValues();
		return o;
	}
	
	/** returnerar de objekt som är markerade i listan med valda element */
	public Object[] getSelectedSelected() {
		Object[] o = selectedList.getSelectedValues();
		return o;
	}
	
	/** returnerar vektorn med valbara element */
	public Vector getSelection() {
		return selection;
	}
	
	/** returnerar vektorn med valda element */
	public Vector getSelected() {
		return selected;
	}
	
	public boolean contains(Object o) {
		if(selection.contains(o) || selected.contains(o)) {
			return true;
		} else {
			return false;
		}
	}
	
	/** sätter rubriken ovanför listan med valbara element till text */
	public void setSelectionText(String text) {
		selectionLabel.setText(text);
	}
	
	/** sätter rubriken ovanför listan med valda element till text */
	public void setSelectedText(String text) {
		selectedLabel.setText(text);
	}
	
	/** klassen som tar hand om knapptryckningarna */
	class ListHandler implements ActionListener {
		/** kollar vilken knapp som tryckts ned och utför lämplig handling */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == addButton) {
				Object[] o = possibleList.getSelectedValues();
				for(int i = 0; i < o.length; i++) {
					selected.addElement((String)o[i]);
					selection.removeElement((String)o[i]);
				}
				selectedList.setListData(selected);
				possibleList.setListData(selection);
			}
			else if(e.getSource() == removeButton) {
				Object[] o = selectedList.getSelectedValues();
				for(int i = 0; i < o.length; i++) {
					selection.addElement((String)o[i]);
					selected.removeElement((String)o[i]);
				}
				selectedList.setListData(selected);
				possibleList.setListData(selection);
			}
			else if(e.getSource() == upButton) {
				if(POSSIBLEINFOCUS && !possibleList.isSelectionEmpty()) {
					move(possibleList, selection, true);
				} else if(!POSSIBLEINFOCUS && !selectedList.isSelectionEmpty()) {
					move(selectedList, selected, true);
				}
			}
			else if(e.getSource() == downButton) {
				if(POSSIBLEINFOCUS && !possibleList.isSelectionEmpty()) {
					move(possibleList, selection, false);
				} else if(!POSSIBLEINFOCUS && !selectedList.isSelectionEmpty()) {
					move(selectedList, selected, false);
				}
			}
		}
		
		/** flyttar markerade objekt i vektorn vector så det syns i JList list, uppåt om up är true */
		private void move(JList list, Vector vector, boolean up) {
			Object[] o = list.getSelectedValues();
			int[] indices = list.getSelectedIndices();
			boolean bad = false;
			if(indices.length == 0) {
				bad = true;
			} else {
				int subtract;
				if(up) {
					subtract = -1;
				} else {
					subtract = 1;
				}
				for(int i = 0; i < indices.length; i++) {
					int newValue = indices[i] + subtract;
					if(newValue != -1 && newValue != vector.size()) {
						indices[i] = newValue;
					} else {
						bad = true;
						break;
					}
				}
			}
			if(!bad) {
				for(int i = 0; i < o.length; i++) {
					vector.remove(o[i]);
					if(up) {
						vector.insertElementAt(o[i], indices[i]);
					} else {
						vector.insertElementAt(o[(o.length-1)-i], indices[(o.length-1)-i]);
					}
				}
				list.setListData(vector);
				list.setSelectedIndices(indices);
			}
		}
	}
	
	/** klassen som kollar vilken JList som sist har blivit "mustryckt" */
	class MouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			POSSIBLEINFOCUS = possibleList.isFocusOwner();
		}
	}
}
