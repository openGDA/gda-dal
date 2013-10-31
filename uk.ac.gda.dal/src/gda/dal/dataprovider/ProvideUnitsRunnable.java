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
import gda.factory.Findable;
import gda.factory.Finder;

import java.util.ArrayList;
import java.util.List;

import org.epics.css.dal.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public abstract class ProvideUnitsRunnable <T> implements  ProvideRunnable<T>  {
	private Scannable scannable;
	private static final Logger logger = LoggerFactory.getLogger(ProvideHighLimitRunnable.class);
	private boolean running;
	protected String currentValue= "";
	// TODO: listeners list isn't used thread safely
	protected List<ProvideDataEventListener<T>> listeners = new ArrayList<ProvideDataEventListener<T>>(1);
	abstract T createValue(Object val);
	
	@Override
	public T getCurrentValue() {
		return createValue(currentValue);
	}

	public void setTargetValue(@SuppressWarnings("unused") double targetValue) {
	}
	
	@Override
	public void setTargetValue(Object targetValue) {
	}

	public ProvideUnitsRunnable(String scannableName) {
		int index = scannableName.lastIndexOf(".");
		if(index != -1)
			scannableName = scannableName.substring(0, index);
		Findable findable = Finder.getInstance().find(scannableName);
		if (findable instanceof Scannable)
			this.scannable = (Scannable) findable;
		else
			throw new RuntimeException("ProvideDataRunnable. " + scannableName + " is not a Scannable");
		updateListeners(readValue());
		running = true;
	}
	
	private Object readValue(){
		Object position = null;
		try {
			position =  scannable.getAttribute("userunits");
			}			
		 catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
