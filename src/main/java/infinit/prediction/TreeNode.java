package infinit.prediction;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {

    private TreeNode<T> parent;
    private List<TreeNode<T>> children;
    private T symbol;

    TreeNode() {
        this.parent = null;
        this.children = new ArrayList<>();
        this.symbol = null;
    }

    public TreeNode(T symbol) {
        this();
        this.symbol = symbol;
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    TreeNode<T> getParent() {
        return this.parent;
    }

    void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public T getSymbol() {
        return this.symbol;
    }

    public boolean addChild(TreeNode<T> child) {
        return this.children.add(child);
    }

    public List<TreeNode<T>> getChildren() {
        return this.children;
    }
}
