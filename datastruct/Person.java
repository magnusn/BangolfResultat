package datastruct;
/** beskriver t�vlingsinformation f�r en person */
public class Person {
	private String name,club;		// namn och klubb
	private int idNbr;				// programmets identifikationsnummer f�r personen
	private int comps,rounds,hits;	// antal t�vlingar, varv och slag
	private double oldMean;			// fjol�rssnitt
	public static final double NO_OLD_MEAN = -1; // v�rde d� inget fjol�rssnitt �r satt
	
	/** skapar resultat f�r en person med namnet name och klubben club 
			antal t�vlingar comps, antal varv rounds och antal slag hits */
	public Person(int idNbr, String name, String club, int comps, int rounds, int hits) {
	    this.idNbr = idNbr;
		this.name = name;
		this.club = club;
		this.comps = comps;
		this.rounds = rounds;
		this.hits = hits;
		this.oldMean = Person.NO_OLD_MEAN;
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
}