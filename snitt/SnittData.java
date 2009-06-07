/*
 * Created on 2005-jul-05
 * 
 * Created by: Magnus
 */
package snitt;

import java.io.IOException;

import javax.swing.JCheckBox;

import datastruct.IOHandler;
import datastruct.Person;

/**
 * SnittData - beskriver inställningarna för snittlistorna
 */
public class SnittData {
    private IOHandler io;			// sköter om skrivning till fil och läsning från fil
    private JCheckBox[][] headers;	// talar om vad som skall visas i snittlistan
    private String[] files;			// innehåller adresser till jämförelsefilerna
    private int nbrTabs;			// anger antal olika snittlistor
    protected static final int NBR_HEADERS = 9;	// antal möjliga rubriker
    
    /** skapar en klass för snittlistornas inställningar */
    protected SnittData(int nbrTabs) {
        this.nbrTabs = nbrTabs;
        io = new IOHandler();
        files = new String[nbrTabs];
    }
    
    /** läser in inställningarna för hur snittlistorna skall se ut */
    protected boolean readAppearanceSettings() {
        boolean success = false;
        try {
            headers = (JCheckBox[][]) io.load("snittapp");
            if(headers.length != nbrTabs || headers[0].length != NBR_HEADERS) {
                JCheckBox[][] tempHeaders = new JCheckBox[nbrTabs][NBR_HEADERS];
                for(int i = 0; i < nbrTabs; i++) {
                	if (headers[0].length == tempHeaders[0].length && headers.length > i) {
                		tempHeaders[i] = headers[i];
                	} else {
                		tempHeaders[i] = getStandardHeaders(i);
                	}
                }
                headers = tempHeaders;
            }
            success = true;
        } catch (Exception e) {
            success = false;
            headers = new JCheckBox[nbrTabs][NBR_HEADERS];
            for(int i = 0; i < nbrTabs; i++) {
                headers[i] = getStandardHeaders(i);
            }
        }
        for(int i = 0; i < headers.length; i++) {
            setOldMeanHeaders(i, false);
        }
        return success;
    }
    
    /** returnerar originalinställningarna för utseende gällande flik tabIndex */
    private JCheckBox[] getStandardHeaders(int tabIndex) {
        JCheckBox[] standardHeaders = new JCheckBox[NBR_HEADERS];
        standardHeaders[Snitt.KLASS] = new JCheckBox("Klass", false);
        standardHeaders[Snitt.NAME] = new JCheckBox("Namn", true);
        standardHeaders[Snitt.NAME].setEnabled(false);
        standardHeaders[Snitt.NAME].setToolTipText("Visas alltid");
        standardHeaders[Snitt.CLUB] = new JCheckBox("Klubb", true);
        standardHeaders[Snitt.COMPS] = new JCheckBox("Tävlingar", true);
        standardHeaders[Snitt.ROUNDS] = new JCheckBox("Varv", true);
        standardHeaders[Snitt.HITSUM] = new JCheckBox("Slag", true);
        standardHeaders[Snitt.MEAN] = new JCheckBox("Snitt", true);
        standardHeaders[Snitt.MEAN].setEnabled(false);
        standardHeaders[Snitt.MEAN].setToolTipText("Visas alltid");
        standardHeaders[Snitt.EX_MEAN] = new JCheckBox("Snitt ifjol", true);
        standardHeaders[Snitt.CHANGE] = new JCheckBox("+/-", true);
        return standardHeaders;
    }
    
    /** sparar utseendeinställningarna */
    protected boolean saveAppearanceSettings() {
        try {
            io.save("snittapp", headers);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    /** läser in adresserna för jämförelsefilerna */
    protected boolean readCompareFiles() {
        try {
            files = (String[]) io.load("comparefiles");
            if(files.length != nbrTabs) {
                String[] tempFiles = new String[nbrTabs];
                for(int i = 0; i < nbrTabs; i++) {
                	if (files.length > i) {
                		tempFiles[i] = files[i];
                	} else {
                		tempFiles[i] = new String();
                	}
                }
                files = tempFiles;
            }
            return true;
        } catch (Exception e) {
            files = new String[nbrTabs];
            for(int i = 0; i < nbrTabs; i++) {
                files[i] = new String();
            }
        }
        return false;
    }
    
    /** sparar adresserna för jämförelsefilerna */
    protected boolean saveCompareFiles() {
        try {
	        io.save("comparefiles", files);
	        return true;
	    } catch (IOException ex) {
	        return false;
	    }
    }
    
    /** returnerar rubrikinställningarna för snittlistefliken tabIndex */
    protected JCheckBox[] getAppearanceHeaders(int tabIndex) {
        return headers[tabIndex];
    }
    
    /** sätter rubrikinställningarna headerSettings för snittlistefliken tabIndex */
    protected void setAppearanceHeaders(JCheckBox[] headerSettings, int tabIndex) {
        headers[tabIndex] = headerSettings;
    }
    
    /** returnerar adressen till jämförelsefilen för snittlistefliken tabIndex */
    protected String getCompareFile(int tabIndex) {
        if(files[tabIndex].equals("") || files[tabIndex] == null) {
            return null;
        } else {
            return files[tabIndex];
        }
    }
    
    /** sätter adressen fileName för jämförelsefilen till snittlistefliken tabIndex */
    protected void setCompareFile(String fileName, int tabIndex) {
        if(!fileName.equals(files[tabIndex])) {
            files[tabIndex] = fileName;
            setOldMeanHeaders(tabIndex, true);
        }
    }
    
    /** ställer in utseendealternativen <EM>Snitt ifjol</EM> och <EM>+/-</EM>
     *  efter om någon jämförelsefil är vald
     * 	@param tabIndex - snittlistefliken som inställningen görs för
     * 	@param compareFileChange - true om jämförelsefilen har ändrats och det är detta som föranleder
     * 	en ändring i utseendeinställningarna för <EM>Snitt ifjol</EM> och <EM>+/-</EM>
     */
    private void setOldMeanHeaders(int tabIndex, boolean compareFileChange) {
        if(getCompareFile(tabIndex) == null) {
            headers[tabIndex][Snitt.EX_MEAN].setEnabled(false);
            headers[tabIndex][Snitt.CHANGE].setEnabled(false);
            headers[tabIndex][Snitt.EX_MEAN].setSelected(false);
            headers[tabIndex][Snitt.CHANGE].setSelected(false);
            headers[tabIndex][Snitt.EX_MEAN].setToolTipText("Aktiveras först då jämförelsefil är vald");
            headers[tabIndex][Snitt.CHANGE].setToolTipText("Aktiveras först då jämförelsefil är vald");
        } else {
            headers[tabIndex][Snitt.EX_MEAN].setEnabled(true);
            headers[tabIndex][Snitt.CHANGE].setEnabled(true);
            headers[tabIndex][Snitt.EX_MEAN].setToolTipText(null);
            headers[tabIndex][Snitt.CHANGE].setToolTipText(null);
            if(compareFileChange) {
                headers[tabIndex][Snitt.EX_MEAN].setSelected(true);
                headers[tabIndex][Snitt.CHANGE].setSelected(true);
            }
        }
    }
    
    /** returnerar värdet value med två decimaler i form av en sträng, 
	 *  heltal anger heltalssiffrorna, decimaltal anger decimaltalssiffrorna
	 *  och om dot är true blir det en punkt emellan annars kommatecken */
	public static String getValueWithTwoDecimals(double value, String heltal, String decimaltal, boolean dot) {
	    String valueAsString;
	    String separator;
	    if(dot) {
	        separator = ".";
	    } else {
	        separator = ",";
	    }
		if(value != Person.NO_VALUE) {
			if(decimaltal.length() > 2) {
				if(Integer.parseInt(decimaltal.substring(2,3)) >= 5) {
					if(Integer.parseInt(decimaltal.substring(0,2)) == 99) {
						valueAsString = (Integer.parseInt(heltal) + 1) + separator + "00";
					} else if(Integer.parseInt(decimaltal.substring(1,2)) == 9) {
						valueAsString = heltal + separator + (Integer.parseInt(decimaltal.substring(0,1)) + 1) + "0";
					} else {
						valueAsString = heltal + separator + decimaltal.substring(0,1) + (Integer.parseInt(decimaltal.substring(1,2))+1);
					}
				} else {
					valueAsString = heltal + separator + decimaltal.substring(0,2);
				}
			} else if(decimaltal.length() > 1) {
				valueAsString = heltal + separator + decimaltal.substring(0,2);
			} else {
				valueAsString = heltal + separator + decimaltal.substring(0,1) + "0";
			}
		} else {
			valueAsString = "-";
		}
		return valueAsString;
	}
}