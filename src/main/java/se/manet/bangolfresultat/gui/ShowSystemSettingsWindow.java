package se.manet.bangolfresultat.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import se.manet.bangolfresultat.datastruct.IOHandler;

public class ShowSystemSettingsWindow extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1600687848842432399L;

	private JDialog dialog;
	private JButton copyToClipboardButton;
	private JButton okButton;

	public ShowSystemSettingsWindow(JFrame owner) {
		super(owner, "Systeminställningar", true);
		setResizable(false);

		dialog = this;
		ActionListener buttonActionListener = new ButtonActionListener();
		copyToClipboardButton = new JButton("Kopiera till urklipp");
		okButton = new JButton("OK");
		copyToClipboardButton.addActionListener(buttonActionListener);
		okButton.addActionListener(buttonActionListener);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(1, 1, 1, 2);
		c.anchor = GridBagConstraints.NORTHWEST;
		panel.setBorder(BorderFactory.createTitledBorder("Systeminställningar"));
		panel.add(new JLabel("Inställningar sparas i:"), c);
		c.gridx = 1;
		panel.add(new JLabel(IOHandler.getSettingsPath()), c);

		setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(panel, c);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		add(copyToClipboardButton, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.NORTHEAST;
		add(okButton, c);

		pack();
		setLocationRelativeTo(owner);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Copies displayed information to clipboard.
	 */
	private void copyToClipboard() {
		StringSelection selection = new StringSelection(
				IOHandler.getSettingsPath());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, null);
	}

	class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				dialog.dispose();
			} else if (e.getSource() == copyToClipboardButton) {
				copyToClipboard();
			}
		}

	}
}
