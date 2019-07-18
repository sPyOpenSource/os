/* Set.java -- A collection that prohibits duplicates
   Copyright (C) 1998 Free Software Foundation, Inc.

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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


// TO DO:
// ~ Doc comments for everything.

package java.util;

public interface Set<E> extends Collection<E>
{
  boolean add(E o);
  boolean addAll(Collection<? extends E> c);
  void clear();
  boolean contains(Object o);
  boolean containsAll(Collection<?> c);
  boolean equals(Object o);
  int hashCode();
  boolean isEmpty();
  Iterator<E> iterator();
  boolean remove(Object o);
  boolean removeAll(Collection<?> c);
  boolean retainAll(Collection<?> c);
  int size();
  Object[]toArray();

 //   <T> T[] toArray(T[] a);
}
