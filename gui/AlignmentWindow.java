/*
 * Created on 2005-jul-18
 * 
 * Created by: Magnus
 */
package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import datastruct.DataManager;

/**
 * NumberOrientationWindow - beskriver fönstret som används för inställning av sifferorienteringen
 */
public class AlignmentWindow extends JDialog {
    private JDialog thisWindow;				// detta fönster
    private int nbrOfOwner;					// numret för den som skapat detta fönster
    private JRadioButton[] orientation;		// radioknappar för de olika orienteringsmöjligheterna
    private JButton accept, cancel;			// knappar för att välja de nya inställningarna eller avbryta
    private DataManager dataManager;		// håller reda på inställningarna;
    public static final int LEFT = 0;		// heltalsvärde för vänsterjusterat
    public static final int CENTER = 1;		// heltalsvärde för centrerat
    public static final int RIGHT = 2;		// heltalsvärde för högerjusterat
    public static final int NBR_OWNERS = 2;	// antal olika sifferorienteringsfönster
    private static final int NBR_ALIGN = 3;	// antal olika orienteringsmöjligheter
    public static final int COMP_OWNER = 0;	// nummer för inställningar för en vanlig tävling
    public static final int SNITT_OWNER = 1;// nummer för inställningar för snittlista
    
    /** skapar ett sifferorienteringsfönster till ägaren owner */
    public AlignmentWindow(JFrame owner, int nbrOfOwner, DataManager dataManager) {
        super(owner, "Sifferorientering", true);
        setResizable(false);
        thisWindow = this;
        this.nbrOfOwner = nbrOfOwner;
        this.dataManager = dataManager;
        
        ButtonHandler buttonHand = new ButtonHandler();
        accept = new JButton("Ok");
        cancel = new JButton("Avbryt");
        accept.addActionListener(buttonHand);
        cancel.addActionListener(buttonHand);
        
        GridLayout mainLayout = new GridLayout(NBR_ALIGN+1,1);
        mainLayout.setHgap(1);
        mainLayout.setVgap(1);
        GridLayout buttonLayout = new GridLayout(1,2);
        buttonLayout.setHgap(1);
        buttonLayout.setVgap(1);
        JPanel mainPanel = new JPanel(mainLayout);
        JPanel buttonPanel = new JPanel(buttonLayout);
        ButtonGroup buttonGroup = new ButtonGroup();
        
        orientation = new JRadioButton[NBR_ALIGN];
        orientation[LEFT] = new JRadioButton("Vänsterjusterat");
        orientation[CENTER] = new JRadioButton("Centrerat");
        orientation[RIGHT] = new JRadioButton("Högerjusterat");
        orientation[DataManager.getOrientation(nbrOfOwner)].setSelected(true);
        
        mainPanel.add(new JLabel("Ställ in önskad sifferjustering:"));
        for(int i = 0; i < orientation.length; i++) {
            buttonGroup.add(orientation[i]);
            mainPanel.add(orientation[i]);
        }
        
        buttonPanel.add(accept);
        buttonPanel.add(cancel);
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    
    /** klassen som tar hand om knapptryckningarna vid val av sifferorientering */
	class ButtonHandler implements ActionListener {
		/** kollar vilken knapp som tryckts ned och utför lämplig handling */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == accept) {
			    int alignment;
			    if(orientation[LEFT].isSelected()) {
			        alignment = LEFT;
			    } else if(orientation[CENTER].isSelected()) {
			        alignment = CENTER;
			    } else {
			        alignment = RIGHT;
			    }
			    if(DataManager.getOrientation(nbrOfOwner) != alignment) {
			        dataManager.setOrientation(alignment, nbrOfOwner);
			        if(nbrOfOwner == AlignmentWindow.COMP_OWNER) {
			            ResultInputWindow.BOARD.update(true);
			        }
			    }
			    
			    thisWindow.dispose();
			} else if (e.getSource() == cancel) {
			    thisWindow.dispose();
			}
		}
	}
}