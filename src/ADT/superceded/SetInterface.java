/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ADT.superceded;

/**
 *
 * @author user
 */
public interface SetInterface<T> {
    // used for checking duplicates 
    // (patient NRIC, doctor id, medication id, duplicate medication within one prescription, even allergies)
    
    boolean add(T item);          // no duplicates
    
    boolean remove(T item);
    
    boolean contains(T item);
    
    int size();
    
    boolean isSubsetOf(SetInterface<T> anotherSet);  // renamed

    SetInterface<T> union(SetInterface<T> anotherSet);  // return new set

    SetInterface<T> intersection(SetInterface<T> anotherSet);
    
    boolean isEmpty();
    
    void clear();
}
