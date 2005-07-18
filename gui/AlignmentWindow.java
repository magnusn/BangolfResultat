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
 * NumberOrientationWindow - beskriver f�nstret som anv�nds f�r inst�llning av sifferorienteringen
 */
public class AlignmentWindow extends JDialog {
    private JDialog thisWindow;				// detta f�nster
    private int nbrOfOwner;					// numret f�r den som skapat detta f�nster
    private JRadioButton[] orientation;		// radioknappar f�r de olika orienteringsm�jligheterna
    private JButton accept, cancel;			// knappar f�r att v�lja de nya inst�llningarna eller avbryta
    private DataManager dataManager;		// h�ller reda p� inst�llningarna;
    public static final int LEFT = 0;		// heltalsv�rde f�r v�nsterjusterat
    public static final int CENTER = 1;		// heltalsv�rde f�r centrerat
    public static final int RIGHT = 2;		// heltalsv�rde f�r h�gerjusterat
    public static final int NBR_OWNERS = 2;	// antal olika sifferorienteringsf�nster
    private static final int NBR_ALIGN = 3;	// antal olika orienteringsm�jligheter
    public static final int COMP_OWNER = 0;	// nummer f�r inst�llningar f�r en vanlig t�vling
    public static final int SNITT_OWNER = 1;// nummer f�r inst�llningar f�r snittlista
    
    /** skapar ett sifferorienteringsf�nster till �garen owner */
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
        orientation[LEFT] = new JRadioButton("V�nsterjusterat");
        orientation[CENTER] = new JRadioButton("Centrerat");
        orientation[RIGHT] = new JRadioButton("H�gerjusterat");
        orientation[DataManager.getOrientation(nbrOfOwner)].setSelected(true);
        
        mainPanel.add(new JLabel("St�ll in �nskad sifferjustering:"));
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
		/** kollar vilken knapp som tryckts ned och utf�r l�mplig handling */
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