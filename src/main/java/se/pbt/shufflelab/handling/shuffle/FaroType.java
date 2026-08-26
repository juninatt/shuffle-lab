package se.pbt.shufflelab.handling.shuffle;

/**
 * Defines the two variants of a Faro shuffle.
 *
 * <p>An out Faro shuffle begins the interleave with the top packet,
 * leaving the original top and bottom cards in place. An in Faro
 * shuffle begins with the bottom packet, moving both the original
 * top and bottom cards inward.</p>
 */
public enum FaroType {

    /**
     * Begins the interleave with the top packet.
     */
    OUT,

    /**
     * Begins the interleave with the bottom packet.
     */
    IN
}