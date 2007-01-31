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
 * AppearanceWindow - beskriver fönstret för att ställa in vad som skall visas i snittlistan
 */
public class AppearanceWindow extends JDialog {
    private boolean[] initialSelection; // talar om vilka alternativ som var valda från början
    private JCheckBox[] headers;		// vektor av rubriker som kan väljas
    private SnittData snittData;		// håller reda på inställningarna
    private JButton acceptButton;		// knapp för att bekräfta inställningarna
    private JButton cancelButton;		// knapp för att avbryta
    private int tabIndex;				// håller reda på vilken snittlista inställningarna gäller

    /** skapar ett fönster med inställningar för vad som skall visas */
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
        appearancePanel.add(new JLabel("Välj vad som skall visas:"));
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
    
    /** stänger ner fönstret utan att ändra på några inställningar */
    private void exitWithoutChanges() {
        for(int i = 0; i < headers.length; i++) {
	        headers[i].setSelected(initialSelection[i]);
	    }
	    snittData.setAppearanceHeaders(headers, tabIndex);
		dispose();
    }
    
    /** klassen som sköter fönsterhanteringen */
	class WindowHandler extends WindowAdapter {
		/** stänger ned fönstret */
		public void windowClosing(WindowEvent e) {
		    exitWithoutChanges();
		}
	}
    
    /** klassen som tar hand om knapptryckningarna vid val av utseende */
    class ButtonHandler implements ActionListener {
        /** kollar vilken knapp som tryckts ned och utför lämplig handling */
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