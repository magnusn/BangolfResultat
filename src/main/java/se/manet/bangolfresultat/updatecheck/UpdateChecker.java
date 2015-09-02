package se.manet.bangolfresultat.updatecheck;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JDialog;

import se.manet.bangolfresultat.datastruct.PropertyReader;
import se.manet.bangolfresultat.gui.GuiUtil;

/**
 * Class to perform check for a newer version of the application.
 */
public class UpdateChecker implements Callable<UpdateCheckResponse> {

	/**
	 * Returns the response after performing an update check.
	 * 
	 * @return the response after performing an update check
	 */
	private static UpdateCheckResponse performUpdateCheck() {
		UpdateCheckResponse response = new UpdateCheckResponse();
		BufferedReader in = null;
		String address = PropertyReader.getApplicationUrl()
				+ "api/updatecheck?version="
				+ PropertyReader.getApplicationVersion();
		try {
			URL url = new URL(address);
			URLConnection connection = url.openConnection();
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			ArrayList<String> inputLines = new ArrayList<String>();
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				inputLines.add(inputLine);
			}

			for (String s : inputLines) {
				String[] stringArray = s.split(" ");
				if (stringArray[0].equals("release") && stringArray.length >= 2) {
					String latestVersion = stringArray[1];
					response.setLatestVersion(latestVersion);
					response.setSuccessful(true);
					response.setUpdateAvailable(VersionCompare.compareVersion(
							PropertyReader.getApplicationVersion(),
							latestVersion) < 0);
					break;
				}
			}
		} catch (MalformedURLException e) {
			response.setSuccessful(false);
			response.setErrorMessage("Felaktig adress: " + address
					+ "\n\nVänligen rapportera problemet till "
					+ "programtillverkaren.");
		} catch (IOException e) {
			response.setSuccessful(false);
			response.setErrorMessage("Senaste versionsinformationen kunde inte "
					+ "hämtas. Försök igen senare.");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}

		return response;
	}

	public UpdateCheckResponse call() throws Exception {
		return performUpdateCheck();
	}

	/**
	 * Displays a progress bar while performing an update check and then returns
	 * the response.
	 * 
	 * @param owner
	 *            frame to be used of owner of the progress bar that will be
	 *            displayed
	 * @return the update check response
	 */
	public static UpdateCheckResponse getResponse(Frame owner) {
		final JDialog progressBarDialog = GuiUtil
				.getIndeterminateProgressBarDialog(owner,
						"Sök efter uppdateringar");
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			executor.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(500);
						synchronized (progressBarDialog) {
							if (progressBarDialog.isDisplayable()) {
								progressBarDialog.setVisible(true);
							}
						}
					} catch (InterruptedException e) {

					}
				}
			});
			return performUpdateCheck();
		} finally {
			synchronized (progressBarDialog) {
				progressBarDialog.dispose();
			}
			if (executor != null) {
				executor.shutdown();
			}
		}
	}

}
