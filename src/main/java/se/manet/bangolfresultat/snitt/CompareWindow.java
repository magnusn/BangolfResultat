/*
 * Created on 2005-jul-01
 * 
 * Created by: Magnus
 */
package se.manet.bangolfresultat.snitt;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import se.manet.bangolfresultat.datastruct.Filter;
import se.manet.bangolfresultat.gui.GuiUtil;
import se.manet.bangolfresultat.gui.SearchWindow;

/**
 * CompareWindow - beskriver f�nstret d�r man v�ljer fil att j�mf�ra snittlistan med
 */
public class CompareWindow extends JDialog {
    private SnittData snittData;	// h�ller reda p� inst�llningarna
    private CompareWindow frame;	// detta f�nster
    private int tabIndex;			// anger vilken snittlista det g�ller
    private JTextField choosenFile; // visar vilken fil som �r vald
    private JButton chooseButton;	// knapp f�r att v�lja fil
    private JButton acceptButton;	// sparar och st�nger ner f�nstret
    private JButton cancelButton;	// avbryter och st�nger ner f�nstret
    private JButton removeButton;	// tar bort vald fil
    
    /** skapar ett f�nster f�r att v�lja snittlista att j�mf�ra med */
    public CompareWindow(JFrame owner, int tabIndex, SnittData snittData) {
        super(owner, "V�lj fil att j�mf�ra med", true);
        setResizable(false);
        this.frame = this;
        this.tabIndex = tabIndex;
        this.snittData = snittData;
        
        ButtonHandler buttonHand = new ButtonHandler();
        chooseButton = new JButton("V�lj fil...");
        chooseButton.addActionListener(buttonHand);
        chooseButton.setMnemonic(KeyEvent.VK_V);
        choosenFile = new JTextField(snittData.getCompareFile(tabIndex), 30);
        choosenFile.setEditable(false);
        
        JPanel chooseFilePanel = new JPanel();
        chooseFilePanel.add(new JLabel("Vald fil:"));
        chooseFilePanel.add(choosenFile);
        chooseFilePanel.add(chooseButton);
        
        FlowLayout flowLayout = new FlowLayout();
        JPanel buttonPanel = new JPanel(flowLayout);
        acceptButton = new JButton("Ok");
        acceptButton.addActionListener(buttonHand);
        acceptButton.setMnemonic(KeyEvent.VK_O);
        removeButton = new JButton("Ta bort");
        removeButton.addActionListener(buttonHand);
        removeButton.setMnemonic(KeyEvent.VK_T);
        cancelButton = new JButton("Avbryt");
        cancelButton.addActionListener(buttonHand);
        cancelButton.setMnemonic(KeyEvent.VK_A);
        GuiUtil.setSameSize(acceptButton, removeButton, cancelButton);
        buttonPanel.add(acceptButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
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
			    snittData.setCompareFile(choosenFile.getText(), tabIndex);
				setVisible(false);
			} else if (e.getSource() == removeButton) {
			    choosenFile.setText("");
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
            choosenFile.setText(path);
            
            if(!name.endsWith(".snitt")) {
                JOptionPane.showMessageDialog(frame, "Filen m�ste ha �ndelsen .snitt", "Varning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
