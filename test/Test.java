package test;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

class Test extends Test2 {
    
    public Test() {
        JFrame frame = new JFrame("Test");
	    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    frame.addWindowListener(new WindowHandler());
	    frame.getContentPane().add(panel);
	    frame.pack();
	    frame.setVisible(true);
    }
    
    public Test(boolean dummy) {
        Object vector = new int[8];
        Object matrix = new int[8][8];
        System.out.println("Vektor är instans av int[]: " + (vector instanceof int[]));
        System.out.println("Matris är instans av int[]: " + (matrix instanceof int[]));
    }
    
    public void show() {
        JOptionPane.showMessageDialog(panel, "Heffaklump");
    }
    
	public static void main(String[] args) {
	    Test test = new Test(true);    
	}
	
	/** klassen som sköter fönsterhanteringen i huvudfönstret */
    class WindowHandler extends WindowAdapter {
    	/** stänger ned fönstret */
    	public void windowClosing(WindowEvent e) {
    		System.exit(0);
    	}
    }
}