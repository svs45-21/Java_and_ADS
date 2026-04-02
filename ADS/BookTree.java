import java.util.*;

class BookNode {
    String title;
    List<BookNode> children;

    BookNode(String title) {
        this.title = title;
        this.children = new ArrayList<>();
    }

    void addChild(BookNode child) {
        children.add(child);
    }
}

public class BookTree {
    // DFS print
    static void printTree(BookNode root, String indent) {
        if (root == null) return;
        System.out.println(indent + root.title);
        for (BookNode child : root.children) {
            printTree(child, indent + "  ");
        }
    }

    public static void main(String[] args) {
        // Example: Book → Chapters → Sections → Subsections
        BookNode book = new BookNode("Book: Data Structures");

        BookNode ch1 = new BookNode("Chapter 1: Introduction");
        BookNode ch2 = new BookNode("Chapter 2: Trees");

        BookNode sec11 = new BookNode("Section 1.1: Basics");
        BookNode sec12 = new BookNode("Section 1.2: Complexity");
        ch1.addChild(sec11);
        ch1.addChild(sec12);

        BookNode subsec111 = new BookNode("Subsection 1.1.1: Definitions");
        sec11.addChild(subsec111);

        BookNode sec21 = new BookNode("Section 2.1: Binary Trees");
        BookNode sec22 = new BookNode("Section 2.2: AVL Trees");
        ch2.addChild(sec21);
        ch2.addChild(sec22);

        book.addChild(ch1);
        book.addChild(ch2);

        printTree(book, "");
    }
}