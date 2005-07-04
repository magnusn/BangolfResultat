package snitt;

import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import datastruct.IOHandler;
import datastruct.Person;
import datastruct.ResultList;


/** klassen som beskriver snitthanteringen */
public class Snitt {
	private HashMap map;				// datastruktur för att lagra resultat
	private String fileName, header;	// filnamnet och rubriken
	public static final int NAME = 0;	// siffran för namn som används vid sortering
	public static final int CLUB = 1;	// siffran för klubb som används vid sortering
	public static final int COMPS = 2;	// siffran för tävlingar som används vid sortering
	public static final int ROUNDS = 3;	// siffran för varv som används vid sortering
	public static final int HITSUM = 4;	// siffran för slagsumma som används vid sortering
	public static final int MEAN = 5;	// siffran för snitt som används vid sortering
	public static final int EX_MEAN=6;	// siffran för fjolårssnitt som används vid sortering
	public static final int CHANGE=7;	// siffran för förändringen som används vid sortering
	private int[][] compare;			// vektorn som håller reda på vad snittlistan skall sorteras efter
	private int tabIndex;				// talar om vilken snittlistsflik som är vald
	
	/** skapar ett objekt för snitthantering, filen att skriva till är fileName och rubriken header */
	public Snitt(String fileName, String header, int tabIndex) {
		map = new HashMap();
		if(!fileName.endsWith(".htm") && !fileName.endsWith(".html")) {
			fileName = fileName + ".htm";
		}
		this.fileName = fileName;
		this.header = header;
		this.tabIndex = tabIndex;
		IOHandler io = new IOHandler();
		loadCompareBy(io);
	}
	
	/** skapar ett objekt för snitthantering, filen att skriva till är fileName */
	public Snitt(String fileName) {
		map = new HashMap();
		if(!fileName.endsWith(".snitt")) {
			fileName = fileName + ".snitt";
		}
		this.fileName = fileName;
		IOHandler io = new IOHandler();
	}
	
	/** läser in resultat ifrån filerna i strängvektorn fileNames och hämtar namn och klubb med hjälp av personTrack */
	public void readResults(String[] fileNames, HashMap personNameTrack) throws FileNotFoundException, IOException, NoSuchElementException {
		String[] files = fileNames;
		HashMap personNameTracker = personNameTrack;
		for(int i = 0; i < files.length; i++) {
			BufferedReader fileIn = new BufferedReader(new FileReader(files[i]));
			String inLine = fileIn.readLine();
			inLine = fileIn.readLine();
			inLine = fileIn.readLine();
			while (inLine != null) {
				String name,club,identity;
				int result,rounds,playedRounds;
				result = 0;
				playedRounds = 0;
				StringTokenizer inString = new StringTokenizer(inLine, ";");
				if(inString.countTokens() != 0) {
					inString.nextToken();
					name = inString.nextToken();
					club = inString.nextToken();
					inString.nextToken();
					identity = name + " " + club;
					inString.nextToken();
					inString.nextToken();
					inString.nextToken();
					rounds = Integer.parseInt(inString.nextToken());
					for(int j = 0; j < rounds; j++) {
					    int nbrHits = Integer.parseInt(inString.nextToken());
					    if(nbrHits < ResultList.MAX_SCORE && nbrHits > ResultList.MIN_SCORE) {
					        result += nbrHits;
					        playedRounds++;
					    }
					}
					identity = inString.nextToken();
					Integer ident = new Integer(identity);
					if(personNameTracker.containsKey(ident)) {
						StringTokenizer token = new StringTokenizer((String)personNameTracker.get(ident), ",");
						name = token.nextToken().trim();
						club = token.nextToken().trim();
					}
					if(map.containsKey(identity)) {
						Person p = (Person) map.get(identity);
						p.changeName(name);
						p.changeClub(club);
						p.addComps();
						p.addRounds(playedRounds);
						p.addHits(result);
					}
					else {
						map.put(identity, new Person(ident.intValue(), name, club, 1, playedRounds, result));
					}
				}
				inLine = fileIn.readLine();
			}
		}
	}
	
	/** räknar ut antalet startande i varje klass och returnerar en HashMap 
	 *  där klassnamnet är nyckel och antalet starter ligger som värde
	 */
	public HashMap countClasses(String[] fileNames) throws FileNotFoundException, IOException {
		String[] files = fileNames;
		HashMap map = new HashMap();
		for(int i = 0; i < files.length; i++) {
			BufferedReader fileIn = new BufferedReader(new FileReader(files[i]));
			String inLine = fileIn.readLine();
			inLine = fileIn.readLine();
			inLine = fileIn.readLine();
			while (inLine != null) {
				StringTokenizer inString = new StringTokenizer(inLine, ";");
				if(inString.countTokens() != 0) {
					for(int j = 0; j < 4; j++) {
						inString.nextToken();
					}
					String klass = inString.nextToken();
					if(map.containsKey(klass)) {
						map.put(klass, new Integer(((Integer)map.get(klass)).intValue() + 1));
					} else {
						map.put(klass, new Integer(1));
					}
				}
				inLine = fileIn.readLine();
			}
		}
		return map;
	}
	
	/** sorterar resultaten och returnerar dem i en länkad lista */
	public LinkedList sortMap() {
		LinkedList list = new LinkedList();
		list.addAll(map.values());
		SnittComparator comparator = new SnittComparator();
		Collections.sort(list, comparator);
		return list;
	}
	
	/** skriver ut snittlistan utifrån resultatlistan list och underlaget surface */
	public void outputToHTML(LinkedList list, int surface, int compareSurface) throws IOException {
		String startRow = "<TR>";
		String endRow = "</TR>";
		String startCol = "<TD class=bgrleft>";
		String startC = "<TD class=bgrcenter>";
		String endCol = "</TD>";
		LinkedList res = list;
		
		BufferedWriter bufferOut = new BufferedWriter(new FileWriter(fileName));
		bufferOut.write("<HTML>");
		bufferOut.newLine();
		bufferOut.write("<HEAD>");
		bufferOut.newLine();
		bufferOut.write("<STYLE id=\"Bangolftävlingar\"><!--table "+
										" .bgrleft{font-size:10.0pt;text-align:left;vertical-align:super;white-space:nowrap;}"+
										" .bgrcenter{font-size:10.0pt;text-align:center;vertical-align:super;white-space:nowrap;}"+
										"--></STYLE>");
		bufferOut.newLine();
		bufferOut.write("<TITLE>" + header + "</TITLE>");
		bufferOut.newLine();
		bufferOut.write("</HEAD>");
		bufferOut.newLine();
		bufferOut.write("<BODY bgcolor=\"white\">");
		bufferOut.newLine();
		bufferOut.write("<CENTER>");
		bufferOut.newLine();
		bufferOut.write("<FONT FACE=\"arial\">");
		bufferOut.newLine();
		bufferOut.write("<H1 style='font-size:14.0pt;font-weight:800;font-style:normal'>" + header + "</H1>");
		bufferOut.newLine();
		bufferOut.write("<TABLE CELLPADDING=0 CELLSPACING=1>");
		bufferOut.newLine();
		bufferOut.write(startRow);
		bufferOut.newLine();
		bufferOut.write("<TD class=bgrcenter WIDTH=59><B>Ranking</B>" + endCol);
		bufferOut.newLine();
		bufferOut.write("<TD class=bgrleft WIDTH=160><B>Namn</B>" + endCol);
		bufferOut.newLine();
		bufferOut.write("<TD class=bgrleft WIDTH=95><B>Klubb</B>" + endCol);
		bufferOut.newLine();
		bufferOut.write("<TD class=bgrcenter WIDTH=60><B>Tävlingar</B>" + endCol);
		bufferOut.newLine();
		bufferOut.write("<TD class=bgrcenter WIDTH=50><B>Varv</B>" + endCol);
		bufferOut.newLine();
		bufferOut.write("<TD class=bgrcenter WIDTH=50><B>Slag</B>" + endCol);
		bufferOut.newLine();
		bufferOut.write("<TD class=bgrcenter WIDTH=50><B>Snitt</B>" + endCol);
		bufferOut.newLine();
		bufferOut.write(endRow);
		bufferOut.newLine();
		
		int sameResult = 0;
		Person oldPerson = null;
		SnittComparator comparator = new SnittComparator();
		for (int i = 1; i <= res.size(); i++) {
			Person person = (Person)res.get(i-1);
			double snitt;
			if(person.getRounds() != 0) {
				snitt = (double)person.getHits()/(double)person.getRounds();
			} else {
				snitt = 0.00;
			}
			StringTokenizer str = new StringTokenizer(String.valueOf(snitt), ".");
			String heltal = str.nextToken();
			String decimaltal = str.nextToken();
			String medel = getMean(snitt, heltal, decimaltal);
			String color = getColor(snitt, surface);
			
			if(oldPerson != null) {
				if(comparator.compare(person, oldPerson) == 0) {
					sameResult++;
				} else {
					sameResult = 0;
				}
			}
			oldPerson = person;
			
			bufferOut.write(startRow);
			bufferOut.newLine();
			bufferOut.write(startC + "<B>" + (i - sameResult) + "</B>" + endCol);
			bufferOut.newLine();
			bufferOut.write(startCol + person.getName() + endCol);
			bufferOut.newLine();
			bufferOut.write(startCol + person.getClub() + endCol);
			bufferOut.newLine();
			bufferOut.write(startC + person.getComps() + endCol);
			bufferOut.newLine();
			bufferOut.write(startC + person.getRounds() + endCol);
			bufferOut.newLine();
			bufferOut.write(startC + person.getHits() + endCol);
			bufferOut.newLine();
			bufferOut.write(startC + "<FONT COLOR=\""+color+"\">" + medel + "</FONT>" + endCol);
			bufferOut.newLine();
			bufferOut.write(endRow);
			bufferOut.newLine();
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
		bufferOut.write("</CENTER>");
		bufferOut.newLine();
		bufferOut.write("</BODY>");
		bufferOut.newLine();
		bufferOut.write("</HTML>");
		
		bufferOut.flush();
		bufferOut.close();
	}
	
	/** skriver ut snittlistan utifrån resultatlistan list och underlaget surface */
	public void outputToCompareFile(LinkedList list, int surface) throws IOException {
		LinkedList res = list;
		
		BufferedWriter bufferOut = new BufferedWriter(new FileWriter(fileName));
		bufferOut.write(String.valueOf(surface));
		bufferOut.newLine();
		
		for (int i = 1; i <= res.size(); i++) {
			Person person = (Person)res.get(i-1);
			double snitt;
			if(person.getRounds() != 0) {
				snitt = (double)person.getHits()/(double)person.getRounds();
			} else {
				snitt = 0.00;
			}
			StringTokenizer str = new StringTokenizer(String.valueOf(snitt), ".");
			String heltal = str.nextToken();
			String decimaltal = str.nextToken();
			String medel = getMean(snitt, heltal, decimaltal);
			String color = getColor(snitt, surface);
			
			bufferOut.write(person.getIdNbr() + ";" + person.getName() + ";" + person.getClub() + ";" + medel);
			bufferOut.newLine();
		}
		
		bufferOut.flush();
		bufferOut.close();
	}
	
	/** läser jämförelsefilens snittlista fileName */
	public int readCompareFile(String fileName) throws FileNotFoundException, IOException {
	    BufferedReader fileIn = new BufferedReader(new FileReader(fileName));
		int surface = Integer.parseInt(fileIn.readLine());
		String inLine = fileIn.readLine();
		
		while (inLine != null) {
			String identity;
			double mean;
			
			StringTokenizer inString = new StringTokenizer(inLine, ";");
			if(inString.countTokens() != 0) {
				identity = inString.nextToken();
				inString.nextToken();
				inString.nextToken();
				mean = Double.parseDouble(inString.nextToken());
				if(map.containsKey(identity)) {
				    Person p = (Person) map.get(identity);
				    p.setOldMean(mean);
				}
			}
			inLine = fileIn.readLine();
		}
		return surface;
	}
	
	/** returnerar snittet som en sträng i önskvärt format */
	private String getMean(double snitt, String heltal, String decimaltal) {
	    String medel;
		if(snitt != 0.00) {
			if(decimaltal.length() > 2) {
				if(Integer.parseInt(decimaltal.substring(2,3)) >= 5) {
					if(Integer.parseInt(decimaltal.substring(0,2)) == 99) {
						medel = (Integer.parseInt(heltal) + 1) + "," + "00";
					} else if(Integer.parseInt(decimaltal.substring(1,2)) == 9) {
						medel = heltal + "," + (Integer.parseInt(decimaltal.substring(0,1)) + 1) + "0";
					} else {
						medel = heltal + "," + decimaltal.substring(0,1) + (Integer.parseInt(decimaltal.substring(1,2))+1);
					}
				} else {
					medel = heltal + "," + decimaltal.substring(0,2);
				}
			} else if(decimaltal.length() > 1) {
				medel = heltal + "," + decimaltal.substring(0,2);
			} else {
				medel = heltal + "," + decimaltal.substring(0,1) + "0";
			}
		} else {
			medel = "-";
		}
		return medel;
	}
	
	/** tar reda på vilken färg snittet snitt skall ha utifrån underlaget surface */
	private String getColor(double snitt, int surface) {
		String color;
		if(surface == ResultList.FILT) {
			if(snitt < 29.99500 && snitt >= 18.0) {
				color = "blue";
			} else if(snitt < 35.99500 && snitt >= 18.0) {
				color = "green";
			} else if(snitt < 39.99500 && snitt >= 18.0) {
				color = "red";
			} else {
				color = "black";
			}
		} else if(surface == ResultList.EB) {
			if(snitt < 19.99500 && snitt >= 18.0) {
				color = "blue";
			} else if(snitt < 24.99500 && snitt >= 18.0) {
				color = "green";
			} else if(snitt < 29.99500 && snitt >= 18.0) {
				color = "red";
			} else {
				color = "black";
			}
		} else if(surface == ResultList.BETONG) {
			if(snitt < 24.99500 && snitt >= 18.0) {
				color = "blue";
			} else if(snitt < 29.99500 && snitt >= 18.0) {
				color = "green";
			} else if(snitt < 35.99500 && snitt >= 18.0) {
				color = "red";
			} else {
				color = "black";
			}
		} else {
			color = "black";
		}
		return color;
	}
	
	/** laddar in data som bestämmer sorteringen */
	private void loadCompareBy(IOHandler io) {
	    try {
			compare = (int[][]) io.load("compareby");
		} catch (Exception e) {
			compare = new int[4][2];
			for(int i = 0; i < compare.length; i++) {
			    compare[i][0] = Snitt.MEAN;
			    compare[i][1] = Snitt.ROUNDS;
			}
			JOptionPane.showMessageDialog(null, "Inläsningen av sorteringsobjektet misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** klassen som beskriver sorteringen av resultaten */
	class SnittComparator implements Comparator {
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 * sorterar efter lägst snitt och sedan efter flest spelade varv
		 */
		public int compare(Object lhs, Object rhs) {
			Person left = (Person) lhs;
			Person right = (Person) rhs;
			Object[] leftCompareBy = new Object[6];
			Object[] rightCompareBy = new Object[6];
			
			leftCompareBy[NAME] = left.getName();
			leftCompareBy[CLUB] = left.getClub();
			leftCompareBy[COMPS] = new Integer(left.getComps());
			leftCompareBy[ROUNDS] = new Integer(left.getRounds());
			leftCompareBy[HITSUM] = new Integer(left.getHits());
			rightCompareBy[NAME] = right.getName();
			rightCompareBy[CLUB] = right.getClub();
			rightCompareBy[COMPS] = new Integer(right.getComps());
			rightCompareBy[ROUNDS] = new Integer(right.getRounds());
			rightCompareBy[HITSUM] = new Integer(right.getHits());
			if(((Integer)rightCompareBy[ROUNDS]).intValue() != 0) {
				double rightMean = ((double)right.getHits()) / ((double)right.getRounds());
				rightCompareBy[MEAN] = new Integer((int) (100000*rightMean));
			} else {
				rightCompareBy[MEAN] = new Integer(127 * 100000);
			}
			if(((Integer)leftCompareBy[ROUNDS]).intValue() != 0) {
				double leftMean = ((double)left.getHits()) / ((double)left.getRounds());
				leftCompareBy[MEAN] = new Integer((int) (100000*leftMean));
			} else {
				leftCompareBy[MEAN] = new Integer(127 * 100000);
			}
			IOHandler io = new IOHandler();
			loadCompareBy(io);
			for(int i = 0; i < compare[tabIndex].length; i++) {
				if(leftCompareBy[compare[tabIndex][i]] instanceof String) {
					if(!((String)leftCompareBy[compare[tabIndex][i]]).equals((String)rightCompareBy[compare[tabIndex][i]])) {
						return ((String)leftCompareBy[compare[tabIndex][i]]).compareTo((String)rightCompareBy[compare[tabIndex][i]]);
					}
				} else {
					if(((Integer)leftCompareBy[compare[tabIndex][i]]).intValue() != ((Integer)rightCompareBy[compare[tabIndex][i]]).intValue()) {
						int value = ((Integer)leftCompareBy[compare[tabIndex][i]]).intValue() - ((Integer)rightCompareBy[compare[tabIndex][i]]).intValue();
						if(compare[tabIndex][i] != COMPS && compare[tabIndex][i] != ROUNDS) {
							return value;
						} else {
							return -value;
						}
					}
				}
			}
			return 0;
		}
	}
}