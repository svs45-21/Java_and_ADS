class PreThreadNode {
    int data;
    PreThreadNode left, right;
    boolean lThread, rThread;

    PreThreadNode(int data) {
        this.data = data;
        lThread = rThread = true;
    }
}

public class Q7_PreorderThreadedTree {
    PreThreadNode root;

    // Simple BST insert, then we will create preorder threads separately
    PreThreadNode insertBST(PreThreadNode node, int key) {
        if (node == null) return new PreThreadNode(key);
        if (key < node.data) {
            node.left = insertBST(node.left, key);
        } else if (key > node.data) {
            node.right = insertBST(node.right, key);
        }
        return node;
    }

    // Create preorder threads
    void createPreorderThreads() {
        root = createPreorderThreads(root, null);
    }

    PreThreadNode createPreorderThreads(PreThreadNode node, PreThreadNode prev) {
        if (node == null) return prev;
        if (prev != null && prev.rThread && prev.right == null) {
            prev.right = node;
        }

        if (node.left == null) {
            node.lThread = true;
        } else {
            node.lThread = false;
        }

        if (node.right == null) {
            node.rThread = true;
        } else {
            node.rThread = false;
        }

        PreThreadNode tempRight = node.right;
        prev = createPreorderThreads(node.left, node);
        prev = createPreorderThreads(tempRight, prev);
        return prev;
    }

    // Preorder traversal using threads (no stack, no recursion)
    void preorderTraversal() {
        PreThreadNode curr = root;
        while (curr != null) {
            System.out.print(curr.data + " ");
            if (!curr.lThread && curr.left != null) {
                curr = curr.left;
            } else if (!curr.rThread && curr.right != null) {
                curr = curr.right;
            } else {
                while (curr != null && (curr.rThread || curr.right == null)) {
                    curr = curr.right;
                }
                if (curr != null) curr = curr.right;
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Q7_PreorderThreadedTree t = new Q7_PreorderThreadedTree();
        int[] keys = {20, 10, 30, 5, 15, 25, 35};
        for (int k : keys) t.root = t.insertBST(t.root, k);
        t.createPreorderThreads();
        t.preorderTraversal();
    }
}