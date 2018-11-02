package infinit.prediction;

import java.util.List;
import java.util.Map;

public class Predictor<T> {

    private Treenode<T> predictionTree;
    private Map<T, Integer /* sequence index */> invertedIndex;
    private Map<T, Treenode<T>> lookupTable;

    public Predictor() {
        predictionTree = new Treenode<>();
    }

    public void addTrainingSequence(Sequence<T> sequence) {
        predictionTree.addSequence(sequence);
    }

    public void addTrainingSequences(List<Sequence<T>> sequences) {
        sequences.forEach(this::addTrainingSequence);
    }
}
