package sequence.prediction.tests;

import infinit.prediction.Predictor;
import infinit.prediction.Sequence;
import infinit.prediction.Treenode;
import org.junit.jupiter.api.Test;

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
        predictor.addTrainingSequence(new Sequence<>(1, 1, 2, 3));
        predictor.addTrainingSequence(new Sequence<>(1, 1, 3, 4));
        predictor.addTrainingSequence(new Sequence<>(2, 5, 1, 7, 8, 9));
    }
}
