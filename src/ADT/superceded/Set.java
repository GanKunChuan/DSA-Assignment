/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT.superceded;

import ADT.DoublyLinkedList;
import ADT.ListInterface;
import java.util.Iterator;
/**
 *
 * @author Soh Lian Ze
 */

// ABANDONED


//wrapper class using adt
public class Set<T> implements SetInterface<T>  {           // or MedicationSet<T>
    
    // used for checking duplicates 
    // (patient NRIC, doctor id, medication id, duplicate medication within one prescription, even allergies)
    
    private ListInterface<T> list;
    
    public Set() {
        list = new DoublyLinkedList<>();
    }

    
    // ================== ADD ==================
    @Override
    public boolean add(T item) {
        if (list.contains(item)) {
            return false; // duplicate not allowed
        }
        list.add(item);
        return true;
    }

    
    // ================== REMOVE ==================
    @Override
    public boolean remove(T element) {
        int position = 1;

        Iterator<T> it = list.getIterator();
        while (it.hasNext()) {
            T current = it.next();

            if ((current == null && element == null) ||
                (current != null && current.equals(element))) {

                list.remove(position);
                return true;
            }
            position++;
        }
        return false;
    }

    
    // ================== CONTAINS ==================
    @Override
    public boolean contains(T item) {
        return list.contains(item);
    }

    
    // ================== SIZE ==================
    @Override
    public int size() {
        return list.getNumberOfEntries();
    }
    
    
    // ================== SUBSET ==================
    @Override
    public boolean isSubsetOf(SetInterface<T> anotherSet) {
        Iterator<T> it = list.getIterator();

        while (it.hasNext()) {
            T current = it.next();
            if (!anotherSet.contains(current)) {
                return false;
            }
        }
        return true;
    }
    
    
    // ================== UNION ==================
    @Override
    public SetInterface<T> union(SetInterface<T> anotherSet) {
        Set<T> result = new Set<>();

        // add all from this set
        Iterator<T> it1 = list.getIterator();
        while (it1.hasNext()) {
            result.add(it1.next());
        }

        // add all from another set (duplicates auto prevented)
        Iterator<T> it2 = ((Set<T>) anotherSet).list.getIterator();
        while (it2.hasNext()) {
            result.add(it2.next());     //no container to receive returned false for duplicates anyway, still ok
        }

        return result;
    }

    
    // ================== INTERSECTION ==================
    @Override
    public SetInterface<T> intersection(SetInterface<T> anotherSet) {
        Set<T> result = new Set<>();

        Iterator<T> it = list.getIterator();
        while (it.hasNext()) {
            T current = it.next();
            if (anotherSet.contains(current)) {
                result.add(current);
            }
        }

        return result;
    }

    
    // ================== EMPTY ==================
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    
    // ================== CLEAR ==================
    @Override
    public void clear() {
        list.clear();
    }
}

