import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    // createKey(functionName, functionName) -> #times functions are called in the same scope
    public Dictionary<String, Integer> functionPairCount = new Hashtable<String, Integer>() {};
    // functionName -> #time function is called
    public Dictionary<String, Integer> functionCounts = new Hashtable<String, Integer>();
    public List<Scope> scopes = new ArrayList<Scope>();; // parsed call tree

    /**
     * Parse a callgraph file
     * @param fileName File containing
     */
    public void parseFile(String fileName) {
        try {
            InputStream fileInputStream = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentLine = bufferedReader.readLine();

            while(currentLine != null) {
                Scope scope = matchNode(currentLine);
                if (scope != null) {
                    parseScope(scope, bufferedReader);
                }
                currentLine = bufferedReader.readLine(); // next line
            }

        } catch(FileNotFoundException e) {
            // panic
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse a single scope
     */
    private void parseScope(Scope scope, BufferedReader bufferedReader) throws IOException {
        assert scope.functionCalls.size() == 0; // scope must be empty before beginning parsing

        String currentLine = bufferedReader.readLine();
        while (currentLine != null) {
            String callee = matchCallee(currentLine);
            if (callee != null) {
                scope.functionCalls.add(callee);
            } else {
                System.out.println("Reached end of scope <" + scope.name + ">");
                break;
            }
            currentLine = bufferedReader.readLine();
        }

        // might as well record pairs now
        List<String> functionCalls = new ArrayList<String>(scope.functionCalls);

        for (Integer i = 0; i < functionCalls.size() - 1; i++) {
            for (Integer j = i+1; j < functionCalls.size(); j++) {
                recordFunctionPair(functionCalls.get(i), functionCalls.get(j));
            }
        }
    }

    /**
     * Creates a key from two function names. (name1, name2) generates the same name as (name2, name1)
     * @return
     */
    static public String createKey(String name1, String name2) {
        String key;
        if (name1.compareTo(name2) < 0) {
            key = name2 + ":" + name1;
        } else {
            key = name1 + ":" + name2;
        }
        return key;
    }

    private void recordFunctionPair(String name1, String name2) {
        String key = createKey(name1, name2);

        Integer current = functionPairCount.get(key);
        if (current == null) {
            functionPairCount.put(key, 1); // not recorded yet
        } else {
            functionPairCount.put(key, current + 1); // increment the count
        }
    }

    private Scope matchNode(String line) {
        // if using regex solution, move this into constructor
        Pattern signature = Pattern.compile("^Call\\sgraph\\snode\\sfor\\sfunction\\:\\s'([a-zA-Z][a-zA-Z0-9]*)'.*([0-9]+)$");

        Matcher matcher = signature.matcher(line);
        if (matcher.find()) {
            String functionName = matcher.group(1);
            Integer callCount = Integer.valueOf(matcher.group(2));

            assert functionCounts.get(functionName) == null; // each node must only appear once!

            Scope scope = new Scope(matcher.group(1));
            functionCounts.put(functionName, callCount);
            scopes.add(scope);
            return scope;
        } else {
            return null;
        }
    }

    private String matchCallee(String line) {
        // if using regex solution, move this into constructor
        Pattern signature =
                Pattern.compile("^\\s+CS<[^>]*>\\scalls\\sfunction\\s'([a-zA-Z][a-zA-Z0-9]*)'");

        Matcher matcher = signature.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
