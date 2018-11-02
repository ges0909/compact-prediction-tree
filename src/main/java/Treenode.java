import java.util.ArrayList;
import java.util.List;

public class Treenode<T> {

  Treenode<T> parent;
  List<Treenode<T>> children;

  T symbol;

  public Treenode() {
    this.parent = null;
    this.children = new ArrayList<>();
    this.symbol = null;
  }

  public Treenode(T symbol) {
    this.parent = null;
    this.children = new ArrayList<>();
    this.symbol = symbol;
  }

  public boolean isRoot() {
    return this.parent == null;
  }

  public Treenode<T> getParent() {
    return this.parent;
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

  private void addSequence(Treenode<T> root, Sequence<T> sequence) {
    if (sequence.isEmpty()) {
      return;
    }
    for (Treenode<T> node : root.getChildren())
      if (node.getSymbol().equals(sequence.get(0))) {
        sequence.remove(0);
        addSequence(node, sequence);
      }
  }

  public void addSequence(Sequence<T> sequence) {
    addSequence(this, sequence);
  }
}
