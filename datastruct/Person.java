package datastruct;
/** beskriver tävlingsinformation för en person */
public class Person {
	private String name,club;		// namn och klubb
	private int idNbr;				// programmets identifikationsnummer för personen
	private int comps,rounds,hits;	// antal tävlingar, varv och slag
	private double oldMean;			// fjolårssnitt
	public static final double NO_OLD_MEAN = -1; // värde då inget fjolårssnitt är satt
	
	/** skapar resultat för en person med namnet name och klubben club 
			antal tävlingar comps, antal varv rounds och antal slag hits */
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
	
	/** returnerar antalet tävlingar */
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
	
	/** ökar antalet tävlingar med 1 */
	public void addComps() {
		comps++;
	}
	
	/** lägger till antalet varv som anges i rounds */
	public void addRounds(int rounds) {
		this.rounds += rounds;
	}
	
	/** ökar antalet slag med hits */
	public void addHits(int hits) {
		this.hits += hits;
	}
	
	/** ändrar namnet till name */
	public void changeName(String name) {
		this.name = name;
	}
	
	/** ändrar klubben till club */
	public void changeClub(String club) {
		this.club = club;
	}
	
	/** sätter fjolårssnittet till oldMean */
	public void setOldMean(double oldMean) {
	    this.oldMean = oldMean;
	}
	
	/** returnerar fjolårssnittet */
	public double getOldMean() {
	    return oldMean;
	}
}