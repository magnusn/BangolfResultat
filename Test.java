class Test {
    
	public static void main(String[] args) {
	    String userNick = "[Kämnet]Judger.xml.bz2";
	    System.out.println(userNick);
	    //userNick.substring(0,userNick.length() - 4);
	    //System.out.println(userNick);
	    if(userNick.endsWith(".bz2")) {
		    userNick = userNick.substring(0,userNick.length() - 4);
		    System.out.println(userNick);
		}
		if(userNick.endsWith(".xml")) {
		    System.out.println(userNick.substring(0,userNick.length() - 4));
		}
	}
}
