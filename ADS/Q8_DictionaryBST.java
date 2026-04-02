import java.util.*;

class DictNode {
    String key;
    String meaning;
    DictNode left, right;

    DictNode(String key, String meaning) {
        this.key = key;
        this.meaning = meaning;
    }
}

public class Q8_DictionaryBST {
    DictNode root;

    DictNode insert(DictNode node, String key, String meaning) {
        if (node == null) return new DictNode(key, meaning);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = insert(node.left, key, meaning);
        else if (cmp > 0) node.right = insert(node.right, key, meaning);
        else node.meaning = meaning; // update
        return node;
    }

    DictNode search(DictNode node, String key, int[] comparisons) {
        while (node != null) {
            comparisons[0]++;
            int cmp = key.compareTo(node.key);
            if (cmp == 0) return node;
            if (cmp < 0) node = node.left;
            else node = node.right;
        }
        return null;
    }

    DictNode delete(DictNode node, String key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = delete(node.left, key);
        else if (cmp > 0) node.right = delete(node.right, key);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            DictNode succ = minNode(node.right);
            node.key = succ.key;
            node.meaning = succ.meaning;
            node.right = delete(node.right, succ.key);
        }
        return node;
    }

    DictNode minNode(DictNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    void inorder(DictNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println(node.key + " : " + node.meaning);
        inorder(node.right);
    }

    void reverseInorder(DictNode node) {
        if (node == null) return;
        reverseInorder(node.right);
        System.out.println(node.key + " : " + node.meaning);
        reverseInorder(node.left);
    }

    public static void main(String[] args) {
        Q8_DictionaryBST dict = new Q8_DictionaryBST();
        dict.root = dict.insert(dict.root, "apple", "a fruit");
        dict.root = dict.insert(dict.root, "ball", "a round object");
        dict.root = dict.insert(dict.root, "cat", "an animal");

        System.out.println("Ascending:");
        dict.inorder(dict.root);

        System.out.println("Descending:");
        dict.reverseInorder(dict.root);

        int[] comps = {0};
        DictNode res = dict.search(dict.root, "cat", comps);
        if (res != null)
            System.out.println("Found 'cat' in " + comps[0] + " comparisons");

        // maximum comparisons in worst case ~ height of tree
    }
}