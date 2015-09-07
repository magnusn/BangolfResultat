package se.manet.bangolfresultat.updatecheck;

/**
 * Represents how often automatic update checks should be run.
 */
public enum Frequency {
	NEVER("Aldrig", Integer.MAX_VALUE), WEEKLY("En gång i veckan", 7), DAILY(
			"En gång om dagen", 1);

	private String display;
	private int interval;

	Frequency(String display, int interval) {
		this.display = display;
		this.interval = interval;
	}

	/**
	 * Returns the interval between automatic update checks in days.
	 * 
	 * @return the interval between automatic update checks in days
	 */
	public int getInterval() {
		return interval;
	}

	@Override
	public String toString() {
		return display;
	}

}
