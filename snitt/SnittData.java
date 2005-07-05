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
 * SnittData - beskriver inst�llningarna f�r snittlistorna
 */
public class SnittData {
    private IOHandler io;			// sk�ter om skrivning till fil och l�sning fr�n fil
    private JCheckBox[][] headers;	// talar om vad som skall visas i snittlistan
    private String[] files;			// inneh�ller adresser till j�mf�relsefilerna
    private int nbrTabs;			// anger antal olika snittlistor
    protected static final int NBR_HEADERS = 8;	// antal m�jliga rubriker
    
    /** skapar en klass f�r snittlistornas inst�llningar */
    protected SnittData(int nbrTabs) {
        this.nbrTabs = nbrTabs;
        io = new IOHandler();
    }
    
    /** l�ser in inst�llningarna f�r hur snittlistorna skall se ut */
    protected boolean readAppearanceSettings() {
        try {
            headers = (JCheckBox[][]) io.load("snittapp");
            if(headers.length != nbrTabs) {
                JCheckBox[][] tempHeaders = new JCheckBox[nbrTabs][NBR_HEADERS];
                for(int i = 0; i < nbrTabs; i++) {
                    tempHeaders[i][Snitt.NAME] = new JCheckBox("Namn", true);
                    tempHeaders[i][Snitt.NAME].setEnabled(false);
                    tempHeaders[i][Snitt.CLUB] = new JCheckBox("Klubb", true);
                    tempHeaders[i][Snitt.COMPS] = new JCheckBox("T�vlingar", true);
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
                headers[i][Snitt.COMPS] = new JCheckBox("T�vlingar", true);
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
    
    /** sparar utseendeinst�llningarna */
    protected boolean saveAppearanceSettings() {
        try {
            io.save("snittapp", headers);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    /** l�ser in adresserna f�r j�mf�relsefilerna */
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
    
    /** sparar adresserna f�r j�mf�relsefilerna */
    protected boolean saveCompareFiles() {
        try {
	        io.save("comparefiles", files);
	        return true;
	    } catch (IOException ex) {
	        return false;
	    }
    }
    
    /** returnerar rubrikinst�llningarna f�r snittlistefliken tabIndex */
    protected JCheckBox[] getAppearanceHeaders(int tabIndex) {
        return headers[tabIndex];
    }
    
    /** s�tter rubrikinst�llningarna headerSettings f�r snittlistefliken tabIndex */
    protected void setAppearanceHeaders(JCheckBox[] headerSettings, int tabIndex) {
        headers[tabIndex] = headerSettings;
    }
    
    /** returnerar adressen till j�mf�relsefilen f�r snittlistefliken tabIndex */
    protected String getCompareFile(int tabIndex) {
        if(files[tabIndex].equals("") || files[tabIndex] == null) {
            return null;
        } else {
            return files[tabIndex];
        }
    }
    
    /** s�tter adressen fileName f�r j�mf�relsefilen till snittlisefliken tabIndex */
    protected void setCompareFile(String fileName, int tabIndex) {
        if(!fileName.equals(files[tabIndex])) {
            files[tabIndex] = fileName;
            initAppearanceHeaders(tabIndex);
        }
    }
    
    /** st�ller automatiskt in utseendet efter att j�mf�relsefilen har �ndrats */
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
            headers[tabIndex][Snitt.EX_MEAN].setToolTipText("Aktiveras f�rst d� j�mf�relsefil �r vald");
            headers[tabIndex][Snitt.CHANGE].setToolTipText("Aktiveras f�rst d� j�mf�relsefil �r vald");
        }
    }
    
    /** returnerar v�rdet value med tv� decimaler i form av en str�ng, 
	 *  heltal anger heltalssiffrorna, decimaltal anger decimaltalssiffrorna
	 *  och om dot �r true blir det en punkt emellan annars kommatecken */
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