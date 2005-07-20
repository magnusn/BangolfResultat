/*
 * Created on 2005-jul-20
 * 
 * Created by: Magnus
 */
package snitt;

import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;

import datastruct.PersonMean;

/**
 * CompareFile - h�ller reda p� en j�mf�relsesnittlista
 */
public class CompareFile {
    private HashMap map;		// inneh�ller resultaten
    private int surface;		// underlaget
    
    /** skapar en ny j�mf�relsesnittlista */
    public CompareFile() {
        map = new HashMap();
    }
    
    /** l�gger in snittet mean f�r personen med ID-nummer identity */
    public void addMean(Integer identity, String nameAndClub, double mean) {
        StringTokenizer str = new StringTokenizer(String.valueOf(mean), ".");
        String heltal = str.nextToken();
        String decimaltal = str.nextToken();
        String meanAsString = SnittData.getValueWithTwoDecimals(mean, heltal, decimaltal, true);
        mean = Double.parseDouble(meanAsString);
        if(map.containsKey(identity)) {
            PersonMean personMean = (PersonMean) map.get(identity);
            personMean.updateData(nameAndClub, mean, meanAsString);
        } else {
            map.put(new PersonMean(identity, nameAndClub, mean, meanAsString), meanAsString);
        }
    }
    
    /** tar bort snittet f�r personen med ID-nummer identity */
    public boolean removeMean(Integer identity) {
        if(map.remove(identity) != null) {
            return true;
        } else {
            return false;
        }
    }
    
    /** st�ller in underlaget */
    public void setSurface(int surface) {
        this.surface = surface;
    }
    
    /** klassen som sk�ter sorteringen av snitten */
	class MeanComparator implements Comparator {
		/**	best�mmer skillnaden mellan lhs och rhs genom att sortera i ordningen
		 *	snitt och d�refter namn
		 *	@see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object lhs, Object rhs) {
			PersonMean left = (PersonMean) lhs;
			PersonMean right = (PersonMean) rhs;
			int meanLeft = (int) (left.getMean() * 1000);
			int meanRight = (int) (right.getMean() * 1000);
			String nameLeft = left.getNameAndClub();
			String nameRight = right.getNameAndClub();
			
			if (meanLeft == meanRight) {
				if (nameLeft.equals(nameRight)) {
				    return 0;
				} else {
				    return nameLeft.compareTo(nameRight);
				}
			} else {
				return meanLeft - meanRight;
			}
		}
	}
}
