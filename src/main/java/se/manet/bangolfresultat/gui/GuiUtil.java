package se.manet.bangolfresultat.gui;

import java.awt.Component;
import java.awt.Dimension;

/**
 * Utility class for graphical user interfaces.
 */
public class GuiUtil {

	/**
	 * Sets the same preferred size on all components based on the largest
	 * preferred width and height within the given components.
	 * 
	 * @param components
	 *            components whose size should be equal
	 */
	public static void setSameSize(Component... components) {
		// just return if no components
		if (components == null) {
			return;
		}

		// get max width and height
		double maxWidth = Double.MIN_VALUE;
		double maxHeight = Double.MIN_VALUE;
		for (Component component : components) {
			Dimension preferredSize = component.getPreferredSize();
			maxWidth = Math.max(maxWidth, preferredSize.getWidth());
			maxHeight = Math.max(maxHeight, preferredSize.getHeight());
		}

		// just return if illegal values
		if (maxWidth == Double.MIN_VALUE || maxHeight == Double.MIN_VALUE) {
			return;
		}

		// set same preferred size
		for (Component component : components) {
			Dimension dimension = new Dimension();
			dimension.setSize(maxWidth, maxHeight);
			component.setPreferredSize(dimension);
		}
	}

}
