/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT;

import java.util.Iterator;

/**
 *
 * @author Soh Lian Ze
 */
public class DoublyLinkedList<T> implements ListInterface<T> {
    //core ADT
    private Node firstNode;
    private Node lastNode;
    private int numberOfEntries;
    
    public DoublyLinkedList(){
        clear();
    }
    
    // ================== NODE ==================
    private class Node implements NodeReference {
        private T data;
        private Node next;
        private Node prev;

        private Node(T data) {
            this.data = data;
            next = null;
            prev = null;
        }
        
        /*private Node(T data, Node prev, Node next){
            this.data = data; 
            this.prev = prev; 
            this.next = next; 
        }*/
    }
    
    
    // ================== ITERATOR ==================
    private class ListIterator implements Iterator<T> {
        private Node currentNode = firstNode;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {       // extra check to avoid nullPointerException
                throw new java.util.NoSuchElementException();
            }
            T data = currentNode.data;      // before pointing to next, put the current data to be returned
            currentNode = currentNode.next;
            return data;
        }
    }
    
    @Override
    public Iterator<T> getIterator() {
        return new ListIterator();
    }
    
    
    // ================== HELPER ==================
    private Node getNodeAt(int position) {
        Node currentNode;
     

        // utilize reference firstNode lastNode at both end with prev and next to traverse in either of the directions, 
        // based on target position, if on the first half then traverse from front to last
        // else on the second half then traverse from last to front
        if (position <= numberOfEntries / 2) {
            currentNode = firstNode;
            for (int i = 1; i < position; i++) {
                currentNode = currentNode.next;
            }
        } else {
            currentNode = lastNode;
            for (int i = numberOfEntries; i > position; i--) {
                currentNode = currentNode.prev;
            }
        }

        return currentNode;
    }



    // ================== ADD (default) ====================
    
    // changed to return node instead of boolean because hash and avt need to store reference too
    
    @Override
    //public boolean add(T newEntry) {
    public NodeReference add(T newEntry){
        Node newNode = new Node(newEntry);

        if (isEmpty()) {
            firstNode = newNode;
            lastNode = newNode;
        } else {
            lastNode.next = newNode;
            newNode.prev = lastNode;
            lastNode = newNode;
        }

        numberOfEntries++;
        //return true;
        return newNode;   // to be used by hash and avl
    }

    // ================== ADD (POSITION) ==================
    @Override
    public NodeReference add(int newPosition, T newEntry) {
        if (newPosition < 1 || newPosition > numberOfEntries + 1) {
            return null;               // invalid range, straight away stop here
        }

        Node newNode = new Node(newEntry);

        if (newPosition == 1) { // insert at beginning
            newNode.next = firstNode;
            if (firstNode != null) {  //not empty list, then also point originally firstNode prev pointer to newNode
                firstNode.prev = newNode;  
            }
            firstNode = newNode;

            if (numberOfEntries == 0) {
                lastNode = newNode;
            }

        } else if (newPosition == numberOfEntries + 1) { // insert at end
            lastNode.next = newNode;
            newNode.prev = lastNode;
            lastNode = newNode;

        } else { // insert in middle
            Node nodeBefore = getNodeAt(newPosition - 1);  //one position before
            Node nodeAfter = nodeBefore.next; 
            // "cut open" the link at nth place
            newNode.next = nodeAfter;
            newNode.prev = nodeBefore;
            nodeBefore.next = newNode;
            nodeAfter.prev = newNode;
        }

        numberOfEntries++;
        return newNode;     
    }

    // ================== REMOVE ==================
    @Override
    public T remove(int givenPosition) {
        if (givenPosition < 1 || givenPosition > numberOfEntries) {
            return null;
        }

        Node nodeToRemove = getNodeAt(givenPosition);
        T data = nodeToRemove.data;

        if (numberOfEntries == 1) { // only one node
            firstNode = null;
            lastNode = null;

        } else if (givenPosition == 1) { // remove first
            firstNode = firstNode.next;
            firstNode.prev = null;

        } else if (givenPosition == numberOfEntries) { // remove last
            lastNode = lastNode.prev;
            lastNode.next = null;

        } else { // middle
            Node prevNode = nodeToRemove.prev;
            Node nextNode = nodeToRemove.next;

            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }

        numberOfEntries--;
        return data;
    }

    @Override
    public T removeNode(NodeReference nodeReference) {    // pointer-based approach removal for avl and hash
        // 1. Cast the generic reference back to your private Node class
        Node nodeToRemove = (Node) nodeReference;
        if (nodeToRemove == null) return null;

        T data = nodeToRemove.data;

        // If it's the only node
        if (numberOfEntries == 1) {
            firstNode = null;
            lastNode = null;
        } 
        // If it's the first node
        else if (nodeToRemove == firstNode) {
            firstNode = firstNode.next;
            firstNode.prev = null;
        } 
        // If it's the last node
        else if (nodeToRemove == lastNode) {
            lastNode = lastNode.prev;
            lastNode.next = null;
        } 
        // If it's in the middle (The real DLL power!)
        else {
            nodeToRemove.prev.next = nodeToRemove.next;
            nodeToRemove.next.prev = nodeToRemove.prev;
        }

        numberOfEntries--;
        return data;
    }    
    
    // ================== GET ==================
    @Override
    public T getEntry(int givenPosition) {
        if (givenPosition < 1 || givenPosition > numberOfEntries) {
            return null;
        }
        return getNodeAt(givenPosition).data;
    }
    

    // ================== REPLACE ==================
    @Override
    public boolean replace(int givenPosition, T newEntry) {
        if (givenPosition < 1 || givenPosition > numberOfEntries) {
            return false;
        }

        Node currentNode = getNodeAt(givenPosition);
        currentNode.data = newEntry;
        return true;
    }

    // ================== CONTAINS ==================
    @Override
    public boolean contains(T anEntry) {    //not only for checking certain entry but also for null
        //Node currentNode = firstNode;

        /*while (currentNode != null) {
            if (currentNode.data.equals(anEntry)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        */
        Iterator<T> it = getIterator();
        while (it.hasNext()) {
            T current = it.next();
            if ((current == null && anEntry == null) || (current != null && current.equals(anEntry))) {
                return true;        // anEntry == null  check put here because list can store nulls
            }
        }
        
        return false;
    }

    // ================== CLEAR ==================
    @Override
    public void clear() {      // for reseting + initializing purpose in testing
        firstNode = null;
        lastNode = null;
        numberOfEntries = 0;
    }

    // ================== SIZE ==================
    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    // ================== EMPTY ==================
    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

}
