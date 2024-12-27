package more.mucho.tguilds.utils;

public class Tuple <T,B>{

    private T first;
    private B second;

    public Tuple(T first, B second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
