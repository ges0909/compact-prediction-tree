package infinit.prediction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Sequence<T> {

    private List<T> symbols;

    @SafeVarargs
    public Sequence(T... symbols) {
        this.symbols = new LinkedList<>(Arrays.asList(symbols));
    }

    List<T> getSymbols() {
        return symbols;
    }

    public boolean isEmpty() {
        return this.symbols.isEmpty();
    }

    Sequence<T> copyWithoutFirstSymbol() {
        return new Sequence(this.symbols.stream().skip(1).toArray());
    }

    public T getFirstSymbol() {
        return this.symbols.get(0);
    }

     void insert(T symbol) {
        this.symbols.add(0, symbol);
    }
}
