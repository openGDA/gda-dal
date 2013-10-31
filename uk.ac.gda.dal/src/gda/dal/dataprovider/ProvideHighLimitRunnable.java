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

package gda.dal.dataprovider;

import gda.device.Scannable;
import gda.device.scannable.ScannableHighLimitChangeEvent;
import gda.factory.Findable;
import gda.factory.Finder;
import gda.observable.IObserver;

import java.util.ArrayList;
import java.util.List;

import org.epics.css.dal.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public abstract class ProvideHighLimitRunnable<T> implements  ProvideRunnable<T>{
	private Scannable scannable;
	private static final Logger logger = LoggerFactory.getLogger(ProvideHighLimitRunnable.class);
	private boolean running;
	protected double currentValue = 0.0;
	// TODO: listeners list isn't used thread safely
	protected List<ProvideDataEventListener<T>> listeners = new ArrayList<ProvideDataEventListener<T>>(1);
	
	@Override
	public T getCurrentValue() {
		return createValue(currentValue);
	}

	public void setTargetValue(@SuppressWarnings("unused") double upperLim) {
	}
	
	@Override
	public void setTargetValue(Object targetValue) {
		if (targetValue instanceof Double) {
			Double dbltarget = (Double) targetValue;
			setTargetValue(dbltarget.doubleValue());
		} else if (targetValue instanceof String) {
			String strtarget = (String) targetValue;
			double val = Double.parseDouble(strtarget);
			setTargetValue(val);
		} else {
			throw new RuntimeException("Unknown type: " + targetValue.getClass().getName());
		}
	}

	/**
	 * @param scannableName
	 */
	public ProvideHighLimitRunnable(String scannableName) {
		int index = scannableName.lastIndexOf(".");
		if(index != -1)
			scannableName = scannableName.substring(0, index);
		Findable findable = Finder.getInstance().find(scannableName);
		if (findable instanceof Scannable) {
			scannable = (Scannable) findable;
			scannable.addIObserver(new IObserver() {

				@Override
				public void update(Object source, Object arg) {
					if (source instanceof Scannable  && arg instanceof ScannableHighLimitChangeEvent)
						if ((((Scannable)source).getName()).equals(scannable.getName()))
							updateListeners(((ScannableHighLimitChangeEvent)arg).newHighLimits[0]);
				}

			});
			
			try {
				updateListeners(readValue());
				running =true;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} 
		else
			throw new RuntimeException("ProvideHighLimitRunnable. " + scannableName + " is not a Scannable");
	}
	
	private Object readValue(){
		Object position = null;
		try {
			Object limits =  scannable.getAttribute("upperMotorLimit");
			if (limits != null)
				position = limits ;
		} catch (Exception e1) {
			logger.error("Error reading High Limits from the scannable " + scannable.getName(), e1);
		}
		return position;
	}
	private void updateListeners(Object position){
		ProvideDataEvent<T> event = new ProvideDataEvent<T>();		
		try{
			event.value = createValue(position);
		} catch(Exception e){
			logger.error(e.getMessage(),e);
			event = null;
			return;
		}
		event.timestamp = new Timestamp();
		for (ProvideDataEventListener<T> listener : listeners)
			listener.newData(event);
	}
	
	abstract T createValue(Object val);
	
	@Override
	public void stop() {
		running = false;
	}
	
	@Override
	public boolean isRunning () {
		return running;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void addListener(ProvideDataEventListener newListener) {
		listeners.add(newListener);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void removeListener(ProvideDataEventListener listenerToRemove) {
		listeners.remove(listenerToRemove);
	}
	@Override
	public void refresh() {
		updateListeners(readValue());
	}
}