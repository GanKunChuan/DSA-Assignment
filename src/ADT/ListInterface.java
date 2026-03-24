/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ADT;

import java.util.Iterator;

/**
 *
 * @author user
 * @param <T>       
 */
public interface ListInterface<T> {
   //what need to be stored(all): patients, doctor, company, service, medication, prescription, visit...

    // A hollow interface that does nothing but act as a "Label", IMPORTANT
    interface NodeReference {}          // avoid using Object, and using extra generic type in parameter
    
    //boolean add(T element);
    public NodeReference add(T newEntry);       //while also return node(reference)
    
    //boolean add(int index, T element);
    public NodeReference add(int newPosition, T newEntry);      //while also return node(reference)
    
    T remove(int index);

    public T removeNode(NodeReference nodeReference);
    
    T getEntry(int index);
    
    boolean replace(int givenPosition, T newEntry);
    
    boolean contains(T anEntry);
    
    // boolean isFull();  there is no such thing as full list in linked data structure.
    
    int getNumberOfEntries(); //size();
    
    boolean isEmpty();
    
    void clear();
   
    Iterator<T> getIterator();

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
