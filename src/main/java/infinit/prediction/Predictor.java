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

    public void train(List<Sequence<T>> sequences) {
        sequences.forEach(this::train);
    }

    private void train(Sequence<T> sequence) {
        int uniqueSequenceId = lookupTable.size(); // is used as index for lookup table, therefore has to start with 0
        // add training sequence to prediction tree
        TreeNode<T> lastNode = addTrainingSequence(sequence);
        // store mapping symbol to sequences containing it
        for (T symbol : sequence.getSymbols()) {
            invertedIndex.putIfAbsent(symbol, new HashSet<>());
            invertedIndex.get(symbol).add(uniqueSequenceId);
        }
        // store last prediction tree node in lookup table
        lookupTable.put(uniqueSequenceId, lastNode);
    }

    private TreeNode<T> addTrainingSequence(Sequence<T> sequence) {
        TreeNode<T> root = predictionTree;
        for (T symbol : sequence.getSymbols()) {
            TreeNode<T> node = find(root, symbol);
            if (node == null) {
                node = new TreeNode<>(symbol);
                root.addChild(node);
                node.setParent(root);
            }
            root = node;
        }
        return root;
    }

    private TreeNode<T> find(TreeNode<T> root, T symbol) {
        for (TreeNode<T> node : root.getChildren()) {
            if (node.getSymbol().equals(symbol)) {
                return node;
            }
        }
        return null;
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
