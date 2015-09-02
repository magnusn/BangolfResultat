package se.manet.bangolfresultat.gui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

/**
 * Utility class for graphical user interfaces.
 */
public class GuiUtil {

	/**
	 * Sets the same preferred size on all components based on the largest
	 * preferred width and height within the given components.
	 * 
	 * @param components
	 *            components whose size should be equal
	 */
	public static void setSameSize(Component... components) {
		// just return if no components
		if (components == null) {
			return;
		}

		// get max width and height
		double maxWidth = Double.MIN_VALUE;
		double maxHeight = Double.MIN_VALUE;
		for (Component component : components) {
			Dimension preferredSize = component.getPreferredSize();
			maxWidth = Math.max(maxWidth, preferredSize.getWidth());
			maxHeight = Math.max(maxHeight, preferredSize.getHeight());
		}

		// just return if illegal values
		if (maxWidth == Double.MIN_VALUE || maxHeight == Double.MIN_VALUE) {
			return;
		}

		// set same preferred size
		for (Component component : components) {
			Dimension dimension = new Dimension();
			dimension.setSize(maxWidth, maxHeight);
			component.setPreferredSize(dimension);
		}
	}

	/**
	 * Returns a <code>JDialog</code> containing an indeterminate progress bar.
	 * 
	 * @param owner
	 *            the <code>Frame</code> from which the dialog is displayed
	 * @param title
	 *            the <code>String</code> to display in the dialog's title bar
	 * @return a <code>JDialog</code> containing an indeterminate progress bar
	 */
	public static JDialog getIndeterminateProgressBarDialog(Frame owner,
			String title) {
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);

		JOptionPane optionPane = new JOptionPane(progressBar,
				JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION,
				null, new Object[] {}, null);

		JDialog dialog = new JDialog(owner, title, true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		dialog.setLocationRelativeTo(owner);

		return dialog;
	}

	/**
	 * Returns a non editable <code>JEditorPane</code> which already has a
	 * hyperlink listener added to itself.
	 * 
	 * @return a non editable <code>JEditorPane</code> which already has a
	 *         hyperlink listener added to itself
	 */
	public static JEditorPane getEditorPaneWithHyperLinkListener() {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setEditorKit(JEditorPane
				.createEditorKitForContentType("text/html"));
		Font font = UIManager.getFont("Label.font");
		if (font != null) {
			String bodyRule = "body { font-family: " + font.getFamily() + "; "
					+ "font-size: " + font.getSize() + "pt; }";
			((HTMLDocument) editorPane.getDocument()).getStyleSheet().addRule(
					bodyRule);
		}
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

		return editorPane;
	}

}
