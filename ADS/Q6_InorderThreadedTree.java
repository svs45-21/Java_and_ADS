class ThreadedNode {
    int data;
    ThreadedNode left, right;
    boolean lThread; // true if left is thread
    boolean rThread; // true if right is thread

    ThreadedNode(int data) {
        this.data = data;
        lThread = rThread = true;
    }
}

public class Q6_InorderThreadedTree {
    ThreadedNode root;

    // Insert into normal BST but keep as non‑threaded structure first
    ThreadedNode insert(ThreadedNode root, int key) {
        if (root == null) return new ThreadedNode(key);
        ThreadedNode curr = root, parent = null;
        while (curr != null) {
            if (key == curr.data) return root;
            parent = curr;
            if (key < curr.data) {
                if (!curr.lThread) curr = curr.left;
                else break;
            } else {
                if (!curr.rThread) curr = curr.right;
                else break;
            }
        }
        ThreadedNode node = new ThreadedNode(key);
        if (key < parent.data) {
            node.left = parent.left;
            node.right = parent;
            parent.lThread = false;
            parent.left = node;
        } else {
            node.left = parent;
            node.right = parent.right;
            parent.rThread = false;
            parent.right = node;
        }
        return root;
    }

    // Inorder traversal without stack/recursion
    void inorder(ThreadedNode root) {
        if (root == null) return;
        ThreadedNode curr = leftMost(root);
        while (curr != null) {
            System.out.print(curr.data + " ");
            if (curr.rThread)
                curr = curr.right;
            else
                curr = leftMost(curr.right);
        }
    }

    ThreadedNode leftMost(ThreadedNode node) {
        if (node == null) return null;
        while (!node.lThread) node = node.left;
        return node;
    }

    public static void main(String[] args) {
        Q6_InorderThreadedTree tbt = new Q6_InorderThreadedTree();
        int[] keys = {20, 10, 30, 5, 15, 25, 35};
        for (int k : keys) {
            if (tbt.root == null) tbt.root = new ThreadedNode(k);
            else tbt.root = tbt.insert(tbt.root, k);
        }
        System.out.print("Inorder threaded traversal: ");
        tbt.inorder(tbt.root);
        System.out.println();
    }
}