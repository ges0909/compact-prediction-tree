import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class PredictorTest {

    @Test
    public void createRootNode() {
        Integer symbol = Integer.valueOf(1);
        Treenode<Integer> node = new Treenode<>(symbol);
        assertNotNull(node);
        assertTrue(node.isRoot());
        assertEquals(symbol, node.getSymbol());
    }

    @Test
    public void createNodeAndAddChild() {
        Treenode<Integer> parent = new Treenode<>(Integer.valueOf(1));
        Treenode<Integer> child = new Treenode<>(Integer.valueOf(1));
        assertTrue(parent.addChild(child));
        List<Treenode<Integer>> children = parent.getChildren();
        assertEquals(1, children.size());
    }

    @Test
    public void testOnNegativeSequenceIndex() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Sequence<>(-1);
        });
    }

    @Test
    public void createSequenceAndAddSymbol() {
        Sequence<Integer> sequence = new Sequence<>(0);
        sequence.add(99);
        assertEquals(1, sequence.length());
        assertEquals(99, sequence.get(0).intValue());
    }

    @Test
    void predictor() {
        Predictor<Integer> predictor = new Predictor<>();
        assertNotNull(predictor);
    }
}
