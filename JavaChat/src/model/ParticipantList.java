package model;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractListModel;

public class ParticipantList<E> extends AbstractListModel<E> implements Iterable<E> {
	
	/**
	 * Manages list of active socket threads (active connections).
     *
     * add() and remove() fires events
	 */
	private static final long serialVersionUID = 8821862783142765860L;
	
	private final ArrayList<E> participants = new ArrayList<E>();
	
	public boolean add(E e) {
		participants.add(e);
		fireIntervalAdded(this, participants.size() - 1, participants.size() - 1 );
		
		return true;
	}
	
	public boolean remove(E e) {
		int i = participants.indexOf(e);
		participants.remove(e);
		fireIntervalRemoved(this, i, i);
		
		return true;
	}
	
	public void elementChanged(E e) {
		int i = participants.indexOf(e);
		
		fireContentsChanged(this, i, i);
	}
	
	public boolean removeAll(Iterable<E> c) {
		for ( E o : c) {
			remove(o);
		}
		
		return true;
	}
	
	public boolean isEmpty() {
		return participants.isEmpty();
	}

	@Override
	public E getElementAt(int arg0) {
		return participants.get(arg0);
	}

	@Override
	public int getSize() {
		return participants.size();
	}

	@Override
	public Iterator<E> iterator() {
		return participants.iterator();
	}

}
