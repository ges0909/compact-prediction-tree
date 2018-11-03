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

    private void addSequenceToPredictionTree(Sequence<T> sequence) {
        int uniqueSequenceId = lookupTable.size(); // is used as index for lookup table, therefore has to start with 0
        // add training sequence to prediction tree
        TreeNode<T> lastNode = addSequenceToPredictionTree(sequence, predictionTree);
        // store mapping symbol to sequences containing it
        for (T symbol : sequence.getSymbols()) {
            invertedIndex.putIfAbsent(symbol, new HashSet<>());
            invertedIndex.get(symbol).add(uniqueSequenceId);
        }
        // store last prediction tree node in lookup table
        lookupTable.put(uniqueSequenceId, lastNode);
    }

    public void addTrainingSequences(List<Sequence<T>> sequences) {
        sequences.forEach(this::addSequenceToPredictionTree);
    }

    private TreeNode<T> addSequenceToPredictionTree(Sequence<T> sequence, TreeNode<T> subTreeNode) {
        if (sequence.isEmpty()) {
            return subTreeNode;
        }
        T symbol = sequence.getFirstSymbol();
        // traverse prediction tree along already existing nodes top-down or ...
        for (TreeNode<T> node : subTreeNode.getChildren()) {
            if (node.getSymbol().equals(symbol)) {
                return addSequenceToPredictionTree(sequence.copyWithoutFirstSymbol(), node);
            }
        }
        // ... create a new node for the symbol
        TreeNode<T> node = new TreeNode<>(symbol);
        subTreeNode.addChild(node);
        node.setParent(subTreeNode);
        return addSequenceToPredictionTree(sequence.copyWithoutFirstSymbol(), node);
    }

    public Map<T, Double> predict(Sequence<T> testSequence) {
        // find unique symbols of the test sequence
        Set<T> uniqueTestSymbols = new HashSet<>(testSequence.getSymbols());
        // find all training sequences containing the unique symbols of the test sequence (intersection)
        Set<Integer> similarSequenceIndexes = new HashSet<>();
        for (T symbol : uniqueTestSymbols) similarSequenceIndexes.addAll(invertedIndex.get(symbol));
        // traverse prediction tree from bottom to top to collect branches containing test sequence potentially
        List<Sequence<T>> branches = new ArrayList<>();
        for (Integer uniqueSequenceId : similarSequenceIndexes) {
            Sequence<T> branch = new Sequence<>();
            for (TreeNode<T> node = lookupTable.get(uniqueSequenceId); !node.isRoot(); node = node.getParent())
                branch.insert(node.getSymbol());
            branches.add(branch);
        }
        // find first occurrence of complete test sequence in branches
        Map<T, Double> predictions = new HashMap<>();
        for (Sequence<T> branch : branches) {
            int startIndex = Collections.indexOfSubList(branch.getSymbols(), testSequence.getSymbols());
            if (startIndex > -1 /*found*/) {
                int behindIndex = startIndex + testSequence.getSymbols().size();
                if (behindIndex < branch.getSymbols().size()) {
                    T symbol = branch.getSymbols().get(behindIndex);
                    predictions.putIfAbsent(symbol, 0d /*initialize counter*/);
                    predictions.computeIfPresent(symbol, (s, v) -> v + 1d /*increment counter*/);
                }
            }
        }
        // transform counter to score
        double counterSum = predictions.values().stream().mapToDouble(Double::doubleValue).sum();
        return predictions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / counterSum));
    }
}
