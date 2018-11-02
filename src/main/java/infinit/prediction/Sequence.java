package infinit.prediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sequence<T> {

    private List<T> symbols;
    private int lookupTableIndex;

    @SafeVarargs
    public Sequence(T... symbols) {
        this.symbols = new ArrayList<>(Arrays.asList(symbols));
    }

    public int getLookupTableIndex() {
        return lookupTableIndex;
    }

    public void setLookupTableIndex(int lookupTableIndex) {
        this.lookupTableIndex = lookupTableIndex;
    }

    public boolean isEmpty() {
        return this.symbols.isEmpty();
    }

    public Sequence<T> copyWithoutFirstSymbol() {
        return new Sequence(this.symbols.stream().skip(1).toArray());
    }

    public T getSymbol(int index) {
        return this.symbols.get(index);
    }

    public T getFirstSymbol() {
        return this.symbols.get(0);
    }
}
