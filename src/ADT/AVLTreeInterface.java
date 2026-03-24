/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ADT;

/**
 *
 * @author Soh Lian Ze
 */

public interface AVLTreeInterface<T> {

    void insert(T key, Object ref);

    Object search(T key);

    void delete(T key);

    boolean contains(T key);

    boolean isEmpty();

    void clear();

}
