import java.util.*;

class ETNode {
    char value;
    ETNode left, right;

    ETNode(char value) {
        this.value = value;
    }
}

public class ExpressionTree {
    // Build expression tree from postfix for simplicity
    // Infix: a - b * c - d / e + f
    // One postfix: a b c * - d e / - f +
    static ETNode buildFromPostfix(String postfix) {
        Stack<ETNode> stack = new Stack<>();
        for (char ch : postfix.toCharArray()) {
            if (Character.isWhitespace(ch)) continue;
            if (isOperand(ch)) {
                stack.push(new ETNode(ch));
            } else {
                ETNode node = new ETNode(ch);
                ETNode right = stack.pop();
                ETNode left = stack.pop();
                node.left = left;
                node.right = right;
                stack.push(node);
            }
        }
        return stack.pop();
    }

    static boolean isOperand(char c) {
        return Character.isLetterOrDigit(c);
    }

    // Inorder traversal (recursive) – to get infix sequence
    static void inorder(ETNode root) {
        if (root == null) return;
        if (!isOperand(root.value)) System.out.print("(");
        inorder(root.left);
        System.out.print(root.value);
        inorder(root.right);
        if (!isOperand(root.value)) System.out.print(")");
    }

    // Non‑recursive postorder traversal
    static void postorderNonRecursive(ETNode root) {
        if (root == null) return;
        Stack<ETNode> stack1 = new Stack<>();
        Stack<ETNode> stack2 = new Stack<>();
        stack1.push(root);
        while (!stack1.isEmpty()) {
            ETNode node = stack1.pop();
            stack2.push(node);
            if (node.left != null) stack1.push(node.left);
            if (node.right != null) stack1.push(node.right);
        }
        while (!stack2.isEmpty()) {
            System.out.print(stack2.pop().value + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String postfix = "a b c * - d e / - f +";
        ETNode root = buildFromPostfix(postfix);

        System.out.print("Inorder: ");
        inorder(root);
        System.out.println();

        System.out.print("Postorder (non‑recursive): ");
        postorderNonRecursive(root);
    }
}