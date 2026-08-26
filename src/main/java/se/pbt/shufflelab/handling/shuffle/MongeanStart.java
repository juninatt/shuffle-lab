package se.pbt.shufflelab.handling.shuffle;

/**
 * Defines where a Mongean shuffle begins placing cards.
 *
 * <p>The first card always starts the receiving pile. Subsequent cards are
 * then placed alternately on the top and bottom of that pile. The selected
 * start determines whether the second card is placed on the top or the
 * bottom, resulting in the two traditional Mongean shuffle variants.</p>
 */
public enum MongeanStart {

    /**
     * Places the second card on top of the receiving pile.
     */
    TOP,

    /**
     * Places the second card on the bottom of the receiving pile.
     */
    BOTTOM
}
