package se.pbt.shufflelab;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * Provides deterministic random generators for shuffle tests.
 *
 * <p>Using the same seed produces the same shuffle decisions on every run,
 * making test results predictable and reproducible.</p>
 */
public final class TestRandoms {

    private TestRandoms() {
    }

    /**
     * Returns the project's standard test random generator.
     *
     * @return a deterministic random generator
     */
    public static RandomGenerator fixedRandom() {
        return RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);
    }

    /**
     * Returns a deterministic random generator using the given seed.
     *
     * <p>The same seed always produces the same shuffle outcome.</p>
     *
     * @param seed the seed to use
     * @return a deterministic random generator
     */
    public static RandomGenerator seededRandom(long seed) {
        return RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(seed);
    }
}