package datastruct;

import java.util.StringTokenizer;

import snitt.SnittData;

/** beskriver t�vlingsinformation f�r en person */
public class Person {
	private String name,club;		// namn och klubb
	private int idNbr;				// programmets identifikationsnummer f�r personen
	private int comps,rounds,hits;	// antal t�vlingar, varv och slag
	private double oldMean;			// fjol�rssnitt
	private double diff;			// differensen mellan �rets snitt och fjol�rets
	private double twoDecimalMean;	// snitt med tv� decimalers noggrannhet
	public static final double NO_VALUE = 127; // d� giltigt v�rde saknas
	
	/** skapar resultat f�r en person med namnet name och klubben club 
			antal t�vlingar comps, antal varv rounds och antal slag hits */
	public Person(int idNbr, String name, String club, int comps, int rounds, int hits) {
	    this.idNbr = idNbr;
		this.name = name;
		this.club = club;
		this.comps = comps;
		this.rounds = rounds;
		this.hits = hits;
		this.oldMean = Person.NO_VALUE;
		this.diff = Person.NO_VALUE;
		this.twoDecimalMean = Person.NO_VALUE;
	}
	
	/** returnerar identifikationsnumret */
	public int getIdNbr() {
	    return idNbr;
	}
	
	/** returnerar namnet */
	public String getName() {
		return name;
	}
	
	/** returnerar klubben */
	public String getClub() {
		return club;
	}
	
	/** returnerar antalet t�vlingar */
	public int getComps() {
		return comps;
	}
	
	/** returnerar antalet spelade varv */
	public int getRounds() {
		return rounds;
	}
	
	/** returnerar antalet slag */
	public int getHits() {
		return hits;
	}
	
	/** returnerar snittet */
	public double getMean() {
	    if(rounds != 0) {
			return (double)hits/(double)rounds;
		} else {
			return Person.NO_VALUE;
		}
	}
	
	/** �kar antalet t�vlingar med 1 */
	public void addComps() {
		comps++;
	}
	
	/** l�gger till antalet varv som anges i rounds */
	public void addRounds(int rounds) {
		this.rounds += rounds;
	}
	
	/** �kar antalet slag med hits */
	public void addHits(int hits) {
		this.hits += hits;
	}
	
	/** �ndrar namnet till name */
	public void changeName(String name) {
		this.name = name;
	}
	
	/** �ndrar klubben till club */
	public void changeClub(String club) {
		this.club = club;
	}
	
	/** s�tter fjol�rssnittet till oldMean */
	public void setOldMean(double oldMean) {
	    this.oldMean = oldMean;
	}
	
	/** returnerar fjol�rssnittet */
	public double getOldMean() {
	    return oldMean;
	}
	
	/** s�tter differensen mellan �rets och fjol�rets snitt till diff */
	public void setDiff(double diff) {
	    this.diff = diff;
	}
	
	/** returnerar differensen mellan �rets och fjol�rets snitt */
	public double getDiff() {
	    return diff;
	}
	
	/** returnerar snittet med tv� decimalers noggrannhet */
	public double getTwoDecimalMean() {
	    double mean = getMean();
	    StringTokenizer str = new StringTokenizer(String.valueOf(mean), ".");
        String heltal = str.nextToken();
        String decimaltal = str.nextToken();
        String meanValue = SnittData.getValueWithTwoDecimals(mean, heltal, decimaltal, true);
	    return Double.parseDouble(meanValue);
	}
}