package infinit.prediction;

import java.util.ArrayList;
import java.util.List;

public final class TreeNode<T> {

    private TreeNode<T> parent;
    private List<TreeNode<T>> children;
    private T symbol;

    protected TreeNode() {
        children = new ArrayList<>();
    }

    public TreeNode(TreeNode<T> root, T symbol) {
        this();
        parent = root;
        root.addChild(this);
        this.symbol = symbol;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public T getSymbol() {
        return symbol;
    }

    public void addChild(TreeNode<T> child) {
        children.add(child);
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }
}
