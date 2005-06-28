package datastruct;
/** beskriver t�vlingsinformation f�r en person */
public class Person {
	private String name,club;		// namn och klubb
	private int comps,rounds,hits;	// antal t�vlingar, varv och slag
	
	/** skapar resultat f�r en person med namnet name och klubben club 
			antal t�vlingar comps, antal varv rounds och antal slag hits */
	public Person(String name, String club, int comps, int rounds, int hits) {
		this.name = name;
		this.club = club;
		this.comps = comps;
		this.rounds = rounds;
		this.hits = hits;
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
}
