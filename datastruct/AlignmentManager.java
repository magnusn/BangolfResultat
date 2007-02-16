/*
 * Created on 2005-jul-18
 * 
 * Created by: Magnus
 */
package datastruct;

import gui.AlignmentWindow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * AlignmentManager - h�ller reda p� olika inst�llningar
 */
public class AlignmentManager {
    private static int[] orientation;	// h�ller reda p� sifferorienteringen
    
    /** skapar en klass f�r att h�lla reda p� inst�llningar */
    public AlignmentManager() {
        orientation = new int[AlignmentWindow.NBR_OWNERS];
        for(int i = 0; i < orientation.length; i++) {
            orientation[i] = AlignmentWindow.RIGHT;
        }
    }
    
    /** laddar inst�llingarna f�r sifferorienteringen
     * 	@return true om inladdningen lyckas, annars false
     */
    public boolean loadOrientation() {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader("data/orientation"));
            String inLine = fileIn.readLine();
            int i = 0;
            while(inLine != null) {
                orientation[i] = Integer.parseInt(inLine);
                i++;
                inLine = fileIn.readLine();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /** sparar inst�llingarna f�r sifferorienteringen 
     * 	@return true om sparandet lyckas, annars false
     */
    public boolean saveOrientation() {
        try {
            BufferedWriter bufferOut = new BufferedWriter(new FileWriter("data/orientation"));
            for(int i = 0; i < orientation.length; i++) {
                bufferOut.write(String.valueOf(orientation[i]));
                bufferOut.newLine();
            }
            bufferOut.flush();
            bufferOut.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    /** st�ller in sifferorienteringen
     * 	@param alignment - talar om vilken sifferjustering som g�ller
     * 	@param nbrOfOwner - anger f�r vilken typ av resultat inst�llningen g�ller
     */
    public void setOrientation(int alignment, int nbrOfOwner) {
        orientation[nbrOfOwner] = alignment;
    }
    
    /** returnerar sifferorienteringen
     * 	@param nbrOfOwner - anger vilken typ av resultat inst�llningen g�ller
     * 	@return	hur justeringen skall vara
     */
    public static int getOrientation(int nbrOfOwner) {
        return orientation[nbrOfOwner];
    }
}