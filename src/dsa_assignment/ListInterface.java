package dsa_assignment;

public interface ListInterface<T> {
    void add(T element);
    void add(int index, T element);
    T remove(int index);
    T get(int index);
    int size();
    boolean isEmpty();
}
