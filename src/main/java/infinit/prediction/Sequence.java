package infinit.prediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sequence<T> {

    private List<T> symbols;

    public Sequence(T... symbols) {
        this.symbols = new ArrayList<>(Arrays.asList(symbols));
        Collections.unmodifiableList(this.symbols );
    }

    public List<T> getSymbols() {
        return symbols;
    }
}
