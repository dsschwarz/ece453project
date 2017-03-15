import java.util.Formatter;
import java.util.Locale;

public class Bug {
    public static void report(String functionName, String scope, String otherFunction, Integer support, double confidence) {
        String pair;
        if (functionName.compareTo(otherFunction) < 0) {
            pair = functionName + ", " + otherFunction;
        } else {
            pair = otherFunction + ", " + functionName;
        }

        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.CANADA);

        formatter.format(
                "bug: %s in scope %s, pair: (%s), support: %d, confidence: %.2f%%",
                functionName,
                scope,
                pair,
                support,
                confidence
        );

        System.out.println(sb.toString());
    }
}
