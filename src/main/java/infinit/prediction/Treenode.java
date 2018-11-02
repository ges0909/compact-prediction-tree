package infinit.prediction;

import java.util.ArrayList;
import java.util.List;

public class Treenode<T> {

    private Treenode<T> parent;
    private List<Treenode<T>> children;
    private T symbol;

    Treenode() {
        this.parent = null;
        this.children = new ArrayList<>();
        this.symbol = null;
    }

    public Treenode(T symbol) {
        this();
        this.symbol = symbol;
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    Treenode<T> getParent() {
        return this.parent;
    }

    void setParent(Treenode<T> parent) {
        this.parent = parent;
    }

    public T getSymbol() {
        return this.symbol;
    }

    public boolean addChild(Treenode<T> child) {
        return this.children.add(child);
    }

    public List<Treenode<T>> getChildren() {
        return this.children;
    }
}
