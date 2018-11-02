package infinit.prediction;

import java.util.*;
import java.util.stream.Collectors;

public class Predictor<T> {

    private Treenode<T> predictionTree;
    private Map<T, Set<Integer>> invertedIndex;
    private Map<Integer, Treenode<T>> lookupTable;

    public Predictor() {
        this.predictionTree = new Treenode<>();
        this.invertedIndex = new HashMap<>();
        this.lookupTable = new HashMap<>();
    }

    public void addToPredictionTree(Sequence<T> sequence) {
        int uniqueSequenceId = this.lookupTable.size(); // will be used later as index into the lookup table
        // add training sequence to prediction tree
        Treenode<T> leaf = addToPredictionTree(sequence, this.predictionTree);
        //
        for (T symbol : sequence.getSymbols()) {
            Set<Integer> sequencesContainingSymbol = this.invertedIndex.get(symbol);
            if (sequencesContainingSymbol == null) {
                sequencesContainingSymbol = new HashSet<>();
                this.invertedIndex.put(symbol, sequencesContainingSymbol);
            }
            sequencesContainingSymbol.add(uniqueSequenceId);
        }
        // save ref. to last prediction tree node of sequence in lookup table
        this.lookupTable.put(uniqueSequenceId, leaf);
    }

    public void addTrainingSequences(List<Sequence<T>> sequences) {
        sequences.forEach(this::addToPredictionTree);
    }

    private Treenode<T> addToPredictionTree(Sequence<T> sequence, Treenode<T> root) {
        if (sequence.isEmpty()) {
            return root;
        }
        T symbol = sequence.getFirstSymbol();
        for (Treenode<T> node : root.getChildren()) {
            if (node.getSymbol().equals(symbol)) {
                return addToPredictionTree(sequence.copyWithoutFirstSymbol(), node);
            }
        }
        // create new node
        Treenode<T> node = new Treenode<>(symbol);
        root.addChild(node);
        node.setParent(root);
        return addToPredictionTree(sequence.copyWithoutFirstSymbol(), node);
    }

    public List<T> predict(Sequence<T> testSequence) {
        // find unique symbols of the test sequence
        Set<T> uniqueSymbols = testSequence.getSymbols().stream().distinct().collect(Collectors.toSet());
        // find all training sequences containing the unique symbols of the test sequence (intersection)
        Set<Integer> similiarSequences = new HashSet<>();
        for (T symbol : uniqueSymbols) {
            similiarSequences.addAll(this.invertedIndex.get(symbol));
        }
        // traverse prediction tree from bottom to top to find involved training sequences
        List<Sequence<T>> branches = new ArrayList<>();
        for (Integer sequenceId : similiarSequences) {
            Sequence<T> branch = new Sequence<>();
            Treenode<T> node = this.lookupTable.get(sequenceId);
            while (!node.isRoot()) {
                branch.insert(node.getSymbol());
                node = node.getParent();
            }
            branches.add(branch);
        }
        // find 1st occurrence of test sequence in branches and take first symbol behind as prediction
        List<T> prediction = new ArrayList<>();
        for (Sequence<T> branch : branches) {
            int startIndex = Collections.indexOfSubList(branch.getSymbols(), testSequence.getSymbols());
            if (startIndex > -1) {
                int nextIndex = startIndex + testSequence.getSymbols().size();
                if (nextIndex < (branch.getSymbols().size()-1)) {
                    prediction.add(branch.getSymbols().get(nextIndex));
                }
            }
        }
        return prediction;
    }
}
