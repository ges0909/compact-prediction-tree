package sequence.prediction.tests;

import infinit.prediction.Predictor;
import infinit.prediction.Sequence;
import infinit.prediction.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PredictorTest {

    @Test
    void createTreeNodeWithIntegerSymbol() {
        int symbol = 1;
        TreeNode<Integer> node = new TreeNode<>(symbol);
        assertNotNull(node);
        assertTrue(node.isRoot());
        assertEquals(symbol, node.getSymbol().intValue());
    }

    @Test
    void createTreeNodeWithStringSymbol() {
        String symbol = "A";
        TreeNode<String> node = new TreeNode<>(symbol);
        assertNotNull(node);
        assertTrue(node.isRoot());
        assertEquals(symbol, node.getSymbol());
    }

    @Test
    void createNodeAndAddChild() {
        TreeNode<Integer> parent = new TreeNode<>(1);
        TreeNode<Integer> child = new TreeNode<>(2);
        assertTrue(parent.addChild(child));
        List<TreeNode<Integer>> children = parent.getChildren();
        assertEquals(1, children.size());
    }

    @Test
    void createSequenceAndAddSymbol() {
        Sequence<Integer> sequence = new Sequence<>(99);
        assertFalse(sequence.isEmpty());
        assertEquals(99, sequence.getFirstSymbol().intValue());
    }

    @Test
    void createPredictorAndTrainingSequence() {
        Predictor<Integer> predictor = new Predictor<>();
        predictor.addSequenceToPredictionTree(new Sequence<>(1, 1, 2, 3));
        predictor.addSequenceToPredictionTree(new Sequence<>(1, 1, 3, 4));
        predictor.addSequenceToPredictionTree(new Sequence<>(2, 5, 1, 7, 8, 9));
    }

    @Test
    void predictNumericSequence() {
        Predictor<Integer> predictor = new Predictor<>();
        List<Sequence<Integer>> trainingSet = Arrays.asList(
                new Sequence<>(1, 1, 2, 1, 1, 1, 3),
                new Sequence<>(1, 1, 2, 1, 1, 1, 1),
                new Sequence<>(0, 9, 8, 7, 6, 5, 4)
        );
        predictor.addTrainingSequences(trainingSet);
        Map<Integer, Float> predictions = predictor.predict(new Sequence<>(2, 1, 1, 1));
        assertEquals(0, predictions.get(3).intValue());
        assertEquals(0, predictions.get(1).intValue());
    }

    @Test
    void predictStringSequence() {
        Predictor<String> predictor = new Predictor<>();
        List<Sequence<String>> trainingSet = Arrays.asList(
                new Sequence<>("A", "B", "C", "D"),
                new Sequence<>("B", "E"),
                new Sequence<>("C", "D")
        );
        predictor.addTrainingSequences(trainingSet);
        Map<String, Float> predictions = predictor.predict(new Sequence<>("C"));
        assertEquals(0, predictions.get("D").floatValue());
    }
}
