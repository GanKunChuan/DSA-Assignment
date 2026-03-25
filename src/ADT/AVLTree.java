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

    private class AVLNode {
        T key;
        //NodeReference ref;   // reference to DLL node
        ListInterface<NodeReference> refs;  // multiple patients, assume case like same names (duplicate but not)

        int height;
        AVLNode left, right;

        AVLNode(T key, NodeReference ref) {
            this.key = key;
            //this.ref = ref;
            this.refs = new DoublyLinkedList<>();
            this.refs.add(ref);
            this.height = 1;
        }
    }

    private AVLNode root;

    // ================= HEIGHT =================
    private int height(AVLNode n) {
        return (n == null) ? 0 : n.height;
    }

    private int getBalance(AVLNode n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(AVLNode n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    // ================= ROTATIONS =================
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // ================= INSERT =================
    @Override
    public void insert(T key, NodeReference ref) {
        root = insertRec(root, key, ref);
    }

    private AVLNode insertRec(AVLNode node, T key, NodeReference ref) {
        if (node == null)
            return new AVLNode(key, ref);

        if (key.compareTo(node.key) < 0){
            node.left = insertRec(node.left, key, ref);
        } else if (key.compareTo(node.key) > 0){
            node.right = insertRec(node.right, key, ref);
        } else {
            node.refs.add(ref); // store duplicate
            return node; 
        }

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
    public void delete(T key, NodeReference ref) {
        root = deleteRec(root, key, ref);
    }
    
    
    private AVLNode deleteRec(AVLNode node, T key, NodeReference ref) {

        if (node == null) return null;

        // 1. NORMAL BST DELETE
        if (key.compareTo(node.key) < 0) {
            node.left = deleteRec(node.left, key, ref);
        } else if (key.compareTo(node.key) > 0) {
            node.right = deleteRec(node.right, key, ref);
        } else {
            // FOUND NODE
            
            // STEP 1: Remove ONLY the specific reference
            ListInterface<NodeReference> list = node.refs;
            int n = list.getNumberOfEntries();

            for (int i = 1; i <= n; i++) {
                if (list.getEntry(i) == ref) {   // reference comparison, use == to compare reference not equals()
                    list.remove(i);
                    break;
                }
            }

            // STEP 2: If still has other refs → STOP here
            if (!list.isEmpty()) {
                return node;
            }

            // STEP 3: If empty → remove node like normal AVL
            //--
            // case 1: no child or 1 child
            if (node.left == null || node.right == null) {
                AVLNode temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    return null; // no child
                } else {
                    return temp; // one child
                }
            }

            // case 2: two children → use inorder successor
            AVLNode successor = getMin(node.right);

            node.key = successor.key;
            node.refs = successor.refs;

            node.right = deleteRec(node.right, successor.key, successor.refs.getEntry(1));
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
    private AVLNode getMin(AVLNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    
    // ================= SEARCH =================
    @Override
    public ListInterface<NodeReference> search(T key) {
        AVLNode result = searchRec(root, key);
        return (result == null) ? null : result.refs;
    }

    private AVLNode searchRec(AVLNode node, T key) {
        if (node == null || key.compareTo(node.key) == 0)
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
    
    public ListInterface<NodeReference> getInOrderList() {
        ListInterface<NodeReference> result = new DoublyLinkedList<>();
        inOrderCollect(root, result);
        return result;
    }
    
    // recursive collector
    private void inOrderCollect(AVLNode node, ListInterface<NodeReference> list) {
        if (node != null) {
            inOrderCollect(node.left, list);
            //list.add(node.ref);   // store DLL node reference
            
            ListInterface<NodeReference> refs = node.refs;
            int n = refs.getNumberOfEntries();

            for (int i = 1; i <= n; i++) {
                list.add(refs.getEntry(i));
            }
            
            inOrderCollect(node.right, list);
        }
    }
}

