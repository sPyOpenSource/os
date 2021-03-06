/* Copyright (C) 1999, 2000, 2002  Free Software Foundation

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

package java.awt;

import java.awt.event.*;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.awt.peer.ContainerPeer;
import java.awt.peer.LightweightPeer;


/* A somewhat incomplete class. */

public class Container extends Component
{
  /* Serialized fields from the serialization spec. */
  int ncomponents;
  Component[] component;
  LayoutManager layoutMgr;
  /* LightweightDispatcher dispatcher; */ // wtf?
  Dimension maxSize;
  int containerSerializedDataVersion;

  /* Anything else is non-serializable, and should be declared "transient". */
    transient ContainerListener containerListener;  

  /**
   * Default constructor for subclasses.
   */
  public Container()
  {
  }

  /**
   * Returns the number of components in this container.
   *
   * @return The number of components in this container.
   */
  public int getComponentCount()
  {
    return ncomponents;
  }

  /**
   * Returns the number of components in this container.
   *
   * @return The number of components in this container.
   *
   * @deprecated This method is deprecated in favor of 
   * <code>getComponentCount()</code>.
   */
  public int countComponents()
  {
    return ncomponents;
  }

  /**
   * Returns the component at the specified index.
   *
   * @param index The index of the component to retrieve.
   *
   * @return The requested component.
   *
   * @exception ArrayIndexOutOfBoundsException If the specified index is not
   * valid.
   */
  public Component getComponent (int n)
  {
    if (n < 0 || n >= ncomponents)
      throw new ArrayIndexOutOfBoundsException("no such component");
    return component[n];
  }

  /**
   * Returns an array of the components in this container.
   *
   * @return The components in this container.
   */
  public Component[] getComponents()
  {
    Component[] result = new Component[ncomponents];
    if (ncomponents > 0)
      System.arraycopy(component, 0, result, 0, ncomponents);
    return result;
  }

  /**
   * Returns the insets for this container, which is the space used for
   * borders, the margin, etc.
   *
   * @return The insets for this container.
   */
  public Insets getInsets()
    {
	if (peer == null)
	    return new Insets(0, 0, 0, 0);
	return ((ContainerPeer) peer).getInsets();
    }

  /**
   * Returns the insets for this container, which is the space used for
   * borders, the margin, etc.
   *
   * @return The insets for this container.
   *
   * @deprecated This method is deprecated in favor of
   * <code>getInsets()</code>.
   */
  public Insets insets()
    {
	return getInsets();
    }

  /**
   * Adds the specified component to this container at the end of the
   * component list.
   *
   * @param component The component to add to the container.
   *
   * @return The same component that was added.
   */
  public Component add (Component comp)
  {
    addImpl (comp, null, -1);
    return comp;
  }

  /**
   * Adds the specified component to the container at the end of the
   * component list.  This method should not be used. Instead, use
   * <code>add(Component, Object</code>.
   *
   * @param name FIXME
   * @param component The component to be added.
   *
   * @return The same component that was added.
   */
  public Component add (String name, Component comp)
  {
    addImpl (comp, name, -1);
    return comp;
  }

  /**
   * Adds the specified component to this container at the specified index
   * in the component list.
   *
   * @param component The component to be added.
   * @param index The index in the component list to insert this child
   * at, or -1 to add at the end of the list.
   *
   * @return The same component that was added.
   *
   * @param throws ArrayIndexOutOfBounds If the specified index is invalid.
   */
  public Component add (Component comp, int index)
  {
    addImpl (comp, null, index);
    return comp;
  }

  /**
   * Adds the specified component to this container at the end of the
   * component list.  The layout manager will use the specified constraints
   * when laying out this component.
   *
   * @param component The component to be added to this container.
   * @param constraints The layout constraints for this component.
   */
  public void add (Component comp, Object constraints)
  {
    addImpl (comp, constraints, -1);
  }

  /**
   * Adds the specified component to this container at the specified index
   * in the component list.  The layout manager will use the specified
   * constraints when layout out this component.
   *
   * @param component The component to be added.
   * @param constraints The layout constraints for this component.
   * @param index The index in the component list to insert this child
   * at, or -1 to add at the end of the list.
   *
   * @param throws ArrayIndexOutOfBounds If the specified index is invalid.
   */
  public void add (Component comp, Object constraints, int index)
  {
    addImpl (comp, constraints, index);
  }

  /**
   * This method is called by all the <code>add()</code> methods to perform
   * the actual adding of the component.  Subclasses who wish to perform
   * their own processing when a component is added should override this
   * method.  Any subclass doing this must call the superclass version of
   * this method in order to ensure proper functioning of the container.
   *
   * @param component The component to be added.
   * @param constraints The layout constraints for this component, or
   * <code>null</code> if there are no constraints.
   * @param index The index in the component list to insert this child
   * at, or -1 to add at the end of the list.
   *
   * @param throws ArrayIndexOutOfBounds If the specified index is invalid.
   */
  protected void addImpl (Component comp, Object constraints, int index)
  {
    if (index > ncomponents
	|| (index < 0 && index != -1)
	|| comp instanceof Window
	|| (comp instanceof Container
	    && ((Container) comp).isAncestorOf (this)))
      throw new IllegalArgumentException ();

    // Reparent component, and make sure component is instantiated if
    // we are.
    // @ Marco Winter
    // -- sehr seltsame Implementierung von Classpath: void remove(MenuComponent);
    //    if (comp.parent != null)
    //  comp.parent.remove (comp);
    comp.parent = this;
    if (peer != null)
      {
	comp.addNotify ();

	if (comp.isLightweight())
	  enableEvents(comp.eventMask);
      }

    invalidate ();

    if (component == null)
      component = new Component[4]; // FIXME, better initial size?

    // This isn't the most efficient implementation.  We could do less
    // copying when growing the array.  It probably doesn't matter.
    if (ncomponents >= component.length)
      {
	int nl = component.length * 2;
	Component[] c = new Component[nl];
	System.arraycopy (component, 0, c, 0, ncomponents);
	component = c;
      }
    if (index == -1)
      component[ncomponents++] = comp;
    else
      {
	System.arraycopy (component, index, component, index + 1,
			  ncomponents - index);
	component[index] = comp;
	++ncomponents;
      }

    // Notify the layout manager.
    if (layoutMgr != null)
      {
	  /*	if (layoutMgr instanceof LayoutManager2)
		{
		LayoutManager2 lm2 = (LayoutManager2) layoutMgr;
		lm2.addLayoutComponent (comp, constraints);
		}
		else */if (constraints instanceof String)
	  layoutMgr.addLayoutComponent ((String) constraints, comp);
	else
	  layoutMgr.addLayoutComponent (null, comp);
      }

    // Post event to notify of adding the container.
    ContainerEvent ce = new ContainerEvent (this,
					    ContainerEvent.COMPONENT_ADDED,
					    comp);
    getToolkit().getSystemEventQueue().postEvent(ce);
  }

  /**
   * Removes the component at the specified index from this container.
   *
   * @param index The index of the component to remove.
   */
  public void remove (int index)
  {
    Component r = component[index];

    r.removeNotify ();

    System.arraycopy (component, index + 1, component, index,
		      ncomponents - index - 1);
    component[--ncomponents] = null;

    invalidate ();

    if (layoutMgr != null)
      layoutMgr.removeLayoutComponent (r);

    // Post event to notify of adding the container.
    ContainerEvent ce = new ContainerEvent (this,
					    ContainerEvent.COMPONENT_REMOVED,
					    r);
    getToolkit().getSystemEventQueue().postEvent(ce);
  }

  /**
   * Removes the specified component from this container.
   *
   * @return component The component to remove from this container.
   */
  public void remove (Component comp)
  {
    for (int i = 0; i < ncomponents; ++i)
      {
	if (component[i] == comp)
	  {
	    remove (i);
	    break;
	  }
      }
  }

  /**
   * Removes all components from this container.
   */
  public void removeAll()
  {
    while (ncomponents > 0)
      remove (0);
  }

  /**
   * Returns the current layout manager for this container.
   *
   * @return The layout manager for this container.
   */
  public LayoutManager getLayout()
  {
    return layoutMgr;
  }

  /**
   * Sets the layout manager for this container to the specified layout
   * manager.
   *
   * @param mgr The new layout manager for this container.
   */
  public void setLayout(LayoutManager mgr)
  {
    layoutMgr = mgr;
    invalidate ();
  }

  /**
   * Layout the components in this container.
   */
  public void doLayout()
  {
    if (layoutMgr != null)
      layoutMgr.layoutContainer (this);
  }

  /**
   * Layout the components in this container.
   *
   * @deprecated This method is deprecated in favor of
   * <code>doLayout()</code>.
   */
  public void layout()
  {
    doLayout();
  }

  /**
   * Invalidates this container to indicate that it (and all parent
   * containers) need to be laid out.
   */
  public void invalidate()
  {
    super.invalidate ();
  }

  /**
   * Re-lays out the components in this container.
   */
  public void validate()
  {
    // FIXME: use the tree lock?
    synchronized (this)
      {
	if (! isValid ())
	  {
	    validateTree ();
	  }
      }
  }

  /**
   * Recursively validates the container tree, recomputing any invalid
   * layouts.
   */
  protected void validateTree()
  {
    if (valid)
      return; 

    ContainerPeer cPeer = null;
    if ((peer != null) && !(peer instanceof LightweightPeer))
      {
	cPeer = (ContainerPeer) peer;
	cPeer.beginValidate();
      }

    doLayout ();
    for (int i = 0; i < ncomponents; ++i)
      {
	Component comp = component[i];
	if (! comp.isValid ())
	  {
	    if (comp instanceof Container)
	      {
		((Container) comp).validateTree();
	      }
	    else
	      {
		component[i].validate();
	      }
	  }
      }

    /* children will call invalidate() when they are layed out. It
       is therefore imporant that valid is not set to true
       before after the children has been layed out. */
    valid = true;

    if (cPeer != null)
      cPeer.endValidate();
  }

    //  public void setFont(Font f)
    //  {
    //    super.setFont(f);
    // FIXME, should invalidate all children with font == null
    //  }

  /**
   * Returns the preferred size of this container.
   *
   * @return The preferred size of this container.
   */
  public Dimension getPreferredSize()
  {
    if (layoutMgr != null)
      return layoutMgr.preferredLayoutSize (this);
    else
      return super.getPreferredSize ();
  }

  /**
   * Returns the preferred size of this container.
   *
   * @return The preferred size of this container.
   * 
   * @deprecated This method is deprecated in favor of 
   * <code>getPreferredSize()</code>.
   */
  public Dimension preferredSize()
  {
    return getPreferredSize();
  }

  /**
   * Returns the minimum size of this container.
   *
   * @return The minimum size of this container.
   */
  public Dimension getMinimumSize()
  {
    if (layoutMgr != null)
      return layoutMgr.minimumLayoutSize (this);
    else
      return super.getMinimumSize ();
  }

  /**
   * Returns the minimum size of this container.
   *
   * @return The minimum size of this container.
   * 
   * @deprecated This method is deprecated in favor of 
   * <code>getMinimumSize()</code>.
   */
  public Dimension minimumSize()
  {
    return getMinimumSize();
  }

  /**
   * Returns the maximum size of this container.
   *
   * @return The maximum size of this container.
   */
  public Dimension getMaximumSize()
  {
      /*    if (layoutMgr != null && layoutMgr instanceof LayoutManager2)
	    {
	    LayoutManager2 lm2 = (LayoutManager2) layoutMgr;
	    return lm2.maximumLayoutSize (this);
	    }
	    else*/
      return super.getMaximumSize ();
  }

  /**
   * Returns the preferred alignment along the X axis.  This is a value
   * between 0 and 1 where 0 represents alignment flush left and
   * 1 means alignment flush right, and 0.5 means centered.
   *
   * @return The preferred alignment along the X axis.
   */
    /*  public float getAlignmentX()
	{
	if (layoutMgr instanceof LayoutManager2)
	{
	LayoutManager2 lm2 = (LayoutManager2) layoutMgr;
	return lm2.getLayoutAlignmentX (this);
	}
	else
	return super.getAlignmentX();
	}*/
	
  /**
   * Returns the preferred alignment along the Y axis.  This is a value
   * between 0 and 1 where 0 represents alignment flush top and
   * 1 means alignment flush bottom, and 0.5 means centered.
   *
   * @return The preferred alignment along the Y axis.
   */
    /*  public float getAlignmentY()
	{
	if (layoutMgr instanceof LayoutManager2)
	{
	LayoutManager2 lm2 = (LayoutManager2) layoutMgr;
	return lm2.getLayoutAlignmentY (this);
	}
	else
	return super.getAlignmentY();
	}*/

  /**
   * Paints this container.  The implementation of this method in this
   * class forwards to any lightweight components in this container.  If
   * this method is subclassed, this method should still be invoked as
   * a superclass method so that lightweight components are properly
   * drawn.
   *
   * @param graphics The graphics context for this paint job.
   */
  public void paint(Graphics g)
  {
      //Naming naming = InitialNaming.getInitialNaming();
      //Debug.out = new DebugPrintStream(new DebugOutputStream((DebugChannel) naming.lookup("DebugChannel0")));
      //Debug.out.println("+++ called paint() from within Container!");
    if (!isShowing())
      return;
    super.paint(g);
    visitChildren(g, GfxPaintVisitor.INSTANCE, true);
  }

  /** 
   * Perform a graphics operation on the children of this container.
   * For each applicable child, the visitChild() method will be called
   * to perform the graphics operation.
   *
   * @param gfx The graphics object that will be used to derive new
   * graphics objects for the children.
   *
   * @param visitor Object encapsulating the graphics operation that
   * should be performed.
   *
   * @param lightweightOnly If true, only lightweight components will
   * be visited.
   */
  private void visitChildren(Graphics gfx, GfxVisitor visitor,
			     boolean lightweightOnly)
  {
    // FIXME: do locking

      //Naming naming = InitialNaming.getInitialNaming();
      //Debug.out = new DebugPrintStream(new DebugOutputStream((DebugChannel) naming.lookup("DebugChannel0")));
      //Debug.out.println("+++ called visitChildren from within Container!");
    for (int i = 0; i < ncomponents; ++i)
      {
	Component comp = component[i];
	boolean applicable = comp.isVisible()
	  && (comp.isLightweight() || !lightweightOnly);

	if (applicable) {
	    //Debug.out.println("++ visiting Component " + comp);
	  visitChild(gfx, visitor, comp);
	}
      }
  }

  /**
   * Perform a graphics operation on a child. A translated and clipped
   * graphics object will be created, and the visit() method of the
   * visitor will be called to perform the operation.
   *
   * @param gfx The graphics object that will be used to derive new
   * graphics objects for the child.
   *
   * @param visitor Object encapsulating the graphics operation that
   * should be performed.
   *
   * @param comp The child component that should be visited.
   */
  private void visitChild(Graphics gfx, GfxVisitor visitor,
			  Component comp)
  {
      Rectangle bounds = comp.getBounds();
      Rectangle clip = gfx.getClipBounds().intersection(bounds);
    
      //if (clip.isEmpty()) return;

    Graphics gfx2 = gfx.create();
    gfx2.setClip(clip.x, clip.y, clip.width, clip.height);
    gfx2.translate(bounds.x, bounds.y);
    
    visitor.visit(comp, gfx2);
  }

  /**
   * Updates this container.  The implementation of this method in this
   * class forwards to any lightweight components in this container.  If
   * this method is subclassed, this method should still be invoked as
   * a superclass method so that lightweight components are properly
   * drawn.
   *
   * @param graphics The graphics context for this update.
   */
  public void update(Graphics g)
  {
    super.update(g);
  }

  /**
   * Prints this container.  The implementation of this method in this
   * class forwards to any lightweight components in this container.  If
   * this method is subclassed, this method should still be invoked as
   * a superclass method so that lightweight components are properly
   * drawn.
   *
   * @param graphics The graphics context for this print job.
   */
  public void print(Graphics g)
  {
    super.print(g);
    visitChildren(g, GfxPrintVisitor.INSTANCE, true);
  }

  /**
   * Paints all of the components in this container.
   *
   * @param graphics The graphics context for this paint job.
   */
  public void paintComponents(Graphics g)
  {
    super.paint(g);
    visitChildren(g, GfxPaintAllVisitor.INSTANCE, true);
  }

  /**
   * Prints all of the components in this container.
   *
   * @param graphics The graphics context for this print job.
   */
  public void printComponents(Graphics g)
  {
    super.paint(g);
    visitChildren(g, GfxPrintAllVisitor.INSTANCE, true);
  }

    void dispatchEventImpl(AWTEvent e)
    {
	if ((e.id <= ContainerEvent.CONTAINER_LAST
	     && e.id >= ContainerEvent.CONTAINER_FIRST)
	    && (containerListener != null
		|| (eventMask & AWTEvent.CONTAINER_EVENT_MASK) != 0))
	    processEvent(e); 
	else
	    super.dispatchEventImpl(e);
    }
    
  /**
   * Adds the specified container listener to this object's list of
   * container listeners.
   *
   * @param listener The listener to add.
   */
    public synchronized void addContainerListener(ContainerListener l)
    {
	containerListener = AWTEventMulticaster.add (containerListener, l);
    }

  /**
   * Removes the specified container listener from this object's list of
   * container listeners.
   *
   * @param listener The listener to remove.
   */
    public synchronized void removeContainerListener(ContainerListener l)
    {
	containerListener = AWTEventMulticaster.remove(containerListener, l);
    }
	
  /** @since 1.3 */
    /*  public EventListener[] getListeners(Class listenerType)
	{
	if (listenerType == ContainerListener.class)
	return getListenersImpl(listenerType, containerListener);
	else return super.getListeners(listenerType);
	}*/

  /**
   * Processes the specified event.  This method calls
   * <code>processContainerEvent()</code> if this method is a
   * <code>ContainerEvent</code>, otherwise it calls the superclass
   * method.
   *
   * @param event The event to be processed.
   */
    protected void processEvent(AWTEvent e)
    {
	if (e instanceof ContainerEvent)
	    processContainerEvent((ContainerEvent) e);
	else super.processEvent(e);
    }

  /**
   * Called when a container event occurs if container events are enabled.
   * This method calls any registered listeners.
   *
   * @param event The event that occurred.
   */
    protected void processContainerEvent(ContainerEvent e)
    {
	if (containerListener == null)
	    return;
	switch (e.id)
	    {
	    case ContainerEvent.COMPONENT_ADDED:
		containerListener.componentAdded(e);
		break;
		
	    case ContainerEvent.COMPONENT_REMOVED:
		containerListener.componentRemoved(e);
		break;    
	    }
    }

  /**
   * AWT 1.0 event processor.
   *
   * @param event The event that occurred.
   *
   * @deprecated This method is deprecated in favor of 
   * <code>dispatchEvent()</code>.
   */
    /*  public void deliverEvent(Event e)
	{
	}*/

  /**
   * Returns the component located at the specified point.  This is done
   * by checking whether or not a child component claims to contain this
   * point.  The first child component that does is returned.  If no
   * child component claims the point, the container itself is returned,
   * unless the point does not exist within this container, in which
   * case <code>null</code> is returned.
   *
   * @param x The X coordinate of the point.
   * @param y The Y coordinate of the point.
   *
   * @return The component containing the specified point, or
   * <code>null</code> if there is no such point.
   */
  public Component getComponentAt (int x, int y)
  {
    if (! contains (x, y))
      return null;
    for (int i = 0; i < ncomponents; ++i)
      {
	// Ignore invisible children...
	if (!component[i].isVisible())
	  continue;

	int x2 = x - component[i].x;
	int y2 = y - component[i].y;
	if (component[i].contains (x2, y2))
	  return component[i];
      }
    return this;
  }

  /**
   * Returns the component located at the specified point.  This is done
   * by checking whether or not a child component claims to contain this
   * point.  The first child component that does is returned.  If no
   * child component claims the point, the container itself is returned,
   * unless the point does not exist within this container, in which
   * case <code>null</code> is returned.
   *
   * @param point The point to return the component at.
   *
   * @return The component containing the specified point, or <code>null</code>
   * if there is no such point.
   *
   * @deprecated This method is deprecated in favor of
   * <code>getComponentAt(int, int)</code>.
   */
  public Component locate(int x, int y)
  {
    return getComponentAt(x, y);
  }

  /**
   * Returns the component located at the specified point.  This is done
   * by checking whether or not a child component claims to contain this
   * point.  The first child component that does is returned.  If no
   * child component claims the point, the container itself is returned,
   * unless the point does not exist within this container, in which
   * case <code>null</code> is returned.
   *
   * @param point The point to return the component at.
   *
   * @return The component containing the specified point, or <code>null</code>
   * if there is no such point.
   */
    public Component getComponentAt(Point p)
    {
        return getComponentAt(p.x, p.y);
    }
    
  public Component findComponentAt (int x, int y)
  {
    if (! contains (x, y))
      return null;

    for (int i = 0; i < ncomponents; ++i)
      {
	// Ignore invisible children...
	if (!component[i].isVisible())
	  continue;

	int x2 = x - component[i].x;
	int y2 = y - component[i].y;
	// We don't do the contains() check right away because
	// findComponentAt would redundantly do it first thing.
	if (component[i] instanceof Container)
	  {
	    Container k = (Container) component[i];
	    Component r = k.findComponentAt (x2, y2);
	    if (r != null)
	      return r;
	  }
	else if (component[i].contains (x2, y2))
	  return component[i];
      }

    return this;
  }

  public Component findComponentAt(Point p)
  {
    return findComponentAt(p.x, p.y);
  }

  /**
   * Called when this container is added to another container to inform it
   * to create its peer.  Peers for any child components will also be
   * created.
   */
  public void addNotify ()
  {
    addNotifyContainerChildren ();
    super.addNotify();
  }

  private void addNotifyContainerChildren()
  {
    for (int i = ncomponents;  --i >= 0; )
      {
	component[i].addNotify();
	if (component[i].isLightweight())
	  enableEvents(component[i].eventMask);
      }
  }

  /**
   * Called when this container is removed from its parent container to
   * inform it to destroy its peer.  This causes the peers of all child
   * component to be destroyed as well.
   */
  public void removeNotify()
  {
    for (int i = 0; i < ncomponents; ++i)
      component[i].removeNotify ();
    super.removeNotify();
  }

  /**
   * Tests whether or not the specified component is contained within
   * this components subtree.
   *
   * @param component The component to test.
   *
   * @return <code>true</code> if this container is an ancestor of the
   * specified component, <code>false</code>.
   */
  public boolean isAncestorOf (Component comp)
  {
    for (;;)
      {
	if (comp == null)
	  return false;
	if (comp == this)
	  return true;
	comp = comp.getParent();
      }
  }

  /**
   * Returns a string representing the state of this container for
   * debugging purposes.
   *
   * @return A string representing the state of this container.
   */
  protected String paramString()
  {
    String param = super.paramString();
    if (layoutMgr != null)
      param = param + "," + layoutMgr.getClass().getName();

    return param;
  }

  /**
   * Writes a listing of this container to the specified stream starting
   * at the specified indentation point.
   *
   * @param stream The <code>PrintStream</code> to write to.
   * @param indent The indentation point.
   */
  public void list (PrintStream out, int indent)
  {
    super.list (out, indent);
    for (int i = 0; i < ncomponents; ++i)
      component[i].list (out, indent + 2);
  }

  /**
   * Writes a listing of this container to the specified stream starting
   * at the specified indentation point.
   *
   * @param stream The <code>PrintWriter</code> to write to.
   * @param indent The indentation point.
   */
  public void list(PrintWriter out, int indent)
  {
    super.list (out, indent);
    for (int i = 0; i < ncomponents; ++i)
      component[i].list (out, indent + 2);
  }


  /* The following classes are used in concert with the
     visitChildren() method to implement all the graphics operations
     that requires traversal of the containment hierarchy. */

  abstract static class GfxVisitor
  {
    public abstract void visit(Component c, Graphics gfx);
  }

  static class GfxPaintVisitor extends GfxVisitor
  {
    public void visit(Component c, Graphics gfx) {
	//Naming naming = InitialNaming.getInitialNaming();
	//Debug.out = new DebugPrintStream(new DebugOutputStream((DebugChannel) naming.lookup("DebugChannel0")));
	//Debug.out.println("+++ called visit() from within Container!");
	//Debug.out.println("++ painting component " + c);

	c.paint(gfx);
}
    public static final GfxVisitor INSTANCE = new GfxPaintVisitor();
  }

  static class GfxPrintVisitor extends GfxVisitor
  {
    public void visit(Component c, Graphics gfx) { c.print(gfx); }
    public static final GfxVisitor INSTANCE = new GfxPrintVisitor();
  }

  static class GfxPaintAllVisitor extends GfxVisitor
  {
    public void visit(Component c, Graphics gfx) { c.paintAll(gfx); }
    public static final GfxVisitor INSTANCE = new GfxPaintAllVisitor();
  }

  static class GfxPrintAllVisitor extends GfxVisitor
  {
    public void visit(Component c, Graphics gfx) { c.printAll(gfx); }
    public static final GfxVisitor INSTANCE = new GfxPrintAllVisitor();
  }

  // This is used to implement Component.transferFocus.
  Component findNextFocusComponent (Component child)
  {
    int start, end;
    if (child != null)
      {
	for (start = 0; start < ncomponents; ++start)
	  {
	    if (component[start] == child)
	      break;
	  }
	end = start;
	// This special case lets us be sure to terminate.
	if (end == 0)
	  end = ncomponents;
	++start;
      }
    else
      {
	start = 0;
	end = ncomponents;
      }

    //    System.out.println("findNextFocusComponent: parent = " + parent + ",start = " + start + ", end = " + end);

    for (int j = start; j != end; ++j)
      {
	if (j >= ncomponents)
	  {
	    // The JCL says that we should wrap here.  However, that
	    // seems wrong.  To me it seems that focus order should be
	    // global within in given window.  So instead if we reach
	    // the end we try to look in our parent, if we have one.
	    if (parent != null) {
	      Component c = parent.findNextFocusComponent (this);
	      // @ Marco Winter
	      // the method should only return if c is not null
	      // instead of returning always
	      if (c != null)
		  return c;
	    }
	    j -= ncomponents;
	  }
	//	  System.out.println("checking component " + component[j]);
	if (component[j] instanceof Container)
	  {
	    Component c = component[j];
	    c = c.findNextFocusComponent (null);
	    if (c != null)
	      return c;
	  }
	else if (component[j].isFocusTraversable ())
	  return component[j];
      }

    return null;
  }
}
