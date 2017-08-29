package se.manet.bangolfresultat.gui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import se.manet.bangolfresultat.datastruct.PropertyReader;

public class AboutWindow extends JDialog {

	public AboutWindow(JFrame owner) {
		super(owner, "Om " + PropertyReader.getApplicationName(), true);
		setResizable(false);
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setEditorKit(JEditorPane
				.createEditorKitForContentType("text/html"));
		editorPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException e1) {
						} catch (URISyntaxException e1) {
						}
					}
				}
			}
		});
		java.net.URL helpURL = AboutWindow.class.getClassLoader().getResource(
				"doc/om.htm");

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
		editorScrollPane.setPreferredSize(new Dimension(250, 150));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));

		getContentPane().add(editorScrollPane);
		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
	}

}