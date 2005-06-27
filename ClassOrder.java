import java.util.HashMap;

class ClassOrder {
	private HashMap map; // håller reda på klassernas ordning
	
	public ClassOrder() {
		map = new HashMap();
		setup();
	}
	
	private void setup() {
		String[] namn = new String[21];
		int i = 0;
		namn[i++] = "Damveteraner";
		namn[i++] = "Herrveteraner";
		namn[i++] = "Oldtimers Damer";
		namn[i++] = "Oldtimers Herrar";
		namn[i++] = "Oldgirls";
		namn[i++] = "Oldboys";
		namn[i++] = "Ungdom flickor C";
		namn[i++] = "Ungdom flickor B";
		namn[i++] = "Ungdom flickor A";
		namn[i++] = "Ungdom pojkar C";
		namn[i++] = "Ungdom pojkar B";
		namn[i++] = "Ungdom pojkar A";
		namn[i++] = "Ungdom";
		namn[i++] = "Damjuniorer";
		namn[i++] = "Herrjuniorer";
		namn[i++] = "Damseniorer";
		namn[i++] = "Herrseniorer";
		namn[i++] = "Klass D";
		namn[i++] = "Klass C";
		namn[i++] = "Klass B";
		namn[i++] = "Klass A";
		
		String s = " ";
		for(int j = 0; j < namn.length; j++) {
			map.put(namn[j], s);
			s = s + " ";
		}
	}
	
	public void save() {
		IOHandler io = new IOHandler();
		try {
			io.save("classorder", map);
		} catch (Exception e) {
			System.out.println("Sparandet av HashMap för klassordning misslyckades.");
		}
	}
	
	public static void main(String[] args) {
		ClassOrder klass = new ClassOrder();
		klass.save();
	}
}
