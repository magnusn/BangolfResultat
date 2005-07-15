/*
 * Created on 2005-jun-28
 * 
 * Created by: Magnus
 */
package snitt;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * AppearanceWindow - beskriver f�nstret f�r att st�lla in vad som skall visas i snittlistan
 */
public class AppearanceWindow extends JDialog {
    private boolean[] initialSelection; // talar om vilka alternativ som var valda fr�n b�rjan
    private JCheckBox[] headers;		// vektor av rubriker som kan v�ljas
    private SnittData snittData;		// h�ller reda p� inst�llningarna
    private JButton acceptButton;		// knapp f�r att bekr�fta inst�llningarna
    private int tabIndex;				// h�ller reda p� vilken snittlista inst�llningarna g�ller

    /** skapar ett f�nster med inst�llningar f�r vad som skall visas */
    public AppearanceWindow(JFrame owner, int tabIndex, SnittData snittData) {
        super(owner, "Utseende", true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowHandler());
        this.tabIndex = tabIndex;
        this.snittData = snittData;
        setLayout(new GridLayout(SnittData.NBR_HEADERS + 2, 1));
        setResizable(false);
        
        headers = snittData.getAppearanceHeaders(tabIndex);
        initialSelection = new boolean[headers.length];
        getContentPane().add(new JLabel("V�lj vad som skall visas:"));
        for(int i = 0; i < headers.length; i++) {
            initialSelection[i] = headers[i].isSelected();
            getContentPane().add(headers[i]);
        }
        
        ButtonHandler buttonHand = new ButtonHandler();
        acceptButton = new JButton("Ok");
        acceptButton.addActionListener(buttonHand);
        getContentPane().add(acceptButton);
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    
    /** klassen som sk�ter f�nsterhanteringen */
	class WindowHandler extends WindowAdapter {
		/** st�nger ned f�nstret */
		public void windowClosing(WindowEvent e) {
		    for(int i = 0; i < headers.length; i++) {
		        headers[i].setSelected(initialSelection[i]);
		    }
		    snittData.setAppearanceHeaders(headers, tabIndex);
			dispose();
		}
	}
    
    /** klassen som tar hand om knapptryckningarna vid val av utseende */
    class ButtonHandler implements ActionListener {
        /** kollar vilken knapp som tryckts ned och utf�r l�mplig handling */
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == acceptButton) {
                snittData.setAppearanceHeaders(headers, tabIndex);
                dispose();
            }
        }
    }
}