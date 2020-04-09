package app.algorithms;

/**
 * Entry is a type casted entry used for a priority queue.
 * @param <T2>
 */
public class Entry<T2> implements Comparable<Entry> {
    private int key;
    private T2 value;

    /**
     * The constructor takes in the entries key and value.
     * @param key
     * @param value
     */
    public Entry(int key, T2 value) {
        this.key = key;
        this.value = value;
    }

    // Getters
    public int getKey() { return key; }
    public T2 getValue() { return value; }

    /**
     * This function is a comparator function the priority queue uses to sort each entry.
     * @param other
     * @return
     */
    @Override
    public int compareTo(Entry other) {
        return this.getKey() - (other.getKey());
    }
}
