import java.util.*;

public class Scope {
    public String name;
    public Set<String> functionCalls; // names of all the functions called in this scope
}

public class Parser {
    // createKey(functionName, functionName) -> #times functions are called in the same scope
    public Dictionary<String, Integer> functionPairCount = new Hashtable<String, Integer>() {};
    // functionName -> #time function is called
    public Dictionary<String, Integer> functionCounts = new Hashtable<String, Integer>();
    public List<Scope> scopes; // parsed call tree

    /**
     * Parse a callgraph file
     * @param fileName
     */
    public void parseFile(String fileName) {

    }

    /**
     * Parse a single scope
     */
    private void parseScope() {

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
}
