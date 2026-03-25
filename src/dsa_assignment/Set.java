package dsa_assignment;

public class Set<T> {
    private ListInterface<T> list;

    public boolean add(T element) { return false; }      // check contains first, then add
    public boolean contains(T element) { return false; } // iterate list, match element
    public void remove(T element) { }                    // iterate list, match element, remove
}