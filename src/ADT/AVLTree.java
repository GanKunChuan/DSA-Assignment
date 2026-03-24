/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT;

/**
 *
 * @author user
 * @param <T>
 */

public class AVLTree<T extends Comparable<T>> implements AVLTreeInterface<T>{

    private class Node {
        T key;
        Object ref;   // R stand for reference to DLL node (keep generic to avoid tight coupling)

        int height;
        Node left, right;

        Node(T key, Object ref) {
            this.key = key;
            this.ref = ref;
            this.height = 1;
        }
    }

    private Node root;

    // ================= HEIGHT =================
    private int height(Node n) {
        return (n == null) ? 0 : n.height;
    }

    private int getBalance(Node n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    // ================= ROTATIONS =================
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // ================= INSERT =================
    @Override
    public void insert(T key, Object ref) {
        root = insertRec(root, key, ref);
    }

    private Node insertRec(Node node, T key, Object ref) {
        if (node == null)
            return new Node(key, ref);

        if (key.compareTo(node.key) < 0)
            node.left = insertRec(node.left, key, ref);
        else if (key.compareTo(node.key) > 0)
            node.right = insertRec(node.right, key, ref);
        else
            return node; // duplicate ignored

        updateHeight(node);

        int balance = getBalance(node);

        // LL
        if (balance > 1 && key.compareTo(node.left.key) < 0)
            return rightRotate(node);

        // RR
        if (balance < -1 && key.compareTo(node.right.key) > 0)
            return leftRotate(node);

        // LR
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }
    
    
    // ================= DELETE =================
    @Override
    public void delete(T key) {
        root = deleteRec(root, key);
    }
    
    
    private Node deleteRec(Node node, T key) {

        if (node == null) return null;

        // 1. NORMAL BST DELETE
        if (key.compareTo(node.key) < 0) {
            node.left = deleteRec(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            node.right = deleteRec(node.right, key);
        } else {
            // FOUND NODE

            // case 1: no child or 1 child
            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    return null; // no child
                } else {
                    return temp; // one child
                }
            }

            // case 2: two children → use inorder successor
            Node successor = getMin(node.right);

            node.key = successor.key;
            node.ref = successor.ref;

            node.right = deleteRec(node.right, successor.key);
        }

        // 2. UPDATE HEIGHT
        updateHeight(node);

        // 3. REBALANCE
        int balance = getBalance(node);

        // LL
        if (balance > 1 && getBalance(node.left) >= 0)
            return rightRotate(node);

        // LR
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RR
        if (balance < -1 && getBalance(node.right) <= 0)
            return leftRotate(node);

        // RL
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    
    // ========= HELPER METHOD FOR SUCCESSOR
    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    
    // ================= SEARCH =================
    @Override
    public Object search(T key) {
        Node result = searchRec(root, key);
        return (result == null) ? null : result.ref;
    }

    private Node searchRec(Node node, T key) {
        if (node == null || key.equals(node.key))
            return node;

        if (key.compareTo(node.key) < 0)
            return searchRec(node.left, key);

        return searchRec(node.right, key);
    }
    
    
    // ================= CONTAINS =================
    @Override
    public boolean contains(T key) {
        return search(key) != null;
    }
    
    
    // ================= CLEAR =================
    @Override
    public void clear() {
        root = null;
    }

    
    // ================= ISEMPTY =================
    @Override
    public boolean isEmpty() {
        return root == null;
    }
    
    
    // ================= INORDER =================
    
    public ListInterface<Object> getInOrderList() {
        ListInterface<Object> result = new DoublyLinkedList<>();
        inOrderCollect(root, result);
        return result;
    }
    
    // recursive collector
    private void inOrderCollect(Node node, ListInterface<Object> list) {
        if (node != null) {
            inOrderCollect(node.left, list);
            list.add(node.ref);   // store DLL node reference
            inOrderCollect(node.right, list);
        }
    }
}

