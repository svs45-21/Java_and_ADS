import java.util.*;

class PhoneEntry {
    String name;
    String number;

    PhoneEntry(String name, String number) {
        this.name = name;
        this.number = number;
    }
}

public class Q14_TelephoneBookHash {
    List<PhoneEntry>[] table;
    int capacity;

    @SuppressWarnings("unchecked")
    Q14_TelephoneBookHash(int capacity) {
        this.capacity = capacity;
        table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new LinkedList<>();
    }

    int hash(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    void put(String name, String number) {
        int idx = hash(name);
        for (PhoneEntry e : table[idx]) {
            if (e.name.equals(name)) {
                e.number = number;
                return;
            }
        }
        table[idx].add(new PhoneEntry(name, number));
    }

    String get(String name) {
        int idx = hash(name);
        for (PhoneEntry e : table[idx]) {
            if (e.name.equals(name)) return e.number;
        }
        return null;
    }

    public static void main(String[] args) {
        Q14_TelephoneBookHash book = new Q14_TelephoneBookHash(10);
        book.put("Alice", "11111");
        book.put("Bob", "22222");
        book.put("Charlie", "33333");

        System.out.println("Bob's number: " + book.get("Bob"));
        System.out.println("Unknown: " + book.get("X"));
    }
}