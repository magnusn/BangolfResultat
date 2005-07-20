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
    private String nameAndClub;			// namn och klubb
    private Integer identity;			// ID-nummer f�r personen
    
    /** skapar ett objekt med ID-numret identity, namn och klubb enligt nameAndClub
     *  snitt efter mean och dessutom snittet som str�ng i form av meanAsString
     * @param identity - ID-nummer
     * @param nameAndClub - namn och klubb
     * @param mean - snitt
     * @param meanAsString - snitt som str�ng
     */
    public PersonMean(Integer identity, String nameAndClub, double mean, String meanAsString) {
        this.identity = identity;
        this.nameAndClub = nameAndClub;
        this.mean = mean;
        this.meanAsString = meanAsString;
    }
    
    /** uppdaterar personens data
     * @param nameAndClub - namn och klubb
     * @param mean - snitt
     * @param meanAsString - snitt som str�ng
     */
    public void updateData(String nameAndClub, double mean, String meanAsString) {
        this.nameAndClub = nameAndClub;
        this.mean = mean;
        this.meanAsString = meanAsString;
    }
    
    /** returnerar personens namn och klubb
     * @return - str�ng med "namn, klubb"
     */
    public String getNameAndClub() {
        return nameAndClub;
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
}