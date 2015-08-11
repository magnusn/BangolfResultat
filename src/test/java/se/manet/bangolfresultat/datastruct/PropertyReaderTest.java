package se.manet.bangolfresultat.datastruct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PropertyReaderTest {

	@Test
	public void testGetApplicationName() {
		assertEquals("BangolfResultat", PropertyReader.getApplicationName());
	}

	@Test
	public void testGetApplicationVersion() {
		String version = PropertyReader.getApplicationVersion();
		assertNotNull(version);
		assertNotEquals("", version);
	}

	@Test
	public void testGetApplicationUrl() {
		assertEquals("http://bangolfresultat.manet.se/",
				PropertyReader.getApplicationUrl());
	}

}
