package se.manet.bangolfresultat.updatecheck;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VersionCompareTest {

	@Test
	public void testCompareVersion() {
		assertTrue(VersionCompare.compareVersion("0.7.1", "0.8") < 0);
		assertTrue(VersionCompare.compareVersion("0.7.1", "0.7.0") > 0);
		assertTrue(VersionCompare.compareVersion("0.8.0", "0.8.0") == 0);
		assertTrue(VersionCompare.compareVersion("1.0", "0.99") > 0);
	}

}
