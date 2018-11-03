package infinit.prediction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Sequence<T> {

    private List<T> symbols; // use linked list because of frequent insertions at beginning


    public Sequence() {
        this.symbols = new LinkedList<>();
    }

    public Sequence(T... symbols) {
        this.symbols = new LinkedList<>(Arrays.asList(symbols));
    }

    public List<T> getSymbols() {
        return symbols;
    }

    public void insert(T symbol) {
        symbols.add(0, symbol);
    }
}
