package snitt;

import gui.AlignmentWindow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

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
	public static final int EX_MEAN = 6;// siffran för fjolårssnitt som används vid sortering
	public static final int CHANGE = 7;	// siffran för förändringen som används vid sortering
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
	}
	
	/** läser in resultat ifrån filerna i strängvektorn fileNames och hämtar namn och klubb med hjälp av personTrack */
	public void readResults(String[] fileNames, HashMap personNameTrack) throws FileNotFoundException, IOException, NoSuchElementException {
		String[] files = fileNames;
		HashMap personNameTracker = personNameTrack;
		for(int i = 0; i < files.length; i++) {
			BufferedReader fileIn = IOHandler.getTextFileReader(files[i]);
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
					inString.nextToken();
					inString.nextToken();
					inString.nextToken();
					rounds = Integer.parseInt(inString.nextToken());
					for(int j = 0; j < rounds; j++) {
					    int nbrHits = Integer.parseInt(inString.nextToken());
					    if(nbrHits <= ResultList.MAX_SCORE && nbrHits >= ResultList.MIN_SCORE) {
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
			fileIn.close();
		}
	}
	
	/** räknar ut antalet startande i varje klass och returnerar en HashMap 
	 *  där klassnamnet är nyckel och antalet starter ligger som värde
	 */
	public HashMap countClasses(String[] fileNames) throws FileNotFoundException, IOException {
		String[] files = fileNames;
		HashMap map = new HashMap();
		for(int i = 0; i < files.length; i++) {
			BufferedReader fileIn = IOHandler.getTextFileReader(files[i]);
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
			fileIn.close();
		}
		return map;
	}
	
	/** sorterar resultaten och returnerar dem i en länkad lista */
	public LinkedList sortMap(HashSet excludedClubs) {
		LinkedList list = new LinkedList();
		if (excludedClubs == null || excludedClubs.isEmpty())
			list.addAll(map.values());
		else {
			Iterator it = map.values().iterator();
			while (it.hasNext()) {
				Person person = (Person) it.next();
				if (!excludedClubs.contains(person.getClub().trim().toLowerCase()))
					list.add(person);
			}
		}
		IOHandler io = new IOHandler();
		loadCompareBy(io);
		SnittComparator comparator = new SnittComparator();
		Collections.sort(list, comparator);
		return list;
	}
	
	/** skriver ut snittlistan utifrån resultatlistan list, underlaget surface 
	 *  och jämförelsefilen med underlaget compareSurface (-1 vid ingen jämförelse) */
	public void outputToHTML(LinkedList list, int surface, int compareSurface,
	        				 boolean[] headerList, int align) throws IOException {
	    String alignment;
	    if(align == AlignmentWindow.LEFT) {
	        alignment = "left";
	    } else if(align == AlignmentWindow.CENTER) {
	        alignment = "center";
	    } else {
	        alignment = "right";
	    }
		String startRow = "<tr>";
		String endRow = "</tr>";
		String startCol = "<td>";
		String startColAligned = startCol;
		if (! alignment.equals("left"))
			startColAligned = IOHandler.addStyle(startCol, "text-align", alignment);
//		String startColLeft = "<TD class=bgrleft>";
//		String startColCenter = "<TD class=bgrcenter>";
//		String startColRight = "<TD class=bgrright>";
		String startColName = "<td style=\"width:160px\">";
		String startColClub = "<td style=\"width:95px\">";
		String startColComps = IOHandler.addStyle("<td style=\"width:60px\">", "text-align", alignment);
		String startColRounds = IOHandler.addStyle("<td style=\"width:50px\">", "text-align", alignment);
		String startColHitSum = IOHandler.addStyle("<td style=\"width:50px\">", "text-align", alignment);
		String startColMean = IOHandler.addStyle("<td style=\"width:50px\">", "text-align", alignment);
		String startColExMean = IOHandler.addStyle("<td style=\"width:65px\">", "text-align", alignment);
		String startColChange = IOHandler.addStyle("<td style=\"width:45px\">", "text-align", alignment);
		String endCol = "</td>";
		String[] headers = new String[headerList.length];
		headers[Snitt.NAME] = startColName + "<b>Namn</b>" + endCol;
		headers[Snitt.CLUB] = startColClub + "<b>Klubb</b>" + endCol;
		headers[Snitt.COMPS] = startColComps + "<b>Tävlingar</b>" + endCol;
		headers[Snitt.ROUNDS] = startColRounds + "<b>Varv</b>" + endCol;
		headers[Snitt.HITSUM] = startColHitSum + "<b>Slag</b>" + endCol;
		headers[Snitt.MEAN] = startColMean + "<b>Snitt</b>" + endCol;
		headers[Snitt.EX_MEAN] = startColExMean + "<b>Snitt ifjol</b>" + endCol;
		headers[Snitt.CHANGE] = startColChange + "<b>+/-</b>" + endCol;
		
		LinkedList res = list;
		
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
				"td {font-size: 10.0pt; vertical-align: super; white-space: nowrap} " +
				"</style>");
		bufferOut.newLine();
		bufferOut.write("</head>");
		bufferOut.newLine();
		
		bufferOut.write("<body style=\"background-color:white\">");
		bufferOut.newLine();
		bufferOut.write("<h1 style=\"font-size:14.0pt; text-align:center\">" + header + "</h1>");
		bufferOut.newLine();
		bufferOut.write("<table cellpadding=\"1\" cellspacing=\"1\" style=\"" +
				"margin-left:auto; margin-right:auto\">");
		bufferOut.newLine();
		bufferOut.write(startRow);
		bufferOut.newLine();
		bufferOut.write("<td style=\"width:59px; text-align:right\"><b>Ranking&nbsp;</b>" + endCol);
		bufferOut.newLine();
		for(int i = 0; i < headerList.length; i++) {
		    if(headerList[i]) {
		        bufferOut.write(headers[i]);
		        bufferOut.newLine();
		    }
		}
		bufferOut.write(endRow);
		bufferOut.newLine();
		
		int sameResult = 0;
		Person oldPerson = null;
		SnittComparator comparator = new SnittComparator();
		for (int i = 1; i <= res.size(); i++) {
			Person person = (Person)res.get(i-1);
			double snitt = person.getTwoDecimalMean();
			double oldMean = person.getOldMean();
			
			StringTokenizer str = new StringTokenizer(String.valueOf(snitt), ".");
			String heltal = str.nextToken();
			String decimaltal = str.nextToken();
			String medel = SnittData.getValueWithTwoDecimals(snitt, heltal, decimaltal, false);
			String color = getColor(snitt, surface);
			
			String compareColor, compareMean, diffValue;
			if(oldMean > ResultList.MIN_SCORE && oldMean < ResultList.MAX_SCORE) {
			    str = new StringTokenizer(String.valueOf(oldMean), ".");
			    heltal = str.nextToken();
			    decimaltal = str.nextToken();
			    compareMean = SnittData.getValueWithTwoDecimals(oldMean, heltal, decimaltal, false);
			    compareColor = getColor(oldMean, compareSurface);
			    
			    double diff = person.getDiff();
			    str = new StringTokenizer(String.valueOf(diff), ".");
			    heltal = str.nextToken();
			    decimaltal = str.nextToken();
			    diffValue = SnittData.getValueWithTwoDecimals(diff, heltal, decimaltal, false);
			    if(diff > 0.00) {
			        diffValue = "+" + diffValue;
			    }
			} else {
			    compareColor = "black";
			    compareMean = "-";
			    diffValue = "-";
			}
			if(snitt == Person.NO_VALUE) {
			    diffValue = "-";
			}
			
			if(oldPerson != null) {
				if(comparator.compare(person, oldPerson) == 0) {
					sameResult++;
				} else {
					sameResult = 0;
				}
			}
			oldPerson = person;
			
			String[] data = new String[headerList.length];
			data[Snitt.NAME] = startCol + person.getName() + endCol;
			data[Snitt.CLUB] = startCol + person.getClub() + endCol;
			data[Snitt.COMPS] = startColAligned + person.getComps() + endCol;
			data[Snitt.ROUNDS] = startColAligned + person.getRounds() + endCol;
			data[Snitt.HITSUM] = startColAligned + person.getHits() + endCol;
			if (! color.equals("black"))
				data[Snitt.MEAN] = IOHandler.addStyle(startColAligned, "color", color) + medel + endCol;
			else
				data[Snitt.MEAN] = startColAligned + medel + endCol;
			if (! compareColor.equals("black"))
				data[Snitt.EX_MEAN] = IOHandler.addStyle(startColAligned, "color", compareColor) + compareMean + endCol;
			else
				data[Snitt.EX_MEAN] = startColAligned + compareMean + endCol;
			data[Snitt.CHANGE] = startColAligned + diffValue + endCol;
			
			bufferOut.write(startRow);
			bufferOut.newLine();
			bufferOut.write(IOHandler.addStyle(startCol, "text-align", "right") + "<b>" + (i - sameResult) + "&nbsp;</b>" + endCol);
			bufferOut.newLine();
			for(int j = 0; j < data.length; j++) {
			    if(headerList[j]) {
			        bufferOut.write(data[j]);
			        bufferOut.newLine();
			    }
			}
			bufferOut.write(endRow);
			bufferOut.newLine();
		}
		
		bufferOut.write("</table>");
		bufferOut.newLine();
		bufferOut.write("<p><br>");
		bufferOut.newLine();
		bufferOut.write("<div style=\"text-align:center; font-size:7.5pt\">Denna sida är skapad av <a href=\"http://bangolfresultat.webhop.org/\">"
				+ "BangolfResultat</a></div>");
		bufferOut.newLine();
		bufferOut.write("</body>");
		bufferOut.newLine();
		bufferOut.write("</html>");
		
		bufferOut.flush();
		bufferOut.close();
	}
	
	/** skriver ut snittlistan utifrån resultatlistan list och underlaget surface */
	public void outputToCompareFile(LinkedList list, int surface) throws IOException {
		LinkedList res = list;
		
		BufferedWriter bufferOut = IOHandler.getTextFileWriter(fileName);
		bufferOut.write(String.valueOf(surface));
		bufferOut.newLine();
		
		Person person;
		double snitt;
		for (int i = 1; i <= res.size(); i++) {
			person = (Person)res.get(i-1);
			snitt = person.getMean();
			if(snitt != Person.NO_VALUE) {
			    StringTokenizer str = new StringTokenizer(String.valueOf(snitt), ".");
			    String heltal = str.nextToken();
			    String decimaltal = str.nextToken();
			    String medel = SnittData.getValueWithTwoDecimals(snitt, heltal, decimaltal, true);
			    
			    bufferOut.write(person.getIdNbr() + ";" + person.getName() + ";" + person.getClub() + ";" + medel);
			    bufferOut.newLine();
			}
		}
		
		bufferOut.flush();
		bufferOut.close();
	}
	
	/** läser jämförelsefilens snittlista fileName */
	public int readCompareFile(String fileName) throws IOException {
	    BufferedReader fileIn = IOHandler.getTextFileReader(fileName);
		int surface = Integer.parseInt(fileIn.readLine());
		String inLine = fileIn.readLine();
		
		while (inLine != null) {
			String identity;
			double oldMean;
			
			StringTokenizer inString = new StringTokenizer(inLine, ";");
			if(inString.countTokens() != 0) {
				identity = inString.nextToken();
				inString.nextToken();
				inString.nextToken();
				oldMean = Double.parseDouble(inString.nextToken());
				if(map.containsKey(identity)) {
				    Person p = (Person) map.get(identity);
				    double mean = p.getMean();
				    if(mean != Person.NO_VALUE && oldMean != Person.NO_VALUE) {
				        double diff = p.getTwoDecimalMean() - oldMean;
				        p.setDiff(diff);
				    }
				    
				    p.setOldMean(oldMean);
				}
			}
			inLine = fileIn.readLine();
		}
		fileIn.close();
		return surface;
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
			compare = new int[SnittWindow.NBR_SNITT][2];
			for(int i = 0; i < compare.length; i++) {
			    compare[i][0] = Snitt.MEAN;
			    compare[i][1] = Snitt.ROUNDS;
			}
			try {
			    io.save("compareby", compare);
			} catch (Exception ex) {}
			if(!(e instanceof ClassCastException)) {
			    JOptionPane.showMessageDialog(null, "Inläsningen av sorteringsobjektet misslyckades", "Varning", JOptionPane.ERROR_MESSAGE);
			}
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
			Object[] leftCompareBy = new Object[SnittData.NBR_HEADERS];
			Object[] rightCompareBy = new Object[SnittData.NBR_HEADERS];
			
			leftCompareBy[NAME] = left.getName();
			leftCompareBy[CLUB] = left.getClub();
			leftCompareBy[COMPS] = new Integer(left.getComps());
			leftCompareBy[ROUNDS] = new Integer(left.getRounds());
			leftCompareBy[HITSUM] = new Integer(left.getHits());
			leftCompareBy[MEAN] = new Integer((int) (left.getMean() * 100000));
			leftCompareBy[EX_MEAN] = new Integer((int) (left.getOldMean() * 100));
			leftCompareBy[CHANGE] = new Integer((int) (left.getDiff() * 100));
			rightCompareBy[NAME] = right.getName();
			rightCompareBy[CLUB] = right.getClub();
			rightCompareBy[COMPS] = new Integer(right.getComps());
			rightCompareBy[ROUNDS] = new Integer(right.getRounds());
			rightCompareBy[HITSUM] = new Integer(right.getHits());
			rightCompareBy[MEAN] = new Integer((int) (right.getMean() * 100000));
			rightCompareBy[EX_MEAN] = new Integer((int) (right.getOldMean() * 100));
			rightCompareBy[CHANGE] = new Integer((int) (right.getDiff() * 100));
			
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