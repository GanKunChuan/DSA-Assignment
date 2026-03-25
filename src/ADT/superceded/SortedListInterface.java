/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ADT.superceded;

/**
 *
 * @author Soh Lian Ze
 */
public interface SortedListInterface<T> {

    // excluded some methods like add(int, t) and replace from listinterface 
    //  because sortedlist is not meant to be manually intervened
    
    
    // using tree such as black red tree or interval tree may be more useful for query but redundant and hard for adt design
    
    boolean add(T element);    // auto-sorted

    T remove(int position);

    T getEntry(int position);

    boolean contains(T element);

    int size();  // numberOfEntries

    boolean isEmpty();

    void clear();
}
