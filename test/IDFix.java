package test;

import java.util.*;

import se.manet.bangolfresultat.datastruct.IOHandler;


class IDFix {
	
	public static void main(String[] args) {
		IOHandler io = new IOHandler();
		HashMap personTracker, personNameTracker;
		try {
			personTracker = (HashMap) io.load("ptrack");
		} catch (Exception e) {
			personTracker = new HashMap();
			personTracker.put(" ", new Integer(0));
		}
		try {
			personNameTracker = (HashMap) io.load("pnametrack");
		} catch (Exception e) {
			personNameTracker = new HashMap();
			Set set = personTracker.entrySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				personNameTracker.put(entry.getValue(), entry.getKey());
			}
			try {
				io.save("pnametrack", personNameTracker);
				System.out.println("Färdig!");
			} catch (Exception f) {
				System.out.println("Följande fel uppstod: " + f);
			}
		}
	}
}
