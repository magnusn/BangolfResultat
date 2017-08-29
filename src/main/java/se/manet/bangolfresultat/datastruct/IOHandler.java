package se.manet.bangolfresultat.datastruct;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import se.manet.bangolfresultat.gui.AlignmentWindow;

/** klassen som sköter om skrivning till filer och läsning från filer */
public class IOHandler {
	private static String applicationDataPath = null;
	private static String iconsPath = null;
	
	/** skapar ett objekt av klassen */
	public IOHandler() {
	}
	
	/** skriver ut en html-sida med filnamnet fileName och med rubriken header utifrån resultatlistan result */
	public void outputToHTML(String fileName, ResultList result, String header, int align) throws IOException {
		ResultList res = result;
		boolean[] extras = res.getExtras();
		String alignment;
		if(align == AlignmentWindow.LEFT) {
		    alignment = "left";
		} else if (align == AlignmentWindow.CENTER) {
		    alignment = "center";
		} else {
		    alignment = "right";
		}
		String startRow = "<tr>";
		String endRow = "</tr>";
		String startCol;
		String startColEmpty = "<td>";
		String startColStartNbr = "<td style=\"text-align:" + alignment + "\">";
		String startColPlaceNbr = "<td style=\"text-align:" + alignment + "\">";
		String startColLicense = "<td>";
		String startColName = "<td>";
		String startColClub = "<td>";
		String startColResult;
		if(res.getNbrRounds() <= 5) {
			startColResult = "<td style=\"text-align:" + alignment + "\">";
		} else {
			startColResult = "<td style=\"text-align:" + alignment + "\">";
		}
		String startColSum = "<td style=\"text-align:" + alignment + "\">";
		String endCol = "</td>";
		
		BufferedWriter bufferOut = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8")
		);
		bufferOut.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
		bufferOut.newLine();
		bufferOut.write("<html lang=\"sv\">");
		bufferOut.newLine();
		bufferOut.write("<head>");
		bufferOut.newLine();
		bufferOut.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		bufferOut.newLine();
		bufferOut.write("<title>" + header + "</title>");
		bufferOut.newLine();
		bufferOut.write("<style type=\"text/css\">" +
				"body {font-family: arial} " +
				"td {font-size: 10.0pt; padding-right: 10px; vertical-align: super; white-space: nowrap}" +
				"</style>");
		bufferOut.newLine();
		bufferOut.write("</head>");
		bufferOut.newLine();
		
		bufferOut.write("<body style=\"background-color:white\">");
		bufferOut.newLine();
		bufferOut.write("<h1 style=\"font-size: 14.0pt\">" + header + "</h1>");
		bufferOut.newLine();
		
		String[][] output = res.getOutput();
		String[][] outputStyle = res.getOutputStyle();
		
		bufferOut.write("<table cellpadding=\"1\" cellspacing=\"1\">");
		bufferOut.newLine();
		
		for(int i = 0; i < output.length; i++) {
			if(output[i][0] != " ") {
				bufferOut.write(startRow);
				bufferOut.newLine();
				for(int j = 0; j < output[i].length; j++) {
					String boldStart = "";
					String boldStop = "";
					if(outputStyle[i][j] == null) {
						outputStyle[i][j] = "black";
					} else if(outputStyle[i][j].equals("Bold")) {
						boldStart = "<b>";
						boldStop = "</b>";
						outputStyle[i][j] = "black";
					} else if(outputStyle[i][j].equals("Bold+")) {
						// Hantering av klassrubrik
						boldStart = "<b style=\"font-size:12.0pt\">";
						boldStop = "</b>";
						outputStyle[i][j] = "black";
						bufferOut.write("<td colspan=\"0\">" + boldStart + output[i][j] + boldStop + endCol);
						bufferOut.newLine();
						break;
					}
					if(output[i][j] == null) {
						output[i][j] = "";
						startCol = startColEmpty;
					} else if(j == 0) {
						startCol = startColName;
					} else if(j == 1) {
						startCol = startColClub;
					} else if(j == 2 && extras[1]) {
						startCol = startColLicense;
					} else if((j == 2 && extras[2]) || (j == 3 && (extras[1] && extras[2]))) {
						startCol = startColStartNbr;
					} else if((j == output[i].length-1 && extras[0]) || (j == output[i].length-2 && extras[0])) {
						startCol = startColPlaceNbr;
					} else {
						startCol = startColResult;
					}
					if(outputStyle[i][j].startsWith("S:a") || output[i][j].startsWith("S:a")) {
					    startCol = startColSum;
					    outputStyle[i][j] = outputStyle[i][j].replaceFirst("S:a", "");
					}
					if (! outputStyle[i][j].equals("black"))
						startCol = addStyle(startCol, "color", outputStyle[i][j]);
					bufferOut.write(startCol + boldStart + output[i][j] + boldStop + endCol);
					bufferOut.newLine();
				}
				bufferOut.write(endRow);
				bufferOut.newLine();
			} else if (outputStyle[i][0] == null || !outputStyle[i][0].equals("Bold+")) {
				bufferOut.write("<tr><td style=\"height:15px\"> </td></tr>");
				bufferOut.newLine();
			}
		}
		
		bufferOut.write("</table>");
		bufferOut.newLine();
		bufferOut.write("<p><br>");
		bufferOut.newLine();
		bufferOut.write("<div style=\"font-size:7.5pt\">Denna sida är skapad av <a href=\"" + PropertyReader.getApplicationUrl() + "\">"
				+ PropertyReader.getApplicationName() + "</a></div>");
		bufferOut.newLine();
		bufferOut.write("</body>");
		bufferOut.newLine();
		bufferOut.write("</html>");
		
		bufferOut.flush();
		bufferOut.close();
	}

	/** skriver ut resultatlistan result till en semikolonseparerad fil med namnet fileName där tävlingens namn sätts till header */
	public void outputToSKV(String fileName, String fileNameHTM, ResultList result, 
													String header, boolean[] editData) throws IOException {
		LinkedList res = result.sortResults();
		BufferedWriter bufferOut = getTextFileWriter(fileName);
		
		if(header.equals("")) {
			header = " ";
		}
		bufferOut.write(header);
		bufferOut.write(";");
		if(fileNameHTM == null || fileNameHTM.equals("")) {
			fileNameHTM = "null";
		}
		bufferOut.write(fileNameHTM);
		bufferOut.newLine();
		bufferOut.write(String.valueOf(result.getNbrRounds()));
		bufferOut.write(";");
		bufferOut.write(String.valueOf(result.getSurface()));
		bufferOut.write(";");
		bufferOut.write(String.valueOf(result.getExtraData()));
		bufferOut.write(";");
		for(int i = 0; i < editData.length; i++) {
			if(editData[i]) {
				bufferOut.write("1");
			} else {
				bufferOut.write("0");
			}
		}
		
		for(int i = 0; i < res.size(); i++) {
			PersonResult pr = (PersonResult) res.get(i);
			String name = pr.getName();
			String club = pr.getClub();
			String ident = (new Integer(pr.getPersonID())).toString();
			
			bufferOut.newLine();
			bufferOut.write(String.valueOf(pr.getStartNr()));
			bufferOut.write(";");
			bufferOut.write(name);
			bufferOut.write(";");
			bufferOut.write(club);
			bufferOut.write(";");
			bufferOut.write(String.valueOf(pr.getLicenseNr()));
			bufferOut.write(";");
			bufferOut.write(pr.getKlass());
			bufferOut.write(";");
			bufferOut.write(String.valueOf(pr.getPrio()));
			bufferOut.write(";");
			bufferOut.write(String.valueOf(pr.getRounds()));
			bufferOut.write(";");
			int nbrRoundsFinished = pr.getNbrRoundsFinished();
			bufferOut.write(String.valueOf(nbrRoundsFinished));
			int[] results = pr.getResultList();
			for(int j = 0; j < nbrRoundsFinished; j++) {
				bufferOut.write(";");
				bufferOut.write(String.valueOf(results[j]));
			}
			bufferOut.write(";");
			bufferOut.write(ident);
		}
		bufferOut.flush();
		bufferOut.close();
	}
	
	/** skriver ut resultatlistan result till en semikolonseparerad fil med namnet fileName där tävlingens namn sätts till header */
	public void outputToSNITT(String fileName, CompareFile compareFile) throws IOException {
		LinkedList res = compareFile.sortResults();
		BufferedWriter bufferOut = getTextFileWriter(fileName);
		
		bufferOut.write(String.valueOf(compareFile.getSurface()));
		for(int i = 0; i < res.size(); i++) {
			PersonMean personMean = (PersonMean) res.get(i);
			String idNbr = String.valueOf(personMean.getID());
			String name = personMean.getName();
			String club = personMean.getClub();
			String mean = personMean.getMeanAsString();
			
			bufferOut.newLine();
			bufferOut.write(idNbr);
			bufferOut.write(";");
			bufferOut.write(name);
			bufferOut.write(";");
			bufferOut.write(club);
			bufferOut.write(";");
			bufferOut.write(mean);
		}
		bufferOut.flush();
		bufferOut.close();
	}
	
	/** läser in resultat från en semikolonseparerad fil med filnamnet fileName 
		returnerar tävlingens namn, resultatlista samt startnummer- och idnummerhashmap */
	public Object[] inputFromSKV(String fileName) throws IOException, NoSuchElementException {
		boolean[] editData;
		boolean[] startData = new boolean[2];
		startData[0] = false;
		startData[1] = false;
		Object[] objects = new Object[5];
		BufferedReader fileIn = getTextFileReader(fileName);
		String inLine = fileIn.readLine();
		StringTokenizer inString = new StringTokenizer(inLine, ";");
		String header = inString.nextToken();
		if(header.equals(" ")) {
			header = "";
		}
		objects[0] = header;
		try {
			String fileNameHTM = inString.nextToken();
			if(fileNameHTM.equals("null")) {
				throw new NullPointerException();
			}
			objects[1] = fileNameHTM;
		} catch (Exception e) {
			objects[1] = null;
		}
		inLine = fileIn.readLine();
		inString = new StringTokenizer(inLine, ";");
		int nbrRounds = Integer.parseInt(inString.nextToken());
		int surface = Integer.parseInt(inString.nextToken());
		int extraData = Integer.parseInt(inString.nextToken());
		if(extraData == ResultList.START_AND_ID_NBR) {
			startData[0] = true;
			startData[1] = true;
		} else if(extraData == ResultList.START_NBR) {
			startData[0] = true;
		} else if(extraData == ResultList.ID_NBR) {
			startData[1] = true;
		}
		editData = new boolean[1+nbrRounds-2];
		try {
			String editString = inString.nextToken();
			for(int i = 0; i < editData.length; i++) {
				if(editString.charAt(i) == '1') {
					editData[i] = true;
				} else {
					editData[i] = false;
				}
			}
		} catch (Exception e) {
			for(int i = 0; i < editData.length; i++) {
				editData[i] = false;
			}
		}
		ResultList res = new ResultList(nbrRounds, surface, startData);
		HashMap startNbrMap = new HashMap();
		if(inLine != null) {
			inLine = fileIn.readLine();
			while (inLine != null) {
				inString = new StringTokenizer(inLine, ";");
				if(inString.countTokens() != 0) {
					int startNr = Integer.parseInt(inString.nextToken());
					startNbrMap.put(new Integer(startNr), null);
					String name = inString.nextToken();
					String club = inString.nextToken();
					String licenseNr = inString.nextToken();
					String klass = inString.nextToken();
					int prioNr = Integer.parseInt(inString.nextToken());
					nbrRounds = Integer.parseInt(inString.nextToken());
					int nbrRoundsFinished = Integer.parseInt(inString.nextToken());
					int[] results = new int[nbrRounds];
					for(int i = 0; i < nbrRoundsFinished; i++) {
						results[i] = Integer.parseInt(inString.nextToken());
					}
					int personID = Integer.parseInt(inString.nextToken());
					res.addResult(startNr, name, club, licenseNr, results, nbrRounds, klass, prioNr, nbrRoundsFinished, personID);
				}
				inLine = fileIn.readLine();
			}
		}
		fileIn.close();
		objects[2] = res;
		objects[3] = startNbrMap;
		objects[4] = editData;
		return objects;
	}
	
	/** läser in resultat från en semikolonseparerad fil med filnamnet fileName 
	 	returnerar tävlingens namn, resultatlista samt startnummer- och idnummerhashmap */
	public CompareFile inputFromSNITT(String fileName) throws IOException, NoSuchElementException {
	    BufferedReader fileIn = getTextFileReader(fileName);
	    String inLine = fileIn.readLine();
	    int surface = Integer.parseInt(inLine);
	    CompareFile compareFile = new CompareFile(surface);
	    
	    StringTokenizer inString;
	    if(inLine != null) {
	        inLine = fileIn.readLine();
	        while (inLine != null) {
	            inString = new StringTokenizer(inLine, ";");
	            if(inString.countTokens() != 0) {
	                Integer idNbr = Integer.valueOf(inString.nextToken());
	                String name = inString.nextToken();
	                String club = inString.nextToken();
	                double mean = Double.parseDouble(inString.nextToken());
	                compareFile.addMean(idNbr, name, club, mean);
	            }
	            inLine = fileIn.readLine();
	        }
	    }
	    fileIn.close();
	    return compareFile;
	}
	
	/** skriver ut en lista med vilka strängar som är valda och vilka som ej är valda till filen fileName
		utifrån innehållet i vektorn v */
	public void writeFileList(String fileName, Vector[] v) throws IOException {
		Vector[] vector = v;
		BufferedWriter bufferOut = getTextFileWriter(getSettingsPath()
				+ fileName);
		for(int i = 0; i < vector.length; i++) {
			if(i%2 == 0) {
				bufferOut.write("Unselected");
			} else {
				bufferOut.write("Selected");
			}
			bufferOut.newLine();
			for(int j = 0; j < vector[i].size(); j++) {
				bufferOut.write((String)vector[i].get(j));
				bufferOut.newLine();
			}
		}
		bufferOut.flush();
		bufferOut.close();
	}
	
	/** läser in antalet size listor med vilka strängar som är valda och ej valda från filen fileName */
	public Vector[] readFileList(String fileName, int size) throws IOException {
		Vector[] vector = new Vector[size];
		for(int i = 0; i < vector.length; i++) {
			vector[i] = new Vector();
		}
		BufferedReader fileIn = getTextFileReader(getSettingsPath()
				+ fileName);
		String inLine = fileIn.readLine();
		int i = -1;
		while(inLine != null && !inLine.trim().equals("")) {
			if(inLine.equals("Selected") || inLine.equals("Unselected")) {
				i++;
			} else {
				vector[i].addElement(inLine);
			}
			inLine = fileIn.readLine();
		}
		fileIn.close();
		return vector;
	}
	
	/** läser av vilken sorts underlag tävlingen i filen fileName har spelats på */
	public int readSurface(String fileName) throws IOException, NoSuchElementException {
		BufferedReader fileIn = getTextFileReader(fileName);
		String inLine = fileIn.readLine();
		inLine = fileIn.readLine();
		StringTokenizer inString = new StringTokenizer(inLine, ";");
		inString.nextToken();
		int surface = Integer.parseInt(inString.nextToken());
		fileIn.close();
		return surface;
	}
	
	/**
	 * Returns a {@link BufferedReader} for the given file name. The file will
	 * be read according to charset UTF-8 if the first line in the file equals
	 * <tt>UTF-8</tt>, otherwise the default charset will be used.
	 * 
	 * @param fileName		name of the file to read
	 * @return				a BufferedReader for the file represented by the
	 * 						given file name
	 * @throws IOException	if an I/O error occurs
	 */
	public static BufferedReader getTextFileReader(String fileName) throws IOException {
		BufferedReader fileIn = new BufferedReader(new InputStreamReader(
        		new FileInputStream(fileName), "UTF-8"
        ));
	    // ---------------------- check charset ---------------------------
		String inLine = fileIn.readLine();
		if (inLine == null)
		{
			// do nothing, default charset will be used
		}
		else if (inLine.equals("UTF-8"))
		{
			return fileIn;
		}
		
		// use default charset
		fileIn.close();
		return new BufferedReader(new FileReader(fileName));
		// ----------------------------------------------------------------
	}
	
	/**
	 * Returns a {@link BufferedWriter} for the given file name. Charset UTF-8
	 * is used and this method writes the first line of the file as
	 * <tt>UTF-8</tt> before the {@link BufferedWriter} is returned.
	 * 
	 * @param fileName		name of the file for which to get a
	 * 						{@link BufferedWriter}
	 * @return				a {@link BufferedWriter} using charset UTF-8
	 * @throws IOException	if an I/O error occurs
	 */
	public static BufferedWriter getTextFileWriter(String fileName) throws IOException {
        BufferedWriter bufferOut = new BufferedWriter(
        		new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8")
        );
        bufferOut.write("UTF-8");
        bufferOut.newLine();
        
        return bufferOut;
	}
	
	/** sparar objektet o till filen file */
	public void save(String file, Object o) throws IOException {
		FileOutputStream fos;
		ObjectOutputStream os;
		fos = new FileOutputStream(getSettingsPath() + file);
		os = new ObjectOutputStream(fos);
		os.writeObject(o);
		os.close();
	}
	
	/** returnerar objektet som lästs in från filen file */
	public Object load(String file) throws IOException, ClassNotFoundException {
		FileInputStream fis;
		ObjectInputStream ois;
		Object o = null;
		fis = new FileInputStream(getSettingsPath() + file);
		ois = new ObjectInputStream(fis);
		o = ois.readObject();
		ois.close();
		return o;
	}
	
	/** loggar informationen om ett exception som har uppstått till filen error.log 
	 *  och returnerar true om operationen lyckas */
	public static boolean logError(Throwable throwable) {
	    LinkedList list = new LinkedList();
        try {
            BufferedReader fileIn = getTextFileReader(getLogsPath()
                    + "error.log");
            String inLine = fileIn.readLine();
            while(inLine != null) {
                list.add(inLine);
                inLine = fileIn.readLine();
            }
            fileIn.close();
        } catch (Exception e) {}
        try {
            File logFile = new File(getLogsPath() + "error.log");
            if (!logFile.exists()) {
                logFile.getParentFile().mkdirs();
            }
            PrintStream printStream = new PrintStream(logFile, "UTF-8");
            printStream.println("UTF-8");
            Date date = new Date(System.currentTimeMillis());
            printStream.println(date.toString());
	        throwable.printStackTrace(printStream);
	        printStream.println();
	        while(list.size() != 0) {
                printStream.println((String)list.removeFirst());
            }
	        printStream.flush();
	        printStream.close();
        } catch (Exception e) {
            return false;
        }
        return true;
	}
	
	/** lägger till ett styleproperty till en HTML-tagg */
	public static String addStyle(String tag, String property, String value) {
		int styleIndex = tag.indexOf("style");
		if (styleIndex == -1) {
			int endOfTag;
			if (tag.endsWith(">")) {
				endOfTag = tag.lastIndexOf('>');
			} else {
				return tag;
			}
			String begin = tag.substring(0, endOfTag);
			String end = tag.substring(endOfTag, tag.length());
			tag = begin + " style=\"\"" + end;
		}
		int startQuoteIndex = tag.indexOf('"', styleIndex);
		int endQuoteIndex = tag.indexOf('"', startQuoteIndex+1);
		if (startQuoteIndex != -1 && endQuoteIndex != -1) {
			String begin = tag.substring(0, endQuoteIndex);
			String end = tag.substring(endQuoteIndex, tag.length());
			String separator;
			if (startQuoteIndex + 1 == endQuoteIndex)
				separator = "";
			else
				separator ="; ";
			return begin + separator + property + ":" + value + end;
		}
		return tag;
	}

	/**
	 * Creates the directory used for storing application settings.
	 */
	public static void createSettingsDirectory() {
		File settingsDirectory = new File(getSettingsPath());
		if (!settingsDirectory.exists()) {
			settingsDirectory.mkdirs();
		}
	}

	/**
	 * Returns the path to the application data directory.
	 * <p>
	 * The path contains a trailing system-depending name-separator character.
	 * 
	 * @return the path to the application data directory
	 */
	public static String getApplicationDataPath() {
		if (applicationDataPath == null) {
			String applicationDataPathProperty = System
					.getProperty("applicationDataPath");
			if (applicationDataPathProperty != null) {
				applicationDataPath = getCanonicalPath(new File(
						applicationDataPathProperty));
			} else {
				String os = System.getProperty("os.name").toLowerCase();
				if (os.contains("win")) {
					String appDataFolder = System.getenv("AppData");
					applicationDataPath = getCanonicalPath(new File(
							appDataFolder + File.separator
									+ PropertyReader.getApplicationName()
									+ File.separator));
				} else {
					String userHome = System.getProperty("user.home");
					if (userHome != null) {
						applicationDataPath = getCanonicalPath(new File(
								userHome + File.separator + "."
										+ PropertyReader.getApplicationName()
										+ File.separator));
					} else {
						applicationDataPath = getCanonicalPath(new File(
								"AppData" + File.separator));
					}
				}
			}
			applicationDataPath += File.separator;
		}
		return applicationDataPath;
	}

	/**
	 * Returns the path to the application settings directory.
	 * <p>
	 * The path contains a trailing system-depending name-separator character.
	 * 
	 * @return the path to the application settings directory
	 */
	public static String getSettingsPath() {
		return getApplicationDataPath() + "Settings" + File.separator;
	}

	/**
	 * Returns the path to the application logs directory.
	 * <p>
	 * The path contains a trailing system-depending name-separator character.
	 * 
	 * @return the path to the application logs directory
	 */
	public static String getLogsPath() {
		return getApplicationDataPath() + "Logs" + File.separator;
	}

	/**
	 * Returns the path to the icons directory.
	 * <p>
	 * The path contains a trailing system-depending name-separator character.
	 * 
	 * @return the path to the icons directory
	 */
	public static String getIconsPath() {
		if (iconsPath == null) {
			iconsPath = getCanonicalPath(new File("icons"
					+ File.separator));
			iconsPath += File.separator;
		}
		return iconsPath;
	}

	/**
	 * Returns the canonical path for the given file, or the absolute path if an
	 * error occurs while getting the canonical path.
	 * 
	 * @param file
	 *            file to get canonical path for
	 * @return the canonical path, or the absolute path if an error occurs
	 */
	public static String getCanonicalPath(File file) {
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			return file.getAbsolutePath();
		}
	}

}