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

/** klassen som sköter om skrivning till filer och läsning från filer */
public class IOHandler {
	
	/** skapar ett objekt av klassen */
	public IOHandler() {
	}
	
	/** skriver ut en html-sida med filnamnet fileName och med rubriken header utifrån resultatlistan result */
	public void outputToHTML(String fileName, ResultList result, String header, int align) throws IOException {
		ResultList res = result;
		boolean[] extras = res.getExtras();
		String alignment;
		if(align == AlignmentWindow.LEFT) {
		    alignment = "bgrleft";
		} else if (align == AlignmentWindow.CENTER) {
		    alignment = "bgrcenter";
		} else {
		    alignment = "bgrright";
		}
		String startRow = "<TR>";
		String endRow = "</TR>";
		String startCol;
		String startColEmpty = "<TD>";
		String startColStartNbr = "<TD CLASS=" + alignment + " WIDTH=50>";
		String startColPlaceNbr = "<TD CLASS=" + alignment + " WIDTH=50>";
		String startColLicense = "<TD CLASS=bgrleft WIDTH=110>";
		String startColName = "<TD CLASS=bgrleft WIDTH=160>";
		String startColClub = "<TD CLASS=bgrleft WIDTH=95>";
		String startColResult;
		if(res.getNbrRounds() <= 5) {
			startColResult = "<TD CLASS=" + alignment + " WIDTH=50>";
		} else {
			startColResult = "<TD CLASS=" + alignment + " WIDTH=28>";
		}
		String startColSum = "<TD CLASS=" + alignment + " WIDTH=30>";
		String endCol = "</TD>";
		
		BufferedWriter bufferOut = new BufferedWriter(new FileWriter(fileName));
		bufferOut.write("<HTML>");
		bufferOut.newLine();
		bufferOut.write("<HEAD>");
		bufferOut.newLine();
		bufferOut.write("<STYLE ID=\"Bangolftävlingar\"><!--table "+
		" .bgrleft{font-size:10.0pt;text-align:left;vertical-align:super;white-space:nowrap;}"+
		" .bgrcenter{font-size:10.0pt;text-align:center;vertical-align:super;white-space:nowrap;}"+
		" .bgrright{font-size:10.0pt;text-align:right;vertical-align:super;white-space:nowrap;}"+
		"--></STYLE>");
		bufferOut.newLine();
		bufferOut.write("<TITLE>" + header + "</TITLE>");
		bufferOut.newLine();
		bufferOut.write("</HEAD>");
		bufferOut.newLine();
		bufferOut.write("<BODY BGCOLOR=\"white\">");
		bufferOut.newLine();
		bufferOut.write("<FONT FACE=\"arial\">");
		bufferOut.newLine();
		bufferOut.write("<H1 STYLE='font-size:14.0pt;font-weight:800;font-style:normal'>" + header + "</H1>");
		bufferOut.newLine();
		
		String[][] output = res.getOutput();
		String[][] outputStyle = res.getOutputStyle();
		
		bufferOut.write("<TABLE CELLPADDING=1 CELLSPACING=1>");
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
						boldStart = "<B>";
						boldStop = "</B>";
						outputStyle[i][j] = "black";
					} else if(outputStyle[i][j].equals("Bold+")) {
						boldStart = "<B style='font-size:12.0pt'>";
						boldStop = "</B>";
						outputStyle[i][j] = "black";
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
					bufferOut.write(startCol + boldStart + "<FONT COLOR=\""+outputStyle[i][j]+"\">"+ 
							output[i][j] +"</FONT>" + boldStop + endCol);
					bufferOut.newLine();
				}
				bufferOut.write(endRow);
				bufferOut.newLine();
			} else {
				bufferOut.write("<TR><TD HEIGHT=15> </TD></TR>");
				bufferOut.newLine();
			}
		}
		
		bufferOut.write("</TABLE>");
		bufferOut.newLine();
		bufferOut.write("<P><BR>");
		bufferOut.newLine();
		bufferOut.write("<FONT SIZE=-2>Denna sida är skapad av <A HREF=\"http://web.telia.com/~u44802129/\" TARGET=\"_top\">"
										+ "BangolfResultat</A></FONT>");
		bufferOut.newLine();
		bufferOut.write("</FONT>");
		bufferOut.newLine();
		bufferOut.write("</BODY>");
		bufferOut.newLine();
		bufferOut.write("</HTML>");
		
		bufferOut.flush();
		bufferOut.close();
	}
	
	/** skriver ut resultatlistan result till en semikolonseparerad fil med namnet fileName där tävlingens namn sätts till header */
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
	
	/** skriver ut resultatlistan result till en semikolonseparerad fil med namnet fileName där tävlingens namn sätts till header */
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
	
	/** läser in resultat från en semikolonseparerad fil med filnamnet fileName 
		returnerar tävlingens namn, resultatlista samt startnummer- och idnummerhashmap */
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
	
	/** läser in resultat från en semikolonseparerad fil med filnamnet fileName 
	 	returnerar tävlingens namn, resultatlista samt startnummer- och idnummerhashmap */
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
	
	/** skriver ut en lista med vilka strängar som är valda och vilka som ej är valda till filen fileName
		utifrån innehållet i vektorn v */
	public void writeFileList(String fileName, Vector[] v) throws IOException {
		Vector[] vector = v;
		BufferedWriter bufferOut = new BufferedWriter(new FileWriter("data/" + fileName));
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
		BufferedReader fileIn = new BufferedReader(new FileReader("data/" + fileName));
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
	
	/** läser av vilken sorts underlag tävlingen i filen fileName har spelats på */
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
		fos = new FileOutputStream("data/" + file);
		os = new ObjectOutputStream(fos);
		os.writeObject(o);
		os.close();
	}
	
	/** returnerar objektet som lästs in från filen file */
	public Object load(String file) throws IOException, ClassNotFoundException {
		FileInputStream fis;
		ObjectInputStream ois;
		Object o = null;
		fis = new FileInputStream("data/" + file);
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
}