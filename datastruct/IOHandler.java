package datastruct;

import gui.AlignmentWindow;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.LinkedList;

/** klassen som sk�ter om skrivning till filer och l�sning fr�n filer */
public class IOHandler {
	public static final String DATA_PATH = "data/";
	
	/** skapar ett objekt av klassen */
	public IOHandler() {
	}
	
	/** skriver ut en html-sida med filnamnet fileName och med rubriken header utifr�n resultatlistan result */
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
		String colWidth;
		String startNbrColWidth = "50px";
		String placeNbrColWidth = "50px";
		String licenseColWidth = "110px";
		String nameColWidth = "160px";
		String clubColWidth = "95px";
		String resultColWidth;
		String sumColWidth = "30px";
		if(res.getNbrRounds() <= 5) {
			startColResult = "<td style=\"text-align:" + alignment + "\">";
			resultColWidth = "50px";
		} else {
			startColResult = "<td style=\"text-align:" + alignment + "\">";
			resultColWidth = "28px";
		}
		String startColSum = "<td style=\"text-align:" + alignment + "\">";
		String endCol = "</td>";
		
		BufferedWriter bufferOut = new BufferedWriter(new FileWriter(fileName));
		bufferOut.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
		bufferOut.newLine();
		bufferOut.write("<html lang=\"sv\">");
		bufferOut.newLine();
		bufferOut.write("<head>");
		bufferOut.newLine();
		bufferOut.write("<title>" + header + "</title>");
		bufferOut.newLine();
		bufferOut.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=WINDOWS-1252\">");
		bufferOut.newLine();
		bufferOut.write("<style type=\"text/css\">" +
				"body {font-family: arial} " +
				"td {font-size: 10.0pt; vertical-align: super; white-space: nowrap} " +
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
						boldStart = "<b style=\"font-size:12.0pt\">";
						boldStop = "</b>";
						outputStyle[i][j] = "black";
					} 
					if(output[i][j] == null) {
						output[i][j] = "";
						startCol = startColEmpty;
						colWidth = "";
					} else if(j == 0) {
						startCol = startColName;
						colWidth = nameColWidth;
					} else if(j == 1) {
						startCol = startColClub;
						colWidth = clubColWidth;
					} else if(j == 2 && extras[1]) {
						startCol = startColLicense;
						colWidth = licenseColWidth;
					} else if((j == 2 && extras[2]) || (j == 3 && (extras[1] && extras[2]))) {
						startCol = startColStartNbr;
						colWidth = startNbrColWidth;
					} else if((j == output[i].length-1 && extras[0]) || (j == output[i].length-2 && extras[0])) {
						startCol = startColPlaceNbr;
						colWidth = placeNbrColWidth;
					} else {
						startCol = startColResult;
						colWidth = resultColWidth;
					}
					if(outputStyle[i][j].startsWith("S:a") || output[i][j].startsWith("S:a")) {
					    startCol = startColSum;
					    colWidth = sumColWidth;
					    outputStyle[i][j] = outputStyle[i][j].replaceFirst("S:a", "");
					}
					if (! boldStart.equals(""))
						startCol = addStyle(startCol, "width", colWidth);
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
//		bufferOut.write("<div style=\"font-size:7.5pt\">Denna sida �r skapad av <a href=\"http://bangolfresultat.webhop.org/\" target=\"_top\">"
//										+ "BangolfResultat</a></div>");
		bufferOut.write("<div style=\"font-size:7.5pt\">Denna sida �r skapad av <a href=\"http://bangolfresultat.webhop.org/\">"
				+ "BangolfResultat</a></div>");
		bufferOut.newLine();
		bufferOut.write("</body>");
		bufferOut.newLine();
		bufferOut.write("</html>");
		
		bufferOut.flush();
		bufferOut.close();
	}

	/** skriver ut resultatlistan result till en semikolonseparerad fil med namnet fileName d�r t�vlingens namn s�tts till header */
	public void outputToSKV(String fileName, String fileNameHTM, ResultList result, 
													String header, boolean[] editData) throws IOException {
		LinkedList res = result.sortResults();
		BufferedWriter bufferOut = new BufferedWriter(new FileWriter(fileName));
		
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
	
	/** skriver ut resultatlistan result till en semikolonseparerad fil med namnet fileName d�r t�vlingens namn s�tts till header */
	public void outputToSNITT(String fileName, CompareFile compareFile) throws IOException {
		LinkedList res = compareFile.sortResults();
		BufferedWriter bufferOut = new BufferedWriter(new FileWriter(fileName));
		
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
	
	/** l�ser in resultat fr�n en semikolonseparerad fil med filnamnet fileName 
		returnerar t�vlingens namn, resultatlista samt startnummer- och idnummerhashmap */
	public Object[] inputFromSKV(String fileName) throws IOException, NoSuchElementException {
		boolean[] editData;
		boolean[] startData = new boolean[2];
		startData[0] = false;
		startData[1] = false;
		Object[] objects = new Object[5];
		BufferedReader fileIn = new BufferedReader(new FileReader(fileName));
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
		objects[2] = res;
		objects[3] = startNbrMap;
		objects[4] = editData;
		return objects;
	}
	
	/** l�ser in resultat fr�n en semikolonseparerad fil med filnamnet fileName 
	 	returnerar t�vlingens namn, resultatlista samt startnummer- och idnummerhashmap */
	public CompareFile inputFromSNITT(String fileName) throws IOException, NoSuchElementException {
	    BufferedReader fileIn = new BufferedReader(new FileReader(fileName));
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
	    return compareFile;
	}
	
	/** skriver ut en lista med vilka str�ngar som �r valda och vilka som ej �r valda till filen fileName
		utifr�n inneh�llet i vektorn v */
	public void writeFileList(String fileName, Vector[] v) throws IOException {
		Vector[] vector = v;
		BufferedWriter bufferOut = new BufferedWriter(new FileWriter(DATA_PATH + fileName));
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
	
	/** l�ser in antalet size listor med vilka str�ngar som �r valda och ej valda fr�n filen fileName */
	public Vector[] readFileList(String fileName, int size) throws IOException {
		Vector[] vector = new Vector[size];
		for(int i = 0; i < vector.length; i++) {
			vector[i] = new Vector();
		}
		BufferedReader fileIn = new BufferedReader(new FileReader(DATA_PATH + fileName));
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
		return vector;
	}
	
	/** l�ser av vilken sorts underlag t�vlingen i filen fileName har spelats p� */
	public int readSurface(String fileName) throws IOException, NoSuchElementException {
		BufferedReader fileIn = new BufferedReader(new FileReader(fileName));
		String inLine = fileIn.readLine();
		inLine = fileIn.readLine();
		StringTokenizer inString = new StringTokenizer(inLine, ";");
		inString.nextToken();
		int surface = Integer.parseInt(inString.nextToken());
		return surface;
	}
	
	/** sparar objektet o till filen file */
	public void save(String file, Object o) throws IOException {
		FileOutputStream fos;
		ObjectOutputStream os;
		fos = new FileOutputStream(DATA_PATH + file);
		os = new ObjectOutputStream(fos);
		os.writeObject(o);
		os.close();
	}
	
	/** returnerar objektet som l�sts in fr�n filen file */
	public Object load(String file) throws IOException, ClassNotFoundException {
		FileInputStream fis;
		ObjectInputStream ois;
		Object o = null;
		fis = new FileInputStream(DATA_PATH + file);
		ois = new ObjectInputStream(fis);
		o = ois.readObject();
		ois.close();
		return o;
	}
	
	/** loggar informationen om ett exception som har uppst�tt till filen error.log 
	 *  och returnerar true om operationen lyckas */
	public static boolean logError(Throwable throwable) {
	    LinkedList list = new LinkedList();
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader("error.log"));
            String inLine = fileIn.readLine();
            while(inLine != null) {
                list.add(inLine);
                inLine = fileIn.readLine();
            }
        } catch (Exception e) {}
        try {
            PrintStream printStream = new PrintStream("error.log");
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
	
	/** l�gger till ett styleproperty till en HTML-tagg */
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
}