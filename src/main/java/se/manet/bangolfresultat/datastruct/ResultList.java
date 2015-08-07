package se.manet.bangolfresultat.datastruct;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.LinkedList;

/** klassen som beskriver resultatlistan */
public class ResultList {
	private HashMap IDMap, classMap, classOrder;// sparar personresultaten, de olika klassnamnen samt klassordningen
	public static final int FILT = 0;			// filtunderlagets heltalsvärde
	public static final int EB = 1;				// EB-underlagets heltalsvärde
	public static final int BETONG = 2;			// Betong-underlagets heltalsvärde
	public static final int NO_EXTRA_DATA = 0;	// heltalsvärde för ingen extradata
	public static final int START_NBR = 10;		// heltalsvärde för att startnummer skall finnas med
	public static final int ID_NBR = 1;			// heltalsvärde för att idnummer skall finnas med
	public static final int START_AND_ID_NBR = 11;	// heltalsvärde för att start- och idnummer skall finnas med
	public static final int NO_RESULT_THIS_ROUND=-1;// markerar att inget resultat registeras för detta varv
	public static final int MAX_SCORE = 126;	// högsta möjliga slagantal på ett varv
	public static final int MIN_SCORE = 18;		// lägsta möjliga slagantal på ett varv
	private int nbrRounds, surface, nbrLapSums, nbrCols;// tävlingens antal varv, underlag, antal delsummor och antal kolumner
	private String[][] output, outputStyle;	// matris med utdata och matris med information om formatet på detta
	private boolean[] lapSum, startData;	// talar om vilka delsummor som valts, om startnummer och idnummer har valts
	private boolean place;					// anger om placeringsnummer skall visas
	
	/** skapar en resultatlista för nbrRounds antal varv, underlaget surface där 0 = filt, 1 = eb och 2 = betong
	 samt vilka extra data som valts där startData[0] = startnummer och startData[1] = idnummer */
	public ResultList(int nbrRounds, int surface, boolean[] startData) {
		this.lapSum = new boolean[nbrRounds-2];
		this.startData = startData;
		this.nbrRounds = nbrRounds;
		this.surface = surface;
		this.nbrLapSums = getNbrLapSums();
		if(startData[0] && startData[1]) {
			this.nbrCols = nbrLapSums + 4 + nbrRounds + 2;
		} else if(startData[0] || startData[1]) {
			this.nbrCols = nbrLapSums + 4 + nbrRounds + 1;
		} else {
			this.nbrCols = nbrLapSums + 4 + nbrRounds;
		}
		IDMap = new HashMap();
		classMap = new HashMap();
		IOHandler io = new IOHandler();
		try {
			classOrder = (HashMap) io.load("classorder");
		} catch (Exception e) {
			classOrder = new HashMap();
		}
	}
	
	/** lägger till ett resultat med startnummer startNbr, namn name, klubb club, idnummer idNbr, varvresultat results
	 antal varv rounds, klassen klass, prioritetsnummer prio och antal varv avklarade nbrRoundsFinished */
	public void addResult(int startNbr, String name, String club, String idNbr, int[] results, int rounds, String klass
			, int prio, int nbrRoundsFinished, int personID) {
		nbrRounds = rounds;
		Integer pID = new Integer(personID);
		if(IDMap.containsKey(pID)) {
			PersonResult pr = (PersonResult) IDMap.get(pID);
			removeFromKlass(pr.getKlass());
			addToKlass(klass);
			pr.changeResults(startNbr,name,club,idNbr,results,klass,prio,nbrRoundsFinished);
		} else {
			addToKlass(klass);
			IDMap.put(new Integer(personID), new PersonResult(startNbr, name, club, idNbr, results, rounds, klass, prio, nbrRoundsFinished, personID));
		}
	}
	
	/** tar bort resultatet för personen med namnet name och klubben club, returnerar true om det lyckas */
	public boolean removeResult(Integer personID) {
		//String identity = name + club;
		if(IDMap.containsKey(personID)) {
			PersonResult pr = (PersonResult) IDMap.get(personID);
			removeFromKlass(pr.getKlass());
			IDMap.remove(personID);
			return true;
		} else {
			return false;
		}
	}
	
	
	/** ändrar namn och klubb för en spelare till newName och newClub */
	public void changeNameAndClub(Integer personID, String newName, String newClub) {
		if(IDMap.containsKey(personID)) {
			PersonResult pr = (PersonResult) IDMap.get(personID);
			pr.changeNameAndClub(newName, newClub);
		}
	}
	
	/** räknar upp antal förekomster av klassen klass eller lägger till klassen om den inte finns med sedan tidigare */
	private void addToKlass(String klass) {
		if(classMap.containsKey(klass)) {
			int n = ((Integer)classMap.get(klass)).intValue();
			classMap.put(klass, new Integer(n++));
		} else {
			classMap.put(klass, new Integer(1));
		}
	}
	
	/** räknar ner antal förekomster av klassen klass */
	private void removeFromKlass(String klass) {
		if (classMap.containsKey(klass)) {
			int n = ((Integer)classMap.get(klass)).intValue();
			classMap.put(klass, new Integer(n--));
		}
	}
	
	/** returnerar PersonResult för personen med id personIdentification */
	public PersonResult getPerson(int personIdentification) {
		Integer personID = new Integer(personIdentification);
		if(IDMap.containsKey(personID)) {
			return (PersonResult) IDMap.get(personID);
		} else {
			return null;
		}
	}
	
	/**
	 * @return iterator över alla personers resultat
	 */
	public Iterator iterator() {
		return IDMap.values().iterator();
	}
	
	/** returnerar resultatlistans storlek */
	public int size() {
		return IDMap.size();
	}
	
	/** sorterar och returnerar därefter resultatlistan som en länkad lista */
	public LinkedList sortResults() {
		LinkedList list = new LinkedList();
		ResultComparator comparator = new ResultComparator();
		list.addAll(IDMap.values());
		Collections.sort(list, comparator);
		return list;
	}
	
	/** returnerar hur många delsummor som skall visas */
	private int getNbrLapSums() {
		int n = 0;
		for(int i = 0; i < nbrRounds-2; i++) {
			if(lapSum[i]) {
				n++;
			}
		}
		return n;
	}
	
	/** returnerar en vektor som talar om ifall placering, startnummer och licensnummer skall visas */
	public boolean[] getExtras() {
		boolean[] b = new boolean[3];
		b[0] = place;
		b[1] = startData[0];
		b[2] = startData[1];
		return b;
	}
	
	/** returnerar en matris med data ifrån resultatlistan */
	public String[][] getOutput() {
		LinkedList list = sortResults();
		ResultComparator comparator = new ResultComparator();
		HashMap tempMap = new HashMap();
		if(list.size() != 0) {
			output = new String[list.size()+3*getNbrOfClasses()-1][nbrCols];
			outputStyle = new String[list.size()+3*getNbrOfClasses()-1][nbrCols];
		} else {
			output = new String[1][1];
			outputStyle = new String[1][1];
		}
		int i = 0;
		int k = 0;
		int placeNbr = 0;
		int sameResult = 0;
		PersonResult oldPerson = null;
		while (i < list.size()) {
			placeNbr++;
			PersonResult pr = (PersonResult)list.get(i);
			
			if(oldPerson != null) {
				if(comparator.compare(pr, oldPerson) == 0) {
					sameResult++;
				} else {
					sameResult = 0;
				}
			}
			oldPerson = pr;
			
			int nbrRoundsFinished = pr.getNbrRoundsFinished();
			String klass = pr.getKlass();
			if(!tempMap.containsKey(klass) && tempMap.size()==0) {
				tempMap.put(klass, klass);
				output[i+k][0] = klass;
				outputStyle[i+k][0] = "Bold+";
				k++;
				setHeader(i,k);
				k++;
				placeNbr = 1;
			} else if(!tempMap.containsKey(klass)) {
				tempMap.put(klass, klass);
				output[i+k][0] = " ";
				k++;
				output[i+k][0] = klass;
				outputStyle[i+k][0] = "Bold+";
				k++;
				setHeader(i,k);
				k++;
				placeNbr = 1;
			}
			int j = 0;
			output[i+k][j] = pr.getName();
			j++;
			output[i+k][j] = pr.getClub();
			j++;
			if(startData[1]) {
				output[i+k][j] = String.valueOf(pr.getLicenseNr());
				j++;
			}
			if(startData[0]) {
				output[i+k][j] = String.valueOf(pr.getStartNr());
				j++;
			}
			int[] results = pr.getResultList();
			int nbrRoundsPlayed = nbrRoundsFinished;
			if(nbrRoundsFinished != 0) {
				if(results[0] != ResultList.NO_RESULT_THIS_ROUND) {
					output[i+k][j] = String.valueOf(results[0]);
					outputStyle[i+k][j] = getColor(results[0], 1);
				} else {
					output[i+k][j] = "-";
					outputStyle[i+k][j] = "black";
					nbrRoundsPlayed--;
				}
			}
			j++;
			int upperLevel;
			if(place) {
				upperLevel = nbrCols-3;
			} else {
				upperLevel = nbrCols-2;
			}
			int varv = 2;
			while(j < upperLevel) {
				if(nbrRoundsFinished >= varv) {
					if(results[varv-1] != ResultList.NO_RESULT_THIS_ROUND) {
						output[i+k][j] = String.valueOf(results[varv-1]);
						outputStyle[i+k][j] = getColor(results[varv-1], 1);
					} else {
						output[i+k][j] = "-";
						outputStyle[i+k][j] = "black";
						nbrRoundsPlayed--;
					}
					if(varv < nbrRounds) {
						// delsummor
						if(lapSum[varv-2]) {
							j++;
							int nbrRoundsPlayedThisFar = varv - (nbrRoundsFinished - nbrRoundsPlayed);
							if (nbrRoundsPlayedThisFar != 0) {
								output[i+k][j] = String.valueOf(pr.getSum(varv));
								outputStyle[i+k][j] = "S:a" + getColor(pr.getSum(varv), nbrRoundsPlayedThisFar);
							} else {
								output[i+k][j] = "-";
								outputStyle[i+k][j] = "black";
							}
						}
					}
					varv++;
				}
				j++;
			}
			if(nbrRoundsFinished != 0) {
				if(nbrRoundsPlayed != 0) {
					output[i+k][j] = String.valueOf(pr.getSum(nbrRoundsFinished));
					outputStyle[i+k][j] = "S:a" + getColor(pr.getSum(nbrRoundsFinished), nbrRoundsPlayed);
				} else {
					output[i+k][j] = "-";
					outputStyle[i+k][j] = "black";
				}
			}
			j++;
			if(nbrRoundsFinished != 0) {
				if(nbrRoundsPlayed != 0) {
					double snitt = (double)pr.getSum(nbrRoundsFinished)/(double)nbrRoundsPlayed;
					StringTokenizer str = new StringTokenizer(String.valueOf(snitt), ".");
					String heltal = str.nextToken();
					String decimaltal = str.nextToken();
					if(decimaltal.length() > 2) {
						if(Integer.parseInt(decimaltal.substring(2,3)) >= 5) {
							if(Integer.parseInt(decimaltal.substring(0,2)) == 99) {
								output[i+k][j] = (Integer.parseInt(heltal) + 1) + "," + "00";
							} else if(Integer.parseInt(decimaltal.substring(1,2)) == 9) {
								output[i+k][j] = heltal + "," + (Integer.parseInt(decimaltal.substring(0,1)) + 1) + "0";
							} else {
								output[i+k][j] = heltal + "," + decimaltal.substring(0,1) + (Integer.parseInt(decimaltal.substring(1,2))+1);
							} 
						} else {
							output[i+k][j] = heltal + "," + decimaltal.substring(0,2);
						} 
					} else if(decimaltal.length() > 1) {
						output[i+k][j] = heltal + "," + decimaltal.substring(0,2);
					} else {
						output[i+k][j] = heltal + "," + decimaltal.substring(0,1) + "0";
					}
					outputStyle[i+k][j] = getColor(pr.getSum(nbrRoundsFinished), nbrRoundsPlayed);
				} else {
					output[i+k][j] = "-";
					outputStyle[i+k][j] = "black";
				}
			}
			j++;
			if(place) {
				output[i+k][j] = String.valueOf(placeNbr - sameResult);
			}
			i++;
		}
		return output;
	}
	
	/** returnerar formateringen på outputen från resultatlistan */
	public String[][] getOutputStyle() {
		return outputStyle;
	}
	
	/** returnerar en sträng som talar om vilken färg resultatet skall ha,
	 	man ger in summan result och på hur många varv varv */
	private String getColor(int result, int varv) {
		double snitt = (double)result/(double)varv;
		String color;
		if(surface == FILT) {
			if(snitt < 29.99500) {
				color = "blue";
			} else if(snitt < 35.99500) {
				color = "green";
			} else if(snitt < 39.99500) {
				color = "red";
			} else {
				color = "black";
			}
		} else if(surface == EB) {
			if(snitt < 19.99500) {
				color = "blue";
			} else if(snitt < 24.99500) {
				color = "green";
			} else if(snitt < 29.99500) {
				color = "red";
			} else {
				color = "black";
			}
		} else {
			if(snitt < 24.99500) {
				color = "blue";
			} else if(snitt < 29.99500) {
				color = "green";
			} else if(snitt < 35.99500) {
				color = "red";
			} else {
				color = "black";
			}
		}
		return color;
	}
	
	/** sätter rubrikerna på rad i+k */
	private void setHeader(int i, int k) {
		String sum;
		if(nbrRounds > 5) {
			sum = "V";
		} else {
			sum = "Varv ";
		}
		int j = 0;
		output[i+k][j] = "Namn";
		outputStyle[i+k][j] = "Bold";
		j++;
		output[i+k][j] = "Klubb";
		outputStyle[i+k][j] = "Bold";
		j++;
		if(startData[1]) {
			output[i+k][j] = "Licensnummer";
			outputStyle[i+k][j] = "Bold";
			j++;
		}
		if(startData[0]) {
			output[i+k][j] = "StartNr";
			outputStyle[i+k][j] = "Bold";
			j++;
		}
		output[i+k][j] = sum + "1";
		outputStyle[i+k][j] = "Bold";
		j++;
		output[i+k][j] = sum + "2";
		outputStyle[i+k][j] = "Bold";
		j++;
		int varv = 2;
		int upperLevel;
		if(place) {
			upperLevel = nbrCols-3;
		} else {
			upperLevel = nbrCols-2;
		}
		while(j < upperLevel) {
			if(lapSum[varv-2]) {
				output[i+k][j] = "S:a";
				outputStyle[i+k][j] = "Bold";
				j++;
			}
			varv++;
			output[i+k][j] = sum + varv;
			outputStyle[i+k][j] = "Bold";
			j++;
		}
		output[i+k][j] = "S:a";
		outputStyle[i+k][j] = "Bold";
		j++;
		output[i+k][j] = "Medel";
		outputStyle[i+k][j] = "Bold";
		j++;
		if(place) {
			output[i+k][j] = "Plac.";
			outputStyle[i+k][j] = "Bold";
		}
	}
	
	/** ställer in vilka delsummor som skall visas utifrån lapSum */
	public void setLapSum(boolean[] lapSum) {
		nbrCols = nbrCols - nbrLapSums;
		this.lapSum = lapSum;
		nbrLapSums = getNbrLapSums();
		nbrCols = nbrCols + nbrLapSums;
	}
	
	/** anger om placering skall visas */
	public void setPlace(boolean place) {
		if(!this.place && place) {
			nbrCols++;
		} else if(this.place && !place) {
			nbrCols--;
		}
		this.place = place;
	}
	
	/** returnerar antalet olika klasser som finns i resultatlistan */
	private int getNbrOfClasses() {
		int n = 0;
		LinkedList list = new LinkedList();
		list.addAll(classMap.values());
		for(int i = 0; i < list.size(); i++) {
			if(((Integer)list.get(i)).intValue() != 0) {
				n++;
			}
		}
		return n;
	}
	
	/** returnerar antalet kolumner som behövs för att visa resultatet */
	public int getNbrCols() {
		return nbrCols;
	}
	
	/** returnerar hur många varv tävlingen är på */
	public int getNbrRounds() {
		return nbrRounds;
	}
	
	/** returnerar tävlingens underlag */
	public int getSurface() {
		return surface;
	}
	
	/** returnerar en vektor som talar om ifall startnummer och idnummer skall visas */
	public boolean[] getStartData() {
		return startData;
	}
	
	/** talar om vilka "extradata" i form av startnummer och idnummer som skall visas */
	public int getExtraData() {
		int extraData = NO_EXTRA_DATA;
		if(startData[0] && startData[1]) {
			extraData = START_AND_ID_NBR;
		} else if(startData[0]) {
			extraData = START_NBR;
		} else if(startData[1]) {
			extraData = ID_NBR;
		}
		return extraData;
	}
	
	/** talar om hur klasserna skall sorteras */
	private String getClassOrder(String className) {
		if(classOrder.containsKey(className)) {
			return (String) classOrder.get(className);
		} else {
			return className;
		}
	}
	
	/** klassen som sköter sorteringen av resultatlistan */
	class ResultComparator implements Comparator {
		/**	bestämmer skillnaden mellan lhs och rhs genom att sortera i ordningen
		 *	klass, avklarade varv, summa, prioritet och differens
		 *	@see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object lhs, Object rhs) {
			PersonResult left = (PersonResult) lhs;
			PersonResult right = (PersonResult) rhs;
			String klassRight = getClassOrder(right.getKlass());
			String klassLeft = getClassOrder(left.getKlass());
			int prioRight = right.getPrio();
			int prioLeft = left.getPrio();
			int roundsLeft = left.getNbrRoundsFinished();
			int roundsRight = right.getNbrRoundsFinished();
			int roundsPlayedLeft = left.getNbrRoundsPlayed();
			int roundsPlayedRight = right.getNbrRoundsPlayed();
			int sumRight, sumLeft;
			/*
			if(roundsPlayedLeft == 0) {
			    sumLeft = 127;
			} else {
			    sumLeft = left.getSum(roundsLeft);
			}
			if(roundsPlayedRight == 0) {
			    sumRight = 127;
			} else {
			    sumRight = right.getSum(roundsRight);
			}*/
			/** sortera på snitt för att se bra ut när någon står över ett varv */
			if(roundsPlayedLeft != 0) {
				sumLeft = (int) (10000*((double)left.getSum(roundsLeft)/(double)roundsPlayedLeft));
			} else {
				sumLeft = 10000 * 127;
			}
			if (roundsPlayedRight != 0) {
				sumRight = (int) (10000*((double)right.getSum(roundsRight)/(double)roundsPlayedRight));
			} else {
				sumRight = 10000 * 127;
			}
			int diffLeft = left.getDiff();
			int diffRight = right.getDiff();
			if (klassLeft.equals(klassRight)) {
				if (roundsLeft == roundsRight) {
					if (sumLeft == sumRight) {
						if (prioLeft == prioRight) {
							if (diffLeft == diffRight) {
								return 0;
							} else {
								return diffLeft - diffRight;
							}
						} else {
							return prioLeft - prioRight;
						}
					} else {
						return sumLeft - sumRight;
					} 
				} else {
					return roundsRight - roundsLeft;
				}
			} else {
				return klassLeft.compareTo(klassRight);
			}
		}
	}
}