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
    private int nbrHeaders = 6;			// antal rubriker som kan väljas mellan
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
                    tempHeaders[i][0] = new JCheckBox("Klubb");
                    tempHeaders[i][1] = new JCheckBox("Tävlingar");
                    tempHeaders[i][2] = new JCheckBox("Varv");
                    tempHeaders[i][3] = new JCheckBox("Slag");
                    tempHeaders[i][4] = new JCheckBox("Snitt ifjol");
                    tempHeaders[i][5] = new JCheckBox("Förändring");
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
                headers[i][0] = new JCheckBox("Klubb");
                headers[i][1] = new JCheckBox("Tävlingar");
                headers[i][2] = new JCheckBox("Varv");
                headers[i][3] = new JCheckBox("Slag");
                headers[i][4] = new JCheckBox("Snitt ifjol");
                headers[i][5] = new JCheckBox("Förändring");
            }
        }
        
        ButtonHandler buttonHand = new ButtonHandler();
        
        getContentPane().add(new JLabel("Välj vad som skall visas:"));
        for(int i = 0; i < nbrHeaders; i++) {
            getContentPane().add(headers[tabIndex][i]);
        }
        
        acceptButton = new JButton("Verkställ");
        acceptButton.addActionListener(buttonHand);
        getContentPane().add(acceptButton);
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    
    /** klassen som tar hand om knapptryckningarna vid val av utseende */
	class ButtonHandler implements ActionListener {
		/** kollar vilken knapp som tryckts ned och utför lämplig handling */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == acceptButton) {
			    try {
			        IOHandler io = new IOHandler();
			        io.save("snittapp", headers);
			    } catch (IOException ex) {
			        JOptionPane.showMessageDialog(thisFrame, "Inställningarna gick ej att spara", "Varning", JOptionPane.ERROR_MESSAGE);
			    }
				setVisible(false);
			}
		}
	}
}