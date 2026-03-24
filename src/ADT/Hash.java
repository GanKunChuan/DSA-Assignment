/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT;

/**
 *
 * @author user
 */
public class Hash<K, V> implements HashInterface<K, V>{

    private ListInterface[] table;
    private int size = 101;   //prime number

    public Hash() {
        clear();
        
    }
    
    private static class Entry<K, V> {
        K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % size;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);

        if (table[index] == null) {
            table[index] = new DoublyLinkedList<>();
        }

        @SuppressWarnings("unchecked")
        ListInterface<Entry<K,V>> list = (ListInterface<Entry<K,V>>) table[index];

        // check duplicate
        if (list != null) { // firstly null check for safety
            for (int i = 1; i <= list.getNumberOfEntries(); i++) {
                Entry<K,V> e = list.getEntry(i);

                if ((e.key == null && key == null) || (e.key != null && e.key.equals(key))) {
                    e.value = value; // update
                    return;
                }
            }
        }
        list.add(new Entry<>(key, value));  // Diamond operator <> ensures type safety

    }

    @Override
    public V get(K key) {
        int index = hash(key);
        
        @SuppressWarnings("unchecked")
        ListInterface<Entry<K,V>> list = (ListInterface<Entry<K,V>>) table[index];

        if (list == null) return null;

        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            Entry<K,V> e = list.getEntry(i);

            if ((e.key == null && key == null) || (e.key != null && e.key.equals(key))) {       
            //instead of equal() to avoid null crash
                return e.value;
            }
        }

        return null;
    }

    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }

    @Override
    public void remove(K key) {
        int index = hash(key);
        
        @SuppressWarnings("unchecked")
        ListInterface<Entry<K,V>> list = (ListInterface<Entry<K,V>>) table[index];

        if (list == null) return;

        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            Entry<K,V> e = list.getEntry(i);

            if ((e.key == null && key == null) || (e.key != null && e.key.equals(key))) {
                list.remove(i);
                return;
            }
        }
    }
    


    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size; i++) {
            if (table[i] != null && table[i].getNumberOfEntries() > 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        table = new ListInterface[size];
    }
}