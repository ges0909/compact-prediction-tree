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

    public void train(List<Sequence<T>> trainingSequences) {
        trainingSequences.forEach(this::train);
    }

    private void train(Sequence<T> trainingSequence) {
        int sequenceIndex = lookupTable.size(); // give each training sequence an unique id to use as index
        // 1. add training sequence to prediction tree
        TreeNode<T> lastNode = addSequenceToPredictionTree(trainingSequence);
        // 2. store in inverted index the mapping "symbol -> sequences containing symbol"
        for (T symbol : trainingSequence.getSymbols()) {
            invertedIndex.putIfAbsent(symbol, new HashSet<>());
            invertedIndex.get(symbol).add(sequenceIndex);
        }
        // 3. store in lookup table the mapping "sequence -> bottom tree node"
        lookupTable.put(sequenceIndex, lastNode);
    }

    private TreeNode<T> addSequenceToPredictionTree(Sequence<T> trainingSequence) {
        TreeNode<T> node = predictionTree;
        for (T symbol : trainingSequence.getSymbols())
            // if symbol not found in children then add new child to node
            node = find(node, symbol).orElse(new TreeNode<>(node, symbol));
        return node;
    }

    private Optional<TreeNode<T>> find(TreeNode<T> root, T symbol) {
        for (TreeNode<T> node : root.getChildren()) {
            if (node.getSymbol().equals(symbol)) {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    public Map<T, Double> predict(Sequence<T> testSequence) {
        // find unique symbols of the test sequence
        Set<T> uniqueTestSymbols = new HashSet<>(testSequence.getSymbols());
        // find all training sequences containing the unique symbols of the test sequence (intersection)
        Set<Integer> similarSequenceIndexes = new HashSet<>();
        for (T symbol : uniqueTestSymbols) similarSequenceIndexes.addAll(invertedIndex.get(symbol));
        // traverse prediction tree from bottom to top to collect branches containing test sequence potentially
        List<List<T>> branches = new ArrayList<>();
        for (Integer sequenceIndex : similarSequenceIndexes) {
            List<T> branch = new LinkedList<>();
            for (TreeNode<T> node = lookupTable.get(sequenceIndex); !node.isRoot(); node = node.getParent())
                branch.add(0, node.getSymbol());
            branches.add(branch);
        }
        // find 1st occurrence of test sequence in branches
        Map<T, Integer> predictions = new HashMap<>();
        for (List<T> branch : branches) {
            int startIdx = Collections.indexOfSubList(branch, testSequence.getSymbols());
            if (startIdx > -1 /*found*/) {
                int behindIdx = startIdx + testSequence.getSymbols().size();
                if (behindIdx < branch.size()) {
                    T symbol = branch.get(behindIdx);
                    predictions.putIfAbsent(symbol, 0 /*initialize counter*/);
                    predictions.computeIfPresent(symbol, (s, v) -> v + 1 /*increment counter*/);
                }
            }
        }
        // calculate scores
        double sum = predictions.values().stream().mapToInt(Integer::intValue).sum();
        return predictions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / sum));
    }
}
