/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ADT;

/**
 *
 * @author Soh Lian Ze
 */

public interface HashInterface<K, V> {

    void put(K key, V value);      // insert or update

    V get(K key);                  // retrieve value

    boolean contains(K key);       // check existence

    void remove(K key);            // delete entry

    boolean isEmpty();             // check if empty
    
    int size();

    void clear();                  // reset table
}