
package gda.dal.csswidgets.model;

import org.csstudio.sds.model.AbstractWidgetModel;
import org.csstudio.sds.model.BorderStyleEnum;
import org.csstudio.sds.model.TextAlignmentEnum;
import org.csstudio.sds.model.WidgetPropertyCategory;
import org.csstudio.sds.util.ColorAndFontUtil;


public final class ComboModel extends AbstractWidgetModel {
	/**
	 * The ID of the label property.
	 */
	public static final String PROP_LABEL = "label"; //$NON-NLS-1$
	/**
	 * The ID of the font property.
	 */
	public static final String PROP_FONT = "font"; //$NON-NLS-1$

	/**
	 * The ID of the text alignment property.
	 */
	public static final String PROP_TEXT_ALIGNMENT = "textAlignment"; //$NON-NLS-1$
	/**
	 * The default value of the height property.
	 */
	private static final int DEFAULT_HEIGHT = 20;

	/**
	 * The default value of the width property.
	 */
	private static final int DEFAULT_WIDTH = 80;
	/**
	 * The ID of this widget model.
	 */
	public static final String ID = "uk.ac.gda.dal.combo";

	/**
	 * Constructor.
	 */
	public ComboModel() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setPropertyValue(PROP_BORDER_STYLE, BorderStyleEnum.RAISED.getIndex());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureProperties() {
		addStringProperty(PROP_LABEL, "Label Text", WidgetPropertyCategory.DISPLAY, "", false); //$NON-NLS-1$
		addFontProperty(PROP_FONT, "Font", WidgetPropertyCategory.DISPLAY, ColorAndFontUtil.toFontString("Arial", 8), false); //$NON-NLS-1$
		addArrayOptionProperty(PROP_TEXT_ALIGNMENT, "Text Alignment", WidgetPropertyCategory.DISPLAY, TextAlignmentEnum.getDisplayNames(),
				TextAlignmentEnum.CENTER.getIndex(), false);
		// addProperty(PROP_ACTIONDATA, new ActionDataProperty("Action Data",
		// WidgetPropertyCategory.Behaviour, new ActionData()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getDefaultToolTip() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(createTooltipParameter(PROP_ALIASES) + "\n");
		buffer.append("Actions:\t");
		buffer.append(createTooltipParameter(PROP_ACTIONDATA));
		return buffer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTypeID() {
		return ID;
	}

	/**
	 * Return the label text.
	 * 
	 * @return The label text.
	 */
	public String getLabel() {
		return getStringProperty(PROP_LABEL);
	}

	/**
	 * Returns the alignment for the text.
	 * 
	 * @return int 0 = Center, 1 = Top, 2 = Bottom, 3 = Left, 4 = Right
	 */
	public int getTextAlignment() {
		return getArrayOptionProperty(PROP_TEXT_ALIGNMENT);
	}

}