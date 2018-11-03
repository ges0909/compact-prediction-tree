package infinit.prediction;

import java.util.*;
import java.util.stream.Collectors;

public class Predictor<T> {

    private TreeNode<T> predictionTree;
    private Map<T, Set<Integer>> invertedIndex;
    private Map<Integer, TreeNode<T>> lookupTable;

    public Predictor() {
        predictionTree = new TreeNode<>();
        invertedIndex = new HashMap<>();
        lookupTable = new HashMap<>();
    }

    public void addSequenceToPredictionTree(Sequence<T> sequence) {
        int uniqueSequenceId = lookupTable.size(); // will be used later as index into the lookup table
        // add training sequence to prediction tree
        TreeNode<T> leaf = addSequenceToPredictionTree(sequence, predictionTree);
        // store symbol containing sequences
        for (T symbol : sequence.getSymbols()) {
            invertedIndex.putIfAbsent(symbol, new HashSet<>());
            invertedIndex.get(symbol).add(uniqueSequenceId);
        }
        // store last prediction tree node in lookup table
        lookupTable.put(uniqueSequenceId, leaf);
    }

    public void addTrainingSequences(List<Sequence<T>> sequences) {
        sequences.forEach(this::addSequenceToPredictionTree);
    }

    private TreeNode<T> addSequenceToPredictionTree(Sequence<T> sequence, TreeNode<T> root) {
        if (sequence.isEmpty()) {
            return root;
        }
        T symbol = sequence.getFirstSymbol();
        for (TreeNode<T> node : root.getChildren()) {
            if (node.getSymbol().equals(symbol)) {
                return addSequenceToPredictionTree(sequence.copyWithoutFirstSymbol(), node);
            }
        }
        // create new node
        TreeNode<T> node = new TreeNode<>(symbol);
        root.addChild(node);
        node.setParent(root);
        return addSequenceToPredictionTree(sequence.copyWithoutFirstSymbol(), node);
    }

    public Map<T, Double> predict(Sequence<T> testSequence) {
        // find unique symbols of the test sequence
        Set<T> uniqueSymbols = new HashSet<>(testSequence.getSymbols());
        // find all training sequences containing the unique symbols of the test sequence (intersection)
        Set<Integer> similarSequences = new HashSet<>();
        for (T symbol : uniqueSymbols) similarSequences.addAll(invertedIndex.get(symbol));
        // traverse prediction tree from bottom to top to find involved training sequences
        List<Sequence<T>> branches = new ArrayList<>();
        for (Integer sequenceId : similarSequences) {
            Sequence<T> branch = new Sequence<>();
            TreeNode<T> node = lookupTable.get(sequenceId);
            while (!node.isRoot()) {
                branch.insert(node.getSymbol());
                node = node.getParent();
            }
            branches.add(branch);
        }
        // find first occurrence of test sequence in branches and take first symbol behind as prediction
        Map<T, Double> predictions = new HashMap<>();
        for (Sequence<T> branch : branches) {
            int startIndex = Collections.indexOfSubList(branch.getSymbols(), testSequence.getSymbols());
            if (startIndex > -1) {
                int behindIndex = startIndex + testSequence.getSymbols().size();
                if (behindIndex < branch.getSymbols().size()) {
                    T symbol = branch.getSymbols().get(behindIndex);
                    predictions.putIfAbsent(symbol, 0d);
                    predictions.computeIfPresent(symbol, (s, v) -> v + 1f);
                }
            }
        }
        double sum = predictions.values().stream().mapToDouble(Double::doubleValue).sum();
        return predictions.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue() / sum));
    }
}
