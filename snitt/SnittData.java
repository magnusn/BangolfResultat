/*
 * Created on 2005-jul-05
 * 
 * Created by: Magnus
 */
package snitt;

import java.io.IOException;

import javax.swing.JCheckBox;

import datastruct.IOHandler;

/**
 * SnittData - beskriver inställningarna för snittlistorna
 */
public class SnittData {
    private IOHandler io;			// sköter om skrivning till fil och läsning från fil
    private JCheckBox[][] headers;	// talar om vad som skall visas i snittlistan
    private String[] files;			// innehåller adresser till jämförelsefilerna
    private int nbrTabs;			// anger antal olika snittlistor
    private int nbrHeaders = 8;		// antal möjliga rubriker
    
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
                JCheckBox[][] tempHeaders = new JCheckBox[nbrTabs][nbrHeaders];
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
                    tempHeaders[i][Snitt.CHANGE] = new JCheckBox("Förändring");
                }
                for(int i = 0; i < headers.length; i++) {
                    tempHeaders[i] = headers[i];
                }
                headers = tempHeaders;
            }
            return true;
        } catch (Exception e) {
            headers = new JCheckBox[nbrTabs][nbrHeaders];
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
                headers[i][Snitt.CHANGE] = new JCheckBox("Förändring");
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
        files[tabIndex] = fileName;
    }
    
    /** returnerar antal möjliga rubriker */
    protected int getNbrHeaders() {
        return nbrHeaders;
    }
}