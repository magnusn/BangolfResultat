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
    protected static final int NBR_HEADERS = 8;	// antal möjliga rubriker
    
    /** skapar en klass för snittlistornas inställningar */
    protected SnittData(int nbrTabs) {
        this.nbrTabs = nbrTabs;
        io = new IOHandler();
    }
    
    /** läser in inställningarna för hur snittlistorna skall se ut */
    protected boolean readAppearanceSettings() {
        try {
            headers = (JCheckBox[][]) io.load("snittapp");
            if(headers.length != nbrTabs) {
                JCheckBox[][] tempHeaders = new JCheckBox[nbrTabs][NBR_HEADERS];
                for(int i = 0; i < nbrTabs; i++) {
                    tempHeaders[i][Snitt.NAME] = new JCheckBox("Namn", true);
                    tempHeaders[i][Snitt.NAME].setEnabled(false);
                    tempHeaders[i][Snitt.CLUB] = new JCheckBox("Klubb", true);
                    tempHeaders[i][Snitt.COMPS] = new JCheckBox("Tävlingar", true);
                    tempHeaders[i][Snitt.ROUNDS] = new JCheckBox("Varv", true);
                    tempHeaders[i][Snitt.HITSUM] = new JCheckBox("Slag", true);
                    tempHeaders[i][Snitt.MEAN] = new JCheckBox("Snitt", true);
                    tempHeaders[i][Snitt.MEAN].setEnabled(false);
                    tempHeaders[i][Snitt.EX_MEAN] = new JCheckBox("Snitt ifjol");
                    tempHeaders[i][Snitt.CHANGE] = new JCheckBox("+/-");
                }
                for(int i = 0; i < headers.length; i++) {
                    tempHeaders[i] = headers[i];
                }
                headers = tempHeaders;
            }
            return true;
        } catch (Exception e) {
            headers = new JCheckBox[nbrTabs][NBR_HEADERS];
            for(int i = 0; i < nbrTabs; i++) {
                headers[i][Snitt.NAME] = new JCheckBox("Namn", true);
                headers[i][Snitt.NAME].setEnabled(false);
                headers[i][Snitt.CLUB] = new JCheckBox("Klubb", true);
                headers[i][Snitt.COMPS] = new JCheckBox("Tävlingar", true);
                headers[i][Snitt.ROUNDS] = new JCheckBox("Varv", true);
                headers[i][Snitt.HITSUM] = new JCheckBox("Slag", true);
                headers[i][Snitt.MEAN] = new JCheckBox("Snitt", true);
                headers[i][Snitt.MEAN].setEnabled(false);
                headers[i][Snitt.EX_MEAN] = new JCheckBox("Snitt ifjol");
                headers[i][Snitt.CHANGE] = new JCheckBox("+/-");
            }
        }
        return false;
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
                    tempFiles[i] = new String();
                }
                for(int i = 0; i < files.length; i++) {
                    tempFiles[i] = files[i];
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
    
    /** sätter adressen fileName för jämförelsefilen till snittlisefliken tabIndex */
    protected void setCompareFile(String fileName, int tabIndex) {
        if(!fileName.equals(files[tabIndex])) {
            files[tabIndex] = fileName;
            initAppearanceHeaders(tabIndex);
        }
    }
    
    /** ställer automatiskt in utseendet efter att jämförelsefilen har ändrats */
    private void initAppearanceHeaders(int tabIndex) {
        if(getCompareFile(tabIndex) != null) {
            headers[tabIndex][Snitt.EX_MEAN].setEnabled(true);
            headers[tabIndex][Snitt.CHANGE].setEnabled(true);
            headers[tabIndex][Snitt.EX_MEAN].setSelected(true);
            headers[tabIndex][Snitt.CHANGE].setSelected(true);
            headers[tabIndex][Snitt.EX_MEAN].setToolTipText(null);
            headers[tabIndex][Snitt.CHANGE].setToolTipText(null);
        } else {
            headers[tabIndex][Snitt.EX_MEAN].setEnabled(false);
            headers[tabIndex][Snitt.CHANGE].setEnabled(false);
            headers[tabIndex][Snitt.EX_MEAN].setSelected(false);
            headers[tabIndex][Snitt.CHANGE].setSelected(false);
            headers[tabIndex][Snitt.EX_MEAN].setToolTipText("Aktiveras först då jämförelsefil är vald");
            headers[tabIndex][Snitt.CHANGE].setToolTipText("Aktiveras först då jämförelsefil är vald");
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