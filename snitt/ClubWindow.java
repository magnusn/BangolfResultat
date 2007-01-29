/*
 * Created on 2005-jun-28
 * 
 * Created by: Magnus
 */
package snitt;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ClubWindow - beskriver f�nstret f�r att st�lla in vilka klubbars resultat som
 * skall visas i snittlistan
 */
public class ClubWindow extends JDialog {
	private int tabIndex;
    private HashSet[] excludedClubs; 	// talar om vilka klubbar som inte �r valda f�r respektive flik
    private JCheckBox[] clubs;			// vektor av klubbar som kan v�ljas
    private JButton acceptButton;		// knapp f�r att bekr�fta inst�llningarna
    private JButton cancelButton;		// knapp f�r att avbryta

    /** skapar ett f�nster med inst�llningar f�r vad som skall visas */
    public ClubWindow(JFrame owner, int tabIndex, String[] clubs, HashSet[] excludedClubs) {
        super(owner, "Klubbar", true);
        this.excludedClubs = excludedClubs;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowHandler());
        setLayout(new GridLayout(clubs.length + 2, 1));
        setResizable(false);
        
        getContentPane().add(new JLabel("V�lj klubbar som resultat skall visas f�r:"));
        //getContentPane().add(new JLabel("V�lj vad som skall visas:"));
        this.clubs = new JCheckBox[clubs.length];
        for(int i = 0; i < clubs.length; i++) {
        	String club = clubs[i];
        	this.clubs[i] = new JCheckBox(club, 
        			!excludedClubs[tabIndex].contains(club.toLowerCase()));
            getContentPane().add(this.clubs[i]);
        }
        
        ButtonHandler buttonHand = new ButtonHandler();
        acceptButton = new JButton("Ok");
        acceptButton.setMnemonic(KeyEvent.VK_O);
        acceptButton.addActionListener(buttonHand);
        cancelButton = new JButton("Avbryt");
        cancelButton.setMnemonic(KeyEvent.VK_A);
        cancelButton.addActionListener(buttonHand);
        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel);
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
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
            }
        }
    }
}