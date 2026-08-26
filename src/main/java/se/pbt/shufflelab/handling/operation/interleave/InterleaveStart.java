package se.pbt.shufflelab.handling.operation.interleave;

/**
 * Defines how an interleave begins.
 *
 * <p>This setting determines which packet releases
 * cards first and can be used to model different interleaving styles
 * or natural variations in card handling.</p>
 */
public enum InterleaveStart {
    TOP,
    BOTTOM
}
