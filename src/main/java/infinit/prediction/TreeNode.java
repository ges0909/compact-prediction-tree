package infinit.prediction;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {

    private TreeNode<T> parent;
    private List<TreeNode<T>> children;
    private T symbol;

    TreeNode() {
        parent = null;
        children = new ArrayList<>();
        symbol = null;
    }

    public TreeNode(T symbol) {
        this();
        this.symbol = symbol;
    }

    public boolean isRoot() {
        return parent == null;
    }

    TreeNode<T> getParent() {
        return parent;
    }

    void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public T getSymbol() {
        return symbol;
    }

    public boolean addChild(TreeNode<T> child) {
        return children.add(child);
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }
}
