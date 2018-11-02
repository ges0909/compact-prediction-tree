import java.util.LinkedList;

class Sequence<T> {

  private long id;
  private LinkedList<T> values;

  public long id() {
    return this.id;
  }

  public long length() {
    return this.values.size();
  }

  public boolean isEmpty() {
    return this.values.length() == 0;
  }

  public Sequence(long id) {
    if (id < 0) {
      throw new IllegalArgumentException("negative sequence id (index)");
    }
    this.values = new LinkedList<>();
    this.id = id;
  }

  public boolean add(T value) {
    return values.add(value);
  }

  public T remove(int index) {
    return values.remove(index);
  }

  public T get(int index) {
    return values.get(index);
  }

  
}