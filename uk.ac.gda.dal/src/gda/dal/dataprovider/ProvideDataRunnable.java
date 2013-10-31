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

import gda.device.DeviceException;
import gda.device.Scannable;
import gda.device.scannable.ScannablePositionChangeEvent;
import gda.device.scannable.ScannableStatus;
import gda.factory.Findable;
import gda.factory.Finder;
import gda.observable.IObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.epics.css.dal.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class ProvideDataRunnable<T> implements ProvideRunnable {
	private static final Logger logger = LoggerFactory.getLogger(ProvideDataRunnable.class);
	private Scannable scannable;
	private boolean running = false;
	private IObserver observer = null;
	private static Timer timer = new Timer();
	private TimerTask timerTask;
	private ProvideDataEvent<T> event = null;
	// TODO: listeners list isn't used thread safely
	protected List<ProvideDataEventListener<T>> listeners = new ArrayList<ProvideDataEventListener<T>>(1);
	private long timeSinceLastBusy = 0;
	private boolean firstUpdate = true;
	
	@Override
	public T getCurrentValue() {
		return event != null ? event.value : null;
	}

	void setTargetValueT(final Object targetValue) {
		try {
			String reason = scannable.checkPositionValid(targetValue);
			if (reason == null || reason.isEmpty()) {
				Runnable x = new Runnable() {
					@Override
					public void run() {
						try {
							scannable.moveTo(targetValue);
						} catch (DeviceException e) {
							logger.error(e.getMessage(), e);
						}
					}
				};
				Thread t = new Thread(x, "moveTo");
				t.start();
			} else
				logger.error("Unable to set " + scannable.getName() + " to " + targetValue + ":" + reason);
		} catch (DeviceException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public ProvideDataRunnable(String scannableName) {
		int index = scannableName.indexOf(".");
		if (index != -1)
			scannableName = scannableName.substring(0, index);
		Findable findable = Finder.getInstance().find(scannableName);
		if (findable instanceof Scannable) {
			this.scannable = (Scannable) findable;

			this.scannable.addIObserver(observer = new IObserver() {
				/*
				 * if event is of type ScannablePositionChangeEvent simply update listeners with new position if event
				 * is ScannableStatus then schedule a callback of this method so that position can be read if source is
				 * null then the method is called by this class in which case read val and update listeners, if busy
				 * then schedule another call later
				 */
				@Override
				public void update(Object source, Object arg) {
					boolean scannableBusy = false;
					try {
						scannableBusy = scannable.isBusy();
						long currentTime = System.currentTimeMillis();
						timeSinceLastBusy = currentTime-timeSinceLastBusy;
					} catch (DeviceException e) {
						logger.error("Error checking if scannable is busy", e);
					}
					
					if(arg instanceof String){
						ProvideDataRunnable.this.readValAndUpdateListeners();
						try {
							if (timerTask != null)
								timerTask.cancel();
							timerTask = new TimerTask() {
								@Override
								public void run() {
									if (ProvideDataRunnable.this.observer != null)
										ProvideDataRunnable.this.observer.update(null, null);
								}
							};
							timer.schedule(timerTask, 100);
						} catch (IllegalStateException e) {
						}
					}
					
					if (scannableBusy || timeSinceLastBusy<10000000 || firstUpdate) {
						
						firstUpdate=false;
						
						if (source == null) {
							ProvideDataRunnable.this.readValAndUpdateListeners();
							try {
								if (timerTask != null)
									timerTask.cancel();
								timerTask = new TimerTask() {
									@Override
									public void run() {
										if (ProvideDataRunnable.this.observer != null)
											ProvideDataRunnable.this.observer.update(null, null);
									}
								};
								timer.schedule(timerTask, 100);
							} catch (IllegalStateException e) {
							}
						}
						
						else if (arg instanceof ScannableStatus) {
							/* if ScannableStatus schedule a task to call readValAndUpdateListeners */
							TimerTask timerTaskScannableStatus = new TimerTask() {
								@Override
								public void run() {
									ProvideDataRunnable.this.observer.update(null, null);
								}
							};
							timer.schedule(timerTaskScannableStatus, 100);
						}
						
						else if (arg instanceof ScannablePositionChangeEvent)
							updateListeners(((ScannablePositionChangeEvent) arg).newPosition);
						
						else if (arg instanceof Double) {
							TimerTask timerTaskDouble = new TimerTask() {
								@Override
								public void run() {
									if (ProvideDataRunnable.this.observer != null)
										ProvideDataRunnable.this.observer.update(null, null);
								}
							};
							timer.schedule(timerTaskDouble, 100);
						}
						
					}
				}
			});
			observer.update(null, null);
			running = true;
		} else {
			stop();
			throw new RuntimeException("ProvideDataRunnable. " + scannableName + " is not a Scannable");
		}
	}

	private void readValAndUpdateListeners() {
		try {
			updateListeners(scannable.getPosition());
		} catch (DeviceException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void updateListeners(Object position) {
		event = new ProvideDataEvent<T>();
		try {
			if (position != null)
				event.value = valueToType(position);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			event = null;
			return;
		}
		event.timestamp = new Timestamp();

		for (ProvideDataEventListener<T> listener : listeners) {
			try {
				listener.newData(event);
			} catch (Throwable e1) {
				logger.error(e1.getMessage(), e1);
			}
		}
	}

	abstract T valueToType(Object val);

	@Override
	public void stop() {
		if (scannable != null && observer != null) {
			scannable.deleteIObserver(observer);
			observer = null;
		}
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void addListener(ProvideDataEventListener newListener) {
		listeners.add(newListener);
		if (event != null)
			newListener.newData(event);
	}

	@Override
	public void removeListener(ProvideDataEventListener listenerToRemove) {
		listeners.remove(listenerToRemove);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
	}
}