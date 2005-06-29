/*
 * Created on 2005-jun-28
 * 
 * Created by: Magnus
 */
package snitt;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * AppearanceWindow - beskriver fönstret för att ställa in vad som skall visas i snittlistan
 */
public class AppearanceWindow extends JDialog {
    private int nbrHeaders = 6;			// antal rubriker som kan väljas mellan
    private JCheckBox[] headers;		// vektor av rubriker som kan väljas
    private JButton acceptButton;		// knapp för att bekräfta inställningarna

    protected AppearanceWindow(JFrame owner) {
        super(owner, "Utseende", true);
        setLayout(new GridLayout(nbrHeaders + 2, 1));
        setResizable(false);
        
        headers = new JCheckBox[nbrHeaders];
        headers[0] = new JCheckBox("Klubb");
        headers[1] = new JCheckBox("Tävlingar");
        headers[2] = new JCheckBox("Varv");
        headers[3] = new JCheckBox("Slag");
        headers[4] = new JCheckBox("Snitt ifjol");
        headers[5] = new JCheckBox("Förändring");
        
        getContentPane().add(new JLabel("Välj vad som skall visas:"));
        for(int i = 0; i < nbrHeaders; i++) {
            getContentPane().add(headers[i]);
        }
        getContentPane().add(new JButton("Verkställ"));
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
