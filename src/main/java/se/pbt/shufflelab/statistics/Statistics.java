package se.pbt.shufflelab.statistics;

import java.util.Arrays;

/**
 * Represents the mean, median, extremes, and spread of a series of numeric
 * samples.
 *
 * <p>{@code standardDeviation} is the population standard deviation of the
 * samples, not a sample-based estimate, since it describes the full set of
 * observations that were measured rather than a subset drawn from a larger
 * population.
 *
 * @param mean the arithmetic mean of the samples
 * @param median the middle value of the samples when sorted, or the average
 *               of the two middle values if there is an even number of samples
 * @param minimum the smallest sample
 * @param maximum the largest sample
 * @param standardDeviation the population standard deviation of the samples
 */
public record Statistics(double mean, double median, double minimum, double maximum, double standardDeviation) {

    public Statistics {
        if (minimum > maximum) {
            throw new IllegalArgumentException("minimum must not be greater than maximum");
        }

        if (median < minimum || median > maximum) {
            throw new IllegalArgumentException("median must be between minimum and maximum");
        }

        if (standardDeviation < 0) {
            throw new IllegalArgumentException("standardDeviation must not be negative");
        }
    }

    /**
     * Computes the statistics of a series of samples.
     *
     * @param samples the samples to summarize; at least one is required
     * @return the mean, median, extremes, and spread of the given samples
     * @throws IllegalArgumentException if no samples are given
     */
    public static Statistics of(double... samples) {
        if (samples.length == 0) {
            throw new IllegalArgumentException("at least one sample is required");
        }

        double sum = 0;
        double minimum = Double.POSITIVE_INFINITY;
        double maximum = Double.NEGATIVE_INFINITY;

        for (double sample : samples) {
            sum += sample;
            minimum = Math.min(minimum, sample);
            maximum = Math.max(maximum, sample);
        }

        double mean = sum / samples.length;

        double squaredDeviations = 0;

        for (double sample : samples) {
            squaredDeviations += Math.pow(sample - mean, 2);
        }

        double standardDeviation = Math.sqrt(squaredDeviations / samples.length);
        double median = medianOf(samples);

        return new Statistics(mean, median, minimum, maximum, standardDeviation);
    }

    /**
     * Computes the median of a series of samples.
     *
     * @param samples the samples to find the median of
     * @return the middle value, or the average of the two middle values if
     *         there is an even number of samples
     */
    private static double medianOf(double[] samples) {
        double[] sorted = samples.clone();
        Arrays.sort(sorted);

        int middle = sorted.length / 2;

        if (sorted.length % 2 == 0) {
            return (sorted[middle - 1] + sorted[middle]) / 2.0;
        }

        return sorted[middle];
    }
}