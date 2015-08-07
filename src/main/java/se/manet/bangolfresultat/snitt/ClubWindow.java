/*
 * Created on 2007-jan-29
 * 
 * Created by: Magnus
 */
package se.manet.bangolfresultat.snitt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

/**
 * ClubWindow - beskriver f�nstret f�r att st�lla in vilka klubbars resultat som
 * skall visas i snittlistan
 */
public class ClubWindow extends JDialog {
	private int tabIndex;
    private HashSet[] excludedClubs; 	// talar om vilka klubbar som inte �r valda f�r respektive flik
    private JCheckBox markAll;			// f�r att markera/avmarkera alla klubbar
    private JCheckBox[] clubs;			// vektor av klubbar som kan v�ljas
    private JButton acceptButton;		// knapp f�r att bekr�fta inst�llningarna
    private JButton cancelButton;		// knapp f�r att avbryta

    /** skapar ett f�nster med inst�llningar f�r vad som skall visas */
    public ClubWindow(JFrame owner, int tabIndex, String[] clubs, HashSet[] excludedClubs) {
        super(owner, "Klubbar", true);
        this.excludedClubs = excludedClubs;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowHandler());
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        c.anchor = GridBagConstraints.WEST;
        
        setLayout(gridbag);
        
        JPanel clubPanel = new JPanel(gridbag);
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.PAGE_AXIS));
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.PAGE_AXIS));
        clubPanel.setBorder(new EmptyBorder(5,5,5,5));
        setResizable(false);
        
        
        addComponentToPanel(clubPanel, 
        		new JLabel("V�lj klubbar som resultat skall visas f�r:"), gridbag, c);
        addComponentToPanel(clubPanel, 
        		Box.createRigidArea(new Dimension(0,5)), gridbag, c);
        markAll = new JCheckBox("Markera/avmarkera alla");
        JSeparator separator = new JSeparator();
        int maxWidth = (int) separator.getMaximumSize().getWidth();
        int preferredHeight = (int) separator.getPreferredSize().getHeight();
        separator.setMaximumSize(new Dimension(maxWidth, preferredHeight));
        headerPanel.add(markAll);
        headerPanel.add(separator);
        
        this.clubs = new JCheckBox[clubs.length];
        int nbrIncluded = 0;
        for(int i = 0; i < clubs.length; i++) {
        	String club = clubs[i];
        	this.clubs[i] = new JCheckBox(club, 
        			!excludedClubs[tabIndex].contains(club.toLowerCase()));
        	if (this.clubs[i].isSelected())
        		nbrIncluded++;
        	scrollPanel.add(this.clubs[i]);
        }
        if (nbrIncluded > (clubs.length/2))
        	markAll.setSelected(true);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(scrollPanel);
        scrollPane.setColumnHeaderView(headerPanel);
        scrollPane.setPreferredSize(new Dimension(200,300));
        
        ButtonHandler buttonHand = new ButtonHandler();
        markAll.addActionListener(buttonHand);
        acceptButton = new JButton("Ok");
        acceptButton.setMnemonic(KeyEvent.VK_O);
        acceptButton.addActionListener(buttonHand);
        cancelButton = new JButton("Avbryt");
        cancelButton.setMnemonic(KeyEvent.VK_A);
        cancelButton.addActionListener(buttonHand);
        JPanel buttonPanel = new JPanel(new GridLayout(1,2,6,0));
        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));
        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        addComponentToPanel(clubPanel, scrollPane, gridbag, c);
        c.fill = GridBagConstraints.NONE;
        addComponent(clubPanel, gridbag, c);
        c.anchor = GridBagConstraints.EAST;
        addComponent(buttonPanel, gridbag, c);
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    
    /** l�gger till en grafisk komponent till detta f�nster */
    private void addComponent(Component component, GridBagLayout gridbag, 
    		GridBagConstraints c) {
    	gridbag.setConstraints(component, c);
    	getContentPane().add(component);
    }
    
    /** l�gger till en grafisk komponent till angiven JPanel */
    private void addComponentToPanel(JPanel panel, Component component,
    		GridBagLayout gridbag, GridBagConstraints c) {
    	gridbag.setConstraints(component, c);
    	panel.add(component);
    }
    
    /** klassen som sk�ter f�nsterhanteringen */
	class WindowHandler extends WindowAdapter {
		/** st�nger ned f�nstret */
		public void windowClosing(WindowEvent e) {
		    dispose();
		}
	}
    
    /** klassen som tar hand om knapptryckningarna vid val av klubbar */
    class ButtonHandler implements ActionListener {
        /** kollar vilken knapp som tryckts ned och utf�r l�mplig handling */
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == acceptButton) {
            	excludedClubs[tabIndex] = new HashSet();
                for (int i = 0; i < clubs.length; ++i) {
                	if (!clubs[i].isSelected()) {
                		excludedClubs[tabIndex].add(clubs[i].getText().toLowerCase());
                	}
                }
                dispose();
            } else if(e.getSource() == cancelButton) {
                dispose();
            } else if(e.getSource() == markAll) {
            	if (markAll.isSelected()) {
            		for (int i = 0; i < clubs.length; ++i)
            			clubs[i].setSelected(true);
            	} else {
            		for (int i = 0; i < clubs.length; ++i)
            			clubs[i].setSelected(false);
            	}
            }
        }
    }
}