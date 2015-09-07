package se.manet.bangolfresultat.datastruct.util;

import java.util.Date;

import se.manet.bangolfresultat.datastruct.DataStore;
import se.manet.bangolfresultat.updatecheck.Frequency;

/**
 * Utility class for handling application settings.
 */
public class Settings {

	/**
	 * Returns how often automatic update checks should be performed.
	 * 
	 * @return how often automatic update checks should be performed, if no
	 *         value is set <code>Frequency.WEEKLY</code> is returned
	 */
	public static Frequency getFrequency() {
		String frequency = (String) DataStore
				.get(DataStore.UPDATE_CHECK_FREQUENCY);
		if (frequency == null) {
			return Frequency.WEEKLY;
		} else {
			return Frequency.valueOf(frequency);
		}
	}

	/**
	 * Returns whether the user should be noticed more than once of an available
	 * version when the automatic update check is run.
	 * 
	 * @return <code>true</code> if user only should be noticed once for a
	 *         version, <code>false</code> otherwise as well if no value is set
	 */
	public static Boolean getDoNotRemind() {
		Boolean doNotRemind = (Boolean) DataStore
				.get(DataStore.UPDATE_CHECK_DO_NOT_REMIND);
		if (doNotRemind == null) {
			return Boolean.FALSE;
		} else {
			return doNotRemind;
		}
	}

	/**
	 * Returns the date when the automatic update check was last run.
	 * 
	 * @return the date when the automatic update check was last run, if no
	 *         value is set January 1, 1970, 00:00:00 GMT is returned
	 */
	public static Date getLastRunDate() {
		Date lastRunDate = (Date) DataStore
				.get(DataStore.UPDATE_CHECK_LAST_RUN_DATE);
		if (lastRunDate == null) {
			return new Date(0);
		} else {
			return lastRunDate;
		}
	}

	/**
	 * Returns the last version found by the automatic update check.
	 * 
	 * @return the last version found by the automatic update check, if no value
	 *         is set <code>"0"</code> is returned
	 */
	public static String getLastVersionFound() {
		String lastVersionFound = (String) DataStore
				.get(DataStore.UPDATE_CHECK_LAST_VERSION_FOUND);
		if (lastVersionFound == null) {
			return "0";
		} else {
			return lastVersionFound;
		}
	}

}
