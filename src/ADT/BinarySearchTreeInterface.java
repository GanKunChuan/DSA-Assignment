/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ADT;

import java.util.Iterator;

/**
 *
 * @author Soh Lian Ze
 * @param <K>
 * @param <T>
 */
public interface BinarySearchTreeInterface<K extends Comparable<K>, T> {

    // Core operations
    boolean add(K key, T value);

    T remove(K key);

    T get(K key);

    boolean contains(K key);
    
    boolean replace(K key, T value);
    
    boolean isEmpty();

    int size();

    void clear();
    
    // Boundary access (for queue / scheduling use)
    T getMin();   // earliest / lowest key
    T getMax();   // highest priority / latest


    // iterator Traversal 
    Iterator<T> inOrderIterator();  // left - root - right
    Iterator<T> preOrderIterator();     // Root - Left - Right (irrelevant to our system not implemented)
    Iterator<T> postOrderIterator();    // Left - Right - Root (irrelevant to our system not implemented)
    
    //query range
    Iterator<T> getRange(K start, K end);

}
