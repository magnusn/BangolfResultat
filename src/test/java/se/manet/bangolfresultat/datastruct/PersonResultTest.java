package se.manet.bangolfresultat.datastruct;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PersonResultTest {

	@Test
	public void testGetDiff() {
		int[] results = new int[] { 29, 44, 55, 39 };
		PersonResult personResult = createPersonResult(results, 4);

		assertEquals(55 - 29, personResult.getDiff());
	}

	@Test
	public void testGetDiffAfterChangingResult() {
		int[] results = new int[] { 40, 0 };
		PersonResult personResult = createPersonResult(results, 1);

		assertEquals(0, personResult.getDiff());

		results = new int[] { 41, 0 };
		changeResults(personResult, results, 1);

		assertEquals(0, personResult.getDiff());
	}

	@Test
	public void testGetNbrRoundsPlayed() {
		int[] results = new int[] { 42, 41, 44 };
		PersonResult personResult = createPersonResult(results, 3);

		assertEquals(3, personResult.getNbrRoundsPlayed());
	}

	@Test
	public void testGetNbrRoundsPlayedWhenOneRoundIsEmpty() {
		int[] results = new int[] { 42, 41, 0 };
		PersonResult personResult = createPersonResult(results, 2);

		assertEquals(2, personResult.getNbrRoundsPlayed());
	}

	@Test
	public void testGetNbrRoundsPlayedWhenOneRoundIsNotPlayed() {
		int[] results = new int[] { 42, -1, 44 };
		PersonResult personResult = createPersonResult(results, 3);

		assertEquals(2, personResult.getNbrRoundsPlayed());
	}

	@Test
	public void testGetNbrRoundsPlayedWhenOneRoundIsEmptyAndOneRoundIsNotPlayed() {
		int[] results = new int[] { -1, 41, 0 };
		PersonResult personResult = createPersonResult(results, 2);

		assertEquals(1, personResult.getNbrRoundsPlayed());
	}

	@Test
	public void testGetNbrRoundsPlayedWhenManyRoundsAreEmpty() {
		int[] results = new int[] { 42, 41, 0, 0, 0, 0, 0, 0 };
		PersonResult personResult = createPersonResult(results, 2);

		assertEquals(2, personResult.getNbrRoundsPlayed());
	}

	private static PersonResult createPersonResult(int[] results, int nbrRoundsFinished) {
		return createPersonResult(0, null, null, null, results, 0, null, 0, nbrRoundsFinished, 0);
	}

	private static PersonResult createPersonResult(int startNbr, String name, String club, String licenseNbr,
			int[] results, int rounds, String klass, int prio, int nbrRoundsFinished, int personID) {
		return new PersonResult(startNbr, name, club, licenseNbr, results, rounds, klass, prio, nbrRoundsFinished,
				personID);
	}

	private static void changeResults(PersonResult personResult, int[] results, int nbrRoundsFinished) {
		personResult.changeResults(0, null, null, null, results, null, 0, nbrRoundsFinished);
	}
}
