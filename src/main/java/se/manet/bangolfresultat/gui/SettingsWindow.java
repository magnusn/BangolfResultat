package se.manet.bangolfresultat.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import se.manet.bangolfresultat.datastruct.DataStore;
import se.manet.bangolfresultat.datastruct.PropertyReader;
import se.manet.bangolfresultat.datastruct.util.Settings;
import se.manet.bangolfresultat.updatecheck.Frequency;

/**
 * Represents the general settings window.
 */
public class SettingsWindow extends JDialog {
	private JComboBox<Frequency> frequencyComboBox;
	private JCheckBox doNotRemindCheckBox;
	private JButton acceptButton;
	private JButton cancelButton;

	/**
	 * Creates the settings window.
	 * 
	 * @param owner
	 *            the <code>Frame</code> from which the dialog is displayed
	 */
	public SettingsWindow(JFrame owner) {
		super(owner, "Inställningar", true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowHandler());

		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(1, 1, 1, 2);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = GridBagConstraints.REMAINDER;
		settingsPanel.setBorder(BorderFactory
				.createTitledBorder("Uppdateringar av "
						+ PropertyReader.getApplicationName()));
		settingsPanel.add(new JLabel(
				"Sök automatiskt efter uppdateringar av BangolfResultat:"), c);

		loadUpdateCheckFrequency();
		loadUpdateCheckDoNotRemind();
		toggleUpdateCheckSettings();
		frequencyComboBox.addItemListener(new FrequencyItemChangeListener());
		settingsPanel.add(frequencyComboBox, c);
		settingsPanel.add(doNotRemindCheckBox, c);
		setResizable(false);

		ButtonHandler buttonHand = new ButtonHandler();
		acceptButton = new JButton("Ok");
		acceptButton.setMnemonic(KeyEvent.VK_O);
		acceptButton.addActionListener(buttonHand);
		cancelButton = new JButton("Avbryt");
		cancelButton.setMnemonic(KeyEvent.VK_A);
		cancelButton.addActionListener(buttonHand);
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 6, 0));
		buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		buttonPanel.add(acceptButton);
		buttonPanel.add(cancelButton);

		setLayout(new GridBagLayout());
		getContentPane().add(settingsPanel, c);
		c.anchor = GridBagConstraints.EAST;
		getContentPane().add(buttonPanel, c);

		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
	}

	/**
	 * Creates the automatic update check frequency <code>ComboBox</code> and
	 * sets initial selected item based on the setting.
	 */
	private void loadUpdateCheckFrequency() {
		frequencyComboBox = new JComboBox<Frequency>(Frequency.values());
		frequencyComboBox.setSelectedItem(Settings.getFrequency());
	}

	/**
	 * Saves the setting for automatic update check frequency.
	 */
	private void saveUpdateCheckFrequency() {
		int selectedIndex = frequencyComboBox.getSelectedIndex();
		if (selectedIndex == -1) {
			return;
		}
		Frequency frequency = frequencyComboBox.getItemAt(selectedIndex);
		DataStore.set(DataStore.UPDATE_CHECK_FREQUENCY, frequency.name());
	}

	/**
	 * Creates the automatic update check reminder <code>CheckBox</code> and
	 * sets its value based on the setting.
	 */
	private void loadUpdateCheckDoNotRemind() {
		doNotRemindCheckBox = new JCheckBox(
				"Informera bara en gång om en och samma version");
		doNotRemindCheckBox.setSelected(Settings.getDoNotRemind());
	}

	/**
	 * Saves the automatic update check reminder setting.
	 */
	private void saveUpdateCheckDoNotRemind() {
		DataStore.set(DataStore.UPDATE_CHECK_DO_NOT_REMIND,
				doNotRemindCheckBox.isSelected());
	}

	/**
	 * Toggles automatic update check settings between enabled or disabled
	 * dependent on the current state of the frequency setting.
	 */
	private void toggleUpdateCheckSettings() {
		Frequency frequency = (Frequency) frequencyComboBox.getSelectedItem();

		if (frequency == Frequency.NEVER && doNotRemindCheckBox.isEnabled()) {
			doNotRemindCheckBox.setEnabled(false);
		} else if (frequency != Frequency.NEVER
				&& !doNotRemindCheckBox.isEnabled()) {
			doNotRemindCheckBox.setEnabled(true);
		}
	}

	/**
	 * Disposes the window without changing any of the settings.
	 */
	private void exitWithoutChanges() {
		dispose();
	}

	/**
	 * Window handler.
	 */
	class WindowHandler extends WindowAdapter {

		public void windowClosing(WindowEvent e) {
			exitWithoutChanges();
		}

	}

	/**
	 * Handles <code>OK</code> and <code>Cancel</code> button actions.
	 */
	class ButtonHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == acceptButton) {
				saveUpdateCheckFrequency();
				saveUpdateCheckDoNotRemind();
				dispose();
			} else if (e.getSource() == cancelButton) {
				exitWithoutChanges();
			}
		}

	}

	/**
	 * Reacts when the currently selected item changes for the automatic update
	 * check frequency setting.
	 */
	class FrequencyItemChangeListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				toggleUpdateCheckSettings();
			}
		}

	}

}
