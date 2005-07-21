/*
 * Created on 2005-jul-20
 * 
 * Created by: Magnus
 */
package datastruct;

/**
 * PersonMean - inneh�ller snittresultat f�r en person
 */
public class PersonMean {
    private double mean;				// snittet som double
    private String meanAsString;		// snittet i form av en textstr�ng
    private String meanWithComma;		// snitt som str�ng med kommatecken
    private String name;				// personens namn
    private String club;				// klubben som personen spelar f�r
    private Integer identity;			// ID-nummer f�r personen
    
    /** skapar ett objekt med ID-numret identity, namn och klubb enligt nameAndClub
     *  snitt efter mean och dessutom snittet som str�ng i form av meanAsString
     * @param identity - ID-nummer
     * @param nameAndClub - namn och klubb
     * @param mean - snitt
     * @param meanAsString - snitt som str�ng
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
     * @param meanAsString - snitt som str�ng
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
     * @return - personens namn som textstr�ng
     */
    public String getName() {
        return name;
    }
    
    /** returnerar personens klubb
     * @return - klubben som personen spelar f�r
     */
    public String getClub() {
        return club;
    }
    
    /** returnerar personens namn och klubb
     * @return - str�ng med "namn, klubb"
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
    
    /** returnerar snittet som en textstr�ng
     * @return - snitt i form av textstr�ng
     */
    public String getMeanAsString() {
        return meanAsString;
    }
    
    /** returnerar snittet som en str�ng med kommatecken
     * @return - snitt som textstr�ng med kommatecken
     */
    public String getMeanWithComma() {
        return meanWithComma;
    }
}