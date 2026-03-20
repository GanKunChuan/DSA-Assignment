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
    boolean add(T element);
    boolean add(int index, T element);
    T remove(int index);
    T getEntry(int index);
    boolean replace(int givenPosition, T newEntry);
    boolean contains(T anEntry);
    // boolean isFull();  there is no such thing as full list in linked data structure.
    int getNumberOfEntries(); //size();
    boolean isEmpty();
    void clear();
    
    //what need to be stored(all): patients, doctor, company, service, medication, prescription, visit....
}
    
    
  /* below are original abstract methods from original sample list interface:
    
  public boolean add(T newEntry);

  public boolean add(int newPosition, T newEntry);

  public T remove(int givenPosition);

  public void clear();

  public boolean replace(int givenPosition, T newEntry);

  public T getEntry(int givenPosition);

  public boolean contains(T anEntry);

  public int getNumberOfEntries();

  public boolean isEmpty();

  public boolean isFull();
    
 */
