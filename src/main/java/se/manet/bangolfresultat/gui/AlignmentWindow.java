/*
 * Created on 2005-jul-18
 * 
 * Created by: Magnus
 */
package se.manet.bangolfresultat.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import se.manet.bangolfresultat.datastruct.AlignmentManager;

/**
 * NumberOrientationWindow - beskriver f�nstret som anv�nds f�r inst�llning av sifferorienteringen
 */
public class AlignmentWindow extends JDialog {
    private JDialog thisWindow;				// detta f�nster
    private int nbrOfOwner;					// numret f�r den som skapat detta f�nster
    private JRadioButton[] orientation;		// radioknappar f�r de olika orienteringsm�jligheterna
    private JButton acceptButton;			// knapp f�r att acceptera de nya inst�llingarna
    private JButton cancelButton;			// knapp f�r att avbryta
    private AlignmentManager alignmentManager;// h�ller reda p� inst�llningarna
    public static final int LEFT = 0;		// heltalsv�rde f�r v�nsterjusterat
    public static final int CENTER = 1;		// heltalsv�rde f�r centrerat
    public static final int RIGHT = 2;		// heltalsv�rde f�r h�gerjusterat
    public static final int NBR_OWNERS = 2;	// antal olika sifferorienteringsf�nster
    private static final int NBR_ALIGN = 3;	// antal olika orienteringsm�jligheter
    public static final int COMP_OWNER = 0;	// nummer f�r inst�llningar f�r en vanlig t�vling
    public static final int SNITT_OWNER = 1;// nummer f�r inst�llningar f�r snittlista
    
    /** skapar ett sifferorienteringsf�nster till �garen owner */
    public AlignmentWindow(JFrame owner, int nbrOfOwner, AlignmentManager alignmentManager) {
        super(owner, "Sifferorientering", true);
        setResizable(false);
        thisWindow = this;
        this.nbrOfOwner = nbrOfOwner;
        this.alignmentManager = alignmentManager;
        
        ButtonHandler buttonHand = new ButtonHandler();
        acceptButton = new JButton("Ok");
        cancelButton = new JButton("Avbryt");
        acceptButton.addActionListener(buttonHand);
        cancelButton.addActionListener(buttonHand);
        acceptButton.setMnemonic(KeyEvent.VK_O);
        cancelButton.setMnemonic(KeyEvent.VK_A);
        
        GridLayout mainLayout = new GridLayout(NBR_ALIGN+1,1);
        mainLayout.setVgap(1);
        GridLayout buttonLayout = new GridLayout(1,2,6,0);
        JPanel mainPanel = new JPanel(mainLayout);
        mainPanel.setBorder(new EmptyBorder(5,5,5,5));
        JPanel buttonPanel = new JPanel(buttonLayout);
        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));
        ButtonGroup buttonGroup = new ButtonGroup();
        
        orientation = new JRadioButton[NBR_ALIGN];
        orientation[LEFT] = new JRadioButton("V�nsterjusterat");
        orientation[CENTER] = new JRadioButton("Centrerat");
        orientation[RIGHT] = new JRadioButton("H�gerjusterat");
        orientation[AlignmentManager.getOrientation(nbrOfOwner)].setSelected(true);
        
        mainPanel.add(new JLabel("St�ll in �nskad sifferjustering:"));
        for(int i = 0; i < orientation.length; i++) {
            buttonGroup.add(orientation[i]);
            mainPanel.add(orientation[i]);
        }
        
        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);
        
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
			if(e.getSource() == acceptButton) {
			    int alignment;
			    if(orientation[LEFT].isSelected()) {
			        alignment = LEFT;
			    } else if(orientation[CENTER].isSelected()) {
			        alignment = CENTER;
			    } else {
			        alignment = RIGHT;
			    }
			    if(AlignmentManager.getOrientation(nbrOfOwner) != alignment) {
			        alignmentManager.setOrientation(alignment, nbrOfOwner);
			        if(nbrOfOwner == AlignmentWindow.COMP_OWNER) {
			            ResultInputWindow.BOARD.update(true);
			        }
			    }
			    
			    thisWindow.dispose();
			} else if (e.getSource() == cancelButton) {
			    thisWindow.dispose();
			}
		}
	}
}