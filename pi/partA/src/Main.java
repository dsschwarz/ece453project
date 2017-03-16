import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static int minConfidence = 65;
    public static int minSupport = 3;

    public static void main(String [] args) {
        String filename = args[0]; // filename that pipair wrote to

        // if support argument, overwrite default minimum support
        if (args.length >= 2) {
            try {
                minSupport = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Support must be a valid integer");
            }
        }

        // if confidence argument is passed in, overwrite default minimum confidence
        if (args.length >= 3) {
            try {
                minConfidence = Integer.valueOf(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Confidence must be a valid integer");
            }
        }

        // System.out.println("Parsing file " + filename);

        Parser parser = new Parser();
        parser.parseFile(filename);

        reportBugs(parser);

        // System.out.println("Scope count " + parser.scopes.size());
    }

    public static void reportBugs(Parser parser) {
        for (Scope scope : parser.scopes) {
            // create a set of all functions not in the current scope
            Set<String> allOtherFunctions = getDifference(scope, parser.functionCounts.keys());

            for (String function : scope.functionCalls) {
                Double callCount = Double.valueOf(parser.functionCounts.get(function)); // number of times this function appears

                // compare this function against all functions not in the current scope
                for (String otherFunction : allOtherFunctions) {
                    String key = Parser.createKey(function, otherFunction);

                    Integer support = parser.functionPairCount.get(key);
                    if (support == null) {
                        continue; // skip if support is 0
                    }

                    double confidence = support / callCount * 100;
                    if (confidence >= minConfidence && support >= minSupport) {
                        Bug.report(function, scope.name, otherFunction, support, confidence);
                    }
                }
            }
        }
    }

    public static Set<String> getDifference(Scope scope, Enumeration<String> allFunctionNames) {
        Set<String> allOthers = new HashSet<>();

        while(allFunctionNames.hasMoreElements()) {
            String name = allFunctionNames.nextElement();
            if (!scope.functionCalls.contains(name)) {
                allOthers.add(name);
            }
        }

        return allOthers;
    }
}
