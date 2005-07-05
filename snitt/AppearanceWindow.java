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
 * AppearanceWindow - beskriver fönstret för att ställa in vad som skall visas i snittlistan
 */
public class AppearanceWindow extends JDialog {
    private JCheckBox[] headers;		// vektor av rubriker som kan väljas
    private SnittData snittData;		// håller reda på inställningarna
    private JButton acceptButton;		// knapp för att bekräfta inställningarna
    private int tabIndex;				// håller reda på vilken snittlista inställningarna gäller

    /** skapar ett fönster med inställningar för vad som skall visas */
    public AppearanceWindow(JFrame owner, int tabIndex, SnittData snittData) {
        super(owner, "Utseende", true);
        this.tabIndex = tabIndex;
        this.snittData = snittData;
        setLayout(new GridLayout(SnittData.NBR_HEADERS + 2, 1));
        setResizable(false);
        
        headers = snittData.getAppearanceHeaders(tabIndex);
        getContentPane().add(new JLabel("Välj vad som skall visas:"));
        for(int i = 0; i < headers.length; i++) {
            getContentPane().add(headers[i]);
        }
        
        ButtonHandler buttonHand = new ButtonHandler();
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
                snittData.setAppearanceHeaders(headers, tabIndex);
                setVisible(false);
            }
        }
    }
}