/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ADT;

/**
 *
 * @author user
 */
public interface ListInterface<T> {
    void add(T element);
    void add(int index, T element);
    T remove(int index);
    T get(int index);
    int size();
    boolean isEmpty();
}
