package infinit.prediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sequence<T> {

    private List<T> symbols;

    public Sequence(T... symbols) {
        this.symbols = new ArrayList<>(Arrays.asList(symbols));
    }

    public List<T> getSymbols() {
        return symbols;
    }
}
