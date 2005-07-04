/*
 * Created on 2005-jun-28
 * 
 * Created by: Magnus
 */
package snitt;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import datastruct.IOHandler;

/**
 * AppearanceWindow - beskriver fönstret för att ställa in vad som skall visas i snittlistan
 */
public class AppearanceWindow extends JDialog {
    private JDialog thisFrame;			// refererar till detta fönster
    private int nbrHeaders = 8;			// antal möjliga rubriker
    private JCheckBox[][] headers;		// vektor av rubriker som kan väljas
    private JButton acceptButton;		// knapp för att bekräfta inställningarna
    private int tabIndex;				// håller reda på vilken snittlista inställningarna gäller

    /** skapar ett fönster med inställningar för vad som skall visas */
    public AppearanceWindow(JFrame owner, int tabIndex, int nbrTabs) {
        super(owner, "Utseende", true);
        thisFrame = this;
        this.tabIndex = tabIndex;
        setLayout(new GridLayout(nbrHeaders + 2, 1));
        setResizable(false);
        
        try {
            IOHandler io = new IOHandler();
            headers = (JCheckBox[][]) io.load("snittapp");
            if(headers.length != nbrTabs) {
                JCheckBox[][] tempHeaders = new JCheckBox[nbrTabs][nbrHeaders];
                for(int i = 0; i < nbrTabs; i++) {
                    tempHeaders[i][Snitt.NAME] = new JCheckBox("Namn", true);
                    tempHeaders[i][Snitt.NAME].setEnabled(false);
                    tempHeaders[i][Snitt.CLUB] = new JCheckBox("Klubb", true);
                    tempHeaders[i][Snitt.COMPS] = new JCheckBox("Tävlingar", true);
                    tempHeaders[i][Snitt.ROUNDS] = new JCheckBox("Varv", true);
                    tempHeaders[i][Snitt.HITSUM] = new JCheckBox("Slag", true);
                    tempHeaders[i][Snitt.MEAN] = new JCheckBox("Snitt", true);
                    tempHeaders[i][Snitt.MEAN].setEnabled(false);
                    tempHeaders[i][Snitt.EX_MEAN] = new JCheckBox("Snitt ifjol");
                    tempHeaders[i][Snitt.CHANGE] = new JCheckBox("Förändring");
                }
                for(int i = 0; i < headers.length; i++) {
                    tempHeaders[i] = headers[i];
                }
                headers = tempHeaders;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(owner, "Föregående inställningar gick ej att läsa in", "Varning", JOptionPane.ERROR_MESSAGE);
            headers = new JCheckBox[nbrTabs][nbrHeaders];
            for(int i = 0; i < nbrTabs; i++) {
                headers[i][Snitt.NAME] = new JCheckBox("Namn", true);
                headers[i][Snitt.NAME].setEnabled(false);
                headers[i][Snitt.CLUB] = new JCheckBox("Klubb", true);
                headers[i][Snitt.COMPS] = new JCheckBox("Tävlingar", true);
                headers[i][Snitt.ROUNDS] = new JCheckBox("Varv", true);
                headers[i][Snitt.HITSUM] = new JCheckBox("Slag", true);
                headers[i][Snitt.MEAN] = new JCheckBox("Snitt", true);
                headers[i][Snitt.MEAN].setEnabled(false);
                headers[i][Snitt.EX_MEAN] = new JCheckBox("Snitt ifjol");
                headers[i][Snitt.CHANGE] = new JCheckBox("Förändring");
            }
        }
        if(CompareWindow.getCompareFile(tabIndex) != null) {
            headers[tabIndex][Snitt.EX_MEAN].setEnabled(true);
            headers[tabIndex][Snitt.CHANGE].setEnabled(true);
            headers[tabIndex][Snitt.EX_MEAN].setToolTipText(null);
            headers[tabIndex][Snitt.CHANGE].setToolTipText(null);
        } else {
            headers[tabIndex][Snitt.EX_MEAN].setEnabled(false);
            headers[tabIndex][Snitt.CHANGE].setEnabled(false);
            headers[tabIndex][Snitt.EX_MEAN].setSelected(false);
            headers[tabIndex][Snitt.CHANGE].setSelected(false);
            headers[tabIndex][Snitt.EX_MEAN].setToolTipText("Aktiveras först då jämförelsefil är vald");
            headers[tabIndex][Snitt.CHANGE].setToolTipText("Aktiveras först då jämförelsefil är vald");
        }
        
        getContentPane().add(new JLabel("Välj vad som skall visas:"));
        for(int i = 0; i < nbrHeaders; i++) {
            getContentPane().add(headers[tabIndex][i]);
        }
        
        ButtonHandler buttonHand = new ButtonHandler();
        acceptButton = new JButton("Verkställ");
        acceptButton.addActionListener(buttonHand);
        getContentPane().add(acceptButton);
        
        pack();
        setLocationRelativeTo(owner);
    }
    
    /** stänger ner och sparar */
    protected void saveAndExit() {
        try {
            IOHandler io = new IOHandler();
            io.save("snittapp", headers);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(thisFrame, "Inställningarna gick ej att spara", "Varning", JOptionPane.ERROR_MESSAGE);
        }
        setVisible(false);
    }
    
    /** klassen som tar hand om knapptryckningarna vid val av utseende */
    class ButtonHandler implements ActionListener {
        /** kollar vilken knapp som tryckts ned och utför lämplig handling */
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == acceptButton) {
                saveAndExit();
            }
        }
    }
}