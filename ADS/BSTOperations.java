class BSTNode {
    int key;
    BSTNode left, right;

    BSTNode(int key) {
        this.key = key;
    }
}

public class BSTOperations {
    BSTNode root;

    // Insert
    BSTNode insert(BSTNode node, int key) {
        if (node == null) return new BSTNode(key);
        if (key < node.key) node.left = insert(node.left, key);
        else if (key > node.key) node.right = insert(node.right, key);
        return node;
    }

    // Count nodes on longest path (height)
    int height(BSTNode node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // Minimum value
    int minValue(BSTNode node) {
        if (node == null) throw new IllegalStateException("Empty tree");
        while (node.left != null) node = node.left;
        return node.key;
    }

    // Mirror tree (swap left/right at every node)
    void mirror(BSTNode node) {
        if (node == null) return;
        BSTNode temp = node.left;
        node.left = node.right;
        node.right = temp;
        mirror(node.left);
        mirror(node.right);
    }

    // Search
    boolean search(BSTNode node, int key) {
        if (node == null) return false;
        if (key == node.key) return true;
        if (key < node.key) return search(node.left, key);
        else return search(node.right, key);
    }

    // Inorder traversal
    void inorder(BSTNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.key + " ");
        inorder(node.right);
    }

    public static void main(String[] args) {
        BSTOperations bst = new BSTOperations();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) bst.root = bst.insert(bst.root, v);

        System.out.print("Inorder: ");
        bst.inorder(bst.root);
        System.out.println();

        // a. Insert new node
        bst.root = bst.insert(bst.root, 25);

        // b. Number of nodes in longest path (height)
        System.out.println("Height (nodes in longest path): " + bst.height(bst.root));

        // c. Minimum data value
        System.out.println("Minimum value: " + bst.minValue(bst.root));

        // d. Mirror tree
        bst.mirror(bst.root);
        System.out.print("Inorder after mirror: ");
        bst.inorder(bst.root);
        System.out.println();

        // e. Search value
        int key = 40;
        System.out.println("Search " + key + ": " + bst.search(bst.root, key));
    }
}