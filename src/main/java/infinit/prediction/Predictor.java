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

    public Treenode<T> addTrainingSequence(Sequence<T> sequence) {
        return addSequence(sequence, predictionTree);
    }

    public void addTrainingSequences(List<Sequence<T>> sequences) {
        sequences.forEach(this::addTrainingSequence);
    }

    private Treenode<T> addSequence(Sequence<T> sequence, Treenode<T> root) {
        if (sequence.isEmpty()) {
            return root; /*may be last eaf*/
        }
        T symbol = sequence.getFirstSymbol();
        for (Treenode<T> node : root.getChildren()) {
            if (node.getSymbol().equals(symbol)) {
                return addSequence(sequence.copyWithoutFirstSymbol(), node);
            }
        }
        // create new node
        Treenode<T> node = new Treenode<>(symbol);
        node.setParent(root);
        root.addChild(node);
        return addSequence(sequence.copyWithoutFirstSymbol(), node);
    }
}
