/*
 * Created on 2005-jul-20
 * 
 * Created by: Magnus
 */
package se.manet.bangolfresultat.datastruct;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import se.manet.bangolfresultat.snitt.SnittData;


/**
 * CompareFile - håller reda på en jämförelsesnittlista
 */
public class CompareFile {
    private HashMap map;	// innehåller resultaten
    private int surface;	// underlaget
    
    /** skapar en ny jämförelsesnittlista */
    public CompareFile(int surface) {
        this.surface = surface;
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
	    String[][] output = new String[list.size() + 1][3];
	    String[][] outputColor = new String[list.size() + 1][3];
	    output[0][0] = "Namn";
        output[0][1] = "Klubb";
        output[0][2] = "Snitt";
	    for(int i = 1; i <= list.size(); i++) {
	        PersonMean personMean = (PersonMean) list.get(i-1);
	        output[i][0] = personMean.getName();
	        output[i][1] = personMean.getClub();
	        output[i][2] = personMean.getMeanWithComma();
	        outputColor[i][2] = getColor(personMean.getMean());
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
		 *	snitt, namn och därefter klubb
		 *	@see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object lhs, Object rhs) {
			PersonMean left = (PersonMean) lhs;
			PersonMean right = (PersonMean) rhs;
			int meanLeft = (int) (left.getMean() * 1000);
			int meanRight = (int) (right.getMean() * 1000);
			String nameLeft = left.getName();
			String nameRight = right.getName();
			String clubLeft = left.getClub();
			String clubRight = right.getClub();
			
			if (meanLeft == meanRight) {
				if (nameLeft.equals(nameRight)) {
				    if(clubLeft.equals(clubRight)) {
				        return 0;
				    } else {
				        return clubLeft.compareTo(clubRight);
				    }
				} else {
				    return nameLeft.compareTo(nameRight);
				}
			} else {
				return meanLeft - meanRight;
			}
		}
	}
}