package se.manet.bangolfresultat.gui;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JDialog;

import se.manet.bangolfresultat.datastruct.PropertyReader;

public class AboutWindow extends JDialog {
	
	public AboutWindow(JFrame owner) {
		super(owner, "Om " + PropertyReader.getApplicationName(), true);
		setResizable(false);
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		java.net.URL helpURL = AboutWindow.class.getClassLoader().getResource("doc/om.htm");
		
        if (helpURL != null) {
			try {
				editorPane.setPage(helpURL);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + helpURL);
			}
		} else {
			System.err.println("Couldn't find file: om.htm");
		}
		
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setPreferredSize(new Dimension(280, 170));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
		
		getContentPane().add(editorScrollPane);
		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
	}
}