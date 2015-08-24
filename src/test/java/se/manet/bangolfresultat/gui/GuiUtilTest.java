package se.manet.bangolfresultat.gui;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;

import javax.swing.JLabel;

import org.junit.Test;

public class GuiUtilTest {

	@Test
	public void testSetSameSize() {
		JLabel label1 = new JLabel("ABC123");
		JLabel label2 = new JLabel("ABCDEFG123456");
		JLabel label3 = new JLabel("a");
		Dimension expectedPreferredSize = label2.getPreferredSize();
		GuiUtil.setSameSize(label1, label2, label3);

		assertEquals(expectedPreferredSize, label1.getPreferredSize());
		assertEquals(expectedPreferredSize, label2.getPreferredSize());
		assertEquals(expectedPreferredSize, label3.getPreferredSize());
	}

}
