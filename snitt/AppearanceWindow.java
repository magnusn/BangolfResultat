/*
 * Created on 2005-jun-28
 * 
 * Created by: Magnus
 */
package snitt;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * AppearanceWindow - beskriver f�nstret f�r att st�lla in vad som skall visas i snittlistan
 */
public class AppearanceWindow extends JDialog {
    private boolean[] initialSelection; // talar om vilka alternativ som var valda fr�n b�rjan
    private JCheckBox[] headers;		// vektor av rubriker som kan v�ljas
    private SnittData snittData;		// h�ller reda p� inst�llningarna
    private JButton acceptButton;		// knapp f�r att bekr�fta inst�llningarna
    private JButton cancelButton;		// knapp f�r att avbryta
    private int tabIndex;				// h�ller reda p� vilken snittlista inst�llningarna g�ller

    /** skapar ett f�nster med inst�llningar f�r vad som skall visas */
    public AppearanceWindow(JFrame owner, int tabIndex, SnittData snittData) {
        super(owner, "Utseende", true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowHandler());
        this.tabIndex = tabIndex;
        this.snittData = snittData;
        
        JPanel appearancePanel = new JPanel(new GridLayout(SnittData.NBR_HEADERS + 1, 1));
        appearancePanel.setBorder(new EmptyBorder(5,5,5,5));
        setResizable(false);
        
        headers = snittData.getAppearanceHeaders(tabIndex);
        initialSelection = new boolean[headers.length];
        appearancePanel.add(new JLabel("V�lj vad som skall visas:"));
        for(int i = 0; i < headers.length; i++) {
            initialSelection[i] = headers[i].isSelected();
            appearancePanel.add(headers[i]);
        }
        
        ButtonHandler buttonHand = new ButtonHandler();
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
        
        getContentPane().add(appearancePanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    
    /** st�nger ner f�nstret utan att �ndra p� n�gra inst�llningar */
    private void exitWithoutChanges() {
        for(int i = 0; i < headers.length; i++) {
	        headers[i].setSelected(initialSelection[i]);
	    }
	    snittData.setAppearanceHeaders(headers, tabIndex);
		dispose();
    }
    
    /** klassen som sk�ter f�nsterhanteringen */
	class WindowHandler extends WindowAdapter {
		/** st�nger ned f�nstret */
		public void windowClosing(WindowEvent e) {
		    exitWithoutChanges();
		}
	}
    
    /** klassen som tar hand om knapptryckningarna vid val av utseende */
    class ButtonHandler implements ActionListener {
        /** kollar vilken knapp som tryckts ned och utf�r l�mplig handling */
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == acceptButton) {
                snittData.setAppearanceHeaders(headers, tabIndex);
                dispose();
            } else if(e.getSource() == cancelButton) {
                exitWithoutChanges();
            }
        }
    }
}