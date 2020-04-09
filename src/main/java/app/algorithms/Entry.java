package app.algorithms;

public class Entry<T2> implements Comparable<Entry> {
    private int key;
    private T2 value;

    public Entry(int key, T2 value) {
        this.key = key;
        this.value = value;
    }

    // getters
    public int getKey() { return key; }
    public T2 getValue() { return value; }

    @Override
    public int compareTo(Entry other) {
        return this.getKey() - (other.getKey());
    }
}
