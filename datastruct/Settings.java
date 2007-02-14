package datastruct;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JOptionPane;

/**
 * This class stores different settings.
 */
public class Settings {
	private static Hashtable settings;
	private static final String filename = "settings";
	
	/**
	 * Sets a setting named key to value.
	 * 
	 * @param key name of setting.
	 * @param value value of setting.
	 */
	public static void set(String key, Object value) {
		if (settings == null)
			load();
		settings.put(key, value);
		save();
	}
	
	/**
	 * Gets the value of the setting named key.
	 * 
	 * @param key name of setting which value is returned.
	 * @return value for this setting.
	 */
	public static Object get(String key) {
		if (settings == null)
			load();
		return settings.get(key);
	}
	
	/**
	 * Loads the Hashtable containing all settings from file.
	 */
	private static void load() {
		IOHandler io = new IOHandler();
		try {
			settings = (Hashtable) io.load(filename);
			if (settings == null)
				settings = new Hashtable();
		} catch (Exception e) {
			File file = new File(IOHandler.dataPath + filename);
			if (file.exists()) {
				JOptionPane.showMessageDialog(null,
						"Inställningarna i filen 'settings' går inte att " +
						"läsa in trots att filen existerar. Programmet " +
						"avslutas.", "Kritiskt fel", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			} else {
				settings = new Hashtable();
			}
		}
	}
	
	/**
	 * Saves the Hashtable that contains all settings to file.
	 */
	private static void save() {
		if (settings == null)
			load();
		IOHandler io = new IOHandler();
		try {
			io.save(filename, settings);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
}
