/*
 * Created on 2005-jun-28
 * 
 * Created by: Magnus
 */
package snitt;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * AppearanceWindow - beskriver f�nstret f�r att st�lla in vad som skall visas i snittlistan
 */
public class AppearanceWindow extends JDialog {
    private JCheckBox[] headers;		// vektor av rubriker som kan v�ljas
    private SnittData snittData;		// h�ller reda p� inst�llningarna
    private JButton acceptButton;		// knapp f�r att bekr�fta inst�llningarna
    private int tabIndex;				// h�ller reda p� vilken snittlista inst�llningarna g�ller

    /** skapar ett f�nster med inst�llningar f�r vad som skall visas */
    public AppearanceWindow(JFrame owner, int tabIndex, SnittData snittData) {
        super(owner, "Utseende", true);
        this.tabIndex = tabIndex;
        this.snittData = snittData;
        setLayout(new GridLayout(snittData.getNbrHeaders() + 2, 1));
        setResizable(false);
        
        headers = snittData.getAppearanceHeaders(tabIndex);
        if(snittData.getCompareFile(tabIndex) != null) {
            headers[Snitt.EX_MEAN].setEnabled(true);
            headers[Snitt.CHANGE].setEnabled(true);
            headers[Snitt.EX_MEAN].setToolTipText(null);
            headers[Snitt.CHANGE].setToolTipText(null);
        } else {
            headers[Snitt.EX_MEAN].setEnabled(false);
            headers[Snitt.CHANGE].setEnabled(false);
            headers[Snitt.EX_MEAN].setSelected(false);
            headers[Snitt.CHANGE].setSelected(false);
            headers[Snitt.EX_MEAN].setToolTipText("Aktiveras f�rst d� j�mf�relsefil �r vald");
            headers[Snitt.CHANGE].setToolTipText("Aktiveras f�rst d� j�mf�relsefil �r vald");
        }
        
        getContentPane().add(new JLabel("V�lj vad som skall visas:"));
        for(int i = 0; i < headers.length; i++) {
            getContentPane().add(headers[i]);
        }
        
        ButtonHandler buttonHand = new ButtonHandler();
        acceptButton = new JButton("Verkst�ll");
        acceptButton.addActionListener(buttonHand);
        getContentPane().add(acceptButton);
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    
    /** klassen som tar hand om knapptryckningarna vid val av utseende */
    class ButtonHandler implements ActionListener {
        /** kollar vilken knapp som tryckts ned och utf�r l�mplig handling */
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == acceptButton) {
                snittData.setAppearanceHeaders(headers, tabIndex);
                setVisible(false);
            }
        }
    }
}