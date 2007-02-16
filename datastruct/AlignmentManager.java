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
 * AlignmentManager - håller reda på olika inställningar
 */
public class AlignmentManager {
    private static int[] orientation;	// håller reda på sifferorienteringen
    
    /** skapar en klass för att hålla reda på inställningar */
    public AlignmentManager() {
        orientation = new int[AlignmentWindow.NBR_OWNERS];
        for(int i = 0; i < orientation.length; i++) {
            orientation[i] = AlignmentWindow.RIGHT;
        }
    }
    
    /** laddar inställingarna för sifferorienteringen
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
    
    /** sparar inställingarna för sifferorienteringen 
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
    
    
    /** ställer in sifferorienteringen
     * 	@param alignment - talar om vilken sifferjustering som gäller
     * 	@param nbrOfOwner - anger för vilken typ av resultat inställningen gäller
     */
    public void setOrientation(int alignment, int nbrOfOwner) {
        orientation[nbrOfOwner] = alignment;
    }
    
    /** returnerar sifferorienteringen
     * 	@param nbrOfOwner - anger vilken typ av resultat inställningen gäller
     * 	@return	hur justeringen skall vara
     */
    public static int getOrientation(int nbrOfOwner) {
        return orientation[nbrOfOwner];
    }
}