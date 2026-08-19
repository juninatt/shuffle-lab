package se.pbt.shufflelab.analysis;

import se.pbt.shufflelab.analysis.displacement.DisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.PreservedOrderResult;

import java.util.Objects;

/**
 * Represents the complete result of analysing a shuffled deck,
 * containing the individual analysis results for each supported metric.
 *
 * <p>The analysis contains separate result objects for each measured aspect
 * of the shuffled deck. Each result describes potential patterns or structural
 * relationships identified by comparing the shuffled deck with its original
 * order.</p>
 *
 * @param displacement result of analysing how far cards have moved from their
 *                     original positions
 * @param preservedOrder result of analysing potential preserved pairs and
 *                       longer preserved sequences
 */
public record DeckAnalysis(
        DisplacementResult displacement,
        PreservedOrderResult preservedOrder
) {

    /**
     * Creates a complete deck analysis from the individual analysis results.
     *
     * @throws NullPointerException if any result is {@code null}
     */
    public DeckAnalysis {
        Objects.requireNonNull(
                displacement,
                "displacement must not be null"
        );

        Objects.requireNonNull(
                preservedOrder,
                "preservedOrder must not be null"
        );
    }
}