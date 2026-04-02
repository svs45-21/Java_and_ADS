class BTNode {
    int data;
    BTNode left, right;

    BTNode(int data) {
        this.data = data;
    }
}

public class CopyAndEraseBinaryTree {
    BTNode root;

    // Deep copy
    BTNode copyTree(BTNode node) {
        if (node == null) return null;
        BTNode newNode = new BTNode(node.data);
        newNode.left = copyTree(node.left);
        newNode.right = copyTree(node.right);
        return newNode;
    }

    // Erase all nodes (allow GC)
    BTNode eraseTree(BTNode node) {
        if (node == null) return null;
        node.left = eraseTree(node.left);
        node.right = eraseTree(node.right);
        // help GC
        node.left = null;
        node.right = null;
        return null;
    }

    void inorder(BTNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.data + " ");
        inorder(node.right);
    }

    public static void main(String[] args) {
        CopyAndEraseBinaryTree tree = new CopyAndEraseBinaryTree();
        tree.root = new BTNode(10);
        tree.root.left = new BTNode(5);
        tree.root.right = new BTNode(15);

        BTNode other = tree.copyTree(tree.root);
        System.out.print("Original: ");
        tree.inorder(tree.root);
        System.out.println();
        System.out.print("Copy: ");
        tree.inorder(other);
        System.out.println();

        tree.root = tree.eraseTree(tree.root);
        System.out.println("After erase, root = " + tree.root);
    }
}