package se.manet.bangolfresultat.datastruct;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Class used to read application properties.
 */
public class PropertyReader {

	private static Properties p;

	static {
		InputStream is = PropertyReader.class.getClassLoader()
				.getResourceAsStream("bangolfresultat.properties");
		p = new Properties();
		try {
			p.load(is);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Properties-filen gick ej att läsa in", "Varning",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Returns the name of the application.
	 * 
	 * @return the name of the application
	 */
	public static String getApplicationName() {
		return getProperty("application.name");
	}

	/**
	 * Returns the version of the application.
	 * 
	 * @return the version of the application
	 */
	public static String getApplicationVersion() {
		return getProperty("application.version");
	}

	/**
	 * Returns the URL to the application home page.
	 * 
	 * @return the URL to the application home page
	 */
	public static String getApplicationUrl() {
		return getProperty("application.url");
	}

	/**
	 * Returns the property specified by the given key. Returns an empty string
	 * if none.
	 * 
	 * @param key
	 *            the property key
	 * @return the property value, an empty string if none
	 */
	public static String getProperty(String key) {
		String value = p.getProperty(key);
		if (value == null) {
			value = "";
		}
		return value;
	}
}
