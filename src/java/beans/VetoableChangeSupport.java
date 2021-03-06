/*
 * java.beans.VetoableChangeSupport: part of the Java Class Libraries project.
 * Copyright (C) 1998, 2000 Free Software Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.beans;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 ** VetoableChangeSupport makes it easy to fire vetoable
 ** change events and handle listeners as well as reversion
 ** of old values when things go wrong.
 **
 ** @author John Keiser
 ** @since JDK1.1
 ** @version 1.2.0, 15 Mar 1998
 **/

public class VetoableChangeSupport implements java.io.Serializable {
	transient Hashtable propertyListeners = new Hashtable();
	transient Vector listeners = new Vector();
	Hashtable children;
	Object source;
	int vetoableChangeSupportSerializedDataVersion = 2;
	private static final long serialVersionUID = -5090210921595982017L;

	/**
	 * Saves the state of the object to the stream. */
	private void writeObject(ObjectOutputStream stream) throws IOException {
		children = propertyListeners.isEmpty() ? null : propertyListeners;
		stream.defaultWriteObject();
		for (Enumeration e = listeners.elements(); e.hasMoreElements(); ) {
			VetoableChangeListener l = (VetoableChangeListener)e.nextElement();
			if (l instanceof Serializable)
			  stream.writeObject(l);
		}
		stream.writeObject(null);
	}

	/**
	 * Reads the object back from stream (deserialization).
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		propertyListeners = (children == null) ? new Hashtable() : children;
		VetoableChangeListener l;
		while ((l = (VetoableChangeListener)stream.readObject()) != null) {
			addVetoableChangeListener(l);
		}
		// FIXME: XXX: There is no spec for JDK 1.1 serialization
		// so it is unclear what to do if the value of
		// vetoableChangeSupportSerializedDataVersion is 1.
	}


	/** Create VetoableChangeSupport to work with a specific
	 ** source bean.
	 ** @param source the source bean to use.
	 **/
	public VetoableChangeSupport(Object source) {
		this.source = source;
	}

	/** Adds a VetoableChangeListener to the list of listeners.
	 ** All property change events will be sent to this listener.
	 ** <P>
	 **
	 ** The listener add is not unique: that is, <em>n</em> adds with
	 ** the same listener will result in <em>n</em> events being sent
	 ** to that listener for every property change.
	 ** <P>
	 **
	 ** Adding a null listener will cause undefined behavior.
	 **
	 ** @param l the listener to add.
	 **/
	public void addVetoableChangeListener(VetoableChangeListener l) {
		listeners.addElement(l);
	}

	/** Adds a VetoableChangeListener listening on the specified property.
	 ** Events will be sent to the listener for that particular property.
	 ** <P>
	 **
	 ** The listener add is not unique; that is, <em>n</em> adds on a
	 ** particular property for a particular listener will result in
	 ** <em>n</em> events being sent to that listener when that
	 ** property is changed.
	 ** <P>
	 **
	 ** The effect is cumulative, too; if you are registered to listen
	 ** to receive events on all property changes, and then you
	 ** register on a particular property, you will receive change
	 ** events for that property twice.
	 ** <P>
	 **
	 ** Adding a null listener will cause undefined behavior.
	 **
	 ** @param propertyName the name of the property to listen on.
	 ** @param l the listener to add.
	 **/
	public void addVetoableChangeListener(String propertyName, VetoableChangeListener l) {
		synchronized(propertyListeners) {
			Vector v = (Vector)propertyListeners.get(propertyName);
			try {
				v.addElement(l);
			} catch(NullPointerException e) {
				/* If v is not found, create a new vector. */
				v = new Vector();
				v.addElement(l);
				propertyListeners.put(propertyName, v);
			}
		}
	}

	/** Removes a VetoableChangeListener from the list of listeners.
	 ** If any specific properties are being listened on, they must
	 ** be deregistered by themselves; this will only remove the
	 ** general listener to all properties.
	 ** <P>
	 **
	 ** If <code>add()</code> has been called multiple times for a
	 ** particular listener, <code>remove()</code> will have to be
	 ** called the same number of times to deregister it.
	 **
	 ** @param l the listener to remove.
	 **/
	public void removeVetoableChangeListener(VetoableChangeListener l) {
		listeners.removeElement(l);
	}

	/** Removes a VetoableChangeListener from listening to a specific property.
	 ** <P>
	 **
	 ** If <code>add()</code> has been called multiple times for a
	 ** particular listener on a property, <code>remove()</code> will
	 ** have to be called the same number of times to deregister it.
	 **
	 ** @param propertyName the property to stop listening on.
	 ** @param l the listener to remove.
	 **/
	public void removeVetoableChangeListener(String propertyName, VetoableChangeListener l) {
		synchronized(propertyListeners) {
			Vector v = (Vector)propertyListeners.get(propertyName);
			try {
				v.removeElement(l);
				if(v.size() == 0) {
					propertyListeners.remove(propertyName);
				}
			} catch(NullPointerException e) {
				/* if v is not found, do nothing. */
			}
		}
	}


	/** Fire a VetoableChangeEvent to all the listeners.
	 ** If any listener objects, a reversion event will be sent to
	 ** those listeners who received the initial event.
	 **
	 ** @param proposedChange the event to send.
	 ** @exception PropertyVetoException if the change is vetoed.
	 **/
	public void fireVetoableChange(PropertyChangeEvent proposedChange) throws PropertyVetoException {
		int currentListener=0;
		try {
			for(;currentListener<listeners.size();currentListener++) {
				((VetoableChangeListener)listeners.elementAt(currentListener)).vetoableChange(proposedChange);
			}
		} catch(PropertyVetoException e) {
			PropertyChangeEvent reversion = new PropertyChangeEvent(proposedChange.getSource(),proposedChange.getPropertyName(),proposedChange.getNewValue(),proposedChange.getOldValue());
			for(int sendAgain=0;sendAgain<currentListener;sendAgain++) {
				try {
					((VetoableChangeListener)listeners.elementAt(sendAgain)).vetoableChange(reversion);
				} catch(PropertyVetoException e2) {
				}
			}
			throw e;
		}

		Vector moreListeners = (Vector)propertyListeners.get(proposedChange.getPropertyName());
		if(moreListeners != null) {
			try {
				for(currentListener = 0; currentListener < moreListeners.size(); currentListener++) {
					((VetoableChangeListener)moreListeners.elementAt(currentListener)).vetoableChange(proposedChange);
				}
			} catch(PropertyVetoException e) {
				PropertyChangeEvent reversion = new PropertyChangeEvent(proposedChange.getSource(),proposedChange.getPropertyName(),proposedChange.getNewValue(),proposedChange.getOldValue());
				for(int sendAgain=0;sendAgain<listeners.size();sendAgain++) {
					try {
						((VetoableChangeListener)listeners.elementAt(currentListener)).vetoableChange(proposedChange);
					} catch(PropertyVetoException e2) {		
					}
				}

				for(int sendAgain=0;sendAgain<currentListener;sendAgain++) {
					try {
						((VetoableChangeListener)moreListeners.elementAt(sendAgain)).vetoableChange(reversion);
					} catch(PropertyVetoException e2) {
					}
				}
				throw e;
			}
		}
	}

	/** Fire a VetoableChangeEvent containing the old and new values of the property to all the listeners.
	 ** If any listener objects, a reversion event will be sent to
	 ** those listeners who received the initial event.
	 **
	 ** @param propertyName the name of the property that
	 ** changed.
	 ** @param oldVal the old value.
	 ** @param newVal the new value.
	 ** @exception PropertyVetoException if the change is vetoed.
	 **/
	public void fireVetoableChange(String propertyName, Object oldVal, Object newVal) throws PropertyVetoException {
		fireVetoableChange(new PropertyChangeEvent(source,propertyName,oldVal,newVal));
	}

	/** Fire a VetoableChangeEvent containing the old and new values of the property to all the listeners.
	 ** If any listener objects, a reversion event will be sent to
	 ** those listeners who received the initial event.
	 **
	 ** @param propertyName the name of the property that
	 ** changed.
	 ** @param oldVal the old value.
	 ** @param newVal the new value.
	 ** @exception PropertyVetoException if the change is vetoed.
	 **/
	public void fireVetoableChange(String propertyName, boolean oldVal, boolean newVal) throws PropertyVetoException {
		fireVetoableChange(new PropertyChangeEvent(source,propertyName,new Boolean(oldVal),new Boolean(newVal)));
	}

	/** Fire a VetoableChangeEvent containing the old and new values of the property to all the listeners.
	 ** If any listener objects, a reversion event will be sent to
	 ** those listeners who received the initial event.
	 **
	 ** @param propertyName the name of the property that
	 ** changed.
	 ** @param oldVal the old value.
	 ** @param newVal the new value.
	 ** @exception PropertyVetoException if the change is vetoed.
	 **/
	public void fireVetoableChange(String propertyName, int oldVal, int newVal) throws PropertyVetoException {
		fireVetoableChange(new PropertyChangeEvent(source,propertyName,new Integer(oldVal),new Integer(newVal)));
	}


	/** Tell whether the specified property is being listened on or not.
	 ** This will only return <code>true</code> if there are listeners
	 ** on all properties or if there is a listener specifically on this
	 ** property.
	 **
	 ** @param propertyName the property that may be listened on
	 ** @return whether the property is being listened on
	 **/
	public boolean hasListeners(String propertyName) {
		return listeners.size() > 0  || propertyListeners.get(propertyName) != null;
	}
}
