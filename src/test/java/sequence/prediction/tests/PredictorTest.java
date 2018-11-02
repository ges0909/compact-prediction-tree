package sequence.prediction.tests;

import infinit.prediction.Predictor;
import infinit.prediction.Sequence;
import infinit.prediction.Treenode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PredictorTest {

    @Test
    void createTreeNodeWithIntegerSymbol() {
        int symbol = 1;
        Treenode<Integer> node = new Treenode<>(symbol);
        assertNotNull(node);
        assertTrue(node.isRoot());
        assertEquals(symbol, node.getSymbol().intValue());
    }

    @Test
    void createTreeNodeWithStringSymbol() {
        String symbol = "A";
        Treenode<String> node = new Treenode<>(symbol);
        assertNotNull(node);
        assertTrue(node.isRoot());
        assertEquals(symbol, node.getSymbol());
    }

    @Test
    void createNodeAndAddChild() {
        Treenode<Integer> parent = new Treenode<>(1);
        Treenode<Integer> child = new Treenode<>(2);
        assertTrue(parent.addChild(child));
        List<Treenode<Integer>> children = parent.getChildren();
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
        predictor.addToPredictionTree(new Sequence<>(1, 1, 2, 3));
        predictor.addToPredictionTree(new Sequence<>(1, 1, 3, 4));
        predictor.addToPredictionTree(new Sequence<>(2, 5, 1, 7, 8, 9));
    }

    @Test
    void check() {
        Predictor<Integer> predictor = new Predictor<>();
        List<Sequence<Integer>> trainingSet = Arrays.asList(
                new Sequence<>(1, 1, 2, 1, 1, 1, 3),
                new Sequence<>(1, 1, 2, 1, 1, 1, 1),
                new Sequence<>(0, 9, 8, 7, 6, 5, 4)
        );
        predictor.addTrainingSequences(trainingSet);
        List<Integer> prediction = predictor.predict(new Sequence(2, 1, 1, 1));
        assertEquals(3, prediction.get(0).intValue());
        assertEquals(1, prediction.get(1).intValue());
    }

    @Test
    void check2() {
        Predictor<String> predictor = new Predictor<>();
        List<Sequence<String>> trainingSet = Arrays.asList(
                new Sequence<>("A", "B", "C", "D"),
                new Sequence<>("B", "C"),
                new Sequence<>("C", "D")
        );
        predictor.addTrainingSequences(trainingSet);
        List<String> prediction = predictor.predict(new Sequence("C"));
        assertEquals("D", prediction.get(0));
    }
}
