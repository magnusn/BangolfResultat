package se.manet.bangolfresultat.datastruct;
/** klassen som beskriver varje persons resultat */
public class PersonResult {
	private String name, club, klass;		// personens namn, klubb och klass
	private String licenseNbr;				// personens licensnummer
	private int startNbr, rounds; 			// startnummer, idnummer och tävlingens antal varv
	private int prio, nbrRoundsFinished; 	// prioritet vid samma antal slag, antal färdigspelade varv
	private int diff;						// skillnaden mellan högsta och lägsta varv
	private int personID;					// nummer för identifiering av personen
	private int[] results;					// vektor med resultaten på varje varv
	
	/** skapar ett resultat till personen med namnet name, klubben club, startnumret startNbr, 
	 *  licensnumret idNbr där results är resultaten på varje varv, rounds antal varv, klass klassen, 
	 * 	prio prioriteten och nbrRoundsFinished antal varv som är slutförda */
	public PersonResult(int startNbr, String name, String club, String idNbr, int[] results, 
						int rounds, String klass, int prio, int nbrRoundsFinished, int personID) {
		this.startNbr = startNbr;
		this.name = name;
		this.club = club;
		this.licenseNbr = idNbr;
		this.results = results;
		this.rounds = rounds;
		this.klass = klass;
		this.prio = prio;
		this.nbrRoundsFinished = nbrRoundsFinished;
		this.personID = personID;
		calculateDiff();
	}
	
	/** returnerar startnumret */
	public int getStartNr() {
		return startNbr;
	}
	
	/** returnerar namnet */
	public String getName() {
		return name;
	}
	
	/** returnerar klubben */
	public String getClub() {
		return club;
	}
	
	/** returnerar idnumret */
	public String getLicenseNr() {
		return licenseNbr;
	}
	
	/** returnerar klassen */
	public String getKlass() {
	    if (klass.trim().equals(""))
	        return " ";
		return klass;
	}
	
	/** returnerar slagsumman till och med varvnumret untilRound */
	public int getSum(int untilRound) {
		int temp = 0;
		for(int i = 0; i < untilRound; i++) {
			if(results[i] <= ResultList.MAX_SCORE && results[i] >= ResultList.MIN_SCORE) {
				temp += results[i];
			}
		}
		return temp;
	}
	
	/** returnerar en vektor med resultatet på varje varv */
	public int[] getResultList() {
		return results;
	}
	
	/** returnerar antal varv tävlingen är på */
	public int getRounds() {
		return rounds;
	}
	
	/** returnerar prioriteten */
	public int getPrio() {
		return prio;
	}
	
	/** returnerar antal avklarade varv */
	public int getNbrRoundsFinished() {
		return nbrRoundsFinished;
	}
	
	/** returnerar antal spelade varv */
	public int getNbrRoundsPlayed() {
		int nbrRoundsPlayed = nbrRoundsFinished;
		for(int i = 0; i < nbrRoundsFinished; i++) {
			if(results[i] > ResultList.MAX_SCORE || results[i] < ResultList.MIN_SCORE) {
				nbrRoundsPlayed--;
			}
		}
		return nbrRoundsPlayed;
	}
	
	/** returnerar numret för att identifiera personen */
	public int getPersonID() {
		return personID;
	}
	
	/** returnerar skillnaden mellan högsta och lägsta varv */
	public int getDiff() {
		return diff;
	}

	/** räknar ut skillnaden mellan högsta och lägsta varv */
	private void calculateDiff() {
		if (getNbrRoundsPlayed() == 0) {
			diff = 0;
		} else {
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			for(int i = 0; i < results.length; i++) {
				int value = results[i];
				if(value <= ResultList.MAX_SCORE && value >= ResultList.MIN_SCORE) {
				    if(value > max) {
				        max = value;
				    }
				    if(value < min) {
				        min = value;
				    }
				}
			}
			diff = max - min;
		}
	}
	
	/** ändrar persondatan */
	public void changeResults(int startNbr, String name, String club, String licenseNbr, 
	        int[] results, String klass, int prio, int nbrRoundsFinished) {
		this.startNbr = startNbr;
		this.name = name;
		this.club = club;
		this.licenseNbr = licenseNbr;
		this.results = results;
		this.klass = klass;
		this.prio = prio;
		this.nbrRoundsFinished = nbrRoundsFinished;
		calculateDiff();
	}
	
	/** ändrar personens namn och klubb */
	public void changeNameAndClub(String name, String club) {
		this.name = name;
		this.club = club;
	}
}