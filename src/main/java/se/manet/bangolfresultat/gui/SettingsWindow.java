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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import se.manet.bangolfresultat.datastruct.DataStore;
import se.manet.bangolfresultat.datastruct.PropertyReader;
import se.manet.bangolfresultat.datastruct.util.Settings;
import se.manet.bangolfresultat.updatecheck.Frequency;

/**
 * Represents the general settings window.
 */
public class SettingsWindow extends JDialog {

	public static final String JAVA_LOOK_AND_FEEL = "Java";
	public static final String SYSTEM_LOOK_AND_FEEL = "Naturlig";

	private JComboBox<Frequency> frequencyComboBox;
	private JCheckBox doNotRemindCheckBox;
	private JComboBox<String> lookAndFeelComboBox;
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

		JPanel updateCheckPanel = new JPanel();
		updateCheckPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(1, 1, 1, 2);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1;
		updateCheckPanel.setBorder(BorderFactory
				.createTitledBorder("Uppdateringar av "
						+ PropertyReader.getApplicationName()));
		updateCheckPanel.add(new JLabel(
				"Sök automatiskt efter uppdateringar av BangolfResultat:"), c);

		loadUpdateCheckFrequency();
		loadUpdateCheckDoNotRemind();
		toggleUpdateCheckSettings();
		frequencyComboBox.addItemListener(new FrequencyItemChangeListener());
		updateCheckPanel.add(frequencyComboBox, c);
		updateCheckPanel.add(doNotRemindCheckBox, c);
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
		c.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(updateCheckPanel, c);
		getContentPane().add(createLookAndFeelPanel(), c);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		getContentPane().add(buttonPanel, c);

		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
	}

	/**
	 * Creates the look and feel panel and calls {@link #loadLookAndFeel()}.
	 * 
	 * @return the look and feel panel
	 */
	private JPanel createLookAndFeelPanel() {
		JPanel lookAndFeelPanel = new JPanel();
		lookAndFeelPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(1, 1, 1, 2);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1;
		lookAndFeelPanel.setBorder(BorderFactory
				.createTitledBorder("Utseende och känsla"));
		lookAndFeelPanel.add(new JLabel("Välj utseende och känsla:"), c);
		lookAndFeelPanel.add(
				new JLabel("(Börjar gälla när "
						+ PropertyReader.getApplicationName()
						+ " har startats om)"), c);
		loadLookAndFeel();
		lookAndFeelPanel.add(lookAndFeelComboBox, c);

		return lookAndFeelPanel;
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
	 * Creates the look and feel <code>ComboBox</code> and sets its value based
	 * on the current setting.
	 */
	private void loadLookAndFeel() {
		String initialSelectedItem = JAVA_LOOK_AND_FEEL;
		String lookAndFeel = (String) DataStore.get(DataStore.LOOK_AND_FEEL);
		if (lookAndFeel != null) {
			if (lookAndFeel.equals(UIManager.getSystemLookAndFeelClassName())) {
				initialSelectedItem = SYSTEM_LOOK_AND_FEEL;
			}
		}
		lookAndFeelComboBox = new JComboBox<String>(new String[] {
				JAVA_LOOK_AND_FEEL, SYSTEM_LOOK_AND_FEEL });
		lookAndFeelComboBox.setSelectedItem(initialSelectedItem);
	}

	/**
	 * Saves the look and feel setting.
	 */
	private void saveLookAndFeel() {
		int selectedIndex = lookAndFeelComboBox.getSelectedIndex();
		if (selectedIndex == -1) {
			return;
		}
		String lookAndFeel = lookAndFeelComboBox.getItemAt(selectedIndex);
		if (lookAndFeel.equals(JAVA_LOOK_AND_FEEL)) {
			DataStore.set(DataStore.LOOK_AND_FEEL,
					UIManager.getCrossPlatformLookAndFeelClassName());
		} else if (lookAndFeel.equals(SYSTEM_LOOK_AND_FEEL)) {
			DataStore.set(DataStore.LOOK_AND_FEEL,
					UIManager.getSystemLookAndFeelClassName());
		}
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
				saveLookAndFeel();
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
