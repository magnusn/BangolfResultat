package se.manet.bangolfresultat.datastruct;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

public class IOHandlerTest {

	private static String expectedAppDataPath = null;

	@BeforeClass
	public static void setUpClass() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			expectedAppDataPath = System.getenv("AppData") + File.separator
					+ PropertyReader.getApplicationName() + File.separator;
		} else {
			expectedAppDataPath = new File("AppData").getPath()
					+ File.separator;
		}
	}

	@Test
	public void testGetApplicationDataPath() {
		String appDataPath = IOHandler.getApplicationDataPath();
		assertEquals(expectedAppDataPath, appDataPath);
	}

	@Test
	public void testGetSettingsPath() {
		String settingsPath = IOHandler.getSettingsPath();
		assertEquals(expectedAppDataPath + "Settings" + File.separator,
				settingsPath);
	}

	@Test
	public void testGetLogsPath() {
		String logsPath = IOHandler.getLogsPath();
		assertEquals(expectedAppDataPath + "Logs" + File.separator, logsPath);
	}

}
