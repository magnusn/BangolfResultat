/*
 * Created on 2005-jul-20
 * 
 * Created by: Magnus
 */
package datastruct;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import snitt.SnittData;


/**
 * CompareFile - håller reda på en jämförelsesnittlista
 */
public class CompareFile {
    private HashMap map;							// innehåller resultaten
    private int surface;							// underlaget
    public static final int SURFACE_NOT_SET = -1;	// heltalsvärde för underlaget innan det är satt
    
    /** skapar en ny jämförelsesnittlista */
    public CompareFile() {
        surface = SURFACE_NOT_SET;
        map = new HashMap();
    }
    
    /** returnerar antal registrerade snitt */
    public int size() {
        return map.size();
    }
    
    /** lägger in snittet mean för personen med ID-nummer identity */
    public void addMean(Integer identity, String name, String club, double mean) {
        StringTokenizer str = new StringTokenizer(String.valueOf(mean), ".");
        String heltal = str.nextToken();
        String decimaltal = str.nextToken();
        String meanAsString = SnittData.getValueWithTwoDecimals(mean, heltal, decimaltal, true);
        String meanWithComma = SnittData.getValueWithTwoDecimals(mean, heltal, decimaltal, false);
        mean = Double.parseDouble(meanAsString);
        if(map.containsKey(identity)) {
            PersonMean personMean = (PersonMean) map.get(identity);
            personMean.updateData(name, club, mean, meanAsString, meanWithComma);
        } else {
            map.put(identity, new PersonMean(identity, name, club, mean, meanAsString, meanWithComma));
        }
    }
    
    /** tar bort snittet för personen med ID-nummer identity */
    public boolean removeMean(Integer identity) {
        if(map.remove(identity) != null) {
            return true;
        } else {
            return false;
        }
    }
    
    /** returnerar personen med ID-nummer identity, null om personen inte finns med */
    public PersonMean getPerson(Integer identity) {
        return (PersonMean) map.get(identity);
    }
    
    /** ställer in underlaget */
    public void setSurface(int surface) {
        this.surface = surface;
    }
    
    /** returnerar underlaget */
    public int getSurface() {
        return surface;
    }
    
    /** sorterar och returnerar därefter resultaten som en länkad lista */
	public LinkedList sortResults() {
		LinkedList list = new LinkedList();
		MeanComparator comparator = new MeanComparator();
		list.addAll(map.values());
		Collections.sort(list, comparator);
		return list;
	}
	
	/** returnerar en strängmatris med vad som skall visas */
	public String[][][] getOutput() {
	    LinkedList list = sortResults();
	    String[][][] data = new String[2][][];
	    String[][] output = new String[list.size()][2];
	    String[][] outputColor = new String[list.size()][2];
	    for(int i = 0; i < list.size(); i++) {
	        PersonMean personMean = (PersonMean) list.get(i);
	        output[i][0] = personMean.getNameAndClub();
	        output[i][1] = personMean.getMeanWithComma();
	        outputColor[i][1] = getColor(personMean.getMean());
	    }
	    data[0] = output;
	    data[1] = outputColor;
	    return data;
	}
	
	/** returnerar en sträng som talar om vilken färg snittet skall ha,
	 	man ger in snittet mean */
	private String getColor(double mean) {
	    String color;
	    if(surface == ResultList.FILT) {
	        if(mean < 29.99500) {
	            color = "blue";
	        } else if(mean < 35.99500) {
	            color = "green";
	        } else if(mean < 39.99500) {
	            color = "red";
	        } else {
	            color = "black";
	        }
	    } else if(surface == ResultList.EB) {
	        if(mean < 19.99500) {
	            color = "blue";
	        } else if(mean < 24.99500) {
	            color = "green";
	        } else if(mean < 29.99500) {
	            color = "red";
	        } else {
	            color = "black";
	        }
	    } else if(surface == ResultList.BETONG) {
	        if(mean < 24.99500) {
	            color = "blue";
	        } else if(mean < 29.99500) {
	            color = "green";
	        } else if(mean < 35.99500) {
	            color = "red";
	        } else {
	            color = "black";
	        }
	    } else {
	        color = "black";
	    }
	    return color;
	}
    
    /** klassen som sköter sorteringen av snitten */
	class MeanComparator implements Comparator {
		/**	bestämmer skillnaden mellan lhs och rhs genom att sortera i ordningen
		 *	snitt och därefter namn
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