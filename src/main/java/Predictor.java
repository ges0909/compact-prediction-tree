import java.util.Map;

class Predictor<T> {

  private Treenode<T> predictionTreeRoot;
  private Map<T, Integer /* sequence index */> invertedIndex;
  private Map<T, Treenode<T>> lookupTable;

  public Predictor() {
    predictionTreeRoot = new Treenode<>();
  }

  public void addTrainingSequence(Sequence<T> sequence) {
    predictionTreeRoot.addSequence(sequence);
  }
}