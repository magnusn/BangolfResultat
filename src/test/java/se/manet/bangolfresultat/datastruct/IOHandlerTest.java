package se.manet.bangolfresultat.datastruct;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import org.junit.BeforeClass;
import org.junit.Test;

public class IOHandlerTest {

	private static String expectedAppDataPath = null;

	@BeforeClass
	public static void setUpClass() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.startsWith("windows")) {
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

	@Test
	public void testInputFromSKV071() throws NoSuchElementException, IOException {
		IOHandler io = new IOHandler();

		Object[] objects =
				io.inputFromSKV("src/test/resources/results/competition_0_7_1.skv");
		assertKalleAnkaCompetition(objects);
	}

	@Test
	public void testInputFromSKV080() throws NoSuchElementException, IOException {
		IOHandler io = new IOHandler();

		Object[] objects =
				io.inputFromSKV("src/test/resources/results/competition_0_8_0.skv");
		assertKalleAnkaCompetition(objects);
	}

	@Test
	public void testOutputToSKV071() throws NoSuchElementException, IOException {
		IOHandler io = new IOHandler();

		String source = "src/test/resources/results/competition_0_7_1.skv";
		String output = "src/test/resources/results/temp.skv";
		String answer = "src/test/resources/results/competition_0_8_0.skv";
		Object[] objects =
				io.inputFromSKV(source);

		io.outputToSKV(output, (String) objects[1], (ResultList) objects[2],
				(String) objects[0], (boolean[]) objects[4]);

		BufferedReader answerReader = null;
		BufferedReader outputReader = null;
		try {
			answerReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(answer), "UTF-8"
	        ));
			outputReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(output), "UTF-8"
	        ));

			int nbrLines = 0;
			String answerLine;
			while ((answerLine = answerReader.readLine()) != null) {
				String outputLine = outputReader.readLine();
				assertEquals(answerLine, outputLine);
				nbrLines++;
			}
			assertEquals(7, nbrLines);
		} finally {
			answerReader.close();
			outputReader.close();
			new File(output).delete();
		}
	}

	@Test
	public void testOutputToSKV080() throws NoSuchElementException, IOException {
		IOHandler io = new IOHandler();

		String source = "src/test/resources/results/competition_0_8_0.skv";
		String output = "src/test/resources/results/temp.skv";
		String answer = "src/test/resources/results/competition_0_8_0.skv";
		Object[] objects =
				io.inputFromSKV(source);

		io.outputToSKV(output, (String) objects[1], (ResultList) objects[2],
				(String) objects[0], (boolean[]) objects[4]);

		BufferedReader answerReader = null;
		BufferedReader outputReader = null;
		try {
			answerReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(answer), "UTF-8"
	        ));
			outputReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(output), "UTF-8"
	        ));

			int nbrLines = 0;
			String s;
			while ((s = answerReader.readLine()) != null) {
				assertEquals(s, outputReader.readLine());
				nbrLines++;
			}
			assertEquals(7, nbrLines);
		} finally {
			answerReader.close();
			outputReader.close();
			new File(output).delete();
		}
	}

	private static void assertKalleAnkaCompetition(Object[] objects) {
		// Header
		assertEquals("Tävling", objects[0]);
		// File name for web page
		assertEquals(null, objects[1]);
		ResultList resultList = (ResultList) objects[2];
		assertEquals(2, resultList.getNbrRounds());
		assertEquals(ResultList.FILT, resultList.getSurface());
		// No extra data (license number and/or start number)
		assertEquals(0, resultList.getExtraData());
		// Show placing but no subtotals
		boolean[] editData = (boolean[]) objects[4];
		assertEquals(1, editData.length);
		assertEquals(true, editData[0]);

		// Kalle Anka
		PersonResult personResult = resultList.getPerson(0);
		assertEquals(-1, personResult.getStartNr());
		assertEquals("Kalle Anka", personResult.getName());
		assertEquals("Ankeborg", personResult.getClub());
		assertEquals("null", personResult.getLicenseNr());
		assertEquals("Klass 1", personResult.getKlass());
		assertEquals(Integer.MAX_VALUE, personResult.getPrio());
		assertEquals(2, personResult.getRounds());
		assertEquals(2, personResult.getNbrRoundsFinished());
		assertEquals(2, personResult.getResultList().length);
		assertEquals(42, personResult.getResultList()[0]);
		assertEquals(44, personResult.getResultList()[1]);
		assertEquals(0, personResult.getPersonID());

		// Knatte
		personResult = resultList.getPerson(1);
		assertEquals(-1, personResult.getStartNr());
		assertEquals("Knatte", personResult.getName());
		assertEquals("Ankeborg", personResult.getClub());
		assertEquals("null", personResult.getLicenseNr());
		assertEquals("Klass 1", personResult.getKlass());
		assertEquals(1, personResult.getPrio());
		assertEquals(2, personResult.getRounds());
		assertEquals(2, personResult.getNbrRoundsFinished());
		assertEquals(2, personResult.getResultList().length);
		assertEquals(42, personResult.getResultList()[0]);
		assertEquals(44, personResult.getResultList()[1]);
		assertEquals(1, personResult.getPersonID());

		// Fnatte
		personResult = resultList.getPerson(2);
		assertEquals(-1, personResult.getStartNr());
		assertEquals("Fnatte", personResult.getName());
		assertEquals("Ankeborg", personResult.getClub());
		assertEquals("null", personResult.getLicenseNr());
		assertEquals("Klass 2", personResult.getKlass());
		assertEquals(Integer.MAX_VALUE, personResult.getPrio());
		assertEquals(2, personResult.getRounds());
		assertEquals(2, personResult.getNbrRoundsFinished());
		assertEquals(2, personResult.getResultList().length);
		assertEquals(45, personResult.getResultList()[0]);
		assertEquals(46, personResult.getResultList()[1]);
		assertEquals(2, personResult.getPersonID());

		// Tjatte
		personResult = resultList.getPerson(3);
		assertEquals(-1, personResult.getStartNr());
		assertEquals("Tjatte", personResult.getName());
		assertEquals("Ankeborg", personResult.getClub());
		assertEquals("null", personResult.getLicenseNr());
		assertEquals("Klass 2", personResult.getKlass());
		assertEquals(Integer.MAX_VALUE, personResult.getPrio());
		assertEquals(2, personResult.getRounds());
		assertEquals(2, personResult.getNbrRoundsFinished());
		assertEquals(2, personResult.getResultList().length);
		assertEquals(46, personResult.getResultList()[0]);
		assertEquals(46, personResult.getResultList()[1]);
		assertEquals(3, personResult.getPersonID());

		assertEquals(null, resultList.getPerson(4));
	}

}
