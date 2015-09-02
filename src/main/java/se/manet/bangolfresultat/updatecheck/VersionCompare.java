package se.manet.bangolfresultat.updatecheck;

/**
 * Class to compare versions.
 */
public class VersionCompare {

	/**
	 * Compares two version strings.
	 * <p>
	 * The version string should be on format <code>x.y.z</code> where x, y and
	 * z are digits. All other characters than digits and period are ignored.
	 * 
	 * @param versionA
	 *            version string A
	 * @param versionB
	 *            version string B
	 * @return the value 0 if versionA is equal to versionB; a value less than 0
	 *         if versionA is older than versionB; a value greater than 0 if
	 *         versionA is newer than versionB.
	 */
	public static int compareVersion(String versionA, String versionB) {
		versionA = versionA.replaceAll("[^\\d.]", "");
		versionB = versionB.replaceAll("[^\\d.]", "");

		String[] arrayA = versionA.split("\\.");
		String[] arrayB = versionB.split("\\.");

		int maxLength = Math.max(arrayA.length, arrayB.length);
		for (int i = 0; i < maxLength; ++i) {
			Integer a = 0;
			if (i < arrayA.length) {
				a = Integer.parseInt(arrayA[i]);
			}
			Integer b = 0;
			if (i < arrayB.length) {
				b = Integer.parseInt(arrayB[i]);
			}

			int compareValue = a.compareTo(b);
			if (compareValue != 0) {
				return compareValue;
			} else if (compareValue == 0 && i == maxLength - 1) {
				return 0;
			}
		}

		return 0;
	}

}
