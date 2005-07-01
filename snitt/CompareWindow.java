/*
 * Created on 2005-jul-01
 * 
 * Created by: Magnus
 */
package snitt;

import gui.SearchWindow;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datastruct.Filter;
import datastruct.IOHandler;

/**
 * CompareWindow - beskriver f�nstret d�r man v�ljer fil att j�mf�ra snittlistan med
 */
public class CompareWindow extends JDialog {
    private CompareWindow frame;	// detta f�nster
    private int tabIndex;			// anger vilken snittlista det g�ller
    private String[] files;			// inneh�ller filerna som valts
    private JTextField choosenFile; // visar vilken fil som �r vald
    private JButton chooseButton;	// knapp f�r att v�lja fil
    private JButton acceptButton;	// sparar och st�nger ner f�nstret
    private JButton cancelButton;	// avbryter och st�nger ner f�nstret
    private JButton removeButton;	// tar bort vald fil
    
    /** skapar ett f�nster f�r att v�lja snittlista att j�mf�ra med */
    public CompareWindow(JFrame owner, int tabIndex, int nbrTabs) {
        super(owner, "V�lj fil att j�mf�ra med", true);
        this.tabIndex = tabIndex;
        //setLayout(new GridLayout(2, 1));
        setResizable(false);
        this.frame = this;
        
        try {
            IOHandler io = new IOHandler();
            files = (String[]) io.load("comparefiles");
            if(files.length != nbrTabs) {
                String[] tempFiles = new String[nbrTabs];
                for(int i = 0; i < nbrTabs; i++) {
                    tempFiles[i] = new String();
                }
                for(int i = 0; i < files.length; i++) {
                    tempFiles[i] = files[i];
                }
                files = tempFiles;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(owner, "F�reg�ende inst�llningar gick ej att l�sa in", "Varning", JOptionPane.ERROR_MESSAGE);
            files = new String[nbrTabs];
            for(int i = 0; i < nbrTabs; i++) {
                files[i] = new String();
            }
        }
        
        ButtonHandler buttonHand = new ButtonHandler();
        chooseButton = new JButton("V�lj fil...");
        chooseButton.addActionListener(buttonHand);
        choosenFile = new JTextField(files[tabIndex]);
        choosenFile.setEditable(false);
        choosenFile.setPreferredSize(new Dimension(300,26));
        
        JPanel chooseFilePanel = new JPanel();
        chooseFilePanel.add(new JLabel("Vald fil:"));
        chooseFilePanel.add(choosenFile);
        chooseFilePanel.add(chooseButton);
        
        FlowLayout flowLayout = new FlowLayout();
        JPanel buttonPanel = new JPanel(flowLayout);
        //flowLayout.setHgap(60);
        acceptButton = new JButton("Ok");
        acceptButton.setPreferredSize(new Dimension(77,26));
        acceptButton.addActionListener(buttonHand);
        removeButton = new JButton("Ta bort");
        removeButton.setPreferredSize(new Dimension(77,26));
        removeButton.addActionListener(buttonHand);
        cancelButton = new JButton("Avbryt");
        cancelButton.setPreferredSize(new Dimension(77,26));
        cancelButton.addActionListener(buttonHand);
        buttonPanel.add(acceptButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1));
        panel.add(chooseFilePanel);
        panel.add(buttonPanel);
        getContentPane().add(panel);
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    
    /** klassen som tar hand om knapptryckningarna vid val av utseende */
	class ButtonHandler implements ActionListener {
		/** kollar vilken knapp som tryckts ned och utf�r l�mplig handling */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == chooseButton) {
			    setCompareFile();
			} else if (e.getSource() == acceptButton) {
			    try {
			        IOHandler io = new IOHandler();
			        io.save("comparefiles", files);
			    } catch (IOException ex) {
			        JOptionPane.showMessageDialog(frame, "Inst�llningarna gick ej att spara", "Varning", JOptionPane.ERROR_MESSAGE);
			    }
				setVisible(false);
			} else if (e.getSource() == removeButton && files[tabIndex] != "") {
			    files[tabIndex] = "";
			    choosenFile.setText(files[tabIndex]);
			} else if (e.getSource() == cancelButton) {
			    frame.dispose();
			}
		}
	}
    
	/** visar f�nster f�r att v�lja fil att j�mf�ra med */
    private void setCompareFile() {
        Filter snittFilter = new Filter(new String[]{"snitt"}, "J�mf�relsefil f�r snittlista");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(SearchWindow.DIRJMF);
        fileChooser.setFileFilter(snittFilter);
        int retval = fileChooser.showOpenDialog(frame);
        if(retval == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            SearchWindow.DIRJMF = fileChooser.getCurrentDirectory();
            
            String name = file.getName();
            String path = file.getPath();
            files[tabIndex] = path;
            choosenFile.setText(files[tabIndex]);
            
            if(name.endsWith(".snitt")) {
                
            } else {
                JOptionPane.showMessageDialog(frame, "Filen m�ste ha �ndelsen .snitt", "Varning", JOptionPane.WARNING_MESSAGE);
            }
        } else if(retval == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(frame, "Anv�ndaren avbr�t operationen. Ingen fil valdes.");
        }
    }
}
