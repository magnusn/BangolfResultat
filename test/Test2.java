/*
 * Created on 2005-jul-20
 * 
 * Created by: Magnus
 */
package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Test2
 */
public class Test2 extends JPanel {
    private JButton popupButton;
    public JPanel panel;
    
    public Test2() {
        popupButton = new JButton("Visa popupfönster!");
        popupButton.addActionListener(new ButtonHandler());
        add(new JLabel("Hej hopp!"));
        add(popupButton);
        panel = this;
    }
    
    public void show() {
        JOptionPane.showMessageDialog(null, "Kalle Anka 2");
    }
    
    class ButtonHandler implements ActionListener {
    	/** kollar vilken knapp som tryckts ned */
    	public void actionPerformed(ActionEvent e) {
    		if(e.getSource() == popupButton) {
    			show();
    		}
    	}
    }
}
