package model;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractListModel;

public class ParticipantList<E> extends AbstractListModel<E> implements Iterable<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8821862783142765860L;
	
	private final ArrayList<E> participants = new ArrayList<E>();
	
	public boolean add(E e) {
		participants.add(e);
		fireIntervalAdded(this, participants.size() - 1, participants.size() - 1 );
		
		return true;
	}
	
	public boolean remove(Object o) {
		int i = participants.indexOf(o);
		participants.remove(o);
		fireIntervalRemoved(this, i, i);
		
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
