/** klassen som beskriver varje persons resultat */
class PersonResult {
	private String name, club, klass;		// personens namn, klubb och klass
	private String licenseNbr;				// personens licensnummer
	private int startNbr, rounds; 			// startnummer, idnummer och t�vlingens antal varv
	private int prio, nbrRoundsFinished; 	// prioritet vid samma antal slag, antal f�rdigspelade varv
	private int max, min;					// h�gsta och l�gsta varv
	private int personID;					// nummer f�r identifiering av personen
	private int[] results;					// vektor med resultaten p� varje varv
	
	/** skapar ett resultat till personen med namnet name och klubben club med startnumret startNbr och licensnumret idNbr
			results �r resultaten p� varje varv, rounds antal varv, klass klassen, prio prioriteten
			och nbrRoundsFinished antal varv som �r slutf�rda */
	public PersonResult(int startNbr, String name, String club, String idNbr, int[] results, 
						int rounds, String klass, int prio, int nbrRoundsFinished, int personID) {
		min = Integer.MAX_VALUE;
		max = Integer.MIN_VALUE;
		this.startNbr = startNbr;
		this.name = name;
		this.club = club;
		this.licenseNbr = idNbr;
		this.results = results;
		setMinMax();
		this.rounds = rounds;
		this.klass = klass;
		this.prio = prio;
		this.nbrRoundsFinished = nbrRoundsFinished;
		this.personID = personID;
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
		return klass;
	}
	
	/** returnerar slagsumman till och med varvnumret untilRound */
	public int getSum(int untilRound) {
		int temp = 0;
		for(int i = 0; i < untilRound; i++) {
			if(results[i] < ResultList.MAX_SCORE && results[i] > ResultList.MIN_SCORE) {
				temp += results[i];
			}
		}
		return temp;
	}
	
	/** returnerar en vektor med resultatet p� varje varv */
	public int[] getResultList() {
		return results;
	}
	
	/** returnerar antal varv t�vlingen �r p� */
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
		for(int i = 0; i < results.length; i++) {
			if(results[i] < ResultList.MAX_SCORE && results[i] > ResultList.MIN_SCORE) {
				nbrRoundsPlayed--;
			}
		}
		return nbrRoundsPlayed;
	}
	
	/** returnerar numret f�r att identifiera personen */
	public int getPersonID() {
		return personID;
	}
	
	/** returnerar skillnaden mellan h�gsta och l�gsta varv */
	public int getDiff() {
		return max - min;
	}
	
	/** best�mmer h�gsta och l�gsta varv */
	private void setMinMax() {
		for(int i = 0; i < results.length; i++) {
			int value = results[i];
			if(value > max) {
				max = value;
			}
			if(value < min) {
				min = value;
			}
		}
	}
	
	/** �ndrar persondatan */
	public void changeResults(int startNbr, String name, String club, String licenseNbr, int[] results, String klass, int prio, int nbrRoundsFinished) {
		this.startNbr = startNbr;
		this.name = name;
		this.club = club;
		this.licenseNbr = licenseNbr;
		this.results = results;
		setMinMax();
		this.klass = klass;
		this.prio = prio;
		this.nbrRoundsFinished = nbrRoundsFinished;
	}
	
	/** �ndrar personens namn och klubb */
	public void changeNameAndClub(String name, String club) {
		this.name = name;
		this.club = club;
	}
}