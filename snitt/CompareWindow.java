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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datastruct.Filter;

/**
 * CompareWindow - beskriver fönstret där man väljer fil att jämföra snittlistan med
 */
public class CompareWindow extends JDialog {
    private SnittData snittData;	// håller reda på inställningarna
    private CompareWindow frame;	// detta fönster
    private int tabIndex;			// anger vilken snittlista det gäller
    private JTextField choosenFile; // visar vilken fil som är vald
    private JButton chooseButton;	// knapp för att välja fil
    private JButton acceptButton;	// sparar och stänger ner fönstret
    private JButton cancelButton;	// avbryter och stänger ner fönstret
    private JButton removeButton;	// tar bort vald fil
    
    /** skapar ett fönster för att välja snittlista att jämföra med */
    public CompareWindow(JFrame owner, int tabIndex, SnittData snittData) {
        super(owner, "Välj fil att jämföra med", true);
        setResizable(false);
        this.frame = this;
        this.tabIndex = tabIndex;
        this.snittData = snittData;
        
        ButtonHandler buttonHand = new ButtonHandler();
        chooseButton = new JButton("Välj fil...");
        chooseButton.addActionListener(buttonHand);
        choosenFile = new JTextField(snittData.getCompareFile(tabIndex));
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
		/** kollar vilken knapp som tryckts ned och utför lämplig handling */
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
    
	/** visar fönster för att välja fil att jämföra med */
    private void setCompareFile() {
        Filter snittFilter = new Filter(new String[]{"snitt"}, "Jämförelsefil för snittlista");
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
                JOptionPane.showMessageDialog(frame, "Filen måste ha ändelsen .snitt", "Varning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
