package infinit.prediction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Sequence<T> {

    private List<T> symbols;

    public Sequence() {
        symbols = new LinkedList<>();
    }

    public Sequence(T... symbols) {
        this.symbols = new LinkedList<>(Arrays.asList(symbols));
    }

    List<T> getSymbols() {
        return symbols;
    }

    public boolean isEmpty() {
        return this.symbols.isEmpty();
    }

    public T getSymbol(int index) {
        return symbols.get(index);
    }

    void insert(T symbol) {
        symbols.add(0, symbol);
    }
}
