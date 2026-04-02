import java.util.*;

class DictEntry<K, V> {
    K key;
    V value;

    DictEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

public class Q15_DictionaryHash<K, V> {
    List<DictEntry<K, V>>[] table;
    int capacity;

    @SuppressWarnings("unchecked")
    public Q15_DictionaryHash(int capacity) {
        this.capacity = capacity;
        table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new LinkedList<>();
    }

    int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // Insert (key, value)
    public void insert(K key, V value) {
        int idx = hash(key);
        for (DictEntry<K, V> e : table[idx]) {
            if (e.key.equals(key)) {
                e.value = value;
                return;
            }
        }
        table[idx].add(new DictEntry<>(key, value));
    }

    // Find(key)
    public V find(K key) {
        int idx = hash(key);
        for (DictEntry<K, V> e : table[idx]) {
            if (e.key.equals(key)) return e.value;
        }
        return null;
    }

    // Delete(key)
    public boolean delete(K key) {
        int idx = hash(key);
        Iterator<DictEntry<K, V>> it = table[idx].iterator();
        while (it.hasNext()) {
            DictEntry<K, V> e = it.next();
            if (e.key.equals(key)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Q15_DictionaryHash<String, Integer> dict = new Q15_DictionaryHash<>(10);
        dict.insert("one", 1);
        dict.insert("two", 2);
        dict.insert("three", 3);

        System.out.println("Find 'two': " + dict.find("two"));
        dict.delete("two");
        System.out.println("Find 'two' after delete: " + dict.find("two"));
    }
}