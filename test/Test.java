package test;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import datastruct.IOHandler;

class Test extends Test2 {
    
    public Test() {
        JFrame frame = new JFrame("Test");
	    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    frame.addWindowListener(new WindowHandler());
	    frame.getContentPane().add(panel);
	    frame.pack();
	    frame.setVisible(true);
    }
    
    public Test(boolean dummy) throws Exception {
        if(dummy) {
            Object vector = new int[8];
            Object matrix = new int[8][8];
            System.out.println("Vektor är instans av int[]: " + (vector instanceof int[]));
            System.out.println("Matris är instans av int[]: " + (matrix instanceof int[]));
        } else {
            IOHandler io = new IOHandler();
            io.load("dummy");
        }
    }
    
    public void show() {
        JOptionPane.showMessageDialog(panel, "Heffaklump");
    }
    
	public static void main(String[] args) {
	    try {
	        Test test = new Test(false);
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Ett fel i programmet har uppstått. " +
	        		"Informationen om detta sparas i filen error.log.\n" +
	        		"Denna fil finns i katalogen där programmet installerades, " +
	        		"vanligtvis C:\\Program\\BangolfResultat.\n\n" +
	        		"Var vänlig spara aktivt arbete och starta om programmet.\n" +
	        		"Om problem uppstår igen tag kontakt med programmets tillverkare.");
            LinkedList list = new LinkedList();
	        try {
	            BufferedReader fileIn = new BufferedReader(new FileReader("error.log"));
	            String inLine = fileIn.readLine();
	            
	            while(inLine != null) {
	                list.add(inLine);
	                inLine = fileIn.readLine();
	            }
	        } catch (Exception ex) {
	        }
	        
	        try {
	            PrintStream printStream = new PrintStream("error.log");
	            for(int i = 0; i < list.size(); i++) {
	                printStream.println((String)list.get(i));
	            }
		        e.printStackTrace(printStream);
		        printStream.println();
		        printStream.flush();
		        printStream.close();
	        } catch (Exception exc) {
	            JOptionPane.showMessageDialog(null, "Loggningen av felet misslyckades");
	        }
	    }
	}
	
	/** klassen som sköter fönsterhanteringen i huvudfönstret */
    class WindowHandler extends WindowAdapter {
    	/** stänger ned fönstret */
    	public void windowClosing(WindowEvent e) {
    		System.exit(0);
    	}
    }
}