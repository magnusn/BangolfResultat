package datastruct;
/** beskriver tävlingsinformation för en person */
public class Person {
	private String name,club;		// namn och klubb
	private int comps,rounds,hits;	// antal tävlingar, varv och slag
	
	/** skapar resultat för en person med namnet name och klubben club 
			antal tävlingar comps, antal varv rounds och antal slag hits */
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
}
