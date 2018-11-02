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

    public Treenode<T> getParent() {
        return this.parent;
    }

    public void setParent(Treenode<T> parent) {
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

    void addSequence(Sequence<T> sequence) {
        addSequence(this, sequence);
    }

    private void addSequence(Treenode<T> root, Sequence<T> sequence) {
        if (sequence.isEmpty()) {
            return;
        }
        T symbol = sequence.getFirstSymbol();
        for (Treenode<T> node : root.getChildren()) {
            if (node.getSymbol().equals(symbol)) {
                addSequence(node, sequence.copyWithoutFirstSymbol());
                return;
            }
        }
        // create new node
        Treenode<T> node = new Treenode<>(symbol);
        node.setParent(root);
        root.addChild(node);
        addSequence(node, sequence.copyWithoutFirstSymbol());
    }
}
