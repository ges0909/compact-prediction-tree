package infinit.prediction;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static infinit.prediction.Predictor.Mode.DETERMINISTIC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

class PredictorTest {

    @Test
    void predictNumericSequence() {
        Predictor<Integer> predictor = new Predictor<>();
        List<Sequence<Integer>> trainingSet = Arrays.asList(
                new Sequence<>(1, 1, 5, 6, 2, 1, 1, 1, 3),
                new Sequence<>(1, 1, 2, 1, 1, 1, 3),
                new Sequence<>(1, 1, 2, 1, 1, 1, 1),
                new Sequence<>(0, 9, 8, 7, 6, 5, 4)
        );
        predictor.train(trainingSet);
        Map<Integer, Double> predictions = predictor.predict(DETERMINISTIC, new Sequence<>(2, 1, 1, 1));
        assertThat(predictions.get(1), greaterThan(0.33));
        assertThat(predictions.get(3), greaterThan(0.66));
    }

    @Test
    void predictStringSequence() {
        Predictor<String> predictor = new Predictor<>();
        List<Sequence<String>> trainingSet = Arrays.asList(
                new Sequence<>("A", "B", "C", "D", "E"),
                new Sequence<>("B", "E", "B", "E", "B", "E", "B", "E", "B", "E", "B", "E", "B", "E", "B", "E"),
                new Sequence<>("C", "D", "F")
        );
        predictor.train(trainingSet);
        Map<String, Double> predictions = predictor.predict(DETERMINISTIC, new Sequence<>("C", "D"));
        assertThat(predictions.size(), is(2));
        assertThat(predictions.get("E"), is(0.5));
        assertThat(predictions.get("F"), is(0.5));
    }

    @Test
    void predictPowerpointDataSet() {
        Predictor<String> predictor = new Predictor<>();
        List<Sequence<String>> trainingSet = Arrays.asList(
                new Sequence<>("A", "B", "C"),
                new Sequence<>("A", "B"),
                new Sequence<>("A", "B", "D", "C"),
                new Sequence<>("B", "C"),
                new Sequence<>("B", "D", "E")
        );
        predictor.train(trainingSet);
        Map<String, Double> predictions = predictor.predict(DETERMINISTIC, new Sequence<>("A", "B"));
        assertThat(predictions.size(), is(2));
        assertThat(predictions.get("C"), is(0.5));
        assertThat(predictions.get("D"), is(0.5));
    }

    @Test
    void bigPredictionTree() {
        Predictor<Integer> predictor = new Predictor<>();
        List<Sequence<Integer>> trainingSet = Arrays.asList(
                new Sequence<>(IntStream.range(1, 10_000).boxed().toArray(Integer[]::new)),
                new Sequence<>(IntStream.range(512, 1_000_000).boxed().toArray(Integer[]::new)),
                new Sequence<>(IntStream.range(12, 997).boxed().toArray(Integer[]::new))
        );
        predictor.train(trainingSet);
        Map<Integer, Double> predictions = predictor.predict(DETERMINISTIC,
                new Sequence<>(990, 991, 992, 993, 994, 995, 996, 997, 998));
        assertThat(predictions.size(), is(1));
        assertThat(predictions.get(999), is(1.0));
    }
}
