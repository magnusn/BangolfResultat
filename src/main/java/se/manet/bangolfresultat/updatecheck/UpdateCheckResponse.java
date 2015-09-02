package se.manet.bangolfresultat.updatecheck;

/**
 * Class representing the update check response.
 */
public class UpdateCheckResponse {

	private String latestVersion = null;
	private String errorMessage = null;
	private boolean updateAvailable = false;
	private boolean successful = false;

	/**
	 * Returns the latest version available.
	 * 
	 * @return the latest version available
	 */
	public String getLatestVersion() {
		return latestVersion;
	}

	/**
	 * Sets the latest version available.
	 * 
	 * @param latestVersion
	 *            the latest version available
	 */
	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}

	/**
	 * Returns the error message, if any.
	 * 
	 * @return the error message, otherwise <code>null</code>
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Tells whether a newer version is available.
	 * 
	 * @return <code>true</code> if a newer version is available,
	 *         <code>false</code> otherwise
	 */
	public boolean isUpdateAvailable() {
		return updateAvailable;
	}

	/**
	 * Sets whether a newer version is available.
	 * 
	 * @param updateAvailable
	 *            <code>true</code> if a newer version is available,
	 *            <code>false</code> otherwise
	 */
	public void setUpdateAvailable(boolean updateAvailable) {
		this.updateAvailable = updateAvailable;
	}

	/**
	 * Tells whether the update check was performed successfully.
	 * 
	 * @return <code>true</code> if the update check was successfully performed,
	 *         <code>false</code> otherwise
	 */
	public boolean isSuccessful() {
		return successful;
	}

	/**
	 * Sets whether the update check was performed successfully.
	 * 
	 * @param successful
	 *            <code>true</code> if the update check was successfully
	 *            performed, <code>false</code> otherwise
	 */
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

}
