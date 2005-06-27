import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;

class ManualWindow extends JFrame {
	
	public ManualWindow() {
		super("Manual för BangolfResultat");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setIconImage(SearchWindow.ICON);
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(new Hyperactive());
		URL helpURL = null;
		try {
			helpURL = AboutWindow.class.getResource("file:/E:/Java/bangolf/doc/manual.htm");
			helpURL = new URL("file:/E:/Java/bangolf/doc/manual.htm");
			System.out.println(AboutWindow.class.getResource(""));
		} catch (Exception e) {
			System.out.println(e);
		}
		if (helpURL != null) {
			try {
				editorPane.setPage(helpURL);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + helpURL);
			}
		} else {
			System.err.println("Couldn't find file: manual.htm");
		}
		
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setPreferredSize(new Dimension(1024, 768));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
		
		getContentPane().add(editorScrollPane);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new ManualWindow();
	}
	
	class Hyperactive implements HyperlinkListener {
		
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				JEditorPane pane = (JEditorPane) e.getSource();
				if (e instanceof HTMLFrameHyperlinkEvent) {
					HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
					HTMLDocument doc = (HTMLDocument)pane.getDocument();
					doc.processHTMLFrameHyperlinkEvent(evt);
				} else {
					try {
						pane.setPage(e.getURL());
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
	}
}