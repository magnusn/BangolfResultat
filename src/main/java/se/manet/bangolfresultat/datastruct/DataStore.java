package se.manet.bangolfresultat.datastruct;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JOptionPane;

/**
 * This class stores data.
 */
public class DataStore {
	/* List of keys. */
	public static final String LOOK_AND_FEEL = "lookAndFeel";
	public static final String SNITT_EXCL_CLUBS = "snittExclClubs";
	public static final String SNITT_FILE_MAP = "snittFileMap";
	public static final String SNITT_TAB_INDEX = "snittTabIndex";
	public static final String SNITT_TAB_TITLE = "snittTabTitle";
	/* End of list of keys. */
	
	private static Hashtable dataStore;
	private static final String filename = "datastore";
	
	/**
	 * Maps the specified key to the specified value.
	 * 
	 * @param key the key.
	 * @param value the value.
	 */
	public static void set(String key, Object value) {
		if (dataStore == null)
			load();
		dataStore.put(key, value);
		save();
	}
	
	/**
	 * Returns the value to which the specified key is mapped.
	 * 
	 * @param key a key.
	 * @return the value to which the key is mapped; {@code null} if no mapping
	 * exists.
	 */
	public static Object get(String key) {
		if (dataStore == null)
			load();
		return dataStore.get(key);
	}
	
	/**
	 * Loads the Hashtable containing all the data from file.
	 */
	private static void load() {
		IOHandler io = new IOHandler();
		try {
			dataStore = (Hashtable) io.load(filename);
			if (dataStore == null)
				dataStore = new Hashtable();
		} catch (Exception e) {
			File file = new File(IOHandler.getSettingsPath() + filename);
			if (file.exists()) {
				JOptionPane.showMessageDialog(null,
						"Data ifrån filen '" + filename + "' går inte att " +
						"läsa in trots att filen existerar. Programmet " +
						"avslutas.", "Kritiskt fel", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			} else {
				dataStore = new Hashtable();
			}
		}
	}
	
	/**
	 * Saves the Hashtable that contains all the data to file.
	 */
	private static void save() {
		if (dataStore == null)
			load();
		IOHandler io = new IOHandler();
		try {
			io.save(filename, dataStore);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
}
