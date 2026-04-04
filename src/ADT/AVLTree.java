/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT;

import java.util.Iterator;

/**
 *
 * @author Soh Lian Ze
 * @param <K>
 * @param <T>
 */
public class AVLTree<K extends Comparable<K>, T> implements BinarySearchTreeInterface<K, T> {
    private Node root;   // root of the tree
    private int size;    // number of elements
    
    private class Node {
        K key;      // used as the sorting
        T value;    // stores actual value
        Node left, right, parent;
        int height;     // the length of the longest path from this(any) node down to a leaf (starting value is 1)

        Node(K key, T value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent; 
            this.height = 1;        // leaf node height is 1 (minimum value)
        }
    }
    
    
    
    //SELF-BALANCING LOGIC
    private int height(Node n) {        //get height of the avl node in the tree
        return (n == null) ? 0 : n.height;    // height 0 mean node not exist
    }
    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }
    private int getBalance(Node n) {        //BALANCE FACTOR (should not more than +1 or -1)
        return height(n.left) - height(n.right);  // left - right
    }       // if negative, right side is taller than left side
            // if positive, left side taller than right side
    
    // BALANCE METHOD (based on balance factor (should not more than +1 or -1))
    private Node balance(Node node) {

        int bf = getBalance(node);  //balance factor

        // CASE 1: LEFT HEAVY (bf > 1)
        if (bf > 1) {   
            if (getBalance(node.left) < 0) {    // Check if it's LEFT-RIGHT case (child is right heavy)
                node.left = rotateLeft(node.left);      // Left-Right: rotate left child first
            }
            return rotateRight(node);       // Left-Left OR after fixing Left-Right: rotate right
        }

        // CASE 2: RIGHT HEAVY (bf < -1)
        if (bf < -1) {          
            if (getBalance(node.right) > 0) {    // Check if it's RIGHT-LEFT case (child is left heavy)
                node.right = rotateRight(node.right);       // Right-Left: rotate right child first
            }
            return rotateLeft(node);         // Right-Right OR after fixing Right-Left: rotate left
        }

        return node;        // Already balanced (bf = -1, 0, or 1)
    }
    
    //ROTATIONS
    // right rotate
    private Node rotateRight(Node y) {
        // y is the unbalanced node (left-heavy)
        // x becomes y's left child, which will become the new root of this subtree
        Node x = y.left; 
        Node T2 = x.right;  // T2 is the right subtree of x (will be moved to become y's left subtree)

        // Perform the actual rotation:
        x.right = y;    // 1. x's right becomes y
        y.left = T2;     // 2. y's left becomes T2 (the subtree that was originally x's right)
        
       // Fix parent pointers:
        x.parent = y.parent;     // x inherits y's parent (x moves up to where y was)
        y.parent = x;       // y becomes child of x
        
        if (T2 != null) {
            T2.parent = y;      // If T2 exists, its parent becomes y (since it's now attached to y)
        }
        
        // Reconnect the grandparent to point to x (new root of this subtree)
        if (x.parent != null) {     // x.parent is the grandparent (original parent of y)
            if (x.parent.left == y) {   // Check if y was left or right child of its parent
                x.parent.left = x;          // y was left child, so x becomes left child
            } else {
                x.parent.right = x;         // y was right child, so x becomes right child
            }
        }

        // Update heights: y first (now child), then x (new parent)
        updateHeight(y);
        updateHeight(x);

        return x;   // Return new root of this subtree (x)
    }
    // left rotate
    private Node rotateLeft(Node x) {
        // x is the unbalanced node (right-heavy)
        // y becomes x's right child, which will become the new root of this subtree
        Node y = x.right;   
        Node T2 = y.left;   // T2 is the left subtree of y (will be moved to become x's right subtree)

        // Perform the actual rotation:
        
        y.left = x;      // 1. y's left becomes x
        x.right = T2;    // 2. x's right becomes T2 (the subtree that was originally y's left)
        
        // Fix parent pointers:
        y.parent = x.parent;    // y inherits x's parent (y moves up to where x was)
        x.parent = y;           // x becomes child of y
        
        if (T2 != null) {
            T2.parent = x;      // If T2 exists, its parent becomes x (since it's now attached to x)
       }
        
        // Reconnect the grandparent to point to y (new root of this subtree)
        if (y.parent != null) {     // y.parent is the grandparent (original parent of x)
            if (y.parent.left == x) {   // Check if x was left or right child of its parent
                y.parent.left = y;          // x was left child, so y becomes left child
            } else {
                y.parent.right = y;         // x was right child, so y becomes right child
            }
        }
        
        // Update heights: x first (now child), then y (new parent)
        updateHeight(x);
        updateHeight(y);

        return y;       // Return new root of this subtree (y)
    }

    
    
    // -----------------------CORE OPERATIONS-----------------------------------------
    
    
    //ADD / INSERT
    @Override
    public boolean add(K key, T value) {
        if (key == null) return false;      // Reject null keys (can't be compared)
        
        int oldSize = size;         // Store original size to detect if insertion actually happened
        root = insert(root, key, value, null);  //use private internal insertion below
        //(Recursive insert - root may change after rebalancing)
        
        return size > oldSize;      // Return true if size increased (new element added)
    }
    // private internal recursive insertion below
    private Node insert(Node node, K key, T value, Node parent) {   // key and value not belong to node(root) here
        // node is root
        if (node == null) {     // BASE CASE: Found empty spot - create new node
            size++;             // Increment size for new element
            return new Node(key, value, parent);    // New node with parent reference
        }

        int cmp = key.compareTo(node.key);      // Compare key with current node's key
        
        // RECURSIVE CASES:
        // Recursively insert (goes down)
        if (cmp < 0) {      // Key is smaller,  go LEFT
            // Update left child reference (may change after rebalancing)
            //left of original root become new root(in this block), update whatever root comes back after insertion
            node.left = insert(node.left, key, value, node);    // this node become parent of next recursive
        } else if (cmp > 0) {       // Key is larger, go RIGHT
            // Update right child reference (may change after rebalancing)
            //right of original root become new root(in this block),update whatever root comes back after insertion
            node.right = insert(node.right, key, value, node);  // this node become parent of next recursive
        } else {
            return node; // Key already exists, duplicate, ignore insertion
        }
        
        // AFTER recursion returns (coming back UP)
        updateHeight(node);         // AFTER insertion: update height of current node
        //When you insert or delete, only nodes on the path from root to the changed node can become unbalanced.
        return balance(node);       // Rebalance if needed and return new root of this subtree
        
        //each level updates its height, balances itself if any, return new root to parent
    }
    
    
    
    //REMOVE
    private class Holder {      //wrapper class for remove to store
        Node node;          // This field will hold the removed node
        // java is pass-by-value, so you cannot directly update a variable across recursive calls
    }
    @Override
    public T remove(K key) {
        // Create a Holder object to capture the removed node as a whole (not just its key or value)
        // The recursive method can modify holder.node (object content)
        // but cannot reassign the holder reference itself
        Holder removed = new Holder(); //  // removed is a reference to an object to store removed node
        
        // Recursive remove - returns new root (may change after rebalancing)
        root = remove(root, key, removed);      // removed reference is copied

        if (root != null) root.parent = null;   // Fix root's parent pointer (root has no parent)
        
        if (removed.node != null) {  // If a node was actually removed
            size--;         // Decrement size
            return removed.node.value;      // Return the value of removed node
        }
        return null;        // Key not found
    }
    // private internal removal
    private Node remove(Node node, K key, Holder removed) {
        // BASE CASE: key not found
        if (node == null) return null;
        
        // Compare key with current node's key
        int cmp = key.compareTo(node.key);
        
        // CASE 1: key is smaller, go LEFT
        if (cmp < 0) {    
            // Recursively remove from left subtree
            node.left = remove(node.left, key, removed);
            // Fix parent pointer of left child (if exists)
            if (node.left != null) node.left.parent = node;
            
        // CASE 2: key is larger → go RIGHT
        } else if (cmp > 0) {
            // Recursively remove from right subtree
            node.right = remove(node.right, key, removed);
            // Fix parent pointer of right child (if exists)
            if (node.right != null) node.right.parent = node;
            
        } else {    
        // CASE 3: FOUND the node to remove
            removed.node = node;        // Store removed node in Holder (to return value later)

            // Subcase A: No left child (0 or 1 child, on right)
            if (node.left == null) {
                Node temp = node.right;       // Could be null or right child
                if (temp != null) temp.parent = node.parent;        // Update parent pointer of child
                return temp;        // Replace node with its right child
            } else if (node.right == null) {
            // Subcase B: No right child (only left child)
                Node temp = node.left;      
                if (temp != null) temp.parent = node.parent;        // Update parent pointer of child
                return temp;            // Replace node with its left child
            }

            // Case 3: two children, use successor (smallest greater node)
            Node successor = getMinNode(node.right);    // Find successor (smallest node in right subtree)
            
            // Copy successor's data into current node
            node.key = successor.key;
            node.value = successor.value;
            
            // Remove the successor (it now has at most one child)
            Holder dummy = new Holder(); // don't overwrite original removed node
            node.right = remove(node.right, successor.key, dummy);
            // Fix parent pointer of right child
            if (node.right != null) node.right.parent = node;
        }

        updateHeight(node);     // AFTER removal: update height of current node
        return balance(node);   // Rebalance if needed and return new root of this subtree
    }
    
    // helper function  - Find node with smallest key in a subtree
    private Node getMinNode(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;  // Keep going left until no more left children
        return node;
    }
    
    
    
    
    
    //SEARCH
    @Override
    public T get(K key) {  // - Iterative approach (no recursion needed)
        Node current = root;        // Start from the root

        while (current != null) {    // Keep going until we find the key or reach null
            int cmp = key.compareTo(current.key);   // Compare key with current node's key

            if (cmp == 0) return current.value;     // 0 = match, Found it! Return the value
            else if (cmp < 0) current = current.left;     // Key is smaller, go LEFT
            else current = current.right;                 // Key is larger, go RIGHT
        }

        return null;        // Key not found in tree
    }
    
    
    //CONTAINS   - Checks if a key exists in the tree
    @Override
    public boolean contains(K key) {    // SAME LOGIC WITH GET BUT THIS ONE RETURN BOOLEAN NOT VALUE
        Node current = root;        

        while (current != null) {
            int cmp = key.compareTo(current.key);

            if (cmp == 0) return true;
            else if (cmp < 0) current = current.left;
            else current = current.right;
        }

        return false;
    }
    

    
    
    //REPLACE   - Updates value for an existing key
    @Override
    public boolean replace(K key, T newValue) {
        Node node = root;       // Start from root

         // Search for the key (same logic as get() and contains())
        while (node != null) {
            int cmp = key.compareTo(node.key);

            if (cmp == 0) {             // Key found - update the value
                node.value = newValue;  
                return true;            // Replacement successful
            } else if (cmp < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;           // Key not found - nothing to replace
    }
    
    
    
    //ISEMPTY
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    
    //SIZE
    @Override
    public int size() {
        return size;
    }
    
    
    //CLEAR
    @Override
    public void clear() {       //RESET
        root = null;    // Discard entire tree (all node unaccessible - garbage collector will clean up)    
        size = 0;   
    }
    
    
    
    //SPECIAL OPERATIONS FOR QUEUE USE
    //GET MIN - Returns the smallest key's value
    @Override
    public T getMin() {
        if (root == null) return null;      // Empty tree
        
        Node current = root;    //start from root
        while (current.left != null) {      // Keep going left until no more left children
            current = current.left;
        }
        return current.value;           // Leftmost node has smallest key
    }
    //GET MAX - Returns the largest key's value
    @Override
    public T getMax() {
        if (root == null) return null;      // Empty tree
        
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;       // Rightmost node has largest key
    }
   
    
    
    //INORDER ITERATOR
    @Override
    public Iterator<T> inOrderIterator() {
        // Return a new iterator that traverses in sorted order (smallest to largest)
        return new InOrderIterator(root);   
    }   
    //PRIVATE ITERATOR CLASS - Implementation hidden from clients
    private class InOrderIterator implements Iterator<T> {

        private Node nextNode;      // The next node to return

        public InOrderIterator(Node root) {  // Constructor: find the smallest (leftmost) node to start
            nextNode =  getMinNode(root);   // reuse helper method
        }
        @Override
        public boolean hasNext() {  // Check if there are more elements
            return nextNode != null;    // If nextNode exists, there's more
        }
        @Override
        public T next() {           // Return current element and advance to next
            Node current = nextNode;            // Store current node

            // move to next node
            nextNode = getSuccessor(current);   // Find next node for next call

            return current.value;       // Return current value
        }
    }
    
    
    // SHARED INTERNAL METHOD - Finds the next node in in-order traversal (left root right)
    // Used by both InOrderIterator and RangeIterator
    private Node getSuccessor(Node node) {

        // CASE 1: Has right subtree
        // Successor = smallest node in right subtree (leftmost)
        if (node.right != null) {
            return getMinNode(node.right);  // Reuses helper method
        }

        // CASE 2: No right subtree
        // Go UP using parent pointers until we come from a LEFT child
        Node parent = node.parent;
        while (parent != null && node == parent.right) {
            // If node is RIGHT child, keep going up
            node = parent;
            parent = parent.parent;
        }
        
        // When we come from a LEFT child, parent is the successor
        return parent;
    }
    
    
    
    
    //QUERY  - Returns iterator for keys in range [start, end]
    @Override
    public Iterator<T> getRange(K start, K end) {
        return new RangeIterator(start, end);
    }
    // RANGE ITERATOR - Traverses only keys between start and end (inclusive)
    private class RangeIterator implements Iterator<T> {
        private Node current;       // Current node in traversal
        private final K end;        // Upper bound (inclusive)

        public RangeIterator(K start, K end) {
            this.end = end;
            this.current = findStartNode(root, start);  // Find the first node >= start
        }

        @Override
        public boolean hasNext() {  // Still has elements if: node exists AND key <= end
            return current != null && current.key.compareTo(end) <= 0;
        }

        @Override
        public T next() {
            Node temp = current;                // Store current
            current = getSuccessor(current);    // Move to next
            return temp.value;                  // Return value
        }
    }
    // Helper: Find smallest node with key >= start (NOT SMALLEST NODE IN SUBTREE)
    private Node findStartNode(Node node, K start) {
        Node result = null;     // Candidate node (smallest node >= start)

        while (node != null) {
            int cmp = start.compareTo(node.key);

            if (cmp <= 0) {
                // Current node is >= start, potential candidate
                result = node;
                 // Try to find smaller node that is still >= start (go left)
                node = node.left;
            } else {
                // Current node < start → need larger key (go right)
                node = node.right;
            }
        }

        return result;      // Returns first node >= start, or null if none
    }
    
    
    
    
    @Override
    public Iterator<T> preOrderIterator() {
        throw new UnsupportedOperationException("Pre-order traversal not implemented for triage system");
    }

    @Override
    public Iterator<T> postOrderIterator() {
        throw new UnsupportedOperationException("Post-order traversal not implemented for triage system");
    }

}

