import java.util.*;

class FormulaNode {
    char op; // operator or variable
    FormulaNode left, right;

    FormulaNode(char op) {
        this.op = op;
    }
}

public class PropositionalFormulaTree {
    static boolean isOperator(char c) {
        return c == '!' || c == '&' || c == '|';
    }

    static int precedence(char op) {
        if (op == '!') return 3;
        if (op == '&') return 2;
        if (op == '|') return 1;
        return 0;
    }

    public static FormulaNode parse(String expr) {
        Stack<FormulaNode> values = new Stack<>();
        Stack<Character> ops = new Stack<>();
        char[] arr = expr.replaceAll("\\s+", "").toCharArray();

        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (Character.isLetter(c)) {
                values.push(new FormulaNode(c));
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    applyOp(ops.pop(), values);
                }
                if (!ops.isEmpty() && ops.peek() == '(') ops.pop();
            } else if (isOperator(c)) {
                while (!ops.isEmpty() && ops.peek() != '(' &&
                        precedence(ops.peek()) >= precedence(c)) {
                    applyOp(ops.pop(), values);
                }
                ops.push(c);
            }
        }
        while (!ops.isEmpty()) {
            applyOp(ops.pop(), values);
        }
        return values.pop();
    }

    private static void applyOp(char op, Stack<FormulaNode> values) {
        FormulaNode node = new FormulaNode(op);
        if (op == '!') {
            node.right = values.pop();
        } else {
            FormulaNode right = values.pop();
            FormulaNode left = values.pop();
            node.left = left;
            node.right = right;
        }
        values.push(node);
    }

    // Preorder print
    static void preorder(FormulaNode root) {
        if (root == null) return;
        System.out.print(root.op + " ");
        preorder(root.left);
        preorder(root.right);
    }

    public static void main(String[] args) {
        String formula = "!(p & q) | r";
        FormulaNode root = parse(formula);
        System.out.print("Preorder of formula tree: ");
        preorder(root);
        System.out.println();
    }
}