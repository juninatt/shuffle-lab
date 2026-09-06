package se.pbt.shufflelab.report;

import java.util.Locale;

/**
 * Small hand-rolled JSON primitives shared by every formatter that embeds
 * JSON in this package, since the data being serialized is always a fixed,
 * simple shape that does not warrant a JSON library dependency.
 */
final class JsonSupport {

    private JsonSupport() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Formats a statistic with a fixed number of decimals, independent of
     * the platform's default locale.
     *
     * @param value the value to format
     * @return the formatted value
     */
    static String number(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    /**
     * Quotes and escapes a string as a JSON string literal.
     *
     * @param value the value to quote
     * @return the value as a JSON string literal, including its surrounding
     *         quotes
     */
    static String quote(String value) {
        StringBuilder quoted = new StringBuilder(value.length() + 2);
        quoted.append('"');

        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);

            switch (character) {
                case '"' -> quoted.append("\\\"");
                case '\\' -> quoted.append("\\\\");
                case '\n' -> quoted.append("\\n");
                case '\r' -> quoted.append("\\r");
                case '\t' -> quoted.append("\\t");
                default -> {
                    if (character < 0x20) {
                        quoted.append(String.format(Locale.ROOT, "\\u%04x", (int) character));
                    } else {
                        quoted.append(character);
                    }
                }
            }
        }

        quoted.append('"');

        return quoted.toString();
    }
}
