import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

/** klassen som beskriver namnlistan */
class NameList {
	private HashMap map;		// i denna hashmap lagras namnlistan
	private LinkedList[] names; // vektor för sorterade namn
	
	/** skapar en namnlista */
	public NameList() {
		map = new HashMap();
	}
	
	/** lägger till en person med namnet name och klubben club i listan, 
			returnerar true om personen inte finns i listan och annars false */
	public boolean add(String name, String club) {
		String identity = name+", "+club;
		if(!map.containsKey(identity)) {
			map.put(identity, identity);
			return true;
		} else {
			return false;
		}
	}
	
	/** tar bort en person med namnet name och klubben club från listan,
			returnerar true om personen finns i listan och annars false */
	public boolean remove(String name, String club) {
		String identity = name+", "+club;
		if(map.containsKey(identity)) {
			map.remove(identity);
			return true;
		} else {
			return false;
		}
	}
	
	/** läser in personer till listan från en fil */
	public void readNames() throws FileNotFoundException, IOException {
		BufferedReader fileIn = new BufferedReader(new FileReader("data/namn"));
		String inLine = fileIn.readLine();
		String name,club,identity;
		while (inLine != null) {
			StringTokenizer inString = new StringTokenizer(inLine, ",");
			name = inString.nextToken();
			club = inString.nextToken();
			identity = name+","+club;
			map.put(identity, identity);
			inLine = fileIn.readLine();
		}
		sortedNames();
	}
	
	/** skriver namnlistan till en fil */
	public void writeNames() throws IOException {
		BufferedWriter bufferOut = new BufferedWriter(new FileWriter("data/namn"));
		LinkedList list = sortNames();
		for(int i = 0; i < list.size()-1; i++) {
			bufferOut.write((String)list.get(i));
			bufferOut.newLine();
		}
		if(list.size()!=0) {
			bufferOut.write((String)list.get(list.size()-1));
		}
		bufferOut.flush();
		bufferOut.close();
	}
	
	/** returnerar personer som matchar strängen s */
	public String[] getNames(String s) {
		String[] namesFound = new String[10];
		if(!s.equals("")) {
			LinkedList list;
			int nbr = s.toLowerCase().charAt(0)-82;
			if(nbr >= 0 && nbr <=25) {
				list = names[nbr];
			}
			else {
				list = names[26];
			}
			int nbrFound = 0;
			int i = 0;
			while(nbrFound < 10 && i < list.size()) {
				String identity = (String)list.get(i);
				if(identity.toLowerCase().startsWith(s.toLowerCase())) {
					namesFound[nbrFound] = identity;
					nbrFound++;
				}
				i++;
			}
			while(nbrFound < 10) {
				namesFound[nbrFound] = null;
				nbrFound++;
			}
		}
		return namesFound;
	}
	
	/** returnerar en sorterad länkad lista */
	private LinkedList sortNames() {
		LinkedList list = new LinkedList();
		list.addAll(map.values());
		Collections.sort(list);
		return list;
	}
	
	/** sorterar personerna beroende på begynnelsebokstaven */
	public void sortedNames() {
		LinkedList list = sortNames();
		names = new LinkedList[27];
		for(int i = 0; i < 27; i++) {
			names[i] = new LinkedList();
		}
		for(int i = 0; i < list.size(); i++) {
			String name = (String)list.get(i);
			int nbr = name.toLowerCase().charAt(0) - 82;
			if(nbr >= 0 && nbr <= 25) {
				names[nbr].add(name);
			}
			else {
				names[26].add(name);
			}
		}
	}
}
