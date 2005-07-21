/*
 * Created on 2005-jul-20
 * 
 * Created by: Magnus
 */
package datastruct;

/**
 * PersonMean - innehåller snittresultat för en person
 */
public class PersonMean {
    private double mean;				// snittet som double
    private String meanAsString;		// snittet i form av en textsträng
    private String meanWithComma;		// snitt som sträng med kommatecken
    private String name;				// personens namn
    private String club;				// klubben som personen spelar för
    private Integer identity;			// ID-nummer för personen
    
    /** skapar ett objekt med ID-numret identity, namn och klubb enligt nameAndClub
     *  snitt efter mean och dessutom snittet som sträng i form av meanAsString
     * @param identity - ID-nummer
     * @param nameAndClub - namn och klubb
     * @param mean - snitt
     * @param meanAsString - snitt som sträng
     */
    public PersonMean(Integer identity, String name, String club, double mean,
            String meanAsString, String meanWithComma) {
        this.identity = identity;
        this.name = name;
        this.club = club;
        this.mean = mean;
        this.meanAsString = meanAsString;
        this.meanWithComma = meanWithComma;
    }
    
    /** uppdaterar personens data
     * @param nameAndClub - namn och klubb
     * @param mean - snitt
     * @param meanAsString - snitt som sträng
     */
    public void updateData(String name, String club, double mean, String meanAsString, String meanWithComma) {
        this.name = name;
        this.club = club;
        this.mean = mean;
        this.meanAsString = meanAsString;
        this.meanWithComma = meanWithComma;
    }
    
    /** returnerar personens ID-nummer
     * @return - ID-numret som en Integer
     */
    public Integer getID() {
        return identity;
    }
    
    /** returnerar personens namn
     * @return - personens namn som textsträng
     */
    public String getName() {
        return name;
    }
    
    /** returnerar personens klubb
     * @return - klubben som personen spelar för
     */
    public String getClub() {
        return club;
    }
    
    /** returnerar personens namn och klubb
     * @return - sträng med "namn, klubb"
     */
    public String getNameAndClub() {
        return name + ", " + club;
    }
    
    /** returnerar snittet
     * @return - personens snitt som en double
     */
    public double getMean() {
        return mean;
    }
    
    /** returnerar snittet som en textsträng
     * @return - snitt i form av textsträng
     */
    public String getMeanAsString() {
        return meanAsString;
    }
    
    /** returnerar snittet som en sträng med kommatecken
     * @return - snitt som textsträng med kommatecken
     */
    public String getMeanWithComma() {
        return meanWithComma;
    }
}