/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ADT;

/**
 *
 * @author Soh Lian Ze
 * @param <T>
 */

public interface AVLTreeInterface<T> {

    void insert(T key, NodeReference ref);

    ListInterface<NodeReference> search(T key);

    void delete(T key, NodeReference ref);  // cater for avoiding deleting all node with same value

    boolean contains(T key);

    boolean isEmpty();

    void clear();

}