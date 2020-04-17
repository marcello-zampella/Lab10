package it.polito.tdp.porto;

import java.util.Comparator;

import it.polito.tdp.porto.model.Author;


public class ComparatorAutore implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Author a1=(Author) o1;
		Author a2 =(Author) o2;
		return a1.getLastname().compareTo(a2.getLastname());
		
	}

}
