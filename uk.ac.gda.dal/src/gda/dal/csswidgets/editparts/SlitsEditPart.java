/*-
 * Copyright © 2009 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package gda.dal.csswidgets.editparts;

import gda.dal.csswidgets.figures.SlitsFigure;
import gda.dal.csswidgets.model.SlitsModel;

import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.IFigure;

public class SlitsEditPart extends AbstractWidgetEditPart {
	private SlitsFigure refreshableSlitsFigure;
	private SlitsModel model;
	private int _yTranslate;
	private int _xTranslateX;
	private int _Vgap;
	private int _Hgap;
	
	/**
	 * Returns the casted model. This is just for convenience.
	 * 
	 * @return the casted {@link SlitsModel}
	 */
	@Override
	protected SlitsModel getCastedModel() {
		return (SlitsModel) getModel();
	}
	
	@Override
	protected IFigure doCreateFigure() {
		model = getCastedModel();
		refreshableSlitsFigure = new SlitsFigure();
		refreshableSlitsFigure.setSlitType(model.getSlitType());
		_yTranslate = refreshableSlitsFigure.getYTranslate();
		_xTranslateX = refreshableSlitsFigure.getXTranslate().x;
		_Vgap = refreshableSlitsFigure.getVGap();
		_Hgap = refreshableSlitsFigure.getHGap();
		setBeamOut(refreshableSlitsFigure);
		return refreshableSlitsFigure;
	}

	@Override
	protected void registerPropertyChangeHandlers() {
		IWidgetPropertyChangeHandler handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				SlitsFigure imageFigure = (SlitsFigure) figure;
				String upPath = imageFigure.getVGapUpPath().toString();
				String downPath = imageFigure.getVGapDownPath().toString();
				if (((String) newValue).equals("Busy")) {
					if (!upPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = upPath.indexOf('.');
						upPath = upPath.substring(beginIndex, endIndex);
						upPath = upPath + "_red.png";
					}
					if (!downPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = downPath.indexOf('.');
						downPath = downPath.substring(beginIndex, endIndex);
						downPath = downPath + "_red.png";
					}
					imageFigure.setVGapUpPath(new Path(upPath));
					imageFigure.setVGapDownPath(new Path(downPath));
					imageFigure.repaint();
				}
				else {
					if (upPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = upPath.indexOf("_red");
						upPath = upPath.substring(beginIndex, endIndex);
						upPath = upPath + ".png";
					}
					if (downPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = downPath.indexOf("_red");
						downPath = downPath.substring(beginIndex, endIndex);
						downPath = downPath + ".png";
					}
					imageFigure.setVGapUpPath(new Path(upPath));
					imageFigure.setVGapDownPath(new Path(downPath));
					imageFigure.repaint();
				}
				return true;
			}
		};
		setPropertyChangeHandler(SlitsModel.PROP_VGAP_BUSY, handle);
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				SlitsFigure slitsFigure = (SlitsFigure) figure;
				String plusPath = slitsFigure.getHGapPlusPath().toString();
				String minusPath = slitsFigure.getHGapMinusPath().toString();
				if (((String) newValue).equals("Busy")) {
					if (!plusPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = plusPath.indexOf('.');
						plusPath = plusPath.substring(beginIndex, endIndex);
						plusPath = plusPath + "_red.png";
					}
					if (!minusPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = minusPath.indexOf('.');
						minusPath = minusPath.substring(beginIndex, endIndex);
						minusPath = minusPath + "_red.png";
					}
					slitsFigure.setHGapPlusPath(new Path(plusPath));
					slitsFigure.setHGapMinusPath(new Path(minusPath));
					slitsFigure.repaint();
				}
				else {
					if (plusPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = plusPath.indexOf("_red");
						plusPath = plusPath.substring(beginIndex, endIndex);
						plusPath = plusPath + ".png";
					}
					if (minusPath.contains("red")) {
						int beginIndex = 0;
						int endIndex = minusPath.indexOf("_red");
						minusPath = minusPath.substring(beginIndex, endIndex);
						minusPath = minusPath + ".png";
					}
					slitsFigure.setHGapPlusPath(new Path(plusPath));
					slitsFigure.setHGapMinusPath(new Path(minusPath));
					slitsFigure.repaint();
				}
				return true;
			}
		};
		setPropertyChangeHandler(SlitsModel.PROP_HGAP_BUSY, handle);
		
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				SlitsFigure slitsFigure = (SlitsFigure) figure;
				_xTranslateX = Integer.parseInt(newValue.toString());
				slitsFigure.setXTranslate(_xTranslateX);
				setBeamOut(slitsFigure);
				return true;
			}
		};
		setPropertyChangeHandler(SlitsModel.PROP_X_Translate, handle);
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				SlitsFigure slitsFigure = (SlitsFigure) figure;
				_yTranslate = Integer.parseInt(newValue.toString());
				slitsFigure.setYTranslate(_yTranslate);
				setBeamOut(slitsFigure);
				return true;
			}
		};
		setPropertyChangeHandler(SlitsModel.PROP_Y_Translate, handle);
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				SlitsFigure slitsFigure = (SlitsFigure) figure;
				_Vgap = Integer.parseInt(newValue.toString());
				slitsFigure.setVGap(_Vgap);
				setBeamOut(slitsFigure);
				return true;
			}
		};
		setPropertyChangeHandler(SlitsModel.PROP_VGAP, handle);
		
		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				SlitsFigure slitsFigure = (SlitsFigure) figure;
				_Hgap = Integer.parseInt(newValue.toString());
				slitsFigure.setHGap(_Hgap);
				setBeamOut(slitsFigure);
				return true;
			}
		};
		setPropertyChangeHandler(SlitsModel.PROP_HGAP, handle);

		handle = new IWidgetPropertyChangeHandler() {
			@Override
			public boolean handleChange(final Object oldValue, final Object newValue, final IFigure figure) {
				SlitsFigure slitsFigure = (SlitsFigure) figure;
				slitsFigure.setSlitType(Integer.parseInt(newValue.toString()));
				return true;
			}
		};
		setPropertyChangeHandler(SlitsModel.PROP_SLIT_TYPE, handle);
	}
	
	public void setBeamOut(SlitsFigure slitsFigure){
		if (slitsFigure.getSlitType() == 0
				&& (Math.abs(_yTranslate / 1.3571428571428572) > 60 + Math.abs(_Vgap) / 2 + 6 || Math
						.abs(_yTranslate / 1.3571428571428572) < Math.abs(_Vgap) / 2)) {
			getCastedModel().getPropertyInternal(SlitsModel.PROP_BEAM_OUT).setManualValue("yes");
		}
		
		else if (slitsFigure.getSlitType() == 1
				&& (Math.abs(_xTranslateX) > 60 + Math.abs(_Hgap) / 2 + 3 || Math.abs(_xTranslateX) < Math.abs(_Hgap) / 2)
				&& (Math.abs(_yTranslate / 1.3571428571428572) > 60 + Math.abs(_Vgap) / 2 + 6 || Math
						.abs(_yTranslate / 1.3571428571428572) < Math.abs(_Vgap) / 2)) {
			getCastedModel().getPropertyInternal(SlitsModel.PROP_BEAM_OUT).setManualValue("yes");
		}
		else
			getCastedModel().getPropertyInternal(SlitsModel.PROP_BEAM_OUT).setManualValue("no");
	}
}