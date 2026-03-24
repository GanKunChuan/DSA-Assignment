/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT.superceded;

import ADT.DoublyLinkedList;
import ADT.ListInterface;
import java.util.Comparator;

/**
 *
 * @author user
 * @param <T>
 */

// ABANDONED


//wrapper class using adt            
public class SortedLinkedList<T /*extends Comparable<T>*/> implements SortedListInterface<T> {
// any object inside this class can be comparable, but if use comparable can only apply one sorting rule
    
    private ListInterface<T> list;
    private Comparator<T> comparator;       // allow for multiple sorting rules (scalable)

    public SortedLinkedList(Comparator<T> comparator) {
        this.list = new DoublyLinkedList<>();
        this.comparator = comparator;
    }

    // ================== ADD (SORTED INSERT) ==================
    @Override
    public boolean add(T element) {

        int position = 1;

        while (position <= list.getNumberOfEntries()) {
            T current = list.getEntry(position);

            if (comparator.compare(element, current) <= 0) {        // no compareTo()
                break;
            }
            position++;
        }
        // if element in some way less than the current looped element,
        //  break the loop to get the position(index) to be used on add method of original DoublyLinkedList.
        list.add(position, element);
        return true;
    }

    // ================== REMOVE ==================
    @Override
    public T remove(int position) {
        return list.remove(position);
    }

    // ================== GET ==================
    @Override
    public T getEntry(int position) {
        return list.getEntry(position);
    }

    // ================== CONTAINS ==================
    @Override
    public boolean contains(T element) {
        return list.contains(element);
    }

    // ================== SIZE ==================
    @Override
    public int size() {
        return list.getNumberOfEntries();
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
