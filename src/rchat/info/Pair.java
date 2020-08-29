package rchat.info;

public class Pair <T1, T2> {
    T1 key;
    T2 value;

    public Pair(T1 t1, T2 t2) {
        this.key = t1;
        this.value = t2;
    }

    public T1 getKey() {
        return key;
    }

    public T2 getValue() {
        return value;
    }
}
